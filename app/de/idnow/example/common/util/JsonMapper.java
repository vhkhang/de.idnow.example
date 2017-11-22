package de.idnow.example.common.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonMapper {

    public static <T> Optional<T> toObj(JsonNode json, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return Optional.of(mapper.treeToValue(json, clazz));
        } catch (JsonProcessingException e) {
            return Optional.empty();
        }
    }

    public static <T> List<T> toObjects(JsonNode json, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json.toString(),
                    mapper.getTypeFactory().constructCollectionType(ArrayList.class, clazz));
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public static String toJson(Object entity) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(entity);
        } catch (JsonProcessingException e) {
            return "";
        }
    }
}
