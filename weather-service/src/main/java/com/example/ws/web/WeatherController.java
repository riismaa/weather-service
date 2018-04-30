package com.example.ws.web;

import java.net.URISyntaxException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ws.domain.TempReading;
import com.example.ws.service.DarkSkyService;
import com.example.ws.service.OpenWeathermapService;
import com.example.ws.service.WeatherService;

/**
 * Weather Controller
 *
 */
@RestController
@RequestMapping(path = "/ws")
@Validated
public class WeatherController {

	WeatherService ws1, ws2;

	@Autowired
	public WeatherController(OpenWeathermapService ws1, DarkSkyService ws2) {
		this.ws1 = ws1;
		this.ws2 = ws2;
	}

	protected WeatherController() {

	}


	/**
	 * Lookup a the temperature.
	 *
	 * @param latitude
	 * @param longitude
	 * @return
	 * @throws URISyntaxException 
	 */
	@RequestMapping(method = RequestMethod.GET, path="/temp")
	public List<TempReading> getAllTempReadings(
			@Max(value = 90) @Min(value= -90) @RequestParam(value = "latitude") double latitude,
			@Max(value = 180) @Min(value= -180) @RequestParam(value = "longitude") double longitude) throws URISyntaxException {

		List<TempReading> temps = new ArrayList<TempReading>();
		temps.add(ws1.getTemp(latitude, longitude));
		temps.add(ws2.getTemp(latitude, longitude));

		return temps;
	}

	/**
	 * Calculate the average temperature.
	 *
	 * @param latitude
	 * @param longitude
	 * @return Tuple of "average" and the average value.
	 * @throws URISyntaxException 
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/average")
	public AbstractMap.SimpleEntry<String, Double> getAverage(
			@Max(value = 90) @Min(value= -90) @RequestParam(value = "latitude") double latitude,
			@Max(value = 180) @Min(value= -180) @RequestParam(value = "longitude") double longitude) throws URISyntaxException {

		List<TempReading> readings = new ArrayList<>();
		readings.add(ws1.getTemp(latitude, longitude));
		readings.add(ws2.getTemp(latitude, longitude));

		OptionalDouble average = readings.stream().mapToDouble(TempReading::getValue).average();
		double result = average.isPresent() ? Math.round(average.getAsDouble()):null;
		return new AbstractMap.SimpleEntry<String, Double>("average", result);
	}

}
