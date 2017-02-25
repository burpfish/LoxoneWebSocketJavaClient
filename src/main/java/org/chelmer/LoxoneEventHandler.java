package org.chelmer;

import io.netty.buffer.ByteBuf;
import org.chelmer.model.LoxoneConfig;
import org.chelmer.response.ComponentChange;
import org.chelmer.response.EventTimerItem;
import org.chelmer.response.WeatherTimerItem;

/**
 * Created by burfo on 22/02/2017.
 */
public interface LoxoneEventHandler {
    void configIncoming(LoxoneConfig config);

    void weatherTimerChangeIncoming(WeatherTimerItem value);

    void daytimeTimerChangeIncoming(EventTimerItem values);

    void textMessageIncoming(String value);

    void binaryMessageIncoming(ByteBuf bytes);

    void componentChangeIncoming(ComponentChange change);
}
