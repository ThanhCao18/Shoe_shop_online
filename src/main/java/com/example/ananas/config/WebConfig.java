package com.example.ananas.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    //E:/Cntt_Class/JavaWeb/BigExercise/Backend/ananas/upload
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/avatar/**").addResourceLocations("file:///E:/Cntt_Class/JavaWeb/BigExercise/Backend/ananas/upload/user/")
                .setCachePeriod(3600);
        registry.addResourceHandler("/images/product/**").addResourceLocations("file:///E:/Cntt_Class/JavaWeb/BigExercise/Backend/ananas/upload/product/")
                .setCachePeriod(3600);
    }
}

