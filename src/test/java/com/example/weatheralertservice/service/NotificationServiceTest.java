package com.example.weatheralertservice.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.weatheralertservice.model.NotificationDTO;
import com.example.weatheralertservice.model.SubscriptionDTO;
import com.example.weatheralertservice.model.WeatherDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import jakarta.persistence.EntityManager;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Arrays;

public class NotificationServiceTest {

    @Mock
    private SubscriptionsService subscriptionsService;

    @Mock
    private WeatherService weatherService;

    @Mock
    private EmailService emailService;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private NotificationService notificationService;

    private SubscriptionDTO subscription;
    private WeatherDTO weatherDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        subscription = new SubscriptionDTO();
        subscription.setEmail("test@example.com");
        subscription.setCity("Kyiv");
        subscription.setCondition("temperature");

        weatherDTO = new WeatherDTO(-5, 80, "Clear");


        when(weatherService.getWeather(anyString())).thenReturn(weatherDTO);
        when(subscriptionsService.findAll()).thenReturn(Arrays.asList(subscription));
    }

    @Test
    void testCheckAndNotify() {
        notificationService.checkAndNotify();

        verify(weatherService, times(1)).getWeather("Kyiv");
        verify(emailService, times(1)).sendNotification(anyString(), eq("Kyiv"), eq("temperature"));
        verify(entityManager, times(1)).persist(any(NotificationDTO.class));
        verify(subscriptionsService, times(1)).updateLastNotified(any(SubscriptionDTO.class), any(Timestamp.class));
    }

    @Test
    void testConditionMatchesTemperature() {
        boolean matches = notificationService.conditionMatches("temperature", weatherDTO);
        assertTrue(matches, "Condition should match for temperature below 0°C");
    }

    @Test
    void testConditionMatchesRain() {
        boolean matches = notificationService.conditionMatches("rain", weatherDTO);
        assertFalse(matches, "Condition should not match for non-rainy weather");
    }

    @Test
    void testNotAlreadyNotifiedToday() {
        Timestamp timestamp = Timestamp.valueOf(LocalDate.now().atStartOfDay());
        subscription.setLastNotified(timestamp);

        boolean result = notificationService.notAlreadyNotifiedToday(subscription);
        assertTrue(result, "Should return true if already notified today");
    }

    @Test
    void testNotAlreadyNotifiedTodayWithNullLastNotified() {
        subscription.setLastNotified(null);

        boolean result = notificationService.notAlreadyNotifiedToday(subscription);
        assertTrue(result, "Should return true if last notified is null");
    }
}
