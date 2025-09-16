package com.rock8tech.sunforecast.client;

import java.util.List;

public class GeocodeResponse {
    public List<Result> results;

    public static class Result {
        public String name;
        public double latitude;
        public double longitude;
        public String country;
    }
}
