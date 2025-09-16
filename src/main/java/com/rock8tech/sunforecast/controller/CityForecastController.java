package com.rock8tech.sunforecast.controller;

import com.rock8tech.sunforecast.dto.SunForecastResponse;
import com.rock8tech.sunforecast.service.ForecastService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
@Validated
public class CityForecastController {

    private final ForecastService forecastService;

    public CityForecastController(ForecastService forecastService) {
        this.forecastService = forecastService;
    }

    @GetMapping("/sun-forecast")
    public Mono<ResponseEntity<SunForecastResponse>> getForecast(@RequestParam(name = "city") @NotBlank String city) {
        return forecastService.getForecast(city).map(ResponseEntity::ok);
    }
}
