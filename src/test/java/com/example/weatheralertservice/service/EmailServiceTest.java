package com.example.weatheralertservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;

import static org.mockito.Mockito.*;

class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendNotificationForTemperature() {
        String email = "test@example.com";
        String city = "Kyiv";
        String condition = "temperature";

        SimpleMailMessage expectedMessage = new SimpleMailMessage();
        expectedMessage.setTo(email);
        expectedMessage.setSubject("Weather Alert");
        expectedMessage.setText("Temperature is below 0°C in " + city);

        emailService.sendNotification(email, city, condition);
        verify(mailSender, times(1)).send(expectedMessage);
    }

    @Test
    void testSendNotificationForRain() {
        String email = "test@example.com";
        String city = "Kyiv";
        String condition = "rain";

        SimpleMailMessage expectedMessage = new SimpleMailMessage();
        expectedMessage.setTo(email);
        expectedMessage.setSubject("Weather Alert");
        expectedMessage.setText("It's raining in " + city);

        emailService.sendNotification(email, city, condition);

        verify(mailSender, times(1)).send(expectedMessage);
    }

    @Test
    void testSendNotificationWithUnknownCondition() {
        String email = "test@example.com";
        String city = "Kyiv";
        String condition = "wind";

        emailService.sendNotification(email, city, condition);

        SimpleMailMessage expectedMessage = new SimpleMailMessage();
        expectedMessage.setTo(email);
        expectedMessage.setSubject("Weather Alert");
        expectedMessage.setText(null);

        verify(mailSender, times(1)).send(expectedMessage);
    }

    @Test
    void testSendNotificationWithException() {
        String email = "test@example.com";
        String city = "Kyiv";
        String condition = "rain";

        doThrow(new RuntimeException("Mail sending failed")).when(mailSender).send(any(SimpleMailMessage.class));

        emailService.sendNotification(email, city, condition);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}

