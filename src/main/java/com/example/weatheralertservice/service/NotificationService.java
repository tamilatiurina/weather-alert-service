package com.example.weatheralertservice.service;

import com.example.weatheralertservice.model.NotificationDTO;
import com.example.weatheralertservice.model.SubscriptionDTO;
import com.example.weatheralertservice.model.WeatherDTO;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Service
public class NotificationService {

    private final SubscriptionsService subscriptionsService;
    private final WeatherService weatherService;

    private final EmailService emailService;

    private final EntityManager entityManager;


    public NotificationService(SubscriptionsService subscriptionsService, WeatherService weatherService, EmailService emailService, EntityManager entityManager){
        this.subscriptionsService = subscriptionsService;
        this.weatherService = weatherService;
        this.emailService = emailService;
        this.entityManager = entityManager;
    }

    @Scheduled(cron = "0 0 7 * * *", zone = "Europe/Warsaw")
    @Transactional
    public void checkAndNotify() {
        List<SubscriptionDTO> subs = subscriptionsService.findAll();

        for (SubscriptionDTO sub : subs) {
            WeatherDTO weather = weatherService.getWeather(sub.getCity());
            if (conditionMatches(sub.getCondition(), weather)){
                if (notAlreadyNotifiedToday(sub)) {
                    emailService.sendNotification(sub.getEmail(), sub.getCity(), sub.getCondition());

                    Timestamp currentTime = new Timestamp(System.currentTimeMillis());
                    createNotification(sub, currentTime);
                    subscriptionsService.updateLastNotified(sub, currentTime);
                }
            }
        }
    }

    public boolean conditionMatches(String condition, WeatherDTO weather){
        if( (condition.equals("temperature") && weather.getTemp() < 0) ||
                (condition.equals("rain") && weather.getSky().toLowerCase().contains("rain"))) {
            return true;
        }

        return false;
    }

    public boolean notAlreadyNotifiedToday(SubscriptionDTO sub){
        if (sub.getLastNotified() == null) return true;

        LocalDate lastDate = sub.getLastNotified().toLocalDateTime().toLocalDate();
        LocalDate today = LocalDate.now();
        return lastDate.isEqual(today);
    }

    public void createNotification(SubscriptionDTO sub, Timestamp currentTime){
        NotificationDTO notification = new NotificationDTO();
        notification.setNotificationTime(currentTime);
        notification.setSubscription(sub);


        addNotification(notification);
    }

    public void addNotification(NotificationDTO notification){
        entityManager.persist(notification);
    }
}

