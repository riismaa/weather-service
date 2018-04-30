package com.example.ws.service;

import java.net.URISyntaxException;

import com.example.ws.domain.TempReading;

public interface WeatherService {

    	public TempReading getTemp(double latitude, double longitude) throws URISyntaxException;

}
