package com.rock8tech.sunforecast.dto;

import jakarta.validation.constraints.NotBlank;

public class CityRequest {
    @NotBlank(message = "city is required")
    private String city;

    public CityRequest() {}
    public CityRequest(String city) { this.city = city; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
}
