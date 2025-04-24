package com.example.weatheralertservice.service;

import com.example.weatheralertservice.model.SubscriptionDTO;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class SubscriptionsService {

        private final EntityManager entityManager;

        public SubscriptionsService(EntityManager entityManager){
                this.entityManager = entityManager;
        }

        @Transactional
        public void subscribe(String email, String city, String condition) {
                SubscriptionDTO subscription = new SubscriptionDTO();
                subscription.setEmail(email);
                subscription.setCity(city);
                subscription.setCondition(condition);

                addSubscription(subscription);
        }

        public void addSubscription(SubscriptionDTO subscription){
                entityManager.persist(subscription);
        }

        public List<SubscriptionDTO> findAll() {
                return entityManager
                        .createQuery("SELECT s FROM SubscriptionDTO s", SubscriptionDTO.class)
                        .getResultList();
        }

        public void updateLastNotified(SubscriptionDTO sub, Timestamp currentTime){
                sub.setLastNotified(currentTime);
                entityManager.merge(sub);
        }
}
