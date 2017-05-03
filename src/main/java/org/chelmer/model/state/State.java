package org.chelmer.model.state;

import com.fasterxml.jackson.annotation.JacksonInject;
import org.chelmer.clientimpl.UuidComponentRegistry;
import org.chelmer.model.ComponentBase;
import org.chelmer.model.control.Control;
import org.chelmer.model.entity.LoxUuid;

import java.util.Collection;

public class State extends ComponentBase {
    private final Collection<LoxUuid> uuids;
    private Control control = null;

    public State(Collection<LoxUuid> uuids, String name, Control control) {
        super(name);

        this.uuids = uuids;
        this.control = control;
    }

    @JacksonInject
    public void setRegistry(UuidComponentRegistry registry) {
        for (LoxUuid uuid : uuids) {
            registry.register(uuid, this);
        }
    }

    public Collection<LoxUuid> getUuids() {
        return uuids;
    }

    @Override
    public String getName() {
        return control.getName() + "(" + super.getName() + ")";
    }

    @Override
    public String toString() {
        return "State{" +
                "uuids=" + uuids +
                ", name='" + getName() + '\'' +
                ", control=" + control +
                '}';
    }

    public Control getControl() {
        return control;
    }
}
