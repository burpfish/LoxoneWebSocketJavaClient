package org.chelmer.model;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.chelmer.clientimpl.LoxoneWebSocketClient;
import org.chelmer.clientimpl.UuidComponentRegistry;
import org.chelmer.deserialize.ObjectMapperFactory;
import org.chelmer.model.miniserver.MsInfo;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class DeserializeMsInfoTest {
    private ObjectMapper mapper;

    @Before
    public void init() {
        mapper = new ObjectMapperFactory().createObjectMapper(new UuidComponentRegistry(), mock(LoxoneWebSocketClient.class));
    }

    @Test
    public void testDeserialize() throws JsonParseException, JsonMappingException, IOException {
        try (InputStream in = getClass().getResourceAsStream("/MsInfo.txt")) {
            MsInfo underTest = mapper.readValue(in, MsInfo.class);
            assertThat(underTest.getMsName(), is("Loxone Miniserver"));
            assertThat(underTest.getCatTitle(), is("Category"));
            assertThat(underTest.getLocation(), is("Epsom"));
        }
    }
}
