package com.cognizant.search;

import com.cognizant.search.model.Product;
import com.cognizant.search.service.SearchService;

import java.util.Optional;

/**
 * Demonstrates and benchmarks linear vs binary search on a product catalogue.
 *
 * <p>Outputs the found product (or "not found"), the elapsed nanoseconds for
 * each strategy, and a Big-O complexity summary.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("=== E-Commerce Product Search — Linear vs Binary ===\n");

        // ---------------------------------------------------------------
        // Dataset
        // ---------------------------------------------------------------
        Product[] catalogue = {
            new Product(105, "Wireless Keyboard",    "Electronics"),
            new Product( 23, "Office Chair",         "Furniture"),
            new Product(312, "Running Shoes",        "Footwear"),
            new Product( 67, "Java Programming Book","Books"),
            new Product(201, "USB-C Hub",            "Electronics"),
            new Product( 45, "Desk Lamp",            "Furniture"),
            new Product(158, "Noise-Cancelling Headphones", "Electronics"),
            new Product(  9, "Yoga Mat",             "Sports"),
            new Product(270, "Coffee Maker",         "Kitchen"),
            new Product( 88, "Mechanical Pencil Set","Stationery")
        };

        SearchService service = new SearchService();
        Product[] sortedCatalogue = service.sortedCopy(catalogue);

        System.out.println("Catalogue size : " + catalogue.length + " products");
        System.out.println("Sorted by ID   : " + java.util.Arrays.toString(
                java.util.Arrays.stream(sortedCatalogue)
                        .map(p -> p.getProductId() + "").toArray()));
        System.out.println();

        // ---------------------------------------------------------------
        // Test scenarios
        // ---------------------------------------------------------------
        int[] searchIds = {
            23,    // exists — near start
            312,   // exists — at end after sort
              9,   // exists — smallest ID (best case for binary search after sort)
            999,   // does not exist
              0    // invalid (edge: productId must be positive — handled externally)
        };

        System.out.printf("%-8s  %-30s  %-15s  %-15s%n",
                "ID", "Result", "Linear (ns)", "Binary (ns)");
        System.out.println("-".repeat(72));

        for (int targetId : searchIds) {
            if (targetId <= 0) {
                System.out.printf("%-8d  %-30s%n", targetId,
                        "[Skipped — productId must be positive]");
                continue;
            }

            long t0 = System.nanoTime();
            Optional<Product> linearResult = service.linearSearch(catalogue, targetId);
            long linearNs = System.nanoTime() - t0;

            long t1 = System.nanoTime();
            Optional<Product> binaryResult = service.binarySearch(sortedCatalogue, targetId);
            long binaryNs = System.nanoTime() - t1;

            String resultDesc = linearResult
                    .map(p -> p.getProductName() + " [" + p.getCategory() + "]")
                    .orElse("NOT FOUND");

            System.out.printf("%-8d  %-30s  %-15d  %-15d%n",
                    targetId, resultDesc, linearNs, binaryNs);
        }

        System.out.println("\n--- Big-O Complexity Summary ---");
        System.out.println("Linear Search : O(n) worst/average | O(1) best | No sort needed");
        System.out.println("Binary Search : O(log n) worst/avg | O(1) best | REQUIRES sorted array");
        System.out.println("\nConclusion: For large catalogues with frequent lookups, sort once and");
        System.out.println("            use binary search. For small / unsorted / one-off lookups,");
        System.out.println("            linear search is simpler and equally effective.");
        System.out.println("\n=== Demo Complete ===");
    }
}
