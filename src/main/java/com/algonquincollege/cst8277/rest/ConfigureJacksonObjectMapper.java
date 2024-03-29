/**************************************************************************************************
 * File: ConfigureJacksonObjectMapper.java
 * Course materials (20W) CST 8277
 * @author Mike Norman
 *
 * update by :
 * Lai Shan Law (040595733)
 * Siyang Xiong (040938012)
 * Angela Zhao (040529234)
 *
 * @date 2020-11-21
 *
 * Note: students do NOT need to change anything in this class
 */
package com.algonquincollege.cst8277.rest;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.algonquincollege.cst8277.utils.HttpErrorAsJSONServlet;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Provider
public class ConfigureJacksonObjectMapper implements ContextResolver<ObjectMapper> {
    private final ObjectMapper objectMapper;

    public ConfigureJacksonObjectMapper() {
        this.objectMapper = createObjectMapper();
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return objectMapper;
    }

    //configure JDK 8's new DateTime objects to use proper ISO-8601 timeformat
    protected ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            // lenient parsing of JSON - if a field has a typo, don't fall to pieces
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            ;
        HttpErrorAsJSONServlet.setObjectMapper(mapper);
        return mapper;
    }
}