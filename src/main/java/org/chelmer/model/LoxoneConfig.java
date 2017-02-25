package org.chelmer.model;

import com.fasterxml.jackson.annotation.JacksonInject;
import org.chelmer.clientimpl.UuidComponentRegistry;
import org.chelmer.model.autopilot.Autopilot;
import org.chelmer.model.category.Category;
import org.chelmer.model.control.Control;
import org.chelmer.model.entity.LoxUuid;
import org.chelmer.model.entity.Room;
import org.chelmer.model.entity.Time;
import org.chelmer.model.miniserver.MsInfo;
import org.chelmer.model.state.GlobalState;
import org.chelmer.model.weatherServer.WeatherServer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LoxoneConfig {
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
    public LoxoneConfig(LocalDateTime lastModified, MsInfo msInfo, Map<String, LoxUuid> globalStates, Map<Integer, String> operatingModes, Map<String, Room> rooms, Map<String, Category> cats, Map<String, Control> controls, WeatherServer weatherServer, Map<Integer, Time> times, Map<String, Autopilot> autopilot) {
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
            this.globalStates.add(new GlobalState(entry.getKey(), entry.getValue()));
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

    public Map<String, Control> getControls() {
        return controls;
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

    @JacksonInject
    public void setRegistry(UuidComponentRegistry registry) {
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
}
