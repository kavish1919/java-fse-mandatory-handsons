package com.cognizant.shop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ShoppingCart – domain class used as the Subject Under Test for Exercise 4.
 *
 * <p>Supports adding/removing items, computing the total, applying a percentage
 * discount, and clearing the cart. These operations produce varied,
 * meaningful outcomes that lend themselves to the AAA pattern.
 */
public class ShoppingCart {

    /** Represents a single line item in the cart. */
    public static class Item {
        private final String name;
        private final double price;
        private final int    quantity;

        public Item(String name, double price, int quantity) {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("Item name must not be blank.");
            }
            if (price < 0) {
                throw new IllegalArgumentException("Item price must be >= 0.");
            }
            if (quantity <= 0) {
                throw new IllegalArgumentException("Quantity must be positive.");
            }
            this.name     = name;
            this.price    = price;
            this.quantity = quantity;
        }

        public String getName()     { return name; }
        public double getPrice()    { return price; }
        public int    getQuantity() { return quantity; }

        /** Subtotal for this line item: price × quantity. */
        public double getSubtotal() { return price * quantity; }

        @Override
        public String toString() {
            return String.format("Item{%s x%d @ %.2f}", name, quantity, price);
        }
    }

    // -----------------------------------------------------------------------

    private final List<Item> items = new ArrayList<>();

    /**
     * Adds an item to the cart.
     *
     * @param item non-null item to add
     * @throws IllegalArgumentException if item is null
     */
    public void addItem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot add a null item to the cart.");
        }
        items.add(item);
    }

    /**
     * Removes the first item whose name matches (case-insensitive).
     *
     * @param name item name to remove
     * @return {@code true} if an item was removed, {@code false} otherwise
     */
    public boolean removeItem(String name) {
        return items.removeIf(i -> i.getName().equalsIgnoreCase(name));
    }

    /**
     * Returns the total price of all items in the cart.
     *
     * @return sum of all item subtotals; 0.0 if cart is empty
     */
    public double getTotal() {
        return items.stream().mapToDouble(Item::getSubtotal).sum();
    }

    /**
     * Calculates the total after applying a percentage discount.
     *
     * @param discountPercent discount as a percentage, e.g. 10.0 = 10%
     * @return discounted total
     * @throws IllegalArgumentException if discountPercent is not in [0, 100]
     */
    public double getTotalAfterDiscount(double discountPercent) {
        if (discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException(
                    "Discount must be in [0, 100]. Got: " + discountPercent);
        }
        return getTotal() * (1 - discountPercent / 100.0);
    }

    /**
     * Removes all items from the cart.
     */
    public void clear() {
        items.clear();
    }

    /**
     * Returns the number of distinct line items in the cart.
     */
    public int getItemCount() {
        return items.size();
    }

    /**
     * Returns {@code true} if the cart contains no items.
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }

    /**
     * Returns an unmodifiable view of the cart's items.
     */
    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }
}
