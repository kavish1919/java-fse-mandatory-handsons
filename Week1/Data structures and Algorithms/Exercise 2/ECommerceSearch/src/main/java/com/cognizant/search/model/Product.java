package com.cognizant.search.model;

import java.util.Objects;

/**
 * Product – Domain model representing a single e-commerce catalogue item.
 *
 * <p>Attributes:
 * <ul>
 *   <li>{@code productId}   – unique numeric identifier (used as binary-search key)</li>
 *   <li>{@code productName} – human-readable product title (used as linear-search key)</li>
 *   <li>{@code category}    – top-level catalogue category</li>
 * </ul>
 *
 * <p>Implements {@link Comparable} on {@code productId} so that arrays of
 * {@code Product} can be sorted (prerequisite for binary search).
 */
public class Product implements Comparable<Product> {

    private final int    productId;
    private final String productName;
    private final String category;

    /**
     * Constructs a fully initialised Product.
     *
     * @param productId   unique product identifier (positive integer)
     * @param productName human-readable product name (non-null, non-empty)
     * @param category    catalogue category (non-null, non-empty)
     * @throws IllegalArgumentException if productId ≤ 0 or any string is blank
     */
    public Product(int productId, String productName, String category) {
        if (productId <= 0) {
            throw new IllegalArgumentException("productId must be a positive integer.");
        }
        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException("productName must not be null or blank.");
        }
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("category must not be null or blank.");
        }
        this.productId   = productId;
        this.productName = productName.trim();
        this.category    = category.trim();
    }

    // -----------------------------------------------------------------------
    // Accessors
    // -----------------------------------------------------------------------

    /** @return the unique product identifier */
    public int getProductId() { return productId; }

    /** @return the product name */
    public String getProductName() { return productName; }

    /** @return the catalogue category */
    public String getCategory() { return category; }

    // -----------------------------------------------------------------------
    // Comparable / equals / hashCode / toString
    // -----------------------------------------------------------------------

    /**
     * Natural ordering by {@code productId} – required for Arrays.sort()
     * before binary search.
     */
    @Override
    public int compareTo(Product other) {
        return Integer.compare(this.productId, other.productId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Product other)) return false;
        return productId == other.productId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }

    @Override
    public String toString() {
        return String.format("Product{id=%d, name='%s', category='%s'}",
                productId, productName, category);
    }
}
