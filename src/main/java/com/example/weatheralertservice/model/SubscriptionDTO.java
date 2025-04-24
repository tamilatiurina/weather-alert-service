package com.example.weatheralertservice.model;


import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
public class SubscriptionDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String city;

    private String condition;

    private Timestamp lastNotified;

    @CreationTimestamp
    private Timestamp createdAt;

    @OneToMany(mappedBy = "subscription", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<NotificationDTO> notificationList = new ArrayList<>();

    public void addNotification(NotificationDTO notification){
        notificationList.add(notification);
    }

    public SubscriptionDTO(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Timestamp getLastNotified() {
        return lastNotified;
    }

    public void setLastNotified(Timestamp lastNotified) {
        this.lastNotified = lastNotified;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public List<NotificationDTO> getNotificationList() {
        return notificationList;
    }

    public void setNotificationList(List<NotificationDTO> notificationList) {
        this.notificationList = notificationList;
    }

    @Override
    public String toString() {
        return "SubscriptionDTO{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", city='" + city + '\'' +
                ", condition='" + condition + '\'' +
                ", lastNotified=" + lastNotified +
                ", createdAt=" + createdAt +
                ", notificationList=" + notificationList +
                '}';
    }
}
