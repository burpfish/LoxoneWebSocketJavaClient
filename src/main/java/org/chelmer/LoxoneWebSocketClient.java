/*
 * Copyright 2014 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.chelmer;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.chelmer.clientimpl.LoxoneWebSocketClientHandler;
import org.chelmer.exceptions.InvalidConfigurationException;
import org.chelmer.model.LoxoneConfig;
import org.chelmer.response.ComponentChange;
import org.chelmer.response.EventTimerItem;
import org.chelmer.response.WeatherTimerItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * The main Loxone client class.
 */
public final class LoxoneWebSocketClient implements LoxoneEventHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoxoneWebSocketClient.class);

    private final URI uri;
    private final String userName;
    private final String password;
    private final List<Function<LoxoneConfig, Boolean>> configListeners = new ArrayList<>();
    private final List<Function<WeatherTimerItem, Boolean>> weatherTimerListeners = new ArrayList<>();
    private final List<Function<String, Boolean>> textMessageListeners = new ArrayList<>();
    private final List<Function<EventTimerItem, Boolean>> daytimeTimerChangeListeners = new ArrayList<>();
    private final List<Function<ByteBuf, Boolean>> binaryMessageListeners = new ArrayList<>();
    private final List<Function<ComponentChange, Boolean>> componentChangeListeners = new ArrayList<>();
    private EventLoopGroup group;
    private Channel channel;

    LoxoneWebSocketClient(String hostName, String userName, String password) {
        this(hostName, 80, userName, password);
    }

    LoxoneWebSocketClient(String hostName, int port, String userName, String password) {
        this.userName = userName;
        this.password = password;

        if (port == -1) {
            port = 80;
        }

        try {
            uri = new URI(String.format("ws://%s:%d/ws/rfc6455", hostName, port));
        } catch (URISyntaxException e) {
            throw new InvalidConfigurationException("Cannot create URI", e);
        }
    }

    public void registerConfigListener(Function<LoxoneConfig, Boolean> funct) {
        configListeners.add(funct);
    }

    public void registerWeatherTimerListener(Function<WeatherTimerItem, Boolean> funct) {
        weatherTimerListeners.add(funct);
    }

    public void registerTextMessageListeners(Function<String, Boolean> funct) {
        textMessageListeners.add(funct);
    }

    public void registerDaytimeTimerChangeListener(Function<EventTimerItem, Boolean> funct) {
        daytimeTimerChangeListeners.add(funct);
    }

    public void registerBinaryMessageListener(Function<ByteBuf, Boolean> funct) {
        binaryMessageListeners.add(funct);
    }

    public void registerComponentChangeListener(Function<ComponentChange, Boolean> funct) {
        componentChangeListeners.add(funct);
    }

    public void connectAndRegisterForUpdates() {
        connect();
        requestStatus();
        registerForUpdates();
    }

    public void connect() {
        group = new NioEventLoopGroup();
        LoxoneWebSocketClientHandler handler = new LoxoneWebSocketClientHandler(uri, userName, password, this);

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
                ChannelPipeline p = ch.pipeline();
                p.addLast(new HttpClientCodec(), new HttpObjectAggregator(8192), handler);
            }
        });

        try {
            channel = bootstrap.connect(uri.getHost(), uri.getPort()).sync().channel();
            handler.handshakeFuture().sync();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.error("InterruptedException disconnecting client", e);
        }
    }

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

    public void ping() {
        WebSocketFrame frame = new PingWebSocketFrame(Unpooled.wrappedBuffer(new byte[]{8, 1, 8, 1}));
        channel.writeAndFlush(frame);
    }

    private void sendTextMessage(String msg) {
        WebSocketFrame frame = new TextWebSocketFrame(msg);
        channel.writeAndFlush(frame);
    }

    private void requestStatus() {
        sendTextMessage("data/LoxAPP3.json");
    }

    private void registerForUpdates() {
        sendTextMessage("jdev/sps/enablebinstatusupdate");
    }

    private <T> void invokeListeners(List<Function<T, Boolean>> listeners, T arg) {
        for (Function<T, Boolean> listener : listeners) {
            listener.apply(arg);
        }
    }

    @Override
    public void configIncoming(LoxoneConfig config) {
        invokeListeners(configListeners, config);
    }

    @Override
    public void weatherTimerChangeIncoming(WeatherTimerItem value) {
        invokeListeners(weatherTimerListeners, value);
    }

    @Override
    public void daytimeTimerChangeIncoming(EventTimerItem value) {
        invokeListeners(daytimeTimerChangeListeners, value);
    }

    @Override
    public void textMessageIncoming(String value) {
        invokeListeners(textMessageListeners, value);
    }

    @Override
    public void binaryMessageIncoming(ByteBuf bytes) {
        invokeListeners(binaryMessageListeners, bytes);
    }

    @Override
    public void componentChangeIncoming(ComponentChange value) {
        invokeListeners(componentChangeListeners, value);
    }

    public void showInteractiveConsole() {
        try {
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String msg = console.readLine();
                if (msg == null) {
                    break;
                } else if ("bye".equals(msg.toLowerCase())) {
                    channel.writeAndFlush(new CloseWebSocketFrame());
                    channel.closeFuture().sync();
                    break;
                } else if ("ping".equals(msg.toLowerCase())) {
                    WebSocketFrame frame = new PingWebSocketFrame(Unpooled.wrappedBuffer(new byte[]{8, 1, 8, 1}));
                    channel.writeAndFlush(frame);
                } else {
                    WebSocketFrame frame = new TextWebSocketFrame(msg);
                    channel.writeAndFlush(frame);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}