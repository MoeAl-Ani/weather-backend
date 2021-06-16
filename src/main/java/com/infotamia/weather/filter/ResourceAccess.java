package com.infotamia.weather.filter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

class ResourceAccess {
    public String httpMethod;
    public String resourcePath;
    public Set<String> roles = new HashSet<>();

    public ResourceAccess() {
    }

    public ResourceAccess(String httpMethod, String resourcePath) {
        this.httpMethod = httpMethod;
        this.resourcePath = resourcePath;
    }

    public ResourceAccess(String httpMethod, String resourcePath, Set<String> roles) {
        this.httpMethod = httpMethod;
        this.resourcePath = resourcePath;
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceAccess resourceAccess = (ResourceAccess) o;
        return httpMethod.equals(resourceAccess.httpMethod) && resourcePath.equals(resourceAccess.resourcePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(httpMethod, resourcePath);
    }

    @Override
    public String toString() {
        return httpMethod + " " + resourcePath + " " + roles;
    }
}
