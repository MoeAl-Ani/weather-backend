package com.infotamia.weather.rest;

import com.infotamia.weather.exception.IncorrectParameterException;
import com.infotamia.weather.filter.SkipFilter;
import com.infotamia.weather.pojos.entities.WeatherFavouriteSearchEntity;
import com.infotamia.weather.pojos.entities.WeatherResult;
import com.infotamia.weather.services.api.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Mohammed Al-Ani
 */
@RestController
@RequestMapping(path = "/weather")
public class WeatherResource {

    private final WeatherService weatherService;

    public WeatherResource(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping(path = "")
    @SkipFilter
    public WeatherResult getWeather(@RequestParam("city") String city) {
        return weatherService.getWeatherByCity(city);
    }

    @PostMapping(path = "favourites")
    public ResponseEntity<WeatherFavouriteSearchEntity> postFavourite(@RequestBody @Valid WeatherFavouriteSearchEntity model) {
        WeatherFavouriteSearchEntity fav = weatherService.saveFavourite(model);
        return ResponseEntity.status(201).body(fav);
    }

    @GetMapping(path = "favourites")
    public List<WeatherFavouriteSearchEntity> getFavourites() {
        return weatherService.getFavourites();
    }

    @DeleteMapping(path = "favourites/{city}")
    public ResponseEntity<Void> deleteFavourites(@PathVariable("city") String city) throws IncorrectParameterException {
        weatherService.deleteFavourite(city);
        return ResponseEntity.status(204).build();
    }

}
