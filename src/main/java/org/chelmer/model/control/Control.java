package org.chelmer.model.control;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.chelmer.clientimpl.LoxoneWebSocketClient;
import org.chelmer.clientimpl.UuidComponentRegistry;
import org.chelmer.exceptions.ParsingException;
import org.chelmer.exceptions.UnknownCommandException;
import org.chelmer.model.Component;
import org.chelmer.model.ComponentBase;
import org.chelmer.model.category.Category;
import org.chelmer.model.control.controlTypes.*;
import org.chelmer.model.entity.LoxUuid;
import org.chelmer.model.entity.Room;
import org.chelmer.model.state.StateType;
import org.chelmer.model.state.States;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Control extends ComponentBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(Component.class);

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
    private LoxoneWebSocketClient client;

    private int id;
    private static int ID_COUNT = 0;

    public Control(String name, ControlType type, LoxUuid uuid, int defaultRating, boolean isSecured) {
        super(name);
        this.type = type;
        this.uuid = uuid;
        this.defaultRating = defaultRating;
        this.isSecured = isSecured;
        this.id = ID_COUNT++;
    }

    @JsonCreator
    public static Control createControl(@JsonProperty("name") String name, @JsonProperty("type") ControlType type, @JsonProperty("uuidAction") LoxUuid uuid, @JsonProperty("defaultRating") int defaultRating, @JsonProperty("isSecured") boolean isSecured) {
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
                control = new DimmerControl(name, type, uuid, defaultRating, isSecured);
                break;
            default:
                LOGGER.warn("Unknown component of type: " + type);
                control = new Control(name, type, uuid, defaultRating, isSecured);
                break;
        }

        return control;
    }

    public boolean isPresence() {
        return presence;
    }

    public void setPresence(boolean presence) {
        this.presence = presence;
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

    @Override
    public String toString() {
        return "Control{" +
                "name='" + getName() + '\'' +
                ", type=" + type +
                ", uuid=" + uuid +
                '}';
    }

    @JacksonInject
    public void setRegistry(UuidComponentRegistry registry) {


        // System.out.println(this.id + " >> " + uuid.toString());


        this.registry = registry;
        registry.register(uuid, this);
        registerStates(registry);
    }

    @JacksonInject
    public void setClient(LoxoneWebSocketClient client) {
        this.client = client;
    }

    protected void registerStates(UuidComponentRegistry registry) {
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

    protected States getStates() {
        return states;
    }

    public void setStates(Map<String, Object> states) {
        if (this.states == null) {
            this.states = new States();
        }
        this.states.setStates(states, this, getStateTypes());
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
            // subControl.setRegistry(registry);
        }
        this.subControls = subControls;
    }

    public Control getParentControl() {
        return parentControl;
    }

    public void setParentControl(Control parentControl) {
        this.parentControl = parentControl;
    }

    protected Map<String, StateType> getStateTypes() {
        return new HashMap<>();
    }

    protected Collection<Command> getCommands() {
        return new ArrayList<>();
    }

    public void invokeCommand(Command command) {
        checkCommand(command);
        client.sendCommand(this, command);
    }

    public void invokeCommand(Command command, int value) {
        checkCommand(command);
        client.sendCommand(this, command, value);
    }

    private void checkCommand(Command command) {
        if (!(getCommands().contains(command))) {
            String msg = String.format("Unknown command: %s for command of type: %s", command, getClass().getName());
            LOGGER.error(msg);
            throw new UnknownCommandException(msg);
        }

        if (client == null) {
            throw new ParsingException("Loxone client not set for control. Cannot send command.");
        }
    }
}
