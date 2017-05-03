package org.chelmer.clientimpl;

import org.chelmer.exceptions.NoSuchComponentException;
import org.chelmer.exceptions.UnexpectedTypeException;
import org.chelmer.model.Component;
import org.chelmer.model.category.Category;
import org.chelmer.model.control.Control;
import org.chelmer.model.entity.LoxUuid;
import org.chelmer.model.entity.Room;

import java.util.*;

/**
 * Created by burfo on 16/02/2017.
 */
public class UuidComponentRegistry {
    private Map<LoxUuid, Component> components = new HashMap<>();

    public void register(LoxUuid uuid, Component component) {
        components.put(uuid, component);
    }

    public Map<LoxUuid, ? extends Component> getAll() {
        return components;
    }

    public Component get(LoxUuid uuid) {
        if (!components.containsKey(uuid)) {
            throw new NoSuchComponentException(uuid.toString());
        }
        return components.get(uuid);
    }

    public <T extends Component> T getComponentOfType(LoxUuid uuid, Class<T> clazz) {
        Component component = get(uuid);

        if (component != null && !(component.getClass().isAssignableFrom(clazz))) {
            throw new UnexpectedTypeException(String.format("Component with uuid %s is of type %s, expected %s", uuid, component.getClass().getName(), clazz.getName()));
        }

        return (T) component;
    }

    public Room getRoom(LoxUuid uuid) {
        return getComponentOfType(uuid, Room.class);
    }

    public Category getCategory(LoxUuid uuid) {
        return getComponentOfType(uuid, Category.class);
    }

    public <T extends Control> List<T> getControls(Class<T> controlType) {
        List<T> controls = new ArrayList<T>();
        for (Component component : components.values()) {
            if (controlType.isInstance(component)) {
                controls.add((T) component);
            }
        }
        return controls;
    }
}
