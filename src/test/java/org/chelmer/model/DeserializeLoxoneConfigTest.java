package org.chelmer.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.chelmer.clientimpl.LoxoneWebSocketClient;
import org.chelmer.clientimpl.UuidComponentRegistry;
import org.chelmer.deserialize.ObjectMapperFactory;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class DeserializeLoxoneConfigTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeserializeLoxoneConfigTest.class);

    private ObjectMapper mapper;

    @Before
    public void init() {
        this.mapper = new ObjectMapperFactory().createObjectMapper(new UuidComponentRegistry(), mock(LoxoneWebSocketClient.class));
    }


    @Test
    public void testDeserialize() throws IOException {
        try (InputStream in = getClass().getResourceAsStream("/LoxoneConfig.txt")) {
            LoxoneConfig underTest = mapper.readValue(in, LoxoneConfig.class);
            LOGGER.debug(underTest.toString());

            assertThat(underTest.getLastModified().toString(), is("2017-02-12T18:18:22"));

            assertThat(underTest.getMsInfo().getLocation(), is("Epsom"));

            assertThat(underTest.getGlobalStates(), is(notNullValue()));
            assertThat(underTest.getGlobalStates().size(), is(7));
            // assertThat(underTest.getGlobalStates().get("modifications").toString(), is("0d6a2dad-00af-0255-ffff504f94000000"));

            assertThat(underTest.getOperatingModes(), is(notNullValue()));
            assertThat(underTest.getOperatingModes().size(), is(16));
            assertThat(underTest.getOperatingModes().get(0), is("Public Holiday"));

            assertThat(underTest.getRooms(), is(notNullValue()));
            assertThat(underTest.getRooms().size(), is(15));
            assertThat(underTest.getRooms().get("0d6a2dad-00fd-0387-ffff504f94000000").getName(), is("Outside"));

            assertThat(underTest.getCats(), is(notNullValue()));
            assertThat(underTest.getCats().size(), is(4));
            assertThat(underTest.getCats().get("0d6a2dad-00be-0293-ffff504f94000000").getName(), is("Temperature"));

            assertThat(underTest.getControls(), is(notNullValue()));
            assertThat(underTest.getControls().size(), is(32));
            assertThat(underTest.getControls().get("0d85e0f2-0138-a73e-ffff504f94000000").getName(), is("Outside lights lighting controller"));

            assertThat(underTest.getWeatherServer(), is(notNullValue()));
            assertThat(underTest.getWeatherServer().getStates().size(), is(2));
            assertThat(underTest.getWeatherServer().getWeatherTypeTexts().get(21), is("Snow"));

            assertThat(underTest.getTimes(), is(notNullValue()));
            assertThat(underTest.getTimes().size(), is(30));
            assertThat(underTest.getTimes().get(299).getId(), is(299));

            assertThat(underTest.getAutopilot(), is(notNullValue()));
            assertThat(underTest.getAutopilot().size(), is(1));
            assertThat(underTest.getAutopilot().get("0f06dc1c-00f6-12f0-ffff504f94102a7d").getName(), is("Autopilots"));

        }
    }
}
