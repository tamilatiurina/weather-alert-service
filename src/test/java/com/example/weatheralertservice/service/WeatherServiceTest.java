package com.example.weatheralertservice.service;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.weatheralertservice.model.WeatherDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

public class WeatherServiceTest {

    @InjectMocks
    private WeatherService weatherService;

    @Mock
    private HttpURLConnection mockConnection;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetWeatherSuccess() {
        String city = "Kyiv";
        String jsonResponse = "{"
                + "\"current\": {"
                + "\"temp_c\": 25, "
                + "\"humidity\": 23, "
                + "\"condition\": {\"text\": \"Sunny\"}"
                + "}"
                + "}";

        InputStream inputStream = new ByteArrayInputStream(jsonResponse.getBytes(StandardCharsets.UTF_8));

        try {
            when(mockConnection.getInputStream()).thenReturn(inputStream);
            JSONObject mockJson = new JSONObject(jsonResponse);
            mockFetchWeatherData(mockJson);
        } catch (Exception e) {
            System.out.println("Error during fetching data for test:" + e.getMessage());
        }

        WeatherDTO weatherDTO = weatherService.getWeather(city);

        assertNotNull(weatherDTO);
        assertEquals(25, weatherDTO.getTemp());
        assertEquals(23, weatherDTO.getHumidity());
        assertEquals("Sunny", weatherDTO.getSky());
    }

    @Test
    public void testGetWeatherFailure() {
        String city = "InvalidCity";
        mockFetchWeatherData(null);

        WeatherDTO weatherDTO = weatherService.getWeather(city);

        assertNull(weatherDTO);
    }

    @Test
    public void testIsCityValidSuccess() {
        String city = "Kyiv";
        String jsonResponse = "{"
                + "\"location\": {\"name\": \"Kyiv\"},"
                + "\"current\": {}"
                + "}";

        InputStream inputStream = new ByteArrayInputStream(jsonResponse.getBytes(StandardCharsets.UTF_8));

        try {
            when(mockConnection.getInputStream()).thenReturn(inputStream);
            JSONObject mockJson = new JSONObject(jsonResponse);
            mockFetchWeatherData(mockJson);
        } catch (Exception e) {
            System.out.println("Error during fetching data for test:" + e.getMessage());
        }

        boolean isValid = weatherService.isCityValid(city);

        assertTrue(isValid);
    }

    @Test
    public void testIsCityValidFailure() {
        String city = "InvalidCity";
        mockFetchWeatherData(null);

        boolean isValid = weatherService.isCityValid(city);

        assertFalse(isValid);
    }

    private void mockFetchWeatherData(JSONObject mockJsonResponse) {
        try {
            when(mockConnection.getInputStream()).thenReturn(new ByteArrayInputStream(mockJsonResponse.toString().getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            System.out.println("Error during fetching the data: " + e.getMessage());
        }
    }
}

