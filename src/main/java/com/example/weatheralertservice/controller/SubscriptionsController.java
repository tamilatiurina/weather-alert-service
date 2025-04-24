package com.example.weatheralertservice.controller;

import com.example.weatheralertservice.service.SubscriptionsService;
import com.example.weatheralertservice.service.WeatherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SubscriptionsController {

    private final SubscriptionsService subscriptionsService;
    private final WeatherService weatherService;

    public SubscriptionsController(SubscriptionsService subscriptionsService, WeatherService weatherService){
        this.subscriptionsService = subscriptionsService;
        this.weatherService = weatherService;
    }

    @GetMapping("/subscriptions")
    public String showSubscriptionForm() {
        return "subscriptionsPage";
    }

    @PostMapping("/subscriptions")
    public String subscriptions(@RequestParam(value = "email", required = false) String email,
                                @RequestParam(value = "fields", required = false) String condition,
                                @RequestParam(value = "city", required = false) String city, Model model,
                                RedirectAttributes redirectAttributes){
        boolean hasErrors = false;

        if (email == null || email.isBlank() || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            model.addAttribute("emailError", "Please enter a valid email address");
            hasErrors = true;
        } else if (!weatherService.isCityValid(city)) {
            model.addAttribute("cityError", "City not found. Please enter a valid city name.");
            hasErrors = true;
        }

        if (city == null || city.isBlank()) {
            model.addAttribute("cityError", "City is required");
            hasErrors = true;
        }

        if (condition == null || condition.isEmpty()) {
            model.addAttribute("fieldsError", "Select at least one notification type");
            hasErrors = true;
        }

        if (hasErrors) {
            model.addAttribute("email", email);
            model.addAttribute("city", city);
            model.addAttribute("selectedField", condition);
            return "subscriptionsPage";
        }

        subscriptionsService.subscribe(email, city, condition);
        redirectAttributes.addFlashAttribute("message", "Successfully subscribed!");

        return "redirect:/subscriptions";
    }
}
