package org.chelmer.model.control.controlTypes;

import org.chelmer.model.control.ControlType;
import org.chelmer.model.entity.LoxUuid;

import java.util.*;

/**
 * Created by burfo on 14/02/2017.
 */
public class SwitchControl extends BooleanControl {
    private static final Collection<Command> commands;

    public void setState(boolean state) {
        this.invokeCommand(state? SwitchCommand.ON : SwitchCommand.OFF);
    }

    public enum SwitchCommand {
        ON (Command.ON),
        OFF (Command.OFF);

        public Command getGenericCommand() {
            return genericCommand;
        }

        private final Command genericCommand;

        SwitchCommand(Command genericCommand) {
            this.genericCommand = genericCommand;
        }
    }

    static {
        List<Command> mutableCommands = new ArrayList<>();
        mutableCommands.add(Command.ON);
        mutableCommands.add(Command.OFF);
        commands = Collections.unmodifiableList(mutableCommands);
    }

    public SwitchControl(String name, ControlType type, LoxUuid uuidAction, int defaultRating, boolean isSecured) {
        super(name, type, uuidAction, defaultRating, isSecured);
    }

    @Override
    protected Collection<Command> getCommands() {
        return commands;
    }

    public void invokeCommand(SwitchCommand command) {
        invokeCommand(command.getGenericCommand());
    }
}
