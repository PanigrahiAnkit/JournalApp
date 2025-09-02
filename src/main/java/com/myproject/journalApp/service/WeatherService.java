package com.myproject.journalApp.service;

import com.myproject.journalApp.api.response.WeatherResponse;
import com.myproject.journalApp.cache.AppCache;
import com.myproject.journalApp.constants.Placeholders;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class WeatherService {

    @Value("${weather.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final RedisService redisService;
    private final AppCache appCache;

    public WeatherResponse getWeather(String city) {
        redisService.get("weather_of_" + city, WeatherResponse.class);
        String finalAPI = appCache
                .appCache
                .get(AppCache.keys.WEATHER_API.toString())
                .replace(Placeholders.CITY, city)
                .replace(Placeholders.API_KEY, apiKey);
        ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI, HttpMethod.GET, null,
                WeatherResponse.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Failed to fetch weather data. Status code: " + response.getStatusCode());
        }
        // The process of converting JSON Response into corresponding Java Objects is known as Deserialisation.
        WeatherResponse body = response.getBody();
        if (body == null) {
            throw new RuntimeException("Weather response body is empty");
        }
        return body;
    }
}