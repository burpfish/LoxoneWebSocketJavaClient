package org.chelmer.model.control.controlTypes;

import org.chelmer.model.control.ControlType;
import org.chelmer.model.entity.LoxUuid;
import org.chelmer.model.state.StateType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by burfo on 14/02/2017.
 */
public class RemoteControl extends BooleanControl {
    private static final String TIMEOUT_STATE_NAME = "timeout";
    private static final String MODE_STATE_NAME = "mode";
    private static final String ACTIVE_STATE_NAME = "active";

    private static final Map<String, StateType> stateTypes;

    static {
        Map<String, StateType> mutableStateTypes = new HashMap();
        mutableStateTypes.put(TIMEOUT_STATE_NAME, StateType.INTEGER);
        mutableStateTypes.put(MODE_STATE_NAME, StateType.INTEGER);
        mutableStateTypes.put(ACTIVE_STATE_NAME, StateType.BOOLEAN);
        stateTypes = Collections.unmodifiableMap(mutableStateTypes);
    }

    public RemoteControl(String name, ControlType type, LoxUuid uuidAction, int defaultRating, boolean isSecured) {
        super(name, type, uuidAction, defaultRating, isSecured);
    }

    @Override
    protected Map<String, StateType> getStateTypes() {
        return stateTypes;
    }
}
