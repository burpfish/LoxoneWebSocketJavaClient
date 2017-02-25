package org.chelmer.model.state;

import com.fasterxml.jackson.annotation.JacksonInject;
import org.chelmer.clientimpl.UuidComponentRegistry;
import org.chelmer.exceptions.UnexpectedTypeException;
import org.chelmer.model.control.Control;
import org.chelmer.model.entity.LoxUuid;

import java.util.*;

public class States {
    private final Collection<State> states = new ArrayList<>();

    public States(Map<String, Object> states, Control control) {
        if (states == null) {
            return;
        }

        for (Map.Entry<String, Object> entry : states.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof String) {
                value = Arrays.asList(value);
            }

            if (value instanceof Collection) {
                List<LoxUuid> list = new ArrayList<>();
                for (Object nodeVal : (Collection) value) {
                    if (!(nodeVal instanceof String)) {
                        throw new UnexpectedTypeException("Unexpected type found for State collection value: " + nodeVal.getClass().getName());
                    }
                    LoxUuid uuid = new LoxUuid((String) nodeVal);
                    list.add(uuid);
                }
                this.states.add(new State(list, key, control));
            } else {
                throw new UnexpectedTypeException("Unexpected type found for State: " + value.getClass().getName());
            }
        }
    }

    @JacksonInject
    public void setRegistry(UuidComponentRegistry registry) {
        for (State state : states) {
            state.setRegistry(registry);
        }
    }
}
