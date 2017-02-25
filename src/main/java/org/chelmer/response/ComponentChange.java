package org.chelmer.response;

import org.chelmer.model.UuidComponent;

/**
 * Created by burfo on 22/02/2017.
 */
public class ComponentChange {
    private final UuidComponent component;
    private final double value;

    public Double getBeforeValue() {
        return beforeValue;
    }

    public void setBeforeValue(Double beforeValue) {
        this.beforeValue = beforeValue;
    }

    private Double beforeValue;

    public ComponentChange(UuidComponent component, double value) {
        this.component = component;
        this.value = value;
    }

    @Override
    public String toString() {
        return "ComponentChange{" +
                "component=" + component +
                ", value=" + value +
                ", beforeValue=" + beforeValue +
                '}';
    }

    public UuidComponent getComponent() {
        return component;
    }

    public double getValue() {
        return value;
    }
}
