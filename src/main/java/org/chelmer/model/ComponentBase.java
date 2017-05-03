package org.chelmer.model;

/**
 * Created by burfo on 28/02/2017.
 */
public abstract class ComponentBase implements Component {
    private final String name;
    private Double rawValue;

    public ComponentBase(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getValue() {
        return rawValue;
    }

    public Double getRawValue() {
        return rawValue;
    }

    @Override
    public  final void setRawValue(Double rawValue) {
        this.rawValue = rawValue;
    }

}
