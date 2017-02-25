package org.chelmer.clientimpl;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import org.chelmer.LoxoneEventHandler;
import org.chelmer.model.LoxoneConfig;
import org.chelmer.response.ComponentChange;
import org.chelmer.response.EventTimerItem;
import org.chelmer.response.WeatherTimerItem;

import java.net.URI;
import java.util.List;
import java.util.function.Function;

/**
 * Created by burfo on 18/02/2017.
 */
public class LoxoneWebSocketClientHandler extends WebSocketClientHandler implements LoxoneEventHandler {
    private final LoxoneEventHandler eventHandler;

    public LoxoneWebSocketClientHandler(URI uri, String userName, String password, LoxoneEventHandler eventHandler) {
        this(WebSocketClientHandshakerFactory.newHandshaker(uri, WebSocketVersion.V13,
                null, false, new DefaultHttpHeaders()), userName, password, eventHandler);
    }

    public LoxoneWebSocketClientHandler(WebSocketClientHandshaker webSocketClientHandshaker, String userName, String password, LoxoneEventHandler eventHandler) {
        super(webSocketClientHandshaker, userName, password);
        this.eventHandler = eventHandler;
    }


    private <T> void invokeListeners(List<Function<T, Boolean>> listeners, T arg) {
        for (Function<T, Boolean> listener : listeners) {
            if (!listener.apply(arg)) {
                break;
            }
        }
    }

    @Override
    public void configIncoming(LoxoneConfig config) {
        eventHandler.configIncoming(config);
    }

    @Override
    public void weatherTimerChangeIncoming(WeatherTimerItem value) {
        eventHandler.weatherTimerChangeIncoming(value);
    }

    @Override
    public void daytimeTimerChangeIncoming(EventTimerItem value) {
        eventHandler.daytimeTimerChangeIncoming(value);
    }

    @Override
    public void textMessageIncoming(String value) {
        eventHandler.textMessageIncoming(value);
    }

    @Override
    public void binaryMessageIncoming(ByteBuf bytes) {
        eventHandler.binaryMessageIncoming(bytes);
    }

    @Override
    public void componentChangeIncoming(ComponentChange value) {
        eventHandler.componentChangeIncoming(value);
    }
}
