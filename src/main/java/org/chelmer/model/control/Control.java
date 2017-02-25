package org.chelmer.model.control;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.chelmer.clientimpl.UuidComponentRegistry;
import org.chelmer.exceptions.CouldNotDeserializeException;
import org.chelmer.model.UuidComponent;
import org.chelmer.model.category.Category;
import org.chelmer.model.control.controlTypes.*;
import org.chelmer.model.entity.LoxUuid;
import org.chelmer.model.entity.Room;
import org.chelmer.model.state.States;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class Control implements UuidComponent {
    private static final Logger LOGGER = LoggerFactory.getLogger(UuidComponent.class);

    private final String name;
    private final ControlType type;

    @JsonProperty("uuidAction")
    private final LoxUuid uuid;

    private final int defaultRating;
    private final boolean isSecured;

    @JsonProperty("room")
    private LoxUuid roomUuid;

    @JsonProperty("cat")
    private LoxUuid categoryUuid;

    private boolean isFavorite;
    private boolean presence;
    private States states;
    private Details details;
    private Map<String, Control> subControls;
    private UuidComponentRegistry registry;
    private Control parentControl;

    public Control(String name, ControlType type, LoxUuid uuid, int defaultRating, boolean isSecured) {
        this.name = name;
        this.type = type;
        this.uuid = uuid;
        this.defaultRating = defaultRating;
        this.isSecured = isSecured;
    }

    @JsonCreator
    public static Control createControl(String name, ControlType type, LoxUuid uuid, int defaultRating, boolean isSecured) {
        Control control = null;

        switch (type) {
            case SWITCH:
                control = new SwitchControl(name, type, uuid, defaultRating, isSecured);
                break;
            case LIGHTCONTROLLER:
                control = new LightingControl(name, type, uuid, defaultRating, isSecured);
                break;
            case PUSHBUTTON:
                control = new PushButtonControl(name, type, uuid, defaultRating, isSecured);
                break;
            case REMOTE:
                control = new RemoteControl(name, type, uuid, defaultRating, isSecured);
                break;
            case APPLICATION:
                control = new ApplicationControl(name, type, uuid, defaultRating, isSecured);
                break;
            case IRCDAYTIMER:
                control = new IrcDayTimerControl(name, type, uuid, defaultRating, isSecured);
                break;
            case IROOMCONTROLLER:
                control = new IRoomControllerControl(name, type, uuid, defaultRating, isSecured);
                break;
            case DIMMER:
                control = new LightingControl(name, type, uuid, defaultRating, isSecured);
                break;
            default:
                throw new CouldNotDeserializeException("Ignoring component of type: " + type);
        }

        return control;
    }

    public boolean isPresence() {
        return presence;
    }

    public void setPresence(boolean presence) {
        this.presence = presence;
    }

    public String getName() {
        return name;
    }

    public ControlType getType() {
        return type;
    }

    public LoxUuid getUuid() {
        return uuid;
    }

    public int getDefaultRating() {
        return defaultRating;
    }

    public boolean isSecured() {
        return isSecured;
    }

    public LoxUuid getRoomUuid() {
        return roomUuid;
    }

    public void setRoomUuid(LoxUuid roomUuid) {
        this.roomUuid = roomUuid;
    }

    @JacksonInject
    public void setRegistry(UuidComponentRegistry registry) {
        this.registry = registry;
        registry.register(uuid, this);

        if (states != null) {
            states.setRegistry(registry);
        }
    }

    public Room getRoom() {
        return registry.getRoom(roomUuid);
    }

    public LoxUuid getCategoryUuid() {
        return categoryUuid;
    }

    public void setCategoryUuid(LoxUuid categoryUuid) {
        this.categoryUuid = categoryUuid;
    }

    public Category getCategory() {
        return registry.getCategory(categoryUuid);
    }

    public void setStates(Map<String, Object> states) {
        this.states = new States(states, this);
    }

    public Details getDetails() {
        return details;
    }

    public void setDetails(Details details) {
        this.details = details;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public Map<String, Control> getSubControls() {
        return subControls;
    }

    public void setSubControls(Map<String, Control> subControls) {
        for (Control subControl : subControls.values()) {
            subControl.setParentControl(this);
        }
        this.subControls = subControls;
    }

    public Control getParentControl() {
        return parentControl;
    }

    public void setParentControl(Control parentControl) {
        this.parentControl = parentControl;
    }
}
