package org.chelmer.model.state;

import org.chelmer.model.entity.LoxUuid;

import java.time.LocalTime;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by burfo on 25/02/2017.
 */
public class GlobalStateFactory {
    public static GlobalState createGlobalState(String name, LoxUuid uuid, final Map<Integer, String> operatingModes) {
        GlobalStateItem item = GlobalStateItem.forName(name);

        switch (item) {
            case OPERATING_MODE: {
                Function<Double, String> converter = new Function<Double, String>() {
                    @Override
                    public String apply(Double rawValue) {
                        return operatingModes.get(rawValue.intValue());
                    }
                };

                return new GlobalState<String>(item, uuid, converter);
            }
            case SUNRISE: {
                Function<Double, LocalTime> converter = new Function<Double, LocalTime>() {
                    @Override
                    public LocalTime apply(Double rawValue) {
                        return LocalTime.ofSecondOfDay(rawValue.longValue() * 60);
                    }
                };

                return new GlobalState<LocalTime>(item, uuid, converter);
            }
            case SUNSET: {
                Function<Double, LocalTime> converter = new Function<Double, LocalTime>() {
                    @Override
                    public LocalTime apply(Double rawValue) {
                        return LocalTime.ofSecondOfDay(rawValue.longValue() * 60);
                    }
                };

                return new GlobalState<LocalTime>(item, uuid, converter);
            }
            default: {
                Function<Double, Double> converter = new Function<Double, Double>() {
                    @Override
                    public Double apply(Double rawValue) {
                        return rawValue;
                    }
                };

                return new GlobalState<Double>(item, uuid, converter);
            }
        }
    }
}
