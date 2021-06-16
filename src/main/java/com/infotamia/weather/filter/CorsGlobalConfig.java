package com.infotamia.weather.filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * basic core config
 * @author Mohammed Al-Ani
 */
@Configuration
public class CorsGlobalConfig {

    private static final String ORIGIN_VALIDATION_REGEX = "https://([a-z0-9-]+\\.)?infotamia.com(.*)";

    @Bean
    CorsFilter corsFilter() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Collections.singletonList("https://*.infotamia.com"));
        configuration.setAllowedMethods(Arrays.asList("OPTIONS", "POST", "PUT", "PATCH", "DELETE"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList(HttpHeaders.CONTENT_TYPE, HttpHeaders.LOCATION, "X-Requested-With"));
        configuration.setExposedHeaders(Arrays.asList(HttpHeaders.CONTENT_TYPE, HttpHeaders.LOCATION, "X-Requested-With"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return new CorsFilter(source) {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
                if (request.getMethod().equals(HttpMethod.OPTIONS.name())) {
                    if (Boolean.FALSE.equals(request.getHeader(HttpHeaders.ORIGIN) != null)) {
                        logger.debug("Aborting OPTIONS request due to missing origin header");
                        response.setStatus(HttpStatus.BAD_REQUEST.value());
                        return;
                    } else if (!hasValidOrigin(request)) {
                        response.setStatus(HttpStatus.BAD_REQUEST.value());
                        return;
                    }
                } else if (!hasValidOrigin(request)) {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    return;
                }

                response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, request.getHeader(HttpHeaders.ORIGIN));
                response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, Arrays.asList("OPTIONS", "POST", "PUT", "PATCH", "DELETE")
                        .stream()
                        .collect(Collectors.joining(",")));
                response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
                response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, Arrays.asList(HttpHeaders.CONTENT_TYPE, HttpHeaders.LOCATION, "X-Requested-With")
                        .stream()
                        .collect(Collectors.joining(",")));
                response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, Arrays.asList(HttpHeaders.CONTENT_TYPE, HttpHeaders.LOCATION, "X-Requested-With")
                        .stream()
                        .collect(Collectors.joining(",")));
                super.doFilterInternal(request, response, filterChain);
            }

        };
    }

    private boolean hasValidOrigin(HttpServletRequest request) {
        final String origin = request.getHeader(HttpHeaders.ORIGIN);
        if (Objects.isNull(origin)) {
            return true;
        }
        return origin.matches(ORIGIN_VALIDATION_REGEX);
    }
}
