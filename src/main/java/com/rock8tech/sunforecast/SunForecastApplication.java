package com.rock8tech.sunforecast;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SunForecastApplication {
    public static void main(String[] args) {
        SpringApplication.run(SunForecastApplication.class, args);
    }
}
