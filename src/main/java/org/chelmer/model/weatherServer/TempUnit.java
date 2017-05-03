package org.chelmer.model.weatherServer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.JsonNode;
import org.chelmer.exceptions.ParsingException;

public enum TempUnit {
    CELSIUS(0),
    FAHRENHEIT(1);

    private final int id;

    private TempUnit(int id) {
        this.id = id;
    }

    @JsonCreator
    public static TempUnit fromValue(final JsonNode jsonNode) {
        for (TempUnit type : TempUnit.values()) {
            if (type.id == (jsonNode.asInt())) {
                return type;
            }
        }

        throw new ParsingException("Invalid value for TempUnit: " + jsonNode.get("value").asText());
    }
}
