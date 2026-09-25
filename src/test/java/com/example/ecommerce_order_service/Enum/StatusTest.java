package com.example.ecommerce_order_service.Enum;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StatusTest {

    @Test
    void pendingCanTransitionToPaid() {
        Status status = Status.PENDING;
        boolean result = status.canTransitionTo(Status.PAID);
        assertTrue(result);
    }

    @Test
    void failedCanTransitionToPaid() {
        Status status = Status.DELIVERED;
        boolean result = status.canTransitionTo(Status.PAID);
        assertFalse(result);
    }

    @Test
    void pendingToCanceledTransition() {
        Status status = Status.PENDING;
        boolean result = status.canTransitionTo(Status.CANCELLED);
        assertTrue(result);
    }

    @Test
    void paidToShippedTransition() {
        Status status = Status.PAID;
        boolean result = status.canTransitionTo(Status.SHIPPED);
        assertTrue(result);
    }

    @Test
    void paidToPendingTransition() {
        Status status = Status.PAID;
        boolean result = status.canTransitionTo(Status.PENDING);
        assertFalse(result);
    }

    @Test
    void canceledToSomethingTransition() {
        Status status = Status.CANCELLED;
        boolean result = status.canTransitionTo(Status.SHIPPED);
        assertFalse(result);
    }
}
