package com.rock8tech.sunforecast.client;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@ConditionalOnProperty(name = "app.http.client", havingValue = "resttemplate")
public class GeocodingClientRt implements GeocodingApi {

    private final RestTemplate rt;

    public GeocodingClientRt(RestTemplate rt) {
        this.rt = rt;
    }

    @Override
    public Mono<GeocodeResponse> geocode(String city) {
        URI uri = UriComponentsBuilder.fromHttpUrl("https://geocoding-api.open-meteo.com/v1/search")
                .queryParam("name", city).queryParam("count", 1).build(true).toUri();
        return Mono.fromSupplier(() -> rt.getForObject(uri, GeocodeResponse.class));
    }
}
