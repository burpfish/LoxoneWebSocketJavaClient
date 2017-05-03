package org.chelmer.model;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.chelmer.clientimpl.LoxoneWebSocketClient;
import org.chelmer.clientimpl.UuidComponentRegistry;
import org.chelmer.deserialize.ObjectMapperFactory;
import org.chelmer.model.control.Control;
import org.chelmer.model.control.ControlType;
import org.chelmer.model.control.controlTypes.SwitchControl;
import org.chelmer.model.entity.LoxUuid;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.mockito.Mockito.mock;

public class SendCommandToControlTest {
    private ObjectMapper mapper;

    @Before
    public void init() {
        mapper = new ObjectMapperFactory().createObjectMapper(new UuidComponentRegistry(), mock(LoxoneWebSocketClient.class));
    }

    @Test
    public void testSwitchControl() throws JsonParseException, JsonMappingException, IOException {
        SwitchControl control = new SwitchControl("name", ControlType.SWITCH, new LoxUuid("0"), 0, false);
        control.setClient(mock(LoxoneWebSocketClient.class));
        control.invokeCommand(SwitchControl.SwitchCommand.ON);
    }

    // todo: Add test with unknown (catch excpetion)
}
