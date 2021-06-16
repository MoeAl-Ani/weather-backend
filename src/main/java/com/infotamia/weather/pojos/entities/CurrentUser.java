package com.infotamia.weather.pojos.entities;

/**
 * @author Mohammed Al-Ani
 */
public class CurrentUser {

    private Integer id;
    private String sessionType;

    public CurrentUser() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSessionType() {
        return sessionType;
    }

    public void setSessionType(String sessionType) {
        this.sessionType = sessionType;
    }
}
