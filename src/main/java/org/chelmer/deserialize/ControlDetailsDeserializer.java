package org.chelmer.deserialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.chelmer.model.control.Control;
import org.chelmer.model.control.ControlDetails;
import org.chelmer.model.control.ControlType;
import org.chelmer.model.control.Details;
import org.chelmer.model.control.detailTypes.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ControlDetailsDeserializer extends StdDeserializer<Details> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ControlDetailsDeserializer.class);

    public ControlDetailsDeserializer() {
        this(null);
    }

    public ControlDetailsDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Details deserialize(
            JsonParser jsonParser,
            DeserializationContext context)
            throws IOException, JsonProcessingException {

        ControlType type = ((Control) jsonParser.getParsingContext().getParent().getCurrentValue()).getType();
        Details value = null;

        switch (type) {
            case SWITCH:
                value = jsonParser.readValueAs(SwitchContolDetails.class);
                break;
            case PUSHBUTTON:
                value = jsonParser.readValueAs(PushButtonContolDetails.class);
                break;
            case LIGHTCONTROLLER:
                value = jsonParser.readValueAs(LightControllerContolDetails.class);
                break;
            case REMOTE:
                value = jsonParser.readValueAs(RemoteContolDetails.class);
                break;
            case APPLICATION:
                value = jsonParser.readValueAs(ApplicationContolDetails.class);
                break;
            case IRCDAYTIMER:
                value = jsonParser.readValueAs(IrcDayTimerContolDetails.class);
                break;
            case IROOMCONTROLLER:
                value = jsonParser.readValueAs(IRoomControllerContolDetails.class);
                break;
            case DIMMER:
                value = jsonParser.readValueAs(SwitchContolDetails.class);
                break;
            case UNDEFINED:
            default:
                LOGGER.warn("Unknown control type (using generic control): " + type);
                value = jsonParser.readValueAs(ControlDetails.class);
                break;
        }

        return value;
    }
}