package me.golf.blog.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/v2/**")
                .allowedOrigins("http://localhost:3000")
                .exposedHeaders("Authorization")
                .allowCredentials(true)
                .allowedMethods(CorsConfiguration.ALL)
                .allowedOriginPatterns(CorsConfiguration.ALL)
                .allowedHeaders(CorsConfiguration.ALL)
                .maxAge(3000);
    }
}