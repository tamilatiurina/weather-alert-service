package com.example.weatheralertservice.controller;

import com.example.weatheralertservice.model.WeatherDTO;
import com.example.weatheralertservice.service.WeatherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WeatherController {
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @RequestMapping("/weather")
    public String weather(@RequestParam(required = true) String city, Model model) {
        try {
            WeatherDTO weatherData = weatherService.getWeather(city);

            if (weatherData != null) {
                model.addAttribute("weather", weatherData);
                model.addAttribute("city", city);
            } else {
                return "redirect:/errorPage";
            }
        } catch (Exception e) {
            return "redirect:/errorPage";
        }

        return "weatherPage";
    }

    @RequestMapping("/errorPage")
    public String errorPage() {
        return "errorPage";
    }

}
