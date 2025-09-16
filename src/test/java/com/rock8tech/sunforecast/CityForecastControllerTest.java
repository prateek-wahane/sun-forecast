package com.rock8tech.sunforecast;

import com.rock8tech.sunforecast.controller.CityForecastController;
import com.rock8tech.sunforecast.controller.GlobalExceptionHandler;
import com.rock8tech.sunforecast.dto.SunForecastResponse;
import com.rock8tech.sunforecast.service.ForecastService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;

@WebFluxTest(controllers = CityForecastController.class)
@Import(GlobalExceptionHandler.class)
public class CityForecastControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ForecastService forecastService;

    @Test
    void badRequestWhenCityMissing() {
        webTestClient.get()
                .uri("/api/sun-forecast")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void returnsOkWhenCityValid() {
        SunForecastResponse resp = new SunForecastResponse(
                "Paris",
                "2023-11-01T07:12:00+05:30",
                "2023-11-01T17:49:00+05:30",
                "Tomorrow in Paris, the sun will rise at 7:12 AM IST and set at 5:49 PM IST."
        );
        when(forecastService.getForecast("Paris")).thenReturn(Mono.just(resp));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/sun-forecast")
                        .queryParam("city", "Paris")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.city").isEqualTo("Paris")
                .jsonPath("$.sunrise").exists()
                .jsonPath("$.sunset").exists()
                .jsonPath("$.enhanced_message").exists();
    }
}
