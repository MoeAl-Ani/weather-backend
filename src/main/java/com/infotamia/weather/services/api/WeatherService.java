package com.infotamia.weather.services.api;

import com.infotamia.weather.exception.IncorrectParameterException;
import com.infotamia.weather.pojos.entities.WeatherResult;
import com.infotamia.weather.pojos.entities.WeatherFavouriteSearchEntity;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Mohammed Al-Ani
 */
public interface WeatherService {
    WeatherResult getWeatherByCity(
            @NotNull(message = "city is mandatory")
            @NotEmpty(message = "city must not be empty") String city);

    WeatherFavouriteSearchEntity saveFavourite(
            @NotNull(message = "search term model can not be null")
            @Valid WeatherFavouriteSearchEntity model);

    List<WeatherFavouriteSearchEntity> getFavourites();

    void deleteFavourite(
            @NotNull(message = "city is mandatory")
            @NotEmpty(message = "city must not be empty") String city) throws IncorrectParameterException;
}
