package com.cognizant.search;

import com.cognizant.search.model.Product;
import com.cognizant.search.service.BinarySearchService;
import com.cognizant.search.service.LinearSearchService;

/**
 * SearchDemo – entry-point demonstrating both search algorithms on a
 * 10-product e-commerce catalogue.
 */
public class SearchDemo {

    public static void main(String[] args) {

        System.out.println("========================================================");
        System.out.println("  E-Commerce Platform Search Function Demo");
        System.out.println("========================================================\n");

        // -----------------------------------------------------------------------
        // Build an UNSORTED catalogue (simulating real-world insertion order)
        // -----------------------------------------------------------------------
        Product[] catalogue = {
            new Product(105, "Wireless Mouse",        "Electronics"),
            new Product( 23, "Running Shoes",         "Footwear"),
            new Product(312, "Java Programming Book", "Books"),
            new Product( 67, "Coffee Maker",          "Kitchen"),
            new Product(200, "Bluetooth Headphones",  "Electronics"),
            new Product( 11, "Yoga Mat",              "Sports"),
            new Product(450, "Office Chair",          "Furniture"),
            new Product( 88, "Stainless Steel Bottle","Kitchen"),
            new Product(375, "Laptop Stand",          "Electronics"),
            new Product( 44, "Sunscreen SPF 50",      "Beauty"),
        };

        // -----------------------------------------------------------------------
        // LINEAR SEARCH — works on unsorted array
        // -----------------------------------------------------------------------
        System.out.println("------ LINEAR SEARCH (unsorted array) ------\n");
        LinearSearchService linear = new LinearSearchService(catalogue);

        // Search by ID — hit (middle of array)
        Product result = linear.searchById(67);
        System.out.println("Result: " + result + "\n");

        // Search by ID — miss
        result = linear.searchById(999);
        System.out.println("Result: " + result + "\n");

        // Search by name
        result = linear.searchByName("Laptop Stand");
        System.out.println("Result: " + result + "\n");

        // Search by name — miss
        result = linear.searchByName("Unknown Item");
        System.out.println("Result: " + result + "\n");

        // -----------------------------------------------------------------------
        // BINARY SEARCH — constructor sorts the array by productId
        // -----------------------------------------------------------------------
        System.out.println("------ BINARY SEARCH (sorted by productId) ------\n");
        BinarySearchService binary = new BinarySearchService(catalogue);

        // Show the sorted order
        System.out.println("[BinarySearch] Sorted catalogue:");
        for (Product p : binary.getSortedProducts()) {
            System.out.println("  " + p);
        }

        // Search — hit (left half)
        result = binary.searchById(44);
        System.out.println("Result: " + result);

        // Search — hit (right half)
        result = binary.searchById(375);
        System.out.println("Result: " + result);

        // Search — miss
        result = binary.searchById(999);
        System.out.println("Result: " + result);

        System.out.println("\n========================================================");
        System.out.println("  Demo complete — see analysis artifact for Big O notes");
        System.out.println("========================================================");
    }
}
