package org.chelmer.model.control.detailTypes;

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

    public IRoomControllerContolDetails(int restrictedToMode, String heatPeriodStart, String heatPeriodEnd, String coolPeriodStart, String coolPeriodEnd, Map<Integer, Temperature> temperatures, String format) {

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
