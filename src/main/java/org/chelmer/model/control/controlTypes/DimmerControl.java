package org.chelmer.model.control.controlTypes;

import org.chelmer.model.control.Control;
import org.chelmer.model.control.ControlType;
import org.chelmer.model.entity.LoxUuid;
import org.chelmer.model.state.IntegerState;
import org.chelmer.model.state.StateType;

import java.util.*;

/**
 * Created by burfo on 15/02/2017.
 */

public class DimmerControl extends Control {
    private static final String STEP_STATE_NAME = "step";
    private static final String MAX_STATE_NAME = "max";
    private static final String MIN_STATE_NAME = "min";
    private static final String POSITION_STATE_NAME = "position";

    private static final Map<String, StateType> stateTypes;
    private static final Collection<Command> commands;

    static {
        Map<String, StateType> mutableStateTypes = new HashMap<>();
        mutableStateTypes.put(STEP_STATE_NAME, StateType.INTEGER);
        mutableStateTypes.put(MAX_STATE_NAME, StateType.INTEGER);
        mutableStateTypes.put(MIN_STATE_NAME, StateType.INTEGER);
        mutableStateTypes.put(POSITION_STATE_NAME, StateType.INTEGER);
        stateTypes = Collections.unmodifiableMap(mutableStateTypes);

        List<Command> mutableCommands = new ArrayList<>();
        mutableCommands.add(Command.SET_VALUE);
        commands = Collections.unmodifiableList(mutableCommands);
    }

    public DimmerControl(String name, ControlType type, LoxUuid uuidAction, int defaultRating, boolean isSecured) {
        super(name, type, uuidAction, defaultRating, isSecured);
    }

    @Override
    protected Map<String, StateType> getStateTypes() {
        return stateTypes;
    }

    public void invokeCommand(DimmerControl.DimmerCommand command) {
        invokeCommand(command.getGenericCommand());
    }

    public void invokeCommand(DimmerControl.DimmerValueCommand command, int value) {
        invokeCommand(command.getGenericCommand(), value);
    }

    public enum DimmerCommand {
        ON (Command.ON),
        OFF (Command.OFF);

        private final Command genericCommand;

        DimmerCommand(Command genericCommand) {
            this.genericCommand = genericCommand;
        }

        public Command getGenericCommand() {
            return genericCommand;
        }
    };

    public enum DimmerValueCommand {
        SET_VALUE(Command.SET_VALUE);

        private final Command genericCommand;

        DimmerValueCommand(Command genericCommand) {
            this.genericCommand = genericCommand;
        }

        public Command getGenericCommand() {
            return genericCommand;
        }
    };

    @Override
    protected Collection<Command> getCommands() {
        return commands;
    }

    @Override
    public Integer getValue() {
        if (getRawValue() == null || getRawValue() < 0) {
            return null;
        }

        return getRawValue().intValue();
    }

    public Integer getBrightness() {
        return getValue();
    }

    public void setBrightness(Integer brightness) {
        invokeCommand(DimmerControl.DimmerValueCommand.SET_VALUE, brightness);
    }

//    @Override
//    public final void setRawValue(Double rawValue) {
//        // donut
//        System.out.println(">>>>> Dim " + rawValue);
//        super.setRawValue(rawValue);
//    }
}
