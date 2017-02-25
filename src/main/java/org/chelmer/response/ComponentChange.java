package org.chelmer.response;

import org.chelmer.model.UuidComponent;

/**
 * Created by burfo on 22/02/2017.
 */
public class ComponentChange {
    private final UuidComponent component;
    private final double value;

    public ComponentChange(UuidComponent component, double value) {
        this.component = component;
        this.value = value;
    }

    @Override
    public String toString() {
        return "ComponentChange{" +
                "component=" + component +
                ", value=" + value +
                '}';
    }
}
