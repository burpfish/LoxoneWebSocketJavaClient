package org.chelmer.model.state;

import com.fasterxml.jackson.annotation.JacksonInject;
import org.chelmer.clientimpl.UuidComponentRegistry;
import org.chelmer.model.ComponentBase;
import org.chelmer.model.entity.LoxUuid;

import java.util.function.Function;

/**
 * Created by burfo on 25/02/2017.
 */
public class GlobalState<T> extends ComponentBase {
    private final GlobalStateItem item;
    private final LoxUuid uuid;
    private Function<Double, T> converter;

    public GlobalState(GlobalStateItem item, LoxUuid uuid, Function<Double, T> converter) {
        super(item.getName());
        this.converter = converter;
        this.item = item;
        this.uuid = uuid;
    }

    public GlobalStateItem getItem() {
        return item;
    }

    @Override
    public String toString() {
        return "GlobalState{" +
                "name='" + item + '\'' +
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

    @Override
    public T getValue() {
        if (getRawValue() == null) {
            return null;
        }

        return converter.apply(getRawValue());
    }
}
