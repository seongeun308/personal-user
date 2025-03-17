package com.personal.user.application.common.config;

import com.personal.user.application.common.interceptor.JwtAuthenticationInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final JwtAuthenticationInterceptor jwtAuthenticationInterceptor;
    private static final List<String> WHITE_LIST = List.of(
            "/login",
            "/register",
            "/duple-email",
            "/reissue",
            "/logout",
            "/admin"
    );

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtAuthenticationInterceptor)
                .addPathPatterns("/*")
                .excludePathPatterns(WHITE_LIST);
    }
}