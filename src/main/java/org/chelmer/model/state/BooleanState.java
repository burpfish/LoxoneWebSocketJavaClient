package org.chelmer.model.state;

import org.chelmer.model.control.Control;
import org.chelmer.model.entity.LoxUuid;

import java.util.Collection;

public class BooleanState extends State {
    public BooleanState(Collection<LoxUuid> uuids, String name, Control control) {
        super(uuids, name, control);
    }

    @Override
    public Boolean getValue() {
        Double rawValue = getRawValue();
        if (rawValue == null) {
            return null;
        }
        return (rawValue > 0);
    }
}
