package org.chelmer.model.control.controlTypes;

import org.chelmer.model.control.ControlType;
import org.chelmer.model.entity.LoxUuid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by burfo on 15/02/2017.
 */
public class PushButtonControl extends BooleanControl {
    static final Collection<Command> commands;

    public enum PushButtonCommand {
        ON (Command.ON),
        OFF (Command.OFF),
        PULSE (Command.PULSE);

        public Command getGenericCommand() {
            return genericCommand;
        }

        private final Command genericCommand;

        PushButtonCommand(Command genericCommand) {
            this.genericCommand = genericCommand;
        }
    }

    static {
        List<Command> mutableCommands = new ArrayList<>();
        mutableCommands.add(Command.ON);
        mutableCommands.add(Command.OFF);
        mutableCommands.add(Command.PULSE);
        commands = Collections.unmodifiableList(mutableCommands);
    }

    @Override
    protected Collection<Command> getCommands() {
        return commands;
    }

    public void invokeCommand(PushButtonControl.PushButtonCommand command) {
        invokeCommand(command.getGenericCommand());
    }

    public PushButtonControl(String name, ControlType type, LoxUuid uuidAction, int defaultRating, boolean isSecured) {
        super(name, type, uuidAction, defaultRating, isSecured);
    }
}
