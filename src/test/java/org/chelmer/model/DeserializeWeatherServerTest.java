package org.chelmer.model;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.chelmer.clientimpl.UuidComponentRegistry;
import org.chelmer.deserialize.ObjectMapperFactory;
import org.chelmer.model.weatherServer.WeatherServer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DeserializeWeatherServerTest {
    private ObjectMapper mapper;
    private UuidComponentRegistry registry = new UuidComponentRegistry();


    @Before
    public void init() {
        mapper = new ObjectMapperFactory().createObjectMapper(registry);
    }

    @Test
    public void testDeserialize() throws JsonParseException, JsonMappingException, IOException {
        try (InputStream in = getClass().getResourceAsStream("/weatherServer.txt")) {
            WeatherServer underTest = mapper.readValue(in, WeatherServer.class);
            assertThat(underTest.getWeatherTypeTexts().get(26), is("Sleet"));
            assertThat(underTest.getWeatherFieldTypes().size(), is(20));
        }
    }
}
