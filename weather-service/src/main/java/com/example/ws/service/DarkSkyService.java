package com.example.ws.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;

import com.example.ws.domain.TempReading;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class DarkSkyService implements WeatherService {

	/**	API call:
	 *    https://api.darksky.net/forecast/[key]/[latitude],[longitude]
	 */    	

	@Override
	public TempReading getTemp(double latitude, double longitude) throws URISyntaxException {

		StringBuilder pathBuilder = new StringBuilder("/forecast/64380937c2ed9f2c0536e3fe6e9aa9ec/");
		pathBuilder.append(String.valueOf(latitude));
		pathBuilder.append(",");
		pathBuilder.append(String.valueOf(longitude));

		URI uri = new URIBuilder()
				.setScheme("https")
				.setHost("api.darksky.net")
				.setPath(pathBuilder.toString())
				.build();

		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet request = new HttpGet(uri);
		System.out.println(request.getURI());
		CloseableHttpResponse response = null;
		try {
			response = client.execute(request);
			int status = response.getStatusLine().getStatusCode();
			if (status >= 200 && status < 300) {
				ObjectMapper objectMapper = new ObjectMapper();  
				JsonNode jsonNodeRoot = objectMapper.readTree(new InputStreamReader(response
						.getEntity().getContent()));
				JsonNode jsonNodeCurrently = jsonNodeRoot.get("currently");
				JsonNode jsonNodeCurrentTemp = jsonNodeCurrently.get("temperature");
				return new TempReading(fahrenheitToKelvin(jsonNodeCurrentTemp.asDouble(0.0)));  
			} else {
				System.out.println("Unexpected response status: " + status);
			}

		} catch (IOException | UnsupportedOperationException e) {
			e.printStackTrace();
		} finally {
			if(null != response){
				try {
					response.close();
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	private double fahrenheitToKelvin(double fahrenheit) {
		double kelvin = 5.0/9.0 * (fahrenheit - 32.0) + 273.15;
		return Math.round(kelvin);
	}
}
