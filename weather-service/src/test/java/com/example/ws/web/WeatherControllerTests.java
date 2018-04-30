package com.example.ws.web;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.example.ws.domain.TempReading;
import com.example.ws.service.DarkSkyService;
import com.example.ws.service.OpenWeathermapService;

@RunWith(SpringRunner.class)
@WebMvcTest(value=WeatherController.class, secure=false)
public class WeatherControllerTests {

   @Autowired
   private MockMvc mvc;
   
   @MockBean
   private OpenWeathermapService ws1;
   @MockBean
   private DarkSkyService ws2;

   @Test
   public void getAllTempReadings() throws Exception {
       TempReading reading1 = new TempReading(300.0);
       TempReading reading2 = new TempReading(290.0);
       
       
       given(ws1.getTemp(10.0, 12.0)).willReturn(reading1);
       given(ws2.getTemp(10.0, 12.0)).willReturn(reading2);
       
       mvc.perform(get("/ws/temp?latitude=10.0&longitude=12.0")
               .contentType(APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(2)))
               .andExpect(jsonPath("$[0].value", is(300.0)))
               .andExpect(jsonPath("$[1].value", is(290.0)));;
   }

   @Test
   public void getAverage() throws Exception {
       TempReading reading1 = new TempReading(300.0);
       TempReading reading2 = new TempReading(290.0);
       
       
       given(ws1.getTemp(10.0, 12.0)).willReturn(reading1);
       given(ws2.getTemp(10.0, 12.0)).willReturn(reading2);

       mvc.perform(get("/ws/average?latitude=10.0&longitude=12.0")
               .contentType(APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.average", is(295.0)));
   }


}
