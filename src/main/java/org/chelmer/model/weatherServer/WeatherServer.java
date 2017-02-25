package org.chelmer.model.weatherServer;

import org.chelmer.model.entity.LoxUuid;

import java.util.Map;

public class WeatherServer {
    private final Map<String, LoxUuid> states;
    private final WeatherServerFormat format;
    private final Map<Integer, String> weatherTypeTexts;
    private final Map<Integer, WeatherFieldType> weatherFieldTypes;

    public WeatherServer(Map<String, LoxUuid> states, WeatherServerFormat format, Map<Integer, String> weatherTypeTexts, Map<Integer, WeatherFieldType> weatherFieldTypes) {
        this.states = states;
        this.format = format;
        this.weatherTypeTexts = weatherTypeTexts;
        this.weatherFieldTypes = weatherFieldTypes;
    }

    public Map<String, LoxUuid> getStates() {
        return states;
    }

    public WeatherServerFormat getFormat() {
        return format;
    }

    public Map<Integer, String> getWeatherTypeTexts() {
        return weatherTypeTexts;
    }

    public Map<Integer, WeatherFieldType> getWeatherFieldTypes() {
        return weatherFieldTypes;
    }
}
