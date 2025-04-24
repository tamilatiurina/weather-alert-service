package com.example.weatheralertservice.controller;

import com.example.weatheralertservice.model.WeatherDTO;
import com.example.weatheralertservice.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class WeatherControllerTest {

    private MockMvc mockMvc;

    @Mock
    private WeatherService weatherService;

    @InjectMocks
    private WeatherController weatherController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(weatherController).build();
    }

    @Test
    public void testWeatherSuccess() throws Exception {
        WeatherDTO mockWeatherData = new WeatherDTO(20, 60, "Clear sky");
        String city = "Kyiv";

        when(weatherService.getWeather(city)).thenReturn(mockWeatherData);

        mockMvc.perform(get("/weather").param("city", city))
                .andExpect(status().isOk())
                .andExpect(view().name("weatherPage"))
                .andExpect(model().attributeExists("weather"))
                .andExpect(model().attribute("city", city))
                .andExpect(model().attribute("weather", mockWeatherData));
    }

    @Test
    public void testWeatherFailure() throws Exception {
        String city = "InvalidCity";

        when(weatherService.getWeather(city)).thenReturn(null);

        mockMvc.perform(get("/weather").param("city", city))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/errorPage"));
    }



    @Test
    public void testWeatherException() throws Exception {
        String city = "Kyiv";

        when(weatherService.getWeather(city)).thenThrow(new RuntimeException("Service error"));

        mockMvc.perform(get("/weather").param("city", city))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/errorPage"));
    }

}
