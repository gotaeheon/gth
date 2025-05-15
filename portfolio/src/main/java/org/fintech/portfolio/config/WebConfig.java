package org.fintech.portfolio.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // /images/** 경로를 d:/upload/ 경로로 매핑
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:///d:/upload/");
    }
}
