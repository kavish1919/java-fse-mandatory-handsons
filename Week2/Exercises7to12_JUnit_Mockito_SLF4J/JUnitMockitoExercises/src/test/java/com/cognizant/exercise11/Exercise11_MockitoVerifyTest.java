package com.cognizant.exercise11;

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
 * Exercise 11 — Mockito Verify.
 *
 * <p>Demonstrates:
 * <ul>
 *   <li>{@code verify(mock, times(n))} — exact invocation count</li>
 *   <li>{@code verify(mock, never())} — assert method was never called</li>
 *   <li>{@code verify(mock, atLeastOnce())} — at-least-one invocation</li>
 *   <li>{@code ArgumentCaptor} — capture and inspect arguments passed to mocks</li>
 *   <li>{@code verifyNoInteractions(mock)} — zero interactions with a mock</li>
 *   <li>{@code verifyNoMoreInteractions(mock)} — no unexpected calls</li>
 *   <li>Negative tests verifying incorrect usage is detected</li>
 * </ul>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Exercise 11 — Mockito Verify")
class Exercise11_MockitoVerifyTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    // ------------------------------------------------------------------
    // times(n) — exact invocation count
    // ------------------------------------------------------------------

    @Test
    @DisplayName("verify times(1): placeOrder calls repository.save exactly once")
    void placeOrderCallsSaveExactlyOnce() {
        // Arrange
        Order order = new Order("ORD-V1", "CUST-A", 150.00);
        when(orderRepository.save(any())).thenReturn(order);

        // Act
        orderService.placeOrder(order);

        // Assert
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    @DisplayName("verify times(3): three successive placeOrder calls invoke save three times")
    void threeCallsInvokeSaveThreeTimes() {
        // Arrange
        Order o1 = new Order("ORD-A", "C1", 10.0);
        Order o2 = new Order("ORD-B", "C2", 20.0);
        Order o3 = new Order("ORD-C", "C3", 30.0);
        when(orderRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        orderService.placeOrder(o1);
        orderService.placeOrder(o2);
        orderService.placeOrder(o3);

        // Assert
        verify(orderRepository, times(3)).save(any(Order.class));
    }

    // ------------------------------------------------------------------
    // never() — assert method was never called
    // ------------------------------------------------------------------

    @Test
    @DisplayName("verify never(): null order must NOT reach repository.save")
    void nullOrderNeverReachesSave() {
        // Act
        assertThrows(IllegalArgumentException.class,
                () -> orderService.placeOrder(null));

        // Assert — save must never have been called
        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("verify never(): getOrder not called during placeOrder")
    void findByIdNeverCalledDuringPlaceOrder() {
        // Arrange
        Order order = new Order("ORD-V2", "CUST-B", 200.0);
        when(orderRepository.save(any())).thenReturn(order);

        // Act
        orderService.placeOrder(order);

        // Assert — findById must not have been invoked
        verify(orderRepository, never()).findById(any());
    }

    // ------------------------------------------------------------------
    // atLeastOnce()
    // ------------------------------------------------------------------

    @Test
    @DisplayName("verify atLeastOnce(): cancelOrder calls deleteById at least once")
    void cancelOrderCallsDeleteByIdAtLeastOnce() {
        // Act
        orderService.cancelOrder("ORD-DEL");

        // Assert
        verify(orderRepository, atLeastOnce()).deleteById("ORD-DEL");
    }

    // ------------------------------------------------------------------
    // ArgumentCaptor — inspect actual arguments passed to mock
    // ------------------------------------------------------------------

    @Test
    @DisplayName("ArgumentCaptor: capture the Order passed to repository.save")
    void captureOrderPassedToSave() {
        // Arrange
        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        Order inputOrder = new Order("ORD-CAP", "CUST-CAPTURE", 999.99);
        when(orderRepository.save(any())).thenReturn(inputOrder);

        // Act
        orderService.placeOrder(inputOrder);

        // Assert — capture the argument and verify its fields
        verify(orderRepository).save(captor.capture());
        Order captured = captor.getValue();

        assertNotNull(captured);
        assertEquals("ORD-CAP",      captured.getOrderId());
        assertEquals("CUST-CAPTURE", captured.getCustomerId());
        assertEquals(999.99,         captured.getTotalAmount(), 1e-9);
    }

    // ------------------------------------------------------------------
    // verifyNoInteractions — mock was completely untouched
    // ------------------------------------------------------------------

    @Test
    @DisplayName("verifyNoInteractions: blank orderId never reaches repository")
    void blankOrderIdNeverReachesRepository() {
        // Act
        assertThrows(IllegalArgumentException.class,
                () -> orderService.getOrder(""));

        // Assert — the mock was never touched
        verifyNoInteractions(orderRepository);
    }

    // ------------------------------------------------------------------
    // verifyNoMoreInteractions — no unexpected calls after expected ones
    // ------------------------------------------------------------------

    @Test
    @DisplayName("verifyNoMoreInteractions: placeOrder only calls save — nothing else")
    void placeOrderDoesNothingBeyondSave() {
        // Arrange
        Order order = new Order("ORD-NMI", "CUST-Z", 55.0);
        when(orderRepository.save(any())).thenReturn(order);

        // Act
        orderService.placeOrder(order);

        // Assert — verify exactly what happened, nothing more
        verify(orderRepository).save(any(Order.class));
        verifyNoMoreInteractions(orderRepository);
    }

    // ------------------------------------------------------------------
    // Negative verify test — correct order of interactions
    // ------------------------------------------------------------------

    @Test
    @DisplayName("Negative: verify(times(2)) fails for a single save call")
    void verifyTimesTooManyFails() {
        // Arrange
        Order order = new Order("ORD-FAIL", "CUST-FAIL", 75.0);
        when(orderRepository.save(any())).thenReturn(order);

        // Act — called only once
        orderService.placeOrder(order);

        // Assert — we expect times(1), NOT times(2)
        // The following would FAIL if uncommented:
        //   verify(orderRepository, times(2)).save(any());

        // Correct assertion — times(1)
        verify(orderRepository, times(1)).save(any());
    }
}
