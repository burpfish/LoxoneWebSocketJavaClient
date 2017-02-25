package org.chelmer.clientimpl;

import org.chelmer.exceptions.NoSuchComponentException;
import org.chelmer.exceptions.UnexpectedTypeException;
import org.chelmer.model.UuidComponent;
import org.chelmer.model.category.Category;
import org.chelmer.model.entity.LoxUuid;
import org.chelmer.model.entity.Room;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by burfo on 16/02/2017.
 */
public class UuidComponentRegistry {
    private Map<LoxUuid, UuidComponent> components = new HashMap<>();

    public void register(LoxUuid uuid, UuidComponent component) {
        components.put(uuid, component);
    }

    public Map<LoxUuid, ? extends UuidComponent> getAll() {
        return components;
    }

    public UuidComponent get(LoxUuid uuid) {
        if (!components.containsKey(uuid)) {
            throw new NoSuchComponentException(uuid.toString());
        }
        return components.get(uuid);
    }

    public <T extends UuidComponent> T getComponentOfType(LoxUuid uuid, Class<T> clazz) {
        UuidComponent component = get(uuid);

        if (!(component.getClass().isAssignableFrom(clazz))) {
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
}
