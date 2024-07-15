package com.frank.springbootinit.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Component
public class ObjectMapperUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static ObjectMapper getMapper(){
        return objectMapper;
    }

}
