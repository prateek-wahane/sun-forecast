package com.rock8tech.sunforecast.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@ConditionalOnProperty(name = "app.http.client", havingValue = "resttemplate")
public class OpenMeteoForecastClientRt implements ForecastApi {

    private final RestTemplate rt;
    private final String dailyParams;
    private final String timezoneParam;

    public OpenMeteoForecastClientRt(RestTemplate rt,
                                     @Value("${app.openMeteo.dailyParams}") String dailyParams,
                                     @Value("${app.timezoneParam}") String timezoneParam) {
        this.rt = rt;
        this.dailyParams = dailyParams;
        this.timezoneParam = timezoneParam;
    }

    @Override
    public Mono<OpenMeteoForecastDto> fetch(double lat, double lon) {
        URI uri = UriComponentsBuilder.fromHttpUrl("https://api.open-meteo.com/v1/forecast")
                .queryParam("latitude", lat)
                .queryParam("longitude", lon)
                .queryParam("daily", dailyParams)
                .queryParam("timezone", timezoneParam)
                .build(true).toUri();
        return Mono.fromSupplier(() -> rt.getForObject(uri, OpenMeteoForecastDto.class));
    }
}
