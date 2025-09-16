package com.rock8tech.sunforecast.client;

import reactor.core.publisher.Mono;

public interface ForecastApi {
    Mono<OpenMeteoForecastDto> fetch(double lat, double lon);
}
