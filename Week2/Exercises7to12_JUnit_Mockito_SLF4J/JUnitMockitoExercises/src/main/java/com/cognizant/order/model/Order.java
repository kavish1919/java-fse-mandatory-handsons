package com.cognizant.order.model;

/**
 * Immutable order model object.
 */
public final class Order {

    private final String orderId;
    private final String customerId;
    private final double totalAmount;

    public Order(String orderId, String customerId, double totalAmount) {
        this.orderId    = orderId;
        this.customerId = customerId;
        this.totalAmount = totalAmount;
    }

    public String getOrderId()      { return orderId;      }
    public String getCustomerId()   { return customerId;   }
    public double getTotalAmount()  { return totalAmount;  }

    @Override
    public String toString() {
        return String.format("Order{id='%s', customer='%s', amount=%.2f}",
                orderId, customerId, totalAmount);
    }
}
