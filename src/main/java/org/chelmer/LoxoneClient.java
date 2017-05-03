package org.chelmer;

import io.netty.buffer.ByteBuf;
import org.chelmer.model.LoxoneConfig;
import org.chelmer.model.control.Control;
import org.chelmer.model.control.controlTypes.Command;
import org.chelmer.model.control.controlTypes.DimmerControl;
import org.chelmer.model.control.controlTypes.SwitchControl;
import org.chelmer.model.entity.LoxUuid;
import org.chelmer.response.EventTimerItem;
import org.chelmer.response.WeatherTimerItem;
import org.chelmer.response.change.ComponentChange;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by burfo on 08/03/2017.
 */
public interface LoxoneClient {
    LoxoneConfig getConfig();

    <T extends Control> T getControl(String id, Class<T> clazz);

    <T extends Control> T getControl(LoxUuid id, Class<T> clazz);

    <T extends Control> List<T> getControls(Class<T> controlType);

    void registerConfigListener(Consumer<LoxoneConfig> funct);

    void registerWeatherTimerListener(Consumer<WeatherTimerItem> funct);

    void registerTextMessageListeners(Consumer<String> funct);

    void registerDaytimeTimerChangeListener(Consumer<EventTimerItem> funct);

    void registerBinaryMessageListener(Consumer<ByteBuf> funct);

    void registerComponentChangeListener(Consumer<ComponentChange> funct);

    void registerSwitchChangeListener(Consumer<SwitchControl> funct);

    void registerDimmerChangeListener(Consumer<DimmerControl> funct);

    void connectAndRegisterForUpdates();

    void connectAndRegisterForUpdates(boolean useMockData);

    void disconnect();

    void sendCommand(String msg);

    void sendCommand(Control control, Command command);

    void sendCommand(Control control, Command command, int value);
}
