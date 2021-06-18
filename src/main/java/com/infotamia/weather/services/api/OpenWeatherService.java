package com.infotamia.weather.services.api;

import com.infotamia.weather.access.AbstractPrincipal;
import com.infotamia.weather.exception.BaseErrorCode;
import com.infotamia.weather.exception.IncorrectParameterException;
import com.infotamia.weather.pojos.entities.WeatherFavouriteSearchEntity;
import com.infotamia.weather.pojos.entities.WeatherResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Mohammed Al-Ani
 */
@Service
public class OpenWeatherService implements WeatherService {
    final Map<Integer, Set<WeatherFavouriteSearchEntity>> favourites = new ConcurrentHashMap<>();

    @Autowired
    AbstractPrincipal user;

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
        favourites.put(user.getId(), new HashSet<>());
        favourites.computeIfPresent(user.getId(), (k, h) -> {
            h.add(model);
            return h;
        });
        return model;
    }

    @Override
    public List<WeatherFavouriteSearchEntity> getFavourites() {
        Set<WeatherFavouriteSearchEntity> weatherFavouriteSearchEntities = favourites.get(user.getId());
        if (weatherFavouriteSearchEntities == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(weatherFavouriteSearchEntities);
    }

    @Override
    public void deleteFavourite(
            @NotNull(message = "city is mandatory")
            @NotEmpty(message = "city must not be empty") String city) {

        WeatherFavouriteSearchEntity fav = new WeatherFavouriteSearchEntity(city);
        favourites.computeIfPresent(user.getId(), (k,v) -> {
            v.remove(fav);
            return v;
        });
    }

}
