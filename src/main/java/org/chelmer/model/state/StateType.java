package org.chelmer.model.state;

/**
 * Created by burfo on 09/03/2017.
 */
public enum StateType {
    INTEGER(IntegerState.class),
    BOOLEAN(BooleanState.class);

    private Class clazz;

    StateType (Class clazz) {
        this.clazz = clazz;
    }

    public Class getImplementingClass() {
        return clazz;
    }
}
