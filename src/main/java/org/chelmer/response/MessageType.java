package org.chelmer.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by burfo on 15/02/2017.
 */
public enum MessageType {
    TEXT_MESSAGE(0),
    BINARY_FILE(1),
    VALUE_TABLE(2),
    TEXT_TABLE(3),
    DAYTIMER_TABLE(4),
    OUT_OF_SERVICE(5),
    KEEPALIVE_RESPONSE(6),
    WEATHER_TABLE(7),
    UNDEFINED(-1);

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageType.class);

    private int typeId;

    private MessageType(int typeId) {
        this.typeId = typeId;
    }

    public static MessageType fromValue(final int typeId) {
        for (MessageType type : MessageType.values()) {
            if (type.typeId == typeId) {
                return type;
            }
        }

        LOGGER.warn("Unknown value for message type: " + typeId + " using UNDEFINED");
        return UNDEFINED;
    }

    public int getTypeId() {
        return typeId;
    }

}
