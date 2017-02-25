package org.chelmer.model.control.controlTypes;

import org.chelmer.model.control.Control;
import org.chelmer.model.control.ControlType;
import org.chelmer.model.entity.LoxUuid;

/**
 * Created by burfo on 14/02/2017.
 */
public class SwitchControl extends Control {
    public SwitchControl(String name, ControlType type, LoxUuid uuidAction, int defaultRating, boolean isSecured) {
        super(name, type, uuidAction, defaultRating, isSecured);
    }

    public LoxUuid getActiveState() {
//        return getStates().getStateAsUuid("active");
        return null;
    }
}
