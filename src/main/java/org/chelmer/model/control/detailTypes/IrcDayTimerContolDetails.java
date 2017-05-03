package org.chelmer.model.control.detailTypes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.chelmer.model.control.ControlDetails;

/**
 * Created by burfo on 15/02/2017.
 */
public class IrcDayTimerContolDetails extends ControlDetails {
    private boolean analog;
    private String format;

    @JsonCreator
    public IrcDayTimerContolDetails(@JsonProperty("analog") boolean analog, @JsonProperty("format") String format) {
        this.analog = analog;
        this.format = format;
    }

    public boolean isAnalog() {
        return analog;
    }

    public String getFormat() {
        return format;
    }
}
