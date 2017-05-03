package org.chelmer.model;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.chelmer.clientimpl.UuidComponentRegistry;
import org.chelmer.model.autopilot.Autopilot;
import org.chelmer.model.category.Category;
import org.chelmer.model.control.Control;
import org.chelmer.model.entity.LoxUuid;
import org.chelmer.model.entity.Room;
import org.chelmer.model.entity.Time;
import org.chelmer.model.miniserver.MsInfo;
import org.chelmer.model.state.GlobalState;
import org.chelmer.model.state.GlobalStateFactory;
import org.chelmer.model.weatherServer.WeatherServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.*;

public class LoxoneConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoxoneConfig.class);

    private final LocalDateTime lastModified;
    private final MsInfo msInfo;
    private final List<GlobalState> globalStates;
    private final Map<Integer, String> operatingModes;
    private final Map<String, Room> rooms;
    private final Map<String, Category> cats;
    private final Map<String, Control> controls;
    private final WeatherServer weatherServer;
    private final Map<Integer, Time> times;
    private final Map<String, Autopilot> autopilot;
    private UuidComponentRegistry registry;

    @JsonCreator
    public LoxoneConfig(@JsonProperty("lastModified") LocalDateTime lastModified, @JsonProperty("msInfo") MsInfo msInfo, @JsonProperty("globalStates") Map<String, LoxUuid> globalStates, @JsonProperty("operatingModes") Map<Integer, String> operatingModes, @JsonProperty("rooms") Map<String, Room> rooms, @JsonProperty("cats") Map<String, Category> cats, @JsonProperty("controls") Map<String, Control> controls, @JsonProperty("weatherServer") WeatherServer weatherServer, @JsonProperty("times") Map<Integer, Time> times, @JsonProperty("autopilot") Map<String, Autopilot> autopilot) {
        this.lastModified = lastModified;
        this.msInfo = msInfo;
        this.operatingModes = operatingModes;
        this.rooms = rooms;
        this.cats = cats;
        this.controls = controls;
        this.weatherServer = weatherServer;
        this.times = times;
        this.autopilot = autopilot;

        this.globalStates = new ArrayList<>();
        for (Map.Entry<String, LoxUuid> entry : globalStates.entrySet()) {
            this.globalStates.add(new GlobalStateFactory().createGlobalState(entry.getKey(), entry.getValue(), operatingModes));
        }
    }

    public List<GlobalState> getGlobalStates() {
        return globalStates;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public MsInfo getMsInfo() {
        return msInfo;
    }

    public Map<Integer, String> getOperatingModes() {
        return operatingModes;
    }

    public Map<String, Room> getRooms() {
        return rooms;
    }

    public Map<String, Category> getCats() {
        return cats;
    }

    public WeatherServer getWeatherServer() {
        return weatherServer;
    }

    public Map<Integer, Time> getTimes() {
        return times;
    }

    public Map<String, Autopilot> getAutopilot() {
        return autopilot;
    }

    public Map<String, Control> getControls() {
        return controls;
    }

    @JacksonInject
    public void setRegistry(UuidComponentRegistry registry) {
        this.registry = registry;
        for (GlobalState gs : globalStates) {
            gs.setRegistry(registry);
        }
    }

    @Override
    public String toString() {
        return "LoxoneConfig{" +
                "lastModified=" + lastModified +
                ", msInfo=" + msInfo +
                ", globalStates=" + globalStates +
                ", operatingModes=" + operatingModes +
                ", rooms=" + rooms +
                ", cats=" + cats +
                ", controls=" + controls +
                ", weatherServer=" + weatherServer +
                ", times=" + times +
                ", autopilot=" + autopilot +
                '}';
    }

    public <T extends Component> T getControl(LoxUuid id, Class<T> clazz) {
        // donut - remove this class when the caller is using the registry directly
        return registry.getComponentOfType(id, clazz);
    }

    public <T extends Control> List<T> getControls(Class<T> controlType) {
        // donut - remove this class when the caller is using the registry directly
        return registry.getControls(controlType);
    }
}
