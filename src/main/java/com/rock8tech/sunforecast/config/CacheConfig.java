// src/main/java/com/rock8tech/sunforecast/config/CacheConfig.java
package com.rock8tech.sunforecast.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class CacheConfig {

    @Bean
    public CaffeineCacheManager cacheManager() {
        CaffeineCacheManager manager = new CaffeineCacheManager("forecastByCity");
        manager.setCaffeine(
                Caffeine.newBuilder()
                        .maximumSize(500)
                        .expireAfterWrite(Duration.ofHours(1))
        );
        // IMPORTANT: needed to cache reactive return types (Mono/Flux)
        manager.setAsyncCacheMode(true);
        return manager;
    }
}
