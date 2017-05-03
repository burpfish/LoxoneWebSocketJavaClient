package org.chelmer.model.miniserver;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.JsonNode;
import org.chelmer.exceptions.ParsingException;

public enum MiniserverType {
    REGULAR(0),
    GO(1);

    private final int id;

    private MiniserverType(int id) {
        this.id = id;
    }

    @JsonCreator
    public static MiniserverType fromValue(final JsonNode jsonNode) {
        for (MiniserverType type : MiniserverType.values()) {
            if (type.id == (jsonNode.asInt())) {
                return type;
            }
        }

        throw new ParsingException("Invalid value for MiniserverType: " + jsonNode.get("value").asText());
    }
}
