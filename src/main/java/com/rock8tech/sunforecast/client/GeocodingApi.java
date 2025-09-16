package com.rock8tech.sunforecast.client;

import reactor.core.publisher.Mono;

public interface GeocodingApi {
    Mono<GeocodeResponse> geocode(String city);
}
