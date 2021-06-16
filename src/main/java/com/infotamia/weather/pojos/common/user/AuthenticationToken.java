package com.infotamia.weather.pojos.common.user;

/**
 * @author Mohammed Al-Ani
 */
public class AuthenticationToken {

    private String token;

    public AuthenticationToken(String token) {
        this.token = token;
    }

    public AuthenticationToken() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
