package com.example.MQAdminTemplate.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    /*@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/assets/**")
                .addResourceLocations("classpath:/assets/css/**")
                .addResourceLocations("classpath:/assets/fonts/**")
                .addResourceLocations("classpath:/assets/images/**")
                .addResourceLocations("classpath:/assets/js/**")
                .addResourceLocations("classpath:/assets/libs/**");
    }*/

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/assets/**")
                .addResourceLocations("classpath:/assets/");  // Правильный путь для всех ресурсов
    }
}