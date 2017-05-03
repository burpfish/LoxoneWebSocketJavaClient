package org.chelmer.model.control.controlTypes;

/**
 *
 */
public enum Command {
    ON("on"),
    OFF("off"),
    PULSE("pulse"),
    SET_VALUE("value");

    public String getCommandStr() {
        return commandStr;
    }

    final String commandStr;

    Command(String commandStr) {
        this.commandStr = commandStr;
    }
}
