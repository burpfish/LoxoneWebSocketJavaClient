package org.chelmer.model.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by burfo on 28/02/2017.
 */
public enum GlobalStateItem {
    OPERATING_MODE("operatingMode"),
    SUNRISE("sunrise"),
    SUNSET("sunset"),
    NOTIFICATIONS("notifications"),
    MODIFICATIONS("modifications"),
    UNKNOWN("unknown");

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalStateItem.class);
    private final String name;

    private GlobalStateItem(String name) {
        this.name = name;
    }

    public static GlobalStateItem forName(String name) {
        for (GlobalStateItem type : GlobalStateItem.values()) {
            if (type.name.equals(name)) {
                return type;
            }
        }

        LOGGER.warn("Unknown Global State: " + name);
        return UNKNOWN;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
