package org.chelmer.model.state;

import com.fasterxml.jackson.annotation.JacksonInject;
import org.chelmer.clientimpl.LoxoneWebSocketClient;
import org.chelmer.clientimpl.UuidComponentRegistry;
import org.chelmer.exceptions.UnexpectedTypeException;
import org.chelmer.model.control.Control;
import org.chelmer.model.entity.LoxUuid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

// States owned by a single component
// Hold a reference to the component
// Register all of the guids to this states object
// Binary controls should defer here for vaue
public class States {
    private static final Logger LOGGER = LoggerFactory.getLogger(States.class);

    private final Collection<State> states = new ArrayList<>();
    private Control control;

    public States() {
    }

    public Control getControl() {
        return control;
    }

    public void setStates(Map<String, Object> states, Control control, Map<String, StateType> stateTypes) {
        this.control = control;

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

                State state = null;
                StateType stateTypeKey = stateTypes.get(key);
                if (stateTypeKey != null) {
                    Class stateType = stateTypeKey.getImplementingClass();
                    try {
                        try {
                            state = (State) stateType.getConstructor(Collection.class, String.class, Control.class).newInstance(list, key, control);
                        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException("Cannot create instance of state type: " + stateType.getName(), e);
                    }
                } else {
                    LOGGER.warn(String.format("State %s not declared for type %s", key, control.getClass().getName()));
                    state = new State(list, key, control);
                }

                this.states.add(state);
            } else {
                throw new UnexpectedTypeException("Unexpected type found for State: " + value.getClass().getName());
            }
        }
    }

    public State getOrThrow(String stateName) {
        for (State state : this.states) {
            if (stateName.equals(state.getName())) {
                return state;
            }
        }

        throw new RuntimeException("blah blah blah");
    }

    public Collection<State> getStates() {
        return states;
    }

    @JacksonInject
    public void setRegistry(UuidComponentRegistry registry) {
        for (State state : states) {
            state.setRegistry(registry);
        }
    }
}
