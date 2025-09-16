package com.rock8tech.sunforecast.service;

import com.rock8tech.sunforecast.ai.NarrationService;
import com.rock8tech.sunforecast.client.GeocodeResponse;
import com.rock8tech.sunforecast.client.GeocodingClient;
import com.rock8tech.sunforecast.client.OpenMeteoForecastClient;
import com.rock8tech.sunforecast.client.OpenMeteoForecastDto;
import com.rock8tech.sunforecast.dto.SunForecastResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.Optional;

import com.rock8tech.sunforecast.client.GeocodingApi;
import com.rock8tech.sunforecast.client.ForecastApi;

@Service
public class ForecastService {

    private final GeocodingApi geocodingClient;
    private final ForecastApi  forecastClient;
    private final NarrationService narrationService;

    public ForecastService(GeocodingApi geocodingClient,
                           ForecastApi forecastClient,
                           NarrationService narrationService) {
        this.geocodingClient = geocodingClient;
        this.forecastClient  = forecastClient;
        this.narrationService = narrationService;
    }

    @Cacheable(cacheNames = "forecastByCity", key = "#city.toLowerCase()")
    public Mono<SunForecastResponse> getForecast(String city) {
        if (!StringUtils.hasText(city) || !city.matches("^[A-Za-z .-]{2,}$")) {
            return Mono.error(new IllegalArgumentException("Invalid city name"));
        }
        return geocodingClient.geocode(city)
                .flatMap(geo -> {
                    GeocodeResponse.Result result = (geo.results == null || geo.results.isEmpty())
                            ? null : geo.results.get(0);
                    if (result == null) {
                        return Mono.error(new IllegalArgumentException("City not found"));
                    }
                    return forecastClient.fetch(result.latitude, result.longitude)
                            .map(forecast -> buildResponse(city, forecast));
                });
    }

    private SunForecastResponse buildResponse(String city, OpenMeteoForecastDto dto) {
    int idx = (dto.daily != null && dto.daily.sunrise != null && dto.daily.sunrise.size() > 1) ? 1 : 0;

    String sunrise = getAt(dto.daily != null ? dto.daily.sunrise : null, idx);
    String sunset  = getAt(dto.daily != null ? dto.daily.sunset  : null, idx);

    String prompt = String.format(
        "%s in %s, the sun will rise at %s and set at %s IST. Enjoy the stunning golden hour!",
        (idx == 1 ? "Tomorrow" : "Today"), city, prettyTime(sunrise), prettyTime(sunset)
    );

    String enhanced = narrationService.narrate(prompt);
    return new SunForecastResponse(city, sunrise, sunset, enhanced);
	}

	private String getAt(java.util.List<String> list, int i) {
    if (list == null || list.size() <= i) return null;
    return list.get(i);
	}

    private Optional<String> first(java.util.List<String> list) {
        if (list == null || list.isEmpty()) return Optional.empty();
        return Optional.ofNullable(list.get(0));
    }

    private String prettyTime(String iso) {
		if (iso == null || iso.isBlank()) return "N/A";
		try { // with offset, e.g. 2025-09-16T10:12:00+05:30
			return to12h(OffsetDateTime.parse(iso).toLocalTime());
		} catch (Exception ignore) {}
		try { // without offset, e.g. 2025-09-16T10:12
			return to12h(LocalDateTime.parse(iso).toLocalTime());
		} catch (Exception ignore) {}
		try { // last resort, parse only the time part
			return to12h(LocalTime.parse(iso.substring(11)));
		} catch (Exception ignore) {}
    return iso; // fallback
	}

	private String to12h(LocalTime t) {
		int h = t.getHour(), m = t.getMinute();
		String ampm = h >= 12 ? "PM" : "AM";
		int h12 = h % 12; if (h12 == 0) h12 = 12;
		return String.format("%d:%02d %s", h12, m, ampm);
	}
}
