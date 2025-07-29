package com.txm.teamprojects.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.txm.teamprojects.interceptors.JwtInterceptor;

@Component
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtInterceptor())
                .addPathPatterns("/666")
                .excludePathPatterns("/patient/pdf")
                .excludePathPatterns("/**/login");
    }
}
