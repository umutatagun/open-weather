package com.umut.weather.service;

import com.umut.weather.constant.Constant;
import com.umut.weather.dto.WeatherDto;
import com.umut.weather.dto.WeatherResponse;
import com.umut.weather.model.WeatherEntity;
import com.umut.weather.repository.WeatherRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class WeatherService {
    private final WeatherRepository weatherRepository;
    private final RestTemplate restTemplate;

    public WeatherService(WeatherRepository weatherRepository, RestTemplate restTemplate) {
        this.weatherRepository = weatherRepository;
        this.restTemplate = restTemplate;
    }

    public WeatherDto getWeatherByCityName(String city) {
        Optional<WeatherEntity> weatherEntityOptional = weatherRepository.findFirstByRequestedCityNameOrderByUpdatedTimeDesc(city);

        return weatherEntityOptional.map(weather -> {
            if(weather.getUpdatedTime()
                    .isBefore(LocalDateTime.now().minusSeconds(30))){
                return WeatherDto.convert(getWeatherFromWeatherStack(city));
            }
            return WeatherDto.convert(weather);
        }).orElseGet(() -> WeatherDto.convert(getWeatherFromWeatherStack(city)));
    }

    private WeatherEntity getWeatherFromWeatherStack(String city) {
        WeatherResponse responseEntity = restTemplate.getForObject(getWeatherStackUrl(city), WeatherResponse.class);

        return saveWeatherEntity(city, responseEntity);
    }

    private String getWeatherStackUrl(String city) {
        return Constant.API_URL
                .concat(Constant.ACCESS_KEY_PARAM)
                .concat(Constant.API_KEY)
                .concat(Constant.QUERY_KEY_PARAM)
                .concat(city);
    }

    private WeatherEntity saveWeatherEntity(String city, WeatherResponse response) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        WeatherEntity weatherEntity = new WeatherEntity();
        weatherEntity.setCityName(city);
        weatherEntity.setRequestedCityName(response.location().name());
        weatherEntity.setCountry(response.location().country());
        weatherEntity.setTemperature(response.current().temperature());
        weatherEntity.setUpdatedTime(LocalDateTime.now());
        weatherEntity.setResponseLocalTime(LocalDateTime.parse(response.location().localTime(), dateTimeFormatter));

        return weatherRepository.save(weatherEntity);
    }

}
