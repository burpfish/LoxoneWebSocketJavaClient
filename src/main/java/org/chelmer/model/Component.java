package org.chelmer.model;

/**
 * Created by burfo on 16/02/2017.
 */
public interface Component {
    String getName();

    /**
     * Sets the value of this data object but does not affect the physical device
     */
    void setRawValue(Double rawValue);

    Object getValue();
}
