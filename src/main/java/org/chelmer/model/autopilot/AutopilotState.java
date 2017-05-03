package org.chelmer.model.autopilot;

import org.chelmer.model.ComponentBase;
import org.chelmer.model.entity.LoxUuid;

/**
 * Created by burfo on 20/02/2017.
 */
public class AutopilotState extends ComponentBase {
    private final LoxUuid uuid;
    private final Autopilot autopilot;

    public AutopilotState(String name, LoxUuid value, Autopilot autopilot) {
        super(name);
        this.uuid = value;
        this.autopilot = autopilot;
    }

    @Override
    public String getName() {
        return autopilot.getName() + "(" + super.getName() + ")";
    }

    public LoxUuid getUuid() {
        return uuid;
    }

    public Autopilot getAutopilot() {
        return autopilot;
    }
}
