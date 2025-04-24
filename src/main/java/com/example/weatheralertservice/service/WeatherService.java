package com.example.weatheralertservice.service;

import com.example.weatheralertservice.model.WeatherDTO;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class WeatherService {

    @Value("${weather.api.key}")
    private String apiKey;

    public WeatherDTO getWeather(String city) {
        JSONObject json = fetchWeatherData(city);
        if (json == null || json.has("error")) return null;

        try {
            JSONObject current = json.getJSONObject("current");
            int temp = (int) current.getDouble("temp_c");
            int humidity = current.getInt("humidity");
            String sky = current.getJSONObject("condition").getString("text");

            return new WeatherDTO(temp, humidity, sky);
        } catch (Exception e) {
            System.out.println("Error getting the current weather: " + e.getMessage());
        }

        return null;
    }

    public boolean isCityValid(String city) {
        JSONObject json = fetchWeatherData(city);
        if (json == null || json.has("error")) return false;

        try {
            String returnedCity = json.getJSONObject("location").getString("name").toLowerCase();
            return returnedCity.contains(city.toLowerCase());
        } catch (Exception e) {
            System.out.println("Error finding the city: " + e.getMessage());
        }

        return false;
    }

    private JSONObject fetchWeatherData(String city) {
        String urlString = String.format(
                "http://api.weatherapi.com/v1/current.json?key=%s&q=%s",
                apiKey, city
        );

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream())
            );
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return new JSONObject(response.toString());

        } catch (Exception e) {
            System.out.println("Error fetching weather data: " + e.getMessage());
            return null;
        }
    }
}
