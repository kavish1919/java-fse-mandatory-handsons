package com.cognizant.search.service;

import com.cognizant.search.model.Product;

import java.util.Arrays;

/**
 * BinarySearchService – searches a <em>sorted</em> product array using
 * the binary search algorithm.
 *
 * <h2>Prerequisite</h2>
 * <p>The array <strong>must be sorted by {@code productId} in ascending order</strong>
 * before any search call.  The constructor sorts the array automatically via
 * {@link Arrays#sort(Object[])} (which uses TimSort, O(n log n)).
 *
 * <h2>Algorithm</h2>
 * <p>At each step, compare the target against the middle element of the
 * current search interval:
 * <ul>
 *   <li>If equal → return the product.</li>
 *   <li>If target &lt; mid → narrow the interval to the left half.</li>
 *   <li>If target &gt; mid → narrow the interval to the right half.</li>
 * </ul>
 * Repeat until the interval is empty (not found) or a match is located.
 *
 * <h2>Time Complexity</h2>
 * <table border="1" cellpadding="4">
 *   <tr><th>Case</th><th>Complexity</th><th>Scenario</th></tr>
 *   <tr><td>Best</td><td>O(1)</td><td>Target is the middle element on the first probe</td></tr>
 *   <tr><td>Average</td><td>O(log n)</td><td>Target requires ~log₂n probes on average</td></tr>
 *   <tr><td>Worst</td><td>O(log n)</td><td>Target not present or at a leaf of the search tree</td></tr>
 * </table>
 *
 * <h2>Space Complexity</h2>
 * <p>O(1) – iterative implementation; no recursion stack overhead.
 */
public class BinarySearchService {

    private final Product[] sortedProducts;

    /**
     * Creates a service, sorting the given array by {@code productId} in-place.
     *
     * @param products the catalogue; must not be null
     */
    public BinarySearchService(Product[] products) {
        if (products == null) {
            throw new IllegalArgumentException("Product array must not be null.");
        }
        // Sort a defensive copy so the caller's original array is unchanged
        this.sortedProducts = products.clone();
        Arrays.sort(this.sortedProducts);   // uses Product.compareTo(productId)
        System.out.println("[BinarySearch] Array sorted by productId (prerequisite satisfied).");
    }

    // -----------------------------------------------------------------------
    // Search by productId (iterative binary search)
    // -----------------------------------------------------------------------

    /**
     * Finds a product by its unique {@code productId} using binary search.
     *
     * @param targetId the id to search for
     * @return the matching {@link Product}, or {@code null} if not found
     */
    public Product searchById(int targetId) {
        System.out.printf("%n[BinarySearch] Searching for productId=%d in %d products...%n",
                targetId, sortedProducts.length);

        int low  = 0;
        int high = sortedProducts.length - 1;
        int comparisons = 0;

        while (low <= high) {
            int mid = low + (high - low) / 2;   // avoids integer overflow
            comparisons++;

            int cmp = Integer.compare(sortedProducts[mid].getProductId(), targetId);
            System.out.printf("[BinarySearch] Probe #%d → index=%d, id=%d%n",
                    comparisons, mid, sortedProducts[mid].getProductId());

            if (cmp == 0) {
                System.out.printf("[BinarySearch] Found at index %d after %d probe(s).%n",
                        mid, comparisons);
                return sortedProducts[mid];
            } else if (cmp < 0) {
                low = mid + 1;   // target is in the right half
            } else {
                high = mid - 1;  // target is in the left half
            }
        }

        System.out.printf("[BinarySearch] Not found after %d probe(s).%n", comparisons);
        return null;
    }

    /**
     * Returns the internal sorted array (for inspection / testing).
     *
     * @return a copy of the sorted product array
     */
    public Product[] getSortedProducts() {
        return sortedProducts.clone();
    }
}
