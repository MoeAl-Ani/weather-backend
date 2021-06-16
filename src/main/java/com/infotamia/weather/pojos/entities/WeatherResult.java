package com.infotamia.weather.pojos.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mohammed Al-Ani
 */
public class WeatherResult {
    private List<WeatherEntity> weather = new ArrayList<>();
    private MainEntity main;

    public WeatherResult() {
    }

    public List<WeatherEntity> getWeather() {
        return weather;
    }

    public void setWeather(List<WeatherEntity> weather) {
        this.weather = weather;
    }

    public MainEntity getMain() {
        return main;
    }

    public void setMain(MainEntity main) {
        this.main = main;
    }

    @Override
    public String toString() {
        return "WeatherResult{" +
                "weather=" + weather +
                ", main=" + main +
                '}';
    }
}
