package com.infotamia.weather.pojos.entities;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * @author Mohammed Al-Ani
 */
public class WeatherFavouriteSearchEntity {
    @NotNull(message = "city is mandatory")
    @NotEmpty(message = "city must not be empty")
    private String city;

    public WeatherFavouriteSearchEntity() {
        //
    }

    public WeatherFavouriteSearchEntity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeatherFavouriteSearchEntity that = (WeatherFavouriteSearchEntity) o;
        return getCity().equals(that.getCity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCity());
    }
}
