package com.cognizant.order.repository;

import com.cognizant.order.model.Order;

import java.util.Optional;

/**
 * Repository contract for persistence operations on {@link Order} objects.
 *
 * <p>The interface boundary is what Mockito stubs in Exercises 10 and 11 —
 * no database is needed to test the service layer.
 */
public interface OrderRepository {

    /**
     * Persists a new order and returns the saved instance.
     *
     * @param order the order to save
     * @return the saved {@link Order} (may carry a generated ID)
     */
    Order save(Order order);

    /**
     * Retrieves an order by its ID.
     *
     * @param orderId the identifier to look up
     * @return {@link Optional} containing the order if found, or empty
     */
    Optional<Order> findById(String orderId);

    /**
     * Deletes an order by its ID.
     *
     * @param orderId the identifier of the order to delete
     */
    void deleteById(String orderId);
}
