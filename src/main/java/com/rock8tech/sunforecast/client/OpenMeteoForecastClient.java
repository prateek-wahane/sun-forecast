package com.rock8tech.sunforecast.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@ConditionalOnProperty(name = "app.http.client", havingValue = "webclient", matchIfMissing = true)
public class OpenMeteoForecastClient implements ForecastApi {

    private final WebClient webClient;
    private final String dailyParams;
    private final String timezoneParam;

    public OpenMeteoForecastClient(WebClient webClient,
                                   @Value("${app.openMeteo.dailyParams}") String dailyParams,
                                   @Value("${app.timezoneParam}") String timezoneParam) {
        this.webClient = webClient;
        this.dailyParams = dailyParams;
        this.timezoneParam = timezoneParam;
    }

    @Override
    public Mono<OpenMeteoForecastDto> fetch(double lat, double lon) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https").host("api.open-meteo.com").path("/v1/forecast")
                        .queryParam("latitude", lat)
                        .queryParam("longitude", lon)
                        .queryParam("daily", dailyParams)
                        .queryParam("timezone", timezoneParam)
                        .build())
                .retrieve()
                .bodyToMono(OpenMeteoForecastDto.class);
    }
}
