package com.demo.infrastructure.helper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Locale;
import java.util.function.Function;

public final class JacksonUtil {

    public static final JsonMapper jsonMapper;
    public static final Function<Object, String> STRINGIFY;
    private static final Logger log = LoggerFactory.getLogger(JacksonUtil.class);
    private static final DateTimeFormatter UTC_DATE_TIME_FORMAT;
    private static final LocalDateTimeDeserializer DATE_TIME_DESERIALIZER;
    private static final LocalDateTimeSerializer DATE_TIME_SERIALIZER;

    static {
        UTC_DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss")
                .withResolverStyle(ResolverStyle.STRICT);
        DATE_TIME_DESERIALIZER = new LocalDateTimeDeserializer(UTC_DATE_TIME_FORMAT);
        DATE_TIME_SERIALIZER = new LocalDateTimeSerializer(UTC_DATE_TIME_FORMAT);
        jsonMapper = JsonMapper.builder()
                .defaultLocale(Locale.forLanguageTag("es_CO"))
                .addModules(new Jdk8Module(),
                        new JavaTimeModule()
                                .addSerializer(LocalDateTime.class, DATE_TIME_SERIALIZER)
                                .addDeserializer(LocalDateTime.class, DATE_TIME_DESERIALIZER),
                        new ParameterNamesModule())
                .enable(DeserializationFeature.USE_LONG_FOR_INTS)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .build();
        STRINGIFY = object -> {
            try {
                return jsonMapper.writer().writeValueAsString(object);
            } catch (JsonProcessingException e) {
                log.error("No se puede serializar, message {}", e.getMessage());
                return "";
            }
        };
    }

    private JacksonUtil() {
        //final class
    }
}
