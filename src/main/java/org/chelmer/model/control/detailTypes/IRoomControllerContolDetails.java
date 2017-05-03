package org.chelmer.model.control.detailTypes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.chelmer.model.control.ControlDetails;
import org.chelmer.model.weatherServer.Temperature;

import java.util.Map;

/**
 * Created by burfo on 15/02/2017.
 */
public class IRoomControllerContolDetails extends ControlDetails {
    private int restrictedToMode;
    private String heatPeriodStart;
    private String heatPeriodEnd;
    private String coolPeriodStart;
    private String coolPeriodEnd;
    private String format;
    private Map<Integer, Temperature> temperatures;

    @JsonCreator
    public IRoomControllerContolDetails(@JsonProperty("restrictedToMode") int restrictedToMode, @JsonProperty("heatPeriodStart") String heatPeriodStart, @JsonProperty("heatPeriodEnd") String heatPeriodEnd, @JsonProperty("coolPeriodStart") String coolPeriodStart, @JsonProperty("coolPeriodEnd") String coolPeriodEnd, @JsonProperty("temperatures") Map<Integer, Temperature> temperatures, @JsonProperty("format") String format) {
        this.restrictedToMode = restrictedToMode;
        this.heatPeriodStart = heatPeriodStart;
        this.heatPeriodEnd = heatPeriodEnd;
        this.coolPeriodStart = coolPeriodStart;
        this.coolPeriodEnd = coolPeriodEnd;
        this.temperatures = temperatures;
    }

    public int getRestrictedToMode() {
        return restrictedToMode;
    }

    public String getHeatPeriodStart() {
        return heatPeriodStart;
    }

    public String getFormat() {
        return format;
    }

    public String getHeatPeriodEnd() {
        return heatPeriodEnd;
    }

    public String getCoolPeriodStart() {
        return coolPeriodStart;
    }

    public String getCoolPeriodEnd() {
        return coolPeriodEnd;
    }

    public Map<Integer, Temperature> getTemperatures() {
        return temperatures;
    }
}
