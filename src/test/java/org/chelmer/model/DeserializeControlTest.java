package org.chelmer.model;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.chelmer.clientimpl.UuidComponentRegistry;
import org.chelmer.deserialize.ObjectMapperFactory;
import org.chelmer.model.control.Control;
import org.chelmer.model.control.ControlType;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class DeserializeControlTest {
    private ObjectMapper mapper;

    @Before
    public void init() {
        mapper = new ObjectMapperFactory().createObjectMapper(new UuidComponentRegistry());
    }

    @Test
    public void testDeserializeControl1() throws JsonParseException, JsonMappingException, IOException {
        try (InputStream in = getClass().getResourceAsStream("/control.txt")) {
            Control underTest = mapper.readValue(in, Control.class);
            assertThat(underTest.getName(), is("All off"));
            assertThat(underTest.getSubControls(), is(nullValue()));
            assertThat(underTest.getType(), is(ControlType.SWITCH));
        }
    }

    @Test
    public void testDeserializeControl2() throws JsonParseException, JsonMappingException, IOException {
        try (InputStream in = getClass().getResourceAsStream("/control2.txt")) {
            Control underTest = mapper.readValue(in, Control.class);
            assertThat(underTest.getName(), is("Danny's lighing controller"));
            assertThat(underTest.getSubControls().size(), is(1));
            assertThat(underTest.getType(), is(ControlType.LIGHTCONTROLLER));
        }
    }

    @Test
    public void testDeserializeControl3() throws JsonParseException, JsonMappingException, IOException {
        try (InputStream in = getClass().getResourceAsStream("/control3.txt")) {
            Control underTest = mapper.readValue(in, Control.class);
            assertThat(underTest.getName(), is("Music"));
            assertThat(underTest.getSubControls(), is(nullValue()));
            assertThat(underTest.getType(), is(ControlType.REMOTE));
        }
    }

    @Test
    public void testDeserializeControl4() throws JsonParseException, JsonMappingException, IOException {
        try (InputStream in = getClass().getResourceAsStream("/control4.txt")) {
            Control underTest = mapper.readValue(in, Control.class);
            assertThat(underTest.getName(), is("Intelligent roomUuid controller"));
            assertThat(underTest.getSubControls().size(), is(2));
            assertThat(underTest.getType(), is(ControlType.IROOMCONTROLLER));
        }
    }
}
