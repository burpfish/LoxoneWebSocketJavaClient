package org.chelmer.deserialize;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.chelmer.clientimpl.UuidComponentRegistry;
import org.chelmer.model.control.Details;

import java.time.LocalDateTime;
import java.time.MonthDay;

/**
 * Created by burfo on 24/02/2017.
 */
public class ObjectMapperFactory {
    public ObjectMapper createObjectMapper(UuidComponentRegistry registry) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
        mapper.registerModule(new JavaTimeModule());
        mapper.configOverride(LocalDateTime.class).setFormat(JsonFormat.Value.forPattern("yyyy-MM-dd HH:mm:ss"));
        mapper.configOverride(MonthDay.class).setFormat(JsonFormat.Value.forPattern("MM-dd"));

        InjectableValues inject = new InjectableValues.Std().addValue(UuidComponentRegistry.class, registry);
        mapper.setInjectableValues(inject);

        SimpleModule module = new SimpleModule();
        module.addDeserializer(Details.class, new ControlDetailsDeserializer());
        mapper.registerModule(module);
        return mapper;
    }
}
