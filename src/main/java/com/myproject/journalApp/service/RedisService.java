package com.myproject.journalApp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.journalApp.api.response.WeatherResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate redisTemplate;

    public void get(String key, Class<WeatherResponse> weatherResponseClass) {
        Object o = redisTemplate.opsForValue().get(key);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValues(o.toString() )
    }
}