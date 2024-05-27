package com.springboot.example.restful.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.springboot.example.restful.serialization.converter.YamlJackson2HttpMessageConverter;

import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.lang.Nullable;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final MediaType MEDIA_TYPE_YML = MediaType.valueOf("application/x-yaml");

    @Value("${cors.originPatterns:default")
    private String corsOriginPatterns = "";

    


    @SuppressWarnings("null")
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        if (registry == null) {
            return;
        }
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:8080", "https://www.google.com.br/")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD", "TRACE", "CONNECT");
        WebMvcConfigurer.super.addCorsMappings(registry);
    }

    @Override
    public void extendMessageConverters(@Nullable List<HttpMessageConverter<?>> converters) {

        if (converters == null) {
            return;
        }
        
        converters.add(new YamlJackson2HttpMessageConverter());
    }

    @Override
    public void configureContentNegotiation(@Nullable ContentNegotiationConfigurer configurer) {

        // configurer.favorParameter(true)
        //     .parameterName("mediaType").ignoreAcceptHeader(true)
        //     .useRegisteredExtensionsOnly(false)
        //     .defaultContentType(MediaType.APPLICATION_JSON)
        //     .mediaType("xml", MediaType.APPLICATION_XML)
        //     .mediaType("json", MediaType.APPLICATION_JSON);

        if (configurer == null) {
            return;
        }

        configurer.favorParameter(false)
            .ignoreAcceptHeader(false)
            .useRegisteredExtensionsOnly(false)
            .defaultContentType(MediaType.APPLICATION_JSON)
                .mediaType("json", MediaType.APPLICATION_JSON)
                .mediaType("xml", MediaType.APPLICATION_XML)
                .mediaType("x-yaml", MEDIA_TYPE_YML);
    }

    

}
