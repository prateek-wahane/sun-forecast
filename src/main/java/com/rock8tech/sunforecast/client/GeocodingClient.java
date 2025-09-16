package com.rock8tech.sunforecast.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@ConditionalOnProperty(name = "app.http.client", havingValue = "webclient", matchIfMissing = true)
public class GeocodingClient implements GeocodingApi {

    private final WebClient webClient;
    private final String geocodeUrl;

    public GeocodingClient(WebClient webClient,
                           @Value("${app.openMeteo.geocodeUrl}") String geocodeUrl) {
        this.webClient = webClient;
        this.geocodeUrl = geocodeUrl;
    }

    @Override
    public Mono<GeocodeResponse> geocode(String city) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https").host("geocoding-api.open-meteo.com")
                        .path("/v1/search").queryParam("name", city).queryParam("count", 1).build())
                .retrieve()
                .bodyToMono(GeocodeResponse.class);
    }
}
