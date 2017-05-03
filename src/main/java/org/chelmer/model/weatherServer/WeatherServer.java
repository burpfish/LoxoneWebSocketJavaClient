package org.chelmer.model.weatherServer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.chelmer.model.entity.LoxUuid;

import java.util.Map;

public class WeatherServer {
    private final Map<String, LoxUuid> states;
    private final WeatherServerFormat format;
    private final Map<Integer, String> weatherTypeTexts;
    private final Map<Integer, WeatherFieldType> weatherFieldTypes;

    @JsonCreator
    public WeatherServer(@JsonProperty("states") Map<String, LoxUuid> states, @JsonProperty("format") WeatherServerFormat format, @JsonProperty("weatherTypeTexts") Map<Integer, String> weatherTypeTexts, @JsonProperty("weatherFieldTypes") Map<Integer, WeatherFieldType> weatherFieldTypes) {
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
