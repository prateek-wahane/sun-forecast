package com.rock8tech.sunforecast.config;

import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.boot.actuate.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Map;

@Configuration
public class AppInfoContributor {

    @Bean
    InfoContributor appInfo(Environment env) {
        return (Info.Builder builder) -> builder.withDetail("app", Map.of(
            "aiEnabled", env.getProperty("app.ai.enabled", "true"),
            "httpClient", env.getProperty("app.http.client", "webclient"),
            "timezoneParam", env.getProperty("app.timezoneParam", "Asia/Kolkata")
        ));
    }
}
