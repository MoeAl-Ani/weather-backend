package com.infotamia.weather.filter;

import com.infotamia.weather.access.SessionType;
import com.infotamia.weather.config.AccessConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

/**
 * pay attention to this class too!
 * @author Mohammed Al-Ani
 */
@Configuration
@EnableWebSecurity
public class ResourceSecurityConfig {

    AccessConfig accessConfig;
    RequestMappingHandlerMapping requestMappingHandlerMapping;
    AuthenticationFilter authenticationFilter;
    AuthorizationFilter authorizationFilter;
    public static final Map<ResourceAccess, ResourceAccess> access = new HashMap<>();

    public ResourceSecurityConfig(
            AccessConfig accessConfig,
            AuthenticationFilter authenticationFilter,
            AuthorizationFilter authorizationFilter,
            RequestMappingHandlerMapping requestMappingHandlerMapping) {
        this.accessConfig = accessConfig;
        this.authenticationFilter = authenticationFilter;
        this.authorizationFilter = authorizationFilter;
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        List<String> roles = Arrays.asList("ANONYMOUS", "USER");
        for (String role : roles) {
            Map<String, List<String>> roleAccess = accessConfig.getRolePermissionMap().get(role);
            if (roleAccess == null) throw new RuntimeException("no role access object found, config issue");
            for (String resource : roleAccess.keySet()) {
                List<String> methods = roleAccess.get(resource);
                for (String resourceMethod : methods) {
                    String[] methodVerbPathPermission = resourceMethod.split(" ");
                    String verb = methodVerbPathPermission[0];
                    String path = methodVerbPathPermission[1];
                    String tempPath = UriComponentsBuilder.fromUriString(transformToPath(resource)).path(transformToPath(path)).encode().toUriString();

                    tempPath = tempPath.substring(0, tempPath.length() - 1);
                    ResourceAccess resourceAccess = new ResourceAccess();
                    resourceAccess.httpMethod = verb;
                    resourceAccess.resourcePath = tempPath;
                    resourceAccess.roles.add(role);
                    access.computeIfPresent(resourceAccess, (key, value) -> {
                        value.roles.add(role);
                        return value;
                    });
                    access.putIfAbsent(resourceAccess, resourceAccess);
                }
            }
        }

        for (RequestMappingInfo resource : requestMappingHandlerMapping.getHandlerMethods().keySet()) {
            if (resource.getMethodsCondition().isEmpty()) continue;
            String path = resource.getPatternsCondition().getPatterns().stream().findFirst().get();
            String verb = resource.getMethodsCondition().getMethods().stream().findFirst().get().name();
            String finalPath = UriComponentsBuilder.fromUriString(path).encode().toUriString();

            ResourceAccess resourceAccess = new ResourceAccess();
            resourceAccess.httpMethod = verb;
            resourceAccess.resourcePath = finalPath;
            resourceAccess.roles.add(SessionType.SYSTEM_ADMIN.name());
            access.computeIfPresent(resourceAccess, (key, value) -> {
                value.roles.add(SessionType.SYSTEM_ADMIN.name());
                return value;
            });
            access.putIfAbsent(resourceAccess, resourceAccess);
        }
        for (ResourceAccess value : new ArrayList<>(access.values())) {
            ResourceAccess resourceAccess = new ResourceAccess(value.httpMethod, value.resourcePath +"/", value.roles);
            access.putIfAbsent(resourceAccess, resourceAccess);
        }
        http.cors()
                .and()
                .csrf().disable()
                .addFilterBefore(authenticationFilter, BasicAuthenticationFilter.class)
                .addFilterAfter(authorizationFilter, BasicAuthenticationFilter.class)
                .authorizeRequests()
                .anyRequest()
                .permitAll();
        return http.build();
    }

    private String transformToPath(String value) {
        if (value.charAt(0) != '/') {
            value = "/" + value;
        }
        if (value.charAt(value.length() - 1) != '/') {
            value = value + "/";
        }
        return value;
    }

}
