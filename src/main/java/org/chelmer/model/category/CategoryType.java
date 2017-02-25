package org.chelmer.model.category;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by burfo on 14/02/2017.
 */
public enum CategoryType {
    MULTIMEDIA("multimedia"),
    LIGHTS("lights"),
    UNDEFINED("undefined"),
    INDOORTEMPERATURE("indoortemperature");

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryType.class);
    private final String displayName;

    private CategoryType(String displayName) {
        this.displayName = displayName;
    }

    @JsonCreator
    public static CategoryType fromValue(final JsonNode jsonNode) {
        for (CategoryType type : CategoryType.values()) {
            if (type.displayName.equals(jsonNode.asText())) {
                return type;
            }
        }

        LOGGER.warn("Unknown value for category type: " + jsonNode.asText() + " using UNDEFINED");
        return UNDEFINED;
    }
}
