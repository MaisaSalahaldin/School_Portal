package com.SchoolPortal.SchoolPortal;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebApplicationConfig implements WebMvcConfigurer {
    @Bean
    // makes it a Spring-managed class
    public AuthenticationFilter authenticationFilter() {
        return new AuthenticationFilter();
    }

    // Register filter with Spring so it will intercept requests
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor( authenticationFilter() );
    }

    //Gives access to the images folder which are stored outside of app
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**").addResourceLocations("file:images/");
    }
}
