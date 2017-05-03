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
public class IRoomControllerControl extends Control {
    private static final String TEMPERATURES = "temperatures";
    private static final String MANUALMODE = "manualMode";
    private static final String MOVEMENT = "movement";
    private static final String OVERRIDETOTAL = "overrideTotal";
    private static final String OPENWINDOW = "openWindow";
    private static final String ISPREPARING = "isPreparing";
    private static final String OVERRIDE = "override";
    private static final String CURRCOOLTEMPIX = "currCoolTempIx";
    private static final String CURRHEATTEMPIX = "currHeatTempIx";
    private static final String SERVICEMODE = "serviceMode";
    private static final String MODE = "mode";
    private static final String ERROR = "error";
    private static final String TEMPACTUAL = "tempActual";
    private static final String TEMPTARGET = "tempTarget";

    private static final Map<String, StateType> stateTypes;

    static {
        Map<String, StateType> mutableStateTypes = new HashMap<>();

        // mutableStateTypes.put(TEMPERATURES, // TODO: An Array
        mutableStateTypes.put(MANUALMODE, StateType.INTEGER);
        mutableStateTypes.put(MOVEMENT, StateType.INTEGER);
        mutableStateTypes.put(OVERRIDETOTAL, StateType.INTEGER);
        mutableStateTypes.put(OPENWINDOW, StateType.BOOLEAN);
        mutableStateTypes.put(ISPREPARING, StateType.INTEGER);
        mutableStateTypes.put(OVERRIDE, StateType.INTEGER);
        mutableStateTypes.put(CURRCOOLTEMPIX, StateType.INTEGER);
        mutableStateTypes.put(CURRHEATTEMPIX, StateType.INTEGER);
        mutableStateTypes.put(SERVICEMODE, StateType.INTEGER);
        mutableStateTypes.put(MODE, StateType.INTEGER);
        mutableStateTypes.put(ERROR, StateType.INTEGER);
        mutableStateTypes.put(TEMPACTUAL, StateType.INTEGER);
        mutableStateTypes.put(TEMPTARGET, StateType.INTEGER);

        stateTypes = Collections.unmodifiableMap(mutableStateTypes);
    }

    public IRoomControllerControl(String name, ControlType type, LoxUuid uuidAction, int defaultRating, boolean isSecured) {
        super(name, type, uuidAction, defaultRating, isSecured);
    }

    @Override
    protected Map<String, StateType> getStateTypes() {
        return stateTypes;
    }
}
