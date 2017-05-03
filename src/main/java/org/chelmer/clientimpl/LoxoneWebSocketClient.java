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
package org.chelmer.clientimpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.chelmer.LoxoneClient;
import org.chelmer.deserialize.ObjectMapperFactory;
import org.chelmer.exceptions.CoundNotAuthenticateException;
import org.chelmer.exceptions.InvalidConfigurationException;
import org.chelmer.exceptions.ParsingException;
import org.chelmer.exceptions.UnexpectedTypeException;
import org.chelmer.model.Component;
import org.chelmer.model.LoxoneConfig;
import org.chelmer.model.control.Control;
import org.chelmer.model.control.controlTypes.BooleanControl;
import org.chelmer.model.control.controlTypes.Command;
import org.chelmer.model.control.controlTypes.DimmerControl;
import org.chelmer.model.control.controlTypes.SwitchControl;
import org.chelmer.model.entity.LoxUuid;
import org.chelmer.model.state.GlobalState;
import org.chelmer.model.state.State;
import org.chelmer.response.*;
import org.chelmer.response.change.ComponentChange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * The main Loxone client class.
 */
public class LoxoneWebSocketClient implements LoxoneClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoxoneWebSocketClient.class);

    private final URI uri;
    private final String userName;
    private final String password;
    private LoxoneConfig config;
    private LoxoneEventHandler handler;

    private final List<Consumer<LoxoneConfig>> configListeners = new ArrayList<>();
    private final List<Consumer<ComponentChange>> componentChangeListeners = new ArrayList<>();
    private final List<Consumer<ByteBuf>> binaryMessageListeners = new ArrayList<>();
    private final List<Consumer<String>> textMessageListeners = new ArrayList<>();
    private final List<Consumer<WeatherTimerItem>> weatherTimerListeners = new ArrayList<>();
    private final List<Consumer<EventTimerItem>> daytimeTimerChangeListeners = new ArrayList<>();
    private final List<Consumer<SwitchControl>> switchChangeListeners = new ArrayList<>();
    private final List<Consumer<DimmerControl>> dimmerChangeListeners = new ArrayList<>();

    public LoxoneWebSocketClient(String hostName, int port, String userName, String password) {
        this.userName = userName;
        this.password = password;

        if (port == -1) {
            port = 80;
        }

        if (hostName.endsWith("/")) {
            hostName = hostName.substring(0, hostName.length() - 1);
        }

        try {
            uri = new URI(String.format("ws://%s:%d/ws/rfc6455", hostName, port));
        } catch (URISyntaxException e) {
            throw new InvalidConfigurationException("Cannot create URI", e);
        }
    }

    @Override
    public LoxoneConfig getConfig() {
        return config;
    }

    @Override
    public void registerSwitchChangeListener(Consumer<SwitchControl> funct) {
        switchChangeListeners.add(funct);
    }

    @Override
    public void registerDimmerChangeListener(Consumer<DimmerControl> funct) {
        dimmerChangeListeners.add(funct);
    }

    @Override
    public <T extends Control> T getControl(String id, Class<T> clazz) {
        return getControl(new LoxUuid(id), clazz);
    }

    @Override
    public <T extends Control> T getControl(LoxUuid id, Class<T> clazz) {
        // donut - should use the registry directly
        return getConfig().getControl(id, clazz);
    }

    @Override
    public <T extends Control> List<T> getControls(Class<T> controlType) {
        // donut - should use the registry directly
        return this.getConfig().getControls(controlType);
    }

    @Override
    public void registerConfigListener(Consumer<LoxoneConfig> funct) {
        configListeners.add(funct);
    }

    @Override
    public void registerWeatherTimerListener(Consumer<WeatherTimerItem> funct) {
        weatherTimerListeners.add(funct);
    }

    @Override
    public void registerTextMessageListeners(Consumer<String> funct) {
        textMessageListeners.add(funct);
    }

    @Override
    public void registerDaytimeTimerChangeListener(Consumer<EventTimerItem> funct) {
        daytimeTimerChangeListeners.add(funct);
    }

    @Override
    public void registerBinaryMessageListener(Consumer<ByteBuf> funct) {
        binaryMessageListeners.add(funct);
    }

    @Override
    public void registerComponentChangeListener(Consumer<ComponentChange> funct) {
        componentChangeListeners.add(funct);
    }

    @Override
    public void connectAndRegisterForUpdates() {
        connectAndRegisterForUpdates(false);
    }

    @Override
    public void connectAndRegisterForUpdates(boolean useMockData) {
        connect(useMockData);
        requestStatus();
        registerForUpdates();
    }

    public void connect(boolean useMockData) {
        if (!useMockData) {
            handler = new LoxoneWebSocketClientHandler(this, uri, userName, password);//, this);
            ((LoxoneWebSocketClientHandler)handler).connect();
        } else {
            handler = new DummyEventHandler(this, uri, userName, password);
        }
    }

    @Override
    public void disconnect() {
        if (handler != null) {
            handler.disconnect();
        }
    }

    @Override
    public void sendCommand(String msg) {
        LOGGER.debug("Sending command: " + msg);
        handler.sendTextMessage(msg);
    }

    @Override
    public void sendCommand(Control control, Command command) {
        String commandStr = String.format("jdev/sps/io/%s/%s", control.getUuid(), command.getCommandStr());
        sendCommand(commandStr);
    }

    @Override
    public void sendCommand(Control control, Command command, int value) {
        String commandStr = String.format("jdev/sps/io/%s/%d", control.getUuid(), value);
        sendCommand(commandStr);
    }

    protected void requestStatus() {
        sendCommand("data/LoxAPP3.json");
    }

    protected void registerForUpdates() {
        sendCommand("jdev/sps/enablebinstatusupdate");
    }

    private <T> void invokeListeners(List<Consumer<T>> listeners, T arg) {
        for (Consumer<T> listener : listeners) {
            listener.accept(arg);
        }
    }

    public void configIncoming(LoxoneConfig config) {
        this.config = config;
        invokeListeners(configListeners, config);
    }

    public void weatherTimerChangeIncoming(WeatherTimerItem value) {
        invokeListeners(weatherTimerListeners, value);
    }

    public void daytimeTimerChangeIncoming(EventTimerItem value) {
        invokeListeners(daytimeTimerChangeListeners, value);
    }

    public void textMessageIncoming(String value) {
        invokeListeners(textMessageListeners, value);
    }

    public void binaryMessageIncoming(ByteBuf bytes) {
        invokeListeners(binaryMessageListeners, bytes);
    }

   public void componentChangeIncoming(ComponentChange change) {
        invokeListeners(componentChangeListeners, change);

        Component component = change.getComponent();
        if (component instanceof SwitchControl) {
            invokeListeners(switchChangeListeners, (SwitchControl)component);
        } else if (component instanceof DimmerControl) {
            invokeListeners(dimmerChangeListeners, (DimmerControl)component);
        } else {
            // donut - handle the other types
            // throw new UnexpectedTypeException("Unhandled component type for Component Change: " + component.getClass().getName());
            LOGGER.warn("Unhandled component type for Component Change: " + component.getClass().getName());
        }

        // Other types:

        // else if (component instanceof GlobalState) {
           // donut - do someting!
        //   System.out.println("  >>-> GlobalState change");
        // }

        // PushButtonControl
        // ComponentChange{component=Control{name='Downstairs All Off', type=PUSHBUTTON, uuid=0e9ea394-029e-31e0-ffff504f94000000}, previousValue=null, newValue=false}

        // LightingControl
        // ComponentChange{component=Control{name='Danny's lighing controller', type=LIGHTCONTROLLER, uuid=0e4fa2f3-00fc-f9a4-ffff504f94000000}, previousValue=null, newValue=0.0}
        //         >>---> org.chelmer.model.control.controlTypes.LightingControl <---<<

        // RemoteControl
        // ComponentChange{component=Control{name='Music', type=REMOTE, uuid=0d848291-0202-518f-ffff504f94000000}, previousValue=null, newValue=true}

        // IRoomControllerControl
        // ComponentChange{component=Control{name='Intelligent room controller', type=IROOMCONTROLLER, uuid=0d808e77-02c6-a44e-ffff504f94000000}, previousValue=null, newValue=0.0}

        // IrcDayTimerControl
        // ComponentChange{component=Control{name='Heating', type=IRCDAYTIMER, uuid=0d808e77-02c6-a41f-ffff855b6901a466}, previousValue=null, newValue=8.0}

        // ApplicationControl
        // ComponentChange{component=Control{name='Sonos', type=APPLICATION, uuid=0d848289-0153-4947-ffff504f94000000}, previousValue=0.0, newValue=0.0}

        //       ComponentChange{component=org.chelmer.model.autopilot.Autopilot@6f938a0a, previousValue=null, newValue=0.0}
        //>>---> org.chelmer.model.autopilot.Autopilot <---<<

       //       ComponentChange{component=org.chelmer.model.autopilot.AutopilotState@56355119, previousValue=null, newValue=0.0}
       //>>---> org.chelmer.model.autopilot.AutopilotState <---<<
    }

    private MessageNotificationItem latestMessageNotification = MessageNotificationItem.nullValue();
    private UuidComponentRegistry registry = new UuidComponentRegistry();

    public void handleTextMessage(String message) {
        LOGGER.debug("WebSocket Client received text message: " + message);

        if (handleAuthHashResponse(message)) {
            return;
        }

        if (handleStatusResponse(message)) {
            return;
        }

        textMessageIncoming(message);

        LOGGER.debug("WebSocket Client received unexpected text message: " + message);
    }

    private boolean handleStatusResponse(String message) {
        boolean isStatusResponse = isStatusResponse(message);

        if (isStatusResponse) {
            LoxoneConfig config = null;
            try {
                ObjectMapper mapper = new ObjectMapperFactory().createObjectMapper(registry, this);
                config = mapper.readValue(message, LoxoneConfig.class);
                configIncoming(config);
            } catch (IOException e) {
                throw new ParsingException("Error deserializing Loxone StaticConfig", e);
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
            String hashed = new Hasher().hash(String.format("%s:%s", userName, password), key);
            handler.sendTextMessage("authenticate/" + hashed);
            handler.authComplete();
        }

        return isAuthHashResponse;
    }

    private boolean isAuthHashResponse(String message) {
        return message.contains("jdev/sys/getkey");
    }

    private boolean isStatusResponse(String message) {
        return message.contains("lastModified");
    }

    public void handleBinaryMessage(ByteBuf bytes) {
        LOGGER.trace("WebSocket Client received binary message");

        binaryMessageIncoming(bytes);

        MessageType messageType = latestMessageNotification.getMessageType();
        long messageLength = latestMessageNotification.getLength();

        LOGGER.debug("WebSocket Client received binary message of type: " + messageType);

        if (messageType == MessageType.KEEPALIVE_RESPONSE) {
            // do nothing
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
        } else {
            LOGGER.debug("Ignoring binary message of type: " + messageType);
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
            Component component = value.getComponent();
            if (component != null) {
                if (component instanceof State) {
                    component = ((State) component).getControl();
                    // donut - this may not always be correct
                    if (component instanceof Control) {
                        Map<String, Control> subControls = ((Control) component).getSubControls();
                        if (subControls != null && subControls.values().size() == 1) {
                            // donut
                            // LOGGER.warn("TODO. Component with one sub control - using it");
                            // component = subControls.values().iterator().next();
                        }
                    }
                }

                ComponentChange change = new ComponentChange(component, value.getValue());
                this.componentChangeIncoming(change);
            } else {
                // donut - what?
                LOGGER.debug("NO ENTRY FOUND FOR: " + value);
            }
        }
    }

    private void handleNotificationMessage(ByteBuf bytes) {
        latestMessageNotification = new DataMessageByteBuff(registry).readObject(bytes, MessageNotificationItem.class);
        LOGGER.trace("Next message: " + latestMessageNotification);
    }

}