package com.cognizant.order.service;

import com.cognizant.order.model.Order;
import com.cognizant.order.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Business logic layer for order management.
 *
 * <p>Depends on {@link OrderRepository} via constructor injection — the
 * dependency is never instantiated here, making it trivially replaceable
 * with a Mockito mock in tests.
 *
 * <p>Uses SLF4J for all diagnostic output (Exercise 12).
 */
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;

    /** Constructor injection — preferred over field/setter injection. */
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * Places a new order.
     *
     * @param order the order to place
     * @return the persisted order
     * @throws IllegalArgumentException if the order or its amount is invalid
     */
    public Order placeOrder(Order order) {
        log.info("Placing order: {}", order);

        if (order == null) {
            log.error("Order must not be null.");
            throw new IllegalArgumentException("Order must not be null.");
        }
        if (order.getTotalAmount() <= 0) {
            log.warn("Attempted to place order with non-positive amount: {}",
                    order.getTotalAmount());
            throw new IllegalArgumentException(
                    "Order amount must be positive; got: " + order.getTotalAmount());
        }

        Order saved = orderRepository.save(order);
        log.info("Order placed successfully: {}", saved);
        return saved;
    }

    /**
     * Retrieves an order by ID, throwing if not found.
     *
     * @param orderId the identifier to look up
     * @return the found order
     * @throws IllegalArgumentException if the orderId is blank
     * @throws java.util.NoSuchElementException if the order does not exist
     */
    public Order getOrder(String orderId) {
        log.debug("Fetching order with id={}", orderId);

        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("orderId must not be blank.");
        }

        Optional<Order> found = orderRepository.findById(orderId);
        if (found.isEmpty()) {
            log.warn("Order not found for id={}", orderId);
            throw new java.util.NoSuchElementException(
                    "No order found with id: " + orderId);
        }

        log.debug("Found: {}", found.get());
        return found.get();
    }

    /**
     * Cancels an order by ID.
     *
     * @param orderId the identifier of the order to cancel
     */
    public void cancelOrder(String orderId) {
        log.info("Cancelling order: {}", orderId);

        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("orderId must not be blank.");
        }

        orderRepository.deleteById(orderId);
        log.info("Order {} cancelled successfully.", orderId);
    }
}
