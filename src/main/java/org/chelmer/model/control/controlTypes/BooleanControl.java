package org.chelmer.model.control.controlTypes;

import org.chelmer.model.control.Control;
import org.chelmer.model.control.ControlType;
import org.chelmer.model.entity.LoxUuid;
import org.chelmer.model.state.BooleanState;
import org.chelmer.model.state.StateType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by burfo on 01/03/2017.
 */
public class BooleanControl extends Control {
    private static final String ACTIVE_STATE_NAME = "active";

    private static final Map<String, StateType> stateTypes;

    static {
        Map<String, StateType> mutableStateTypes = new HashMap<>();
        mutableStateTypes.put(ACTIVE_STATE_NAME, StateType.BOOLEAN);
        stateTypes = Collections.unmodifiableMap(mutableStateTypes);
    }

    public BooleanControl(String name, ControlType type, LoxUuid uuid, int defaultRating, boolean isSecured) {
        super(name, type, uuid, defaultRating, isSecured);
    }

    @Override
    protected Map<String, StateType> getStateTypes() {
        return stateTypes;
    }

    @Override
    public Object getValue() {
        if (getRawValue() == null) {
            return null;
        }

        return getRawValue() > 0;
    }

    public boolean getState() {
        return (boolean)getValue();
    }
}
