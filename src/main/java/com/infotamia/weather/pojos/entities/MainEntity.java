package com.infotamia.weather.pojos.entities;

/**
 * @author Mohammed Al-Ani
 */
public class MainEntity {
    private double temp;

    public MainEntity() {
    }

    public double getTemp() {
        return (temp - 273.15);
    }
    public String getFormattedTemp() {
        return String.format("%dC",(int) getTemp());
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    @Override
    public String toString() {
        return "MainEntity{" +
                "temp=" + temp +
                '}';
    }
}
