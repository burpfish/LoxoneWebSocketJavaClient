package org.chelmer.clientimpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import org.chelmer.LoxoneEventHandler;
import org.chelmer.deserialize.ObjectMapperFactory;
import org.chelmer.exceptions.CouldNotDeserializeException;
import org.chelmer.exceptions.CoundNotAuthenticateException;
import org.chelmer.model.LoxoneConfig;
import org.chelmer.response.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public abstract class WebSocketClientHandler extends SimpleChannelInboundHandler<Object> implements LoxoneEventHandler {
    public static final int KEEPALIVE_DELAY = 60 * 3 * 1000;
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketClientHandler.class);
    private final WebSocketClientHandshaker handshaker;
    private final ObjectMapper mapper;
    private final String userName;
    private final String password;
    private final Hasher hasher = new Hasher();
    private final UuidComponentRegistry registry;
    private ChannelPromise handshakeFuture;
    private MessageNotificationItem latestMessageNotification = MessageNotificationItem.nullValue();
    private Channel channel;
    private Timer keepaliveTimer;

    public WebSocketClientHandler(WebSocketClientHandshaker handshaker, String userName, String password) {
        this.handshaker = handshaker;
        this.registry = new UuidComponentRegistry();
        this.mapper = new ObjectMapperFactory().createObjectMapper(registry);
        this.userName = userName;
        this.password = password;
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
                sendTextMessage("keepalive");
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

    void handleTextMessage(String message) {
        LOGGER.debug("WebSocket Client received text message: " + message);
        textMessageIncoming(message);

        if (handleAuthHashResponse(message)) {
            return;
        }

        if (handleStatusResponse(message)) {
            return;
        }

        LOGGER.debug("WebSocket Client received unexpected text message: " + message);
    }

    private boolean handleStatusResponse(String message) {
        boolean isStatusResponse = isStatusResponse(message);

        if (isStatusResponse) {
            LoxoneConfig config = null;
            try {
                config = mapper.readValue(message, LoxoneConfig.class);
                configIncoming(config);
            } catch (IOException e) {
                throw new CouldNotDeserializeException("Error deserializing Loxone config", e);
            }
        }

        return isStatusResponse;
    }

    private boolean handleAuthHashResponse(String message) {
        boolean isAuthHashResponse = isAuthHashResponse(message);

        if (isAuthHashResponse) {
            HashResponse response = HashResponse.create(message);

            if (response.getCode() != 200) {
                throw new CoundNotAuthenticateException("Error getting hash code: " + response.getCode());
            }

            byte[] key = DatatypeConverter.parseHexBinary(response.getValue());
            String hashed = hasher.hash(String.format("%s:%s", userName, password), key);
            sendTextMessage("authenticate/" + hashed);
            handshakeFuture.setSuccess();
        }

        return isAuthHashResponse;
    }

    void sendTextMessage(String msg) {
        if (channel != null) {
            WebSocketFrame frame = new TextWebSocketFrame(msg);
            channel.writeAndFlush(frame);
        } else {
            LOGGER.error("Channel is null - Could not send text massage: " + msg);
        }
    }


    private boolean isAuthHashResponse(String message) {
        return message.contains("jdev/sys/getkey");
    }

    private boolean isStatusResponse(String message) {
        return message.contains("lastModified");
    }

    private void finishHandshakeAndRequestAuthKey(FullHttpResponse msg, Channel ch) {
        handshaker.finishHandshake(ch, msg);
        LOGGER.debug("WebSocket Client connected");

        // Request hash key
        WebSocketFrame frame = new TextWebSocketFrame("jdev/sys/getkey");
        ch.writeAndFlush(frame);
    }

    void handleBinaryMessage(ByteBuf bytes) {
        LOGGER.debug("WebSocket Client received binary message");

        binaryMessageIncoming(bytes);

        MessageType messageType = latestMessageNotification.getMessageType();
        long messageLength = latestMessageNotification.getLength();

        if (messageType == MessageType.KEEPALIVE_RESPONSE) {
            LOGGER.trace("Keepalive response incoming");
        } else if (bytes.capacity() == 8 && bytes.getByte(0) == 0x03) {
            handleNotificationMessage(bytes);
        } else if (messageType == MessageType.VALUE_TABLE) {
            handleValueTableMessage(bytes, messageLength);
        } else if (messageType == MessageType.TEXT_TABLE) {
            handleTextTableMessage(bytes, messageLength);
        } else if (messageType == MessageType.DAYTIMER_TABLE) {
            handleDaytimeTimerMessage(bytes, messageLength);
        } else if (messageType == MessageType.WEATHER_TABLE) {
            handleWeatherTableMessage(bytes, messageLength);
        }
    }

    private void handleWeatherTableMessage(ByteBuf bytes, long messageLength) {
        DataMessageByteBuff dmbb = new DataMessageByteBuff(registry);

        List<WeatherTimerItem> values = new ArrayList<>();
        int startingIndex = bytes.readerIndex();
        while ((bytes.readerIndex() - startingIndex) < messageLength) {
            WeatherTimerItem item = dmbb.readObject(bytes, WeatherTimerItem.class);
            item.setEntries(dmbb.readFixedNumberOfObjects(bytes, WeatherTimerItemEntry.class, item.getnEntries()));
            values.add(item);
        }

        traceItems(values);

        for (WeatherTimerItem value : values) {
            weatherTimerChangeIncoming(value);
        }
    }

    protected void traceItems(List<?> values) {
        if (LOGGER.isTraceEnabled()) {
            for (Object value : values) {
                LOGGER.trace(value.toString());
            }
        }
    }

    private void handleDaytimeTimerMessage(ByteBuf bytes, long messageLength) {
        DataMessageByteBuff dmbb = new DataMessageByteBuff(registry);

        List<EventTimerItem> values = new ArrayList<>();
        int startingIndex = bytes.readerIndex();
        while ((bytes.readerIndex() - startingIndex) < messageLength) {
            EventTimerItem eventTimerItem = dmbb.readObject(bytes, EventTimerItem.class);
            eventTimerItem.setEntries(dmbb.readFixedNumberOfObjects(bytes, EventTimerItemEntry.class, eventTimerItem.getnEntries()));
            values.add(eventTimerItem);
        }

        traceItems(values);

        for (EventTimerItem value : values) {
            daytimeTimerChangeIncoming(value);
        }
    }

    private void handleTextTableMessage(ByteBuf bytes, long messageLength) {
        List<TextItem> values = new DataMessageByteBuff(registry).readObjects(bytes, TextItem.class, messageLength);
        traceItems(values);
    }

    private void handleValueTableMessage(ByteBuf bytes, long messageLength) {
        List<ValueItem> values = new DataMessageByteBuff(registry).readObjects(bytes, ValueItem.class, messageLength);

        for (ValueItem value : values) {
            if (value.getComponent() != null) {
                this.componentChangeIncoming(new ComponentChange(value.getComponent(), value.getValue()));
            } else {
                LOGGER.debug("NO ENTRY FOUND FOR: " + value);
            }
        }
    }

    private void handleNotificationMessage(ByteBuf bytes) {
        latestMessageNotification = new DataMessageByteBuff(registry).readObject(bytes, MessageNotificationItem.class);
        LOGGER.trace("Next message: " + latestMessageNotification);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error("Client handler error", cause);

        if (!handshakeFuture.isDone()) {
            handshakeFuture.setFailure(cause);
        }

        ctx.close();
    }
}
