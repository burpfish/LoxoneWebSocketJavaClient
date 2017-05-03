package org.chelmer.model.control.controlTypes;

import org.chelmer.model.control.Control;
import org.chelmer.model.control.ControlType;
import org.chelmer.model.entity.LoxUuid;
import org.chelmer.model.state.BooleanState;
import org.chelmer.model.state.IntegerState;
import org.chelmer.model.state.StateType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by burfo on 15/02/2017.
 */
public class IrcDayTimerControl extends Control {
    private static final String VALUE = "value";
    private static final String MODELIST = "modeList";
    private static final String MODE = "mode";
    private static final String ENTRIESANDDEFAULTVALUE = "entriesAndDefaultValue";

    private static final Map<String, StateType> stateTypes;

    static {
        Map<String, StateType> mutableMap = new HashMap<>();
        mutableMap.put(VALUE, StateType.INTEGER);
        mutableMap.put(MODELIST, StateType.INTEGER);
        mutableMap.put(MODE, StateType.INTEGER);
        mutableMap.put(ENTRIESANDDEFAULTVALUE, StateType.BOOLEAN);
        stateTypes = Collections.unmodifiableMap(mutableMap);
    }

    public IrcDayTimerControl(String name, ControlType type, LoxUuid uuidAction, int defaultRating, boolean isSecured) {
        super(name, type, uuidAction, defaultRating, isSecured);
    }

    @Override
    protected Map<String, StateType> getStateTypes() {
        return stateTypes;
    }
}
