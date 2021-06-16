package com.infotamia.weather.services.api;

import com.infotamia.weather.exception.BaseErrorCode;
import com.infotamia.weather.exception.IncorrectParameterException;
import com.infotamia.weather.pojos.entities.WeatherFavouriteSearchEntity;
import com.infotamia.weather.pojos.entities.WeatherResult;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * @author Mohammed Al-Ani
 */
@Service
public class OpenWeatherService implements WeatherService {
    final Set<WeatherFavouriteSearchEntity> favourites = Collections.synchronizedSet(new HashSet<>());

    @Override
    public WeatherResult getWeatherByCity(
            @NotNull(message = "city is mandatory")
            @NotEmpty(message = "city must not be empty") String city) {

        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&APPID=ba96e059524abe8fed603491ee9454c0";
        ResponseEntity<WeatherResult> response
                = restTemplate.getForEntity(fooResourceUrl, WeatherResult.class);
        return response.getBody();
    }

    @Override
    public WeatherFavouriteSearchEntity saveFavourite(
            @NotNull(message = "search term model can not be null")
            @Valid WeatherFavouriteSearchEntity model) {
        synchronized (favourites) {
            favourites.add(model);
        }
        return model;
    }

    @Override
    public List<WeatherFavouriteSearchEntity> getFavourites() {
        return new ArrayList<>(favourites);
    }

    @Override
    public void deleteFavourite(
            @NotNull(message = "city is mandatory")
            @NotEmpty(message = "city must not be empty") String city) throws IncorrectParameterException {

        WeatherFavouriteSearchEntity fav = new WeatherFavouriteSearchEntity(city);
        if (!favourites.contains(fav)) {
            throw new IncorrectParameterException("delete failed, provided city was not found!", BaseErrorCode.INVALID_PARAMETERS);
        }

        favourites.remove(fav);
    }

}
