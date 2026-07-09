package com.cognizant.search.service;

import com.cognizant.search.model.Product;

/**
 * LinearSearchService – searches an <em>unsorted</em> product array using
 * the linear (sequential) search algorithm.
 *
 * <h2>Algorithm</h2>
 * <p>Iterate through every element in the array from index 0 to n-1,
 * comparing the target key against each product in turn.  Return the
 * first matching product, or {@code null} if none is found.
 *
 * <h2>Time Complexity</h2>
 * <table border="1" cellpadding="4">
 *   <tr><th>Case</th><th>Complexity</th><th>Scenario</th></tr>
 *   <tr><td>Best</td><td>O(1)</td><td>Target is the first element</td></tr>
 *   <tr><td>Average</td><td>O(n)</td><td>Target is in the middle on average</td></tr>
 *   <tr><td>Worst</td><td>O(n)</td><td>Target is last or not present</td></tr>
 * </table>
 *
 * <h2>Space Complexity</h2>
 * <p>O(1) – no auxiliary data structures are created.
 */
public class LinearSearchService {

    private final Product[] products;

    /**
     * Creates a service backed by the given (unsorted) product array.
     *
     * @param products the catalogue; must not be null
     */
    public LinearSearchService(Product[] products) {
        if (products == null) {
            throw new IllegalArgumentException("Product array must not be null.");
        }
        this.products = products;
    }

    // -----------------------------------------------------------------------
    // Search by productId
    // -----------------------------------------------------------------------

    /**
     * Finds a product by its unique {@code productId}.
     *
     * @param targetId the id to search for
     * @return the matching {@link Product}, or {@code null} if not found
     */
    public Product searchById(int targetId) {
        System.out.printf("[LinearSearch] Searching for productId=%d in %d products...%n",
                targetId, products.length);

        for (int i = 0; i < products.length; i++) {
            if (products[i].getProductId() == targetId) {
                System.out.printf("[LinearSearch] Found at index %d after %d comparison(s).%n",
                        i, i + 1);
                return products[i];
            }
        }

        System.out.printf("[LinearSearch] Not found after %d comparison(s).%n", products.length);
        return null;
    }

    // -----------------------------------------------------------------------
    // Search by productName (case-insensitive)
    // -----------------------------------------------------------------------

    /**
     * Finds a product by its name (case-insensitive, exact match).
     *
     * @param targetName the name to search for
     * @return the matching {@link Product}, or {@code null} if not found
     */
    public Product searchByName(String targetName) {
        if (targetName == null || targetName.isBlank()) return null;

        System.out.printf("[LinearSearch] Searching for productName='%s' in %d products...%n",
                targetName, products.length);

        for (int i = 0; i < products.length; i++) {
            if (products[i].getProductName().equalsIgnoreCase(targetName.trim())) {
                System.out.printf("[LinearSearch] Found at index %d after %d comparison(s).%n",
                        i, i + 1);
                return products[i];
            }
        }

        System.out.printf("[LinearSearch] Not found after %d comparison(s).%n", products.length);
        return null;
    }
}
