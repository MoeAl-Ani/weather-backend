package com.infotamia.weather;

import com.fasterxml.jackson.databind.JavaType;
import com.infotamia.BaseTest;
import com.infotamia.weather.pojos.entities.WeatherFavouriteSearchEntity;
import com.infotamia.weather.pojos.entities.WeatherResult;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.wildfly.common.Assert;

import javax.servlet.http.Cookie;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Mohammed Al-Ani
 */
class WeatherResourceTest extends BaseTest {

    @Test
    void testWeatherFlow() throws Exception {

        byte[] weatherBytes = mvc.perform(MockMvcRequestBuilders.get("/weather")
                .param("city", "baghdad")
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(new Cookie(HttpHeaders.AUTHORIZATION, jwt)))
                .andExpect(status().isOk())
                .andReturn().getResponse()
                .getContentAsByteArray();
        JavaType javaType = mapper.getTypeFactory().constructType(WeatherResult.class);
        WeatherResult weatherResult = mapper.readValue(weatherBytes, javaType);
        Assert.assertNotNull(weatherResult);
        Assert.assertNotNull(weatherResult.getMain());
        Assert.assertNotNull(weatherResult.getMain().getTemp());

        WeatherFavouriteSearchEntity fav = new WeatherFavouriteSearchEntity();
        fav.setCity("baghdad");
        mvc.perform(MockMvcRequestBuilders.post("/weather/favourites")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(fav))
                .cookie(new Cookie(HttpHeaders.AUTHORIZATION, jwt)))
                .andExpect(status().is(201))
                .andReturn().getResponse()
                .getContentAsByteArray();

        byte[] favouritesBytes = mvc.perform(MockMvcRequestBuilders.get("/weather/favourites")
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(new Cookie(HttpHeaders.AUTHORIZATION, jwt)))
                .andExpect(status().isOk())
                .andReturn().getResponse()
                .getContentAsByteArray();

        javaType = mapper.getTypeFactory().constructParametricType(List.class, WeatherFavouriteSearchEntity.class);
        List<WeatherFavouriteSearchEntity> favourites = mapper.readValue(favouritesBytes, javaType);

        Assert.assertNotNull(favourites);
        Assert.assertFalse(favourites.isEmpty());
        Assert.assertTrue(favourites.size() == 1);

        mvc.perform(MockMvcRequestBuilders.delete("/weather/favourites/nonexist")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(fav))
                .cookie(new Cookie(HttpHeaders.AUTHORIZATION, jwt)))
                .andExpect(status().is(204));

        mvc.perform(MockMvcRequestBuilders.delete("/weather/favourites/baghdad")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(fav))
                .cookie(new Cookie(HttpHeaders.AUTHORIZATION, jwt)))
                .andExpect(status().is(204));

        favouritesBytes = mvc.perform(MockMvcRequestBuilders.get("/weather/favourites")
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(new Cookie(HttpHeaders.AUTHORIZATION, jwt)))
                .andExpect(status().isOk())
                .andReturn().getResponse()
                .getContentAsByteArray();

        javaType = mapper.getTypeFactory().constructParametricType(List.class, WeatherFavouriteSearchEntity.class);
        favourites = mapper.readValue(favouritesBytes, javaType);
        Assert.assertNotNull(favourites);
        Assert.assertTrue(favourites.isEmpty());
    }
}
