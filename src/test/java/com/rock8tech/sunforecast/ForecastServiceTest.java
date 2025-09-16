package com.rock8tech.sunforecast;

import com.rock8tech.sunforecast.ai.NarrationService;
import com.rock8tech.sunforecast.client.GeocodeResponse;
import com.rock8tech.sunforecast.client.GeocodingClient;
import com.rock8tech.sunforecast.client.OpenMeteoForecastClient;
import com.rock8tech.sunforecast.client.OpenMeteoForecastDto;
import com.rock8tech.sunforecast.dto.SunForecastResponse;
import com.rock8tech.sunforecast.service.ForecastService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;

import com.rock8tech.sunforecast.client.GeocodingApi;
import com.rock8tech.sunforecast.client.ForecastApi;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class ForecastServiceTest {

    GeocodingApi  geocodingClient = Mockito.mock(GeocodingApi .class);
    ForecastApi   forecastClient = Mockito.mock(ForecastApi  .class);
    NarrationService narrationService = prompt -> "Nice day in Berlin!";

    ForecastService service;

    @BeforeEach
    void setUp() {
        service = new ForecastService(geocodingClient, forecastClient, narrationService);
    }

    @Test
    void invalidCityRejected() {
        assertThrows(IllegalArgumentException.class, () -> service.getForecast("1@").block());
    }

    @Test
    void validFlowReturnsResponse() {
        GeocodeResponse geo = new GeocodeResponse();
        GeocodeResponse.Result r = new GeocodeResponse.Result();
        r.name = "Berlin"; r.latitude = 52.5; r.longitude = 13.41;
        geo.results = List.of(r);

        when(geocodingClient.geocode("Berlin")).thenReturn(Mono.just(geo));

        OpenMeteoForecastDto dto = new OpenMeteoForecastDto();
        OpenMeteoForecastDto.Daily daily = new OpenMeteoForecastDto.Daily();
        daily.sunrise = List.of("2023-11-01T07:12:00+05:30");
        daily.sunset  = List.of("2023-11-01T17:49:00+05:30");
        dto.daily = daily;

        when(forecastClient.fetch(52.5, 13.41)).thenReturn(Mono.just(dto));

        SunForecastResponse resp = service.getForecast("Berlin").block();
        assertThat(resp).isNotNull();
        assertThat(resp.getCity()).isEqualTo("Berlin");
        assertThat(resp.getSunrise()).contains("07:12");
        assertThat(resp.getSunset()).contains("17:49");
        assertThat(resp.getEnhanced_message()).isNotBlank();
    }
}
