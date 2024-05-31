package com.springboot.example.restful.integrationTests.controller.withyaml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import io.restassured.mapper.ObjectMapper;
import io.restassured.mapper.ObjectMapperDeserializationContext;
import io.restassured.mapper.ObjectMapperSerializationContext;

public class MapperYaml implements ObjectMapper{

    private com.fasterxml.jackson.databind.ObjectMapper objectMapper;
    protected TypeFactory typeFactory;

    public MapperYaml() {
        this.objectMapper = new com.fasterxml.jackson.databind.ObjectMapper(new YAMLFactory());
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        typeFactory = TypeFactory.defaultInstance();
    }



    @Override
    public Object deserialize(ObjectMapperDeserializationContext context) {

        try {
            return objectMapper.readValue(context.getDataToDeserialize().asString(), typeFactory.constructType(context.getType()));
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object serialize(ObjectMapperSerializationContext context) {

        try {
            return objectMapper.writeValueAsString(context.getObjectToSerialize());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
