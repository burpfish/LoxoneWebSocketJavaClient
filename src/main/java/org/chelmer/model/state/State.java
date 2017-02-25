package org.chelmer.model.state;

import com.fasterxml.jackson.annotation.JacksonInject;
import org.chelmer.clientimpl.UuidComponentRegistry;
import org.chelmer.model.UuidComponent;
import org.chelmer.model.control.Control;
import org.chelmer.model.entity.LoxUuid;

import java.util.Collection;

public class State implements UuidComponent {
    private final Collection<LoxUuid> uuids;
    private final String name;
    private Control control = null;

    public State(Collection<LoxUuid> uuids, String name, Control control) {
        this.uuids = uuids;
        this.name = name;
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
        return control.getName() + "(" + name + ")";
    }

    public Control getControl() {
        return control;
    }
}
