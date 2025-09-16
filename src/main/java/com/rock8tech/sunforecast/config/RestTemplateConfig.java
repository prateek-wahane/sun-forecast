package com.rock8tech.sunforecast.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;


import java.time.Duration;

@Configuration
@ConditionalOnProperty(name = "app.http.client", havingValue = "resttemplate")
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofSeconds(15))
                .setReadTimeout(Duration.ofSeconds(30))
                .build();
        // Note: corporate proxies can be set via JVM props:
        // -Dhttps.proxyHost=proxy -Dhttps.proxyPort=8080
    }
}
