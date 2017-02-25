package org.chelmer.model.state;

import com.fasterxml.jackson.annotation.JacksonInject;
import org.chelmer.clientimpl.UuidComponentRegistry;
import org.chelmer.model.UuidComponent;
import org.chelmer.model.entity.LoxUuid;

/**
 * Created by burfo on 25/02/2017.
 */
public class GlobalState implements UuidComponent {
    private final String name;
    private final LoxUuid uuid;
    private Double value = null;

    public GlobalState(String name, LoxUuid uuid) {
        this.name = name;
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    @Override
    public Double getValue() {
        return value;
    }

    @Override
    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "GlobalState{" +
                "name='" + name + '\'' +
                ", uuid=" + uuid +
                '}';
    }

    public LoxUuid getUuid() {
        return uuid;
    }

    @JacksonInject
    public void setRegistry(UuidComponentRegistry registry) {
        registry.register(uuid, this);
    }
}
