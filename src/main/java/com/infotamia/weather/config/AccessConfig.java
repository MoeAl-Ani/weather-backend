package com.infotamia.weather.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mohammed Al-Ani
 */
@Configuration
@ConfigurationProperties("role-access")
public class AccessConfig {

    private Map<String, Map<String, List<String>>> rolePermissionMap = new HashMap<>();

    public AccessConfig() {
    }

    public Map<String, Map<String, List<String>>> getRolePermissionMap() {
        return rolePermissionMap;
    }

    public void setRolePermissionMap(Map<String, Map<String, List<String>>> rolePermissionMap) {
        this.rolePermissionMap = rolePermissionMap;
    }
}
