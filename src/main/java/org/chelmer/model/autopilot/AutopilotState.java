package org.chelmer.model.autopilot;

import org.chelmer.model.UuidComponent;
import org.chelmer.model.entity.LoxUuid;

/**
 * Created by burfo on 20/02/2017.
 */
public class AutopilotState implements UuidComponent {
    private final String name;
    private final LoxUuid uuid;
    private final Autopilot autopilot;
    private Double value = null;

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public AutopilotState(String name, LoxUuid value, Autopilot autopilot) {
        this.name = name;
        this.uuid = value;
        this.autopilot = autopilot;
    }

    @Override
    public String getName() {
        return autopilot.getName() + "(" + name + ")";
    }

    public LoxUuid getUuid() {
        return uuid;
    }

    public Autopilot getAutopilot() {
        return autopilot;
    }
}
