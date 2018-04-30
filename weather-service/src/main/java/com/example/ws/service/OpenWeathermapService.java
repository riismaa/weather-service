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


/**	
 * API call:
 *		api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}
 */
@Service
public class OpenWeathermapService implements WeatherService {

	@Override
	public TempReading getTemp(double latitude, double longitude) throws URISyntaxException {

		URI uri = new URIBuilder()
					.setScheme("http")
					.setHost("api.openweathermap.org")
					.setPath("/data/2.5/weather")
					.setParameter("lat", String.valueOf(latitude))
					.setParameter("lon", String.valueOf(longitude))
					.setParameter("appid", "7439dade2a66eb46bb6404f4ce35338d")
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
				JsonNode jsonNodeMain = jsonNodeRoot.get("main");
				JsonNode jsonNodeMainTemp = jsonNodeMain.get("temp");
				return new TempReading(jsonNodeMainTemp.asDouble(0.0));
				
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

}
