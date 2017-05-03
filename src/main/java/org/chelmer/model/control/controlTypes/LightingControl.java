package org.chelmer.model.control.controlTypes;

import org.chelmer.model.control.Control;
import org.chelmer.model.control.ControlType;
import org.chelmer.model.entity.LoxUuid;
import org.chelmer.model.state.IntegerState;
import org.chelmer.model.state.StateType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by burfo on 14/02/2017.
 */
public class LightingControl extends Control {
    private static final String ACTIVE_SCENE_STATE_NAME = "activeScene";
    private static final String SCENE_LIST_STATE_NAME = "sceneList";
    private static final String PRESENCE_FROM_STATE_NAME = "presenceFrom";
    private static final String PRESENCE_TO_STATE_NAME = "presenceTo";

    private static final Map<String, StateType> stateTypes;

    static {
        Map<String, StateType> mutableStateTypes = new HashMap<>();
        mutableStateTypes.put(ACTIVE_SCENE_STATE_NAME, StateType.INTEGER);
//        mutableMap.put(PRESENCE_FROM_STATE_NAME , ???.class);
//        mutableMap.put(PRESENCE_TO_STATE_NAME , ???.class);
//        mutableMap.put(SCENE_LIST_STATE_NAME , ???.class);
        stateTypes = Collections.unmodifiableMap(mutableStateTypes);
    }

    private boolean presence;

    public LightingControl(String name, ControlType type, LoxUuid uuidAction, int defaultRating, boolean isSecured) {
        super(name, type, uuidAction, defaultRating, isSecured);

//        this.presence = presence;
    }

    public boolean getPresence() {
        return presence;
    }

    public void setPresence(boolean presence) {
        this.presence = presence;
    }

    @Override
    protected Map<String, StateType> getStateTypes() {
        return stateTypes;
    }
}
