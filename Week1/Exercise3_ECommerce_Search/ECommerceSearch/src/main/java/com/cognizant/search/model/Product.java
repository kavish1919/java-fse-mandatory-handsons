package com.cognizant.search.model;

/**
 * Immutable value object representing a product in the e-commerce catalogue.
 *
 * <p>Implements {@link Comparable} so arrays of products can be sorted by
 * {@code productId}, which is the key used by binary search.
 *
 * <p>Design: records would be idiomatic Java 17, but a standard class is used
 * here to keep the code accessible for learners unfamiliar with records.
 */
public final class Product implements Comparable<Product> {

    private final int    productId;
    private final String productName;
    private final String category;

    public Product(int productId, String productName, String category) {
        if (productId <= 0) {
            throw new IllegalArgumentException("productId must be positive; got: " + productId);
        }
        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException("productName must not be blank.");
        }
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("category must not be blank.");
        }
        this.productId   = productId;
        this.productName = productName;
        this.category    = category;
    }

    // -------------------------------------------------------------------------
    // Accessors
    // -------------------------------------------------------------------------

    public int    getProductId()   { return productId;   }
    public String getProductName() { return productName; }
    public String getCategory()    { return category;    }

    /**
     * Natural ordering by {@code productId} — required for binary search on sorted arrays.
     */
    @Override
    public int compareTo(Product other) {
        return Integer.compare(this.productId, other.productId);
    }

    @Override
    public String toString() {
        return String.format("Product{id=%d, name='%s', category='%s'}",
                productId, productName, category);
    }
}
