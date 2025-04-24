package com.example.weatheralertservice.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.weatheralertservice.model.SubscriptionDTO;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import jakarta.persistence.EntityManager;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

public class SubscriptionsServiceTest {

    @Mock
    private EntityManager entityManager;
    @Mock
    private TypedQuery<SubscriptionDTO> typedQuery;

    @InjectMocks
    private SubscriptionsService subscriptionsService;

    private SubscriptionDTO subscription;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        subscription = new SubscriptionDTO();
        subscription.setEmail("test@example.com");
        subscription.setCity("Kyiv");
        subscription.setCondition("temperature");
    }

    @Test
    public void testSubscribe() {
        subscriptionsService.subscribe(subscription.getEmail(), subscription.getCity(), subscription.getCondition());

        verify(entityManager, times(1)).persist(any(SubscriptionDTO.class));
    }

    @Test
    public void testAddSubscription() {
        subscriptionsService.addSubscription(subscription);

        verify(entityManager, times(1)).persist(subscription);
    }

    @Test
    public void testFindAll() {
        when(entityManager.createQuery("SELECT s FROM SubscriptionDTO s", SubscriptionDTO.class))
                .thenReturn(typedQuery);

        when(typedQuery.getResultList()).thenReturn(Arrays.asList(subscription));

        List<SubscriptionDTO> subscriptions = subscriptionsService.findAll();

        assertNotNull(subscriptions);
        assertEquals(1, subscriptions.size());
        assertEquals(subscription, subscriptions.get(0));

        verify(entityManager, times(1)).createQuery("SELECT s FROM SubscriptionDTO s", SubscriptionDTO.class);
        verify(typedQuery, times(1)).getResultList();
    }

    @Test
    public void testUpdateLastNotified() {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());

        subscriptionsService.updateLastNotified(subscription, currentTime);

        verify(entityManager, times(1)).merge(subscription);

        assertEquals(currentTime, subscription.getLastNotified());
    }
}
