package com.cognizant.search.service;

import com.cognizant.search.model.Product;

import java.util.Arrays;
import java.util.Optional;

/**
 * Provides search operations over a product catalogue.
 *
 * <h2>Algorithm Overview</h2>
 * <table border="1">
 *   <tr><th>Algorithm</th><th>Best</th><th>Average</th><th>Worst</th><th>Sorted Required?</th></tr>
 *   <tr><td>Linear Search</td><td>O(1)</td><td>O(n)</td><td>O(n)</td><td>No</td></tr>
 *   <tr><td>Binary Search</td><td>O(1)</td><td>O(log n)</td><td>O(log n)</td><td>Yes</td></tr>
 * </table>
 *
 * <h2>Why Binary Search Requires Sorted Data</h2>
 * Binary search works by repeatedly halving the search interval. At each step it
 * discards half the remaining elements by comparing the target against the midpoint.
 * This is only valid when we can infer "all elements to the left are smaller, all to
 * the right are larger". That invariant only holds if the array is sorted.
 */
public class SearchService {

    // -------------------------------------------------------------------------
    // Linear Search  —  O(n) time, O(1) space
    // -------------------------------------------------------------------------

    /**
     * Searches the unsorted (or sorted) {@code products} array for the first
     * product with the given {@code targetId}.
     *
     * <p>Examines every element sequentially until a match is found or the
     * array is exhausted. Works on any ordering of elements.
     *
     * @param products array to search (must not be {@code null})
     * @param targetId the product ID to locate
     * @return an {@link Optional} containing the matching product, or empty if absent
     */
    public Optional<Product> linearSearch(Product[] products, int targetId) {
        validateInput(products);

        for (Product product : products) {
            if (product.getProductId() == targetId) {
                return Optional.of(product);
            }
        }
        return Optional.empty();
    }

    // -------------------------------------------------------------------------
    // Binary Search  —  O(log n) time, O(1) space (iterative, no stack overhead)
    // -------------------------------------------------------------------------

    /**
     * Searches a <strong>sorted</strong> {@code products} array for a product
     * with the given {@code targetId} using the divide-and-conquer strategy.
     *
     * <p>Pre-condition: the array must be sorted ascending by {@code productId}.
     * Violation of this contract will produce incorrect results without error.
     *
     * @param sortedProducts array sorted ascending by product ID
     * @param targetId       the product ID to locate
     * @return an {@link Optional} containing the matching product, or empty if absent
     */
    public Optional<Product> binarySearch(Product[] sortedProducts, int targetId) {
        validateInput(sortedProducts);

        int low  = 0;
        int high = sortedProducts.length - 1;

        while (low <= high) {
            // Use unsigned right-shift to prevent integer overflow for very large indices.
            int mid = (low + high) >>> 1;
            int midId = sortedProducts[mid].getProductId();

            if (midId == targetId) {
                return Optional.of(sortedProducts[mid]);     // found
            } else if (midId < targetId) {
                low = mid + 1;   // target is in the right half
            } else {
                high = mid - 1;  // target is in the left half
            }
        }
        return Optional.empty();
    }

    // -------------------------------------------------------------------------
    // Utility
    // -------------------------------------------------------------------------

    /**
     * Sorts a copy of the given array by product ID and returns it.
     * The original array is not mutated.
     *
     * @param products the unsorted source array
     * @return a new, sorted copy
     */
    public Product[] sortedCopy(Product[] products) {
        validateInput(products);
        Product[] copy = Arrays.copyOf(products, products.length);
        Arrays.sort(copy);     // uses Product.compareTo → sorts by productId
        return copy;
    }

    /**
     * Guards against null or empty arrays being passed to search methods.
     */
    private void validateInput(Product[] products) {
        if (products == null) {
            throw new IllegalArgumentException("Product array must not be null.");
        }
    }
}
