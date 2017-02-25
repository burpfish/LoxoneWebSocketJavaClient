package org.chelmer.model.control;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by burfo on 14/02/2017.
 */
public enum ControlType {
    UNDEFINED("Undefined"),
    SWITCH("Switch"),
    PUSHBUTTON("Pushbutton"),
    LIGHTCONTROLLER("LightController"),
    REMOTE("Remote"),
    APPLICATION("Application"),
    IRCDAYTIMER("IRCDaytimer"),
    IROOMCONTROLLER("IRoomController"),
    DIMMER("Dimmer");

    private static final Logger LOGGER = LoggerFactory.getLogger(ControlType.class);

    private final String displayName;

    private ControlType(String displayName) {
        this.displayName = displayName;
    }

    @JsonCreator
    public static ControlType fromValue(final JsonNode jsonNode) {
        for (ControlType type : ControlType.values()) {
            if (type.displayName.equals(jsonNode.asText())) {
                return type;
            }
        }

        LOGGER.warn("Unknown value for control type: " + jsonNode.asText() + " using UNDEFINED");
        return UNDEFINED;
    }
}
