package com.example.weatheralertservice.model;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
public class NotificationDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Timestamp notificationTime;

    @ManyToOne
    @JoinColumn(name = "subscription_id")
    private SubscriptionDTO subscription;

    public NotificationDTO() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getNotificationTime() {
        return notificationTime;
    }

    public void setNotificationTime(Timestamp notificationTime) {
        this.notificationTime = notificationTime;
    }

    public SubscriptionDTO getSubscription() {
        return subscription;
    }

    public void setSubscription(SubscriptionDTO subscription) {
        this.subscription = subscription;
    }

    @Override
    public String toString() {
        return "NotificationDTO{" +
                "id=" + id +
                ", notificationTime=" + notificationTime +
                ", subscription=" + subscription +
                '}';
    }
}
