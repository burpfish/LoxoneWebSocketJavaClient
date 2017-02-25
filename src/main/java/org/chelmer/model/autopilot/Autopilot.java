package org.chelmer.model.autopilot;

import com.fasterxml.jackson.annotation.JacksonInject;
import org.chelmer.clientimpl.UuidComponentRegistry;
import org.chelmer.model.UuidComponent;
import org.chelmer.model.entity.LoxUuid;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Autopilot implements UuidComponent {
    private final String name;
    private final LoxUuid uuid;
    private final List<AutopilotState> states;

    public Autopilot(String name, LoxUuid uuidAction, Map<String, LoxUuid> states) {
        this.name = name;
        this.uuid = uuidAction;
        this.states = new ArrayList<>();

        for (Map.Entry<String, LoxUuid> entry : states.entrySet()) {
            String stateName = entry.getKey();
            LoxUuid uuid = entry.getValue();

            AutopilotState state = new AutopilotState(stateName, uuid, this);
            this.states.add(state);
        }
    }

    @JacksonInject
    public void setRegistry(UuidComponentRegistry registry) {
        for (AutopilotState state : states) {
            registry.register(state.getUuid(), state);
        }

        registry.register(uuid, this);
    }

    public String getName() {
        return name;
    }

    public LoxUuid getUuid() {
        return uuid;
    }

    public List<AutopilotState> getStates() {
        return states;
    }
}
