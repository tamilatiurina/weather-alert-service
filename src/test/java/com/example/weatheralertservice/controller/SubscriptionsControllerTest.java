package com.example.weatheralertservice.controller;

import com.example.weatheralertservice.service.SubscriptionsService;
import com.example.weatheralertservice.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class SubscriptionsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SubscriptionsService subscriptionsService;

    @Mock
    private WeatherService weatherService;

    @InjectMocks
    private SubscriptionsController subscriptionsController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(subscriptionsController).build();
    }

    @Test
    public void testShowSubscriptionForm() throws Exception {
        mockMvc.perform(get("/subscriptions"))
                .andExpect(status().isOk())
                .andExpect(view().name("subscriptionsPage"));
    }

    @Test
    public void testValidSubscription() throws Exception {
        String email = "test@example.com";
        String city = "Kyiv";
        String condition = "temperature";

        when(weatherService.isCityValid(city)).thenReturn(true);

        mockMvc.perform(post("/subscriptions")
                        .param("email", email)
                        .param("city", city)
                        .param("fields", condition))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/subscriptions"))
                .andExpect(flash().attribute("message", "Successfully subscribed!"));

        verify(subscriptionsService, times(1)).subscribe(email, city, condition);
    }

    @Test
    public void testInvalidEmail() throws Exception {
        String invalidEmail = "invalid-email";
        String city = "Kyiv";
        String condition = "temperature";

        mockMvc.perform(post("/subscriptions")
                        .param("email", invalidEmail)
                        .param("city", city)
                        .param("fields", condition))
                .andExpect(status().isOk())
                .andExpect(view().name("subscriptionsPage"))
                .andExpect(model().attributeExists("emailError"))
                .andExpect(model().attribute("emailError", "Please enter a valid email address"));
    }

    @Test
    public void testCityNotFound() throws Exception {
        String email = "test@example.com";
        String city = "InvalidCity";
        String condition = "temperature";

        when(weatherService.isCityValid(city)).thenReturn(false);

        mockMvc.perform(post("/subscriptions")
                        .param("email", email)
                        .param("city", city)
                        .param("fields", condition))
                .andExpect(status().isOk())
                .andExpect(view().name("subscriptionsPage"))
                .andExpect(model().attributeExists("cityError"))
                .andExpect(model().attribute("cityError", "City not found. Please enter a valid city name."));
    }

    @Test
    public void testMissingCity() throws Exception {
        String email = "test@example.com";
        String city = "";
        String condition = "temperature";

        mockMvc.perform(post("/subscriptions")
                        .param("email", email)
                        .param("city", city)
                        .param("fields", condition))
                .andExpect(status().isOk())
                .andExpect(view().name("subscriptionsPage"))
                .andExpect(model().attributeExists("cityError"))
                .andExpect(model().attribute("cityError", "City is required"));
    }

    @Test
    public void testMissingCondition() throws Exception {
        String email = "test@example.com";
        String city = "Kyiv";
        String condition = "";

        mockMvc.perform(post("/subscriptions")
                        .param("email", email)
                        .param("city", city)
                        .param("fields", condition))
                .andExpect(status().isOk())
                .andExpect(view().name("subscriptionsPage"))
                .andExpect(model().attributeExists("fieldsError"))
                .andExpect(model().attribute("fieldsError", "Select at least one notification type"));
    }
}
