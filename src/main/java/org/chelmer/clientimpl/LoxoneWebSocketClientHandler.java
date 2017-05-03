package org.chelmer.clientimpl;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import org.chelmer.deserialize.ObjectMapperFactory;

import java.net.URI;
import java.util.List;
import java.util.function.Function;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import org.chelmer.deserialize.ObjectMapperFactory;
import org.chelmer.exceptions.CoundNotAuthenticateException;
import org.chelmer.exceptions.ParsingException;
import org.chelmer.model.Component;
import org.chelmer.model.LoxoneConfig;
import org.chelmer.model.control.controlTypes.BooleanControl;
import org.chelmer.model.state.State;
import org.chelmer.response.*;
import org.chelmer.response.change.ComponentChange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by burfo on 18/02/2017.
 */
public class LoxoneWebSocketClientHandler extends SimpleChannelInboundHandler<Object> implements LoxoneEventHandler {
    private static final int KEEPALIVE_DELAY = 60 * 3 * 1000;
    private static final Logger LOGGER = LoggerFactory.getLogger(LoxoneWebSocketClientHandler.class);
    private final WebSocketClientHandshaker handshaker;
    private final ObjectMapper mapper;
    private final String userName;
    private final String password;
    private final Hasher hasher = new Hasher();
    private final UuidComponentRegistry registry;
    private final EventLoopGroup group;
    private final URI uri;
    private ChannelPromise handshakeFuture;
    private MessageNotificationItem latestMessageNotification = MessageNotificationItem.nullValue();
    private Channel channel;
    private Timer keepaliveTimer;
    private final LoxoneWebSocketClient client;

    protected void handleTextMessage(String text) {
        client.handleTextMessage(text);
    }

    protected void handleBinaryMessage(ByteBuf content) {
        client.handleBinaryMessage(content);
    }


    public LoxoneWebSocketClientHandler(LoxoneWebSocketClient client, URI uri, String userName, String password) {
        this.client = client;
        this.registry = new UuidComponentRegistry();
        this.mapper = new ObjectMapperFactory().createObjectMapper(registry, client);
        this.userName = userName;
        this.password = password;
        this.handshaker = WebSocketClientHandshakerFactory.newHandshaker(uri, WebSocketVersion.V13,
                null, false, new DefaultHttpHeaders());
        group = new NioEventLoopGroup();
        this.uri = uri;
    }

    public void connect() {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
                ChannelPipeline p = ch.pipeline();
                p.addLast(new HttpClientCodec(), new HttpObjectAggregator(8192), LoxoneWebSocketClientHandler.this);
            }
        });

        try {
            channel = bootstrap.connect(uri.getHost(), uri.getPort()).sync().channel();
            handshakeFuture().sync();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.error("InterruptedException disconnecting client", e);
        }
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        setHandshakeFuture(handshakeFuture = ctx.newPromise());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        channel = ctx.channel();
        handshaker.handshake(ctx.channel());

        TimerTask keepaliveTask = new TimerTask() {
            public void run() {
                sendTextMessage("jdev/keepalive");
            }
        };
        keepaliveTimer = new Timer();
        keepaliveTimer.schedule(keepaliveTask, KEEPALIVE_DELAY, KEEPALIVE_DELAY);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        keepaliveTimer.cancel();
        keepaliveTimer = null;
        channel = null;
        LOGGER.debug("WebSocket Client disconnected!");
    }

    @Override
    public void disconnect() {
        if (channel != null) {
            channel.writeAndFlush(new CloseWebSocketFrame());
            try {
                channel.closeFuture().sync();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LOGGER.error("InterruptedException disconnecting client", e);
            }

            group.shutdownGracefully();
        }
    }

    public ChannelPromise handshakeFuture() {
        return handshakeFuture;
    }

    public void setHandshakeFuture(ChannelPromise handshakeFuture) {
        this.handshakeFuture = handshakeFuture;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) {
        handleIncoming(msg);
    }

    private void handleIncoming(Object msg) {
        if (handleHandshake(channel, msg)) {
            return;
        }

        WebSocketFrame frame = (WebSocketFrame) msg;
        if (frame instanceof BinaryWebSocketFrame) {
            handleBinaryMessage(frame.content());
        } else if (frame instanceof TextWebSocketFrame) {
            handleTextMessage(((TextWebSocketFrame) frame).text());
        } else if (frame instanceof PongWebSocketFrame) {
            LOGGER.debug("WebSocket Client received pong");
        } else if (frame instanceof CloseWebSocketFrame) {
            LOGGER.debug("WebSocket Client received closing");
        } else {
            LOGGER.warn("WebSocket Client received unknown message: " + frame);
        }
    }

    private boolean handleHandshake(Channel ch, Object msg) {
        if (!handshaker.isHandshakeComplete()) {
            finishHandshakeAndRequestAuthKey((FullHttpResponse) msg, ch);
            return true;
        }

        if (msg instanceof FullHttpResponse) {
            FullHttpResponse response = (FullHttpResponse) msg;
            throw new IllegalStateException("Unexpected FullHttpResponse (getStatus=" + response.status().code()
                    + ", content=" + response.content().toString(CharsetUtil.UTF_8) + ')');
        }
        return false;
    }

    public void sendTextMessage(String msg) {
        if (channel != null) {
            WebSocketFrame frame = new TextWebSocketFrame(msg);
            channel.writeAndFlush(frame);
        } else {
            LOGGER.error("Channel is null - Could not send text massage: " + msg);
        }
    }

    @Override
    public void authComplete() {
        handshakeFuture.setSuccess();
    }

    private void finishHandshakeAndRequestAuthKey(FullHttpResponse msg, Channel ch) {
        handshaker.finishHandshake(ch, msg);
        LOGGER.debug("WebSocket Client connected");

        // Request hash key
        WebSocketFrame frame = new TextWebSocketFrame("jdev/sys/getkey");
        ch.writeAndFlush(frame);
    }

    protected void traceItems(List<?> values) {
        if (LOGGER.isTraceEnabled()) {
            for (Object value : values) {
                LOGGER.trace(value.toString());
            }
        }
    }

    private void handleNotificationMessage(ByteBuf bytes) {
        latestMessageNotification = new DataMessageByteBuff(registry).readObject(bytes, MessageNotificationItem.class);
        LOGGER.trace("Next message: " + latestMessageNotification);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error("Client handler error: ", cause);

        if (!handshakeFuture.isDone()) {
            handshakeFuture.setFailure(cause);
        }

//        donut - do we need to do this? Close and reconnect ...
//        ctx.close();
//        connect();
    }
}
