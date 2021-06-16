package com.infotamia.weather.pojos.entities;

/**
 * @author Mohammed Al-Ani
 */
public class WeatherEntity {

    private String main;
    private String description;

    public WeatherEntity() {
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "WeatherEntity{" +
                "main='" + main + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
