package com.example.user.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.fasterxml.jackson.databind.DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL;

@Slf4j
public class Utils {

    private static final String ERR_STRING_FORMAT = "Error in formating string, possibly due to mismatched number of placeholders and objects";

    public static String formatSafe(String format, Object... args) {
        try {
            return String.format(format, args);
        } catch (Exception ex) {
            log.error(ERR_STRING_FORMAT, ex);
        }
        return null;
    }

    public static <T> boolean isEmpty(T t) {
        return t == null || t.toString().length() == 0;
    }

    public static <T> boolean isEmpty(List<T> list) {
        return list == null || list.size() == 0;
    }

    public static <T> T fromJson(String json, Class<T> klass, boolean failOnUnknownProps) throws IOException {
        ObjectMapper mapper = getJsonMapper().enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
        if (!failOnUnknownProps)
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper.readValue(json, klass);
    }

    public static <T> T fromJson(String json, Class<T> klass, boolean readUnknownEnumAsNull, boolean failOnUnknownProps) throws IOException {
        ObjectMapper mapper = getJsonMapper().configure(READ_UNKNOWN_ENUM_VALUES_AS_NULL, readUnknownEnumAsNull);
        if (failOnUnknownProps)
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper.readValue(json, klass);
    }

    public static <T> T fromJson(String json, Class<T> klass) throws IOException {
        return getJsonMapper()
                .enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING)
                .readValue(json, klass);
    }

    public static String toJson(Object object) throws JsonProcessingException {
        return getJsonMapper()
                .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
                .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                .enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
                .writeValueAsString(object);
    }

    public static <T> List<T> listFromJson(String json, Class<T> klass) throws IOException, ClassNotFoundException {
        try {
            Class<T[]> arrayClass = (Class<T[]>) Class.forName("[L" + klass.getName() + ";");
            T[] objects = getJsonMapper().readValue(json, arrayClass);
            return Arrays.asList(objects);
        } catch (IOException | ClassNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            return new ArrayList<T>();
        }
    }

    private static ObjectMapper getJsonMapper() {
        ObjectMapper mapper = new ObjectMapper().enable(JsonParser.Feature.STRICT_DUPLICATE_DETECTION);
        mapper.enable(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS);
        return mapper;
    }
}
