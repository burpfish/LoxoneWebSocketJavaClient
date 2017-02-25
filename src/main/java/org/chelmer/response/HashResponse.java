package org.chelmer.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class HashResponse {
    private String control;
    private String value;
    private int code;

    @JsonCreator
    public HashResponse(@JsonProperty("control") String control, @JsonProperty("value") String value,
                        @JsonProperty("Code") int code) {
        super();

        this.control = control;
        this.value = value;
        this.code = code;
    }

    public static HashResponse create(String json) {
        HashResponse response;
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(json);
            response = mapper.treeToValue(node.get("LL"), HashResponse.class);
        } catch (IOException e) {
            throw new RuntimeException("Error parsing model:" + json, e);
        }
        return response;
    }

    @Override
    public String toString() {
        return String.format("code=%d, control=%s, value=%s", code, control, value);
    }

    public int getCode() {
        return code;
    }

    public String getControl() {
        return control;
    }

    public String getValue() {
        return value;
    }
}