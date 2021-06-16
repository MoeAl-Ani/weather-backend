package com.infotamia.weather.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.infotamia.weather.config.jackson.LocalDateTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * @author Mohammed Al-Ani
 */
@Configuration
public class JacksonConfig {

    /**
     * custom jackson configuration
     * @return
     */
    @Bean
    public Jackson2ObjectMapperBuilder jacksonBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.modulesToInstall(new LocalDateTimeModule());
        builder = builder.featuresToEnable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);
        builder = builder.featuresToEnable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        builder = builder.serializationInclusion(JsonInclude.Include.NON_NULL);
        return builder;
    }
}
