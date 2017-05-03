package org.chelmer.response;

import com.fasterxml.jackson.annotation.JacksonInject;
import org.chelmer.clientimpl.UuidComponentRegistry;
import org.chelmer.model.Component;
import org.chelmer.model.entity.LoxUuid;

/**
 * Created by burfo on 21/02/2017.
 */
public class ValueItem {
    private final LoxUuid uuid;
    private final double value;
    private UuidComponentRegistry registry;

    public ValueItem(LoxUuid uuid, double value) {
        this.uuid = uuid;
        this.value = value;
    }

    public LoxUuid getUuid() {
        return uuid;
    }

    public double getValue() {
        return value;
    }

    public Component getComponent() {
        return registry.get(uuid);
    }

    @JacksonInject
    public void setRegistry(UuidComponentRegistry registry) {
        this.registry = registry;
    }

    @Override
    public String toString() {
        Component component = getComponent();

        return "ValueItem{" +
                "uuid=" + (component != null ? component.getName() : uuid) +
                ", value=" + value +
                '}';
    }
}
