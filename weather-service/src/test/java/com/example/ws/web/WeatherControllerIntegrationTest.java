package com.example.ws.web;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class WeatherControllerIntegrationTest {

	@Autowired
	MockMvc mvc;

	@Test
	public void getAllTempReadings() throws Exception {

		mvc.perform(get("/ws/temp?latitude=10.0&longitude=12.0")
				.contentType(APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", hasSize(2)))
		.andExpect(jsonPath("$[0].value").isNumber())
		.andExpect( jsonPath("$[1].value").isNumber());
	}
	
	@Test
	public void getAverage() throws Exception {

		mvc.perform(get("/ws/average?latitude=10.0&longitude=12.0")
				.contentType(APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect( jsonPath("$.average").isNumber());
	}

}
