package org.chelmer.response.change;

import org.chelmer.model.Component;

/**
 * Created by burfo on 22/02/2017.
 */
public class ComponentChange {
    private final Component component;
    private final Object previousValue;

    public ComponentChange(Component component, Double value) {
        this.component = component;
        this.previousValue = component.getValue();
        component.setRawValue(value);
    }

    public String getMessage() {
        return String.format("%s : %s -> %s", getComponent().getName(),
                (previousValue == null ? "" : previousValue),
                (component.getValue() == null ? "" : component.getValue()));
    }

    @Override
    public String toString() {
        return "ComponentChange{" +
                "component=" + component +
                ", previousValue=" + previousValue +
                ", newValue=" + component.getValue() +
                '}';
    }

    public Component getComponent() {
        return component;
    }
}
