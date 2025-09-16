package com.rock8tech.sunforecast.client;

import java.util.List;

public class OpenMeteoForecastDto {
    public Daily daily;

    public static class Daily {
        public List<String> time;
        public List<String> sunrise;
        public List<String> sunset;
    }
}
