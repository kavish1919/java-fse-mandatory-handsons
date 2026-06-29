package com.cognizant.exercise10;

import com.cognizant.order.model.Order;
import com.cognizant.order.repository.OrderRepository;
import com.cognizant.order.service.OrderService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Exercise 10 — Mockito: Mocking and Stubbing.
 *
 * <p>Demonstrates:
 * <ul>
 *   <li>{@code @Mock} — creates a mock of {@link OrderRepository}</li>
 *   <li>{@code @InjectMocks} — injects the mock into {@link OrderService}</li>
 *   <li>{@code when(...).thenReturn(...)} — stubbing happy-path responses</li>
 *   <li>{@code when(...).thenThrow(...)} — stubbing error-path responses</li>
 *   <li>{@code any()} matcher for flexible argument matching</li>
 * </ul>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Exercise 10 — Mockito: Mocking and Stubbing")
class Exercise10_MockitoMockingTest {

    /** Mock — Mockito generates a no-op implementation of this interface. */
    @Mock
    private OrderRepository orderRepository;

    /**
     * The real service under test — Mockito automatically injects
     * {@code orderRepository} via constructor injection.
     */
    @InjectMocks
    private OrderService orderService;

    // ------------------------------------------------------------------
    // Happy-path (stub returns valid data)
    // ------------------------------------------------------------------

    @Test
    @DisplayName("placeOrder: repository.save() is called and saved order is returned")
    void placeOrderCallsSaveAndReturnsOrder() {
        // Arrange
        Order inputOrder = new Order("ORD-001", "CUST-A", 250.00);
        Order savedOrder = new Order("ORD-001", "CUST-A", 250.00);
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        // Act
        Order result = orderService.placeOrder(inputOrder);

        // Assert
        assertNotNull(result);
        assertEquals("ORD-001", result.getOrderId());
        assertEquals(250.00, result.getTotalAmount(), 1e-9);
    }

    @Test
    @DisplayName("getOrder: repository.findById() stub returns order when present")
    void getOrderReturnsOrderWhenFound() {
        // Arrange
        Order expected = new Order("ORD-002", "CUST-B", 99.95);
        when(orderRepository.findById("ORD-002")).thenReturn(Optional.of(expected));

        // Act
        Order result = orderService.getOrder("ORD-002");

        // Assert
        assertNotNull(result);
        assertEquals("CUST-B", result.getCustomerId());
    }

    // ------------------------------------------------------------------
    // Error-path (stub triggers exception / empty optional)
    // ------------------------------------------------------------------

    @Test
    @DisplayName("getOrder: throws NoSuchElementException when repository returns empty")
    void getOrderThrowsWhenNotFound() {
        // Arrange
        when(orderRepository.findById("ORD-999")).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(java.util.NoSuchElementException.class,
                () -> orderService.getOrder("ORD-999"),
                "Missing order must cause NoSuchElementException.");
    }

    @Test
    @DisplayName("placeOrder: repository.save() throws RuntimeException — service propagates it")
    void placeOrderPropagatesRepositoryException() {
        // Arrange
        Order inputOrder = new Order("ORD-003", "CUST-C", 500.00);
        when(orderRepository.save(any(Order.class)))
                .thenThrow(new RuntimeException("Database unavailable"));

        // Act + Assert
        assertThrows(RuntimeException.class,
                () -> orderService.placeOrder(inputOrder));
    }

    // ------------------------------------------------------------------
    // Input validation (service-level guards — repository not called)
    // ------------------------------------------------------------------

    @Test
    @DisplayName("placeOrder: null order must throw IllegalArgumentException")
    void placeOrderThrowsForNullOrder() {
        assertThrows(IllegalArgumentException.class,
                () -> orderService.placeOrder(null));

        // Repository must not have been called
        verifyNoInteractions(orderRepository);
    }

    @Test
    @DisplayName("placeOrder: non-positive amount must throw IllegalArgumentException")
    void placeOrderThrowsForNonPositiveAmount() {
        Order badOrder = new Order("ORD-BAD", "CUST-X", -10.0);
        assertThrows(IllegalArgumentException.class,
                () -> orderService.placeOrder(badOrder));
    }

    @Test
    @DisplayName("getOrder: blank orderId must throw IllegalArgumentException")
    void getOrderThrowsForBlankId() {
        assertThrows(IllegalArgumentException.class,
                () -> orderService.getOrder("  "));
    }
}
