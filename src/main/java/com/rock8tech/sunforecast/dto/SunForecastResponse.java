package com.rock8tech.sunforecast.dto;

public class SunForecastResponse {
    private String city;
    private String sunrise;
    private String sunset;
    private String enhanced_message;

    public SunForecastResponse() {}

    public SunForecastResponse(String city, String sunrise, String sunset, String enhanced_message) {
        this.city = city;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.enhanced_message = enhanced_message;
    }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getSunrise() { return sunrise; }
    public void setSunrise(String sunrise) { this.sunrise = sunrise; }
    public String getSunset() { return sunset; }
    public void setSunset(String sunset) { this.sunset = sunset; }
    public String getEnhanced_message() { return enhanced_message; }
    public void setEnhanced_message(String enhanced_message) { this.enhanced_message = enhanced_message; }
}
