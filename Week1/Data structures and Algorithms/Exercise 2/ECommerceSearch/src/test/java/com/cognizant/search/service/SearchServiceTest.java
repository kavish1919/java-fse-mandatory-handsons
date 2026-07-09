package com.cognizant.search.service;

import com.cognizant.search.model.Product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SearchServiceTest – JUnit 5 test suite for both search algorithms.
 *
 * <p>Test cases:
 *
 * <b>LinearSearchService (by ID)</b>
 * <ol>
 *   <li>Found at first position (best case)</li>
 *   <li>Found at last position (worst case)</li>
 *   <li>Found in the middle</li>
 *   <li>Not found → returns null</li>
 * </ol>
 *
 * <b>LinearSearchService (by Name)</b>
 * <ol start="5">
 *   <li>Case-insensitive name match</li>
 *   <li>Name not found → returns null</li>
 *   <li>Null name → returns null (no exception)</li>
 * </ol>
 *
 * <b>BinarySearchService (by ID)</b>
 * <ol start="8">
 *   <li>Found at mid (best case)</li>
 *   <li>Found in left half</li>
 *   <li>Found in right half</li>
 *   <li>Not found → returns null</li>
 *   <li>Single-element array — hit</li>
 *   <li>Single-element array — miss</li>
 * </ol>
 *
 * <b>BinarySearchService — sort guarantee</b>
 * <ol start="14">
 *   <li>Constructor sorts array by productId in ascending order</li>
 * </ol>
 *
 * <b>Product model</b>
 * <ol start="15">
 *   <li>Invalid productId throws IllegalArgumentException</li>
 *   <li>Blank productName throws IllegalArgumentException</li>
 * </ol>
 */
class SearchServiceTest {

    // Shared test catalogue (intentionally unsorted)
    private Product[] catalogue;

    @BeforeEach
    void setUp() {
        catalogue = new Product[]{
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
    }

    // =========================================================================
    // LinearSearchService — by ID
    // =========================================================================

    @Test
    @DisplayName("TC-01 [Linear/ID] Found at first index — best case O(1)")
    void linearById_foundAtFirst() {
        LinearSearchService svc = new LinearSearchService(catalogue);
        Product result = svc.searchById(105);   // first element
        assertNotNull(result);
        assertEquals(105, result.getProductId());
        assertEquals("Wireless Mouse", result.getProductName());
    }

    @Test
    @DisplayName("TC-02 [Linear/ID] Found at last index — worst case O(n)")
    void linearById_foundAtLast() {
        LinearSearchService svc = new LinearSearchService(catalogue);
        Product result = svc.searchById(44);    // last element
        assertNotNull(result);
        assertEquals(44, result.getProductId());
    }

    @Test
    @DisplayName("TC-03 [Linear/ID] Found in the middle")
    void linearById_foundInMiddle() {
        LinearSearchService svc = new LinearSearchService(catalogue);
        Product result = svc.searchById(200);
        assertNotNull(result);
        assertEquals("Bluetooth Headphones", result.getProductName());
    }

    @Test
    @DisplayName("TC-04 [Linear/ID] Not found — returns null")
    void linearById_notFound() {
        LinearSearchService svc = new LinearSearchService(catalogue);
        assertNull(svc.searchById(9999));
    }

    // =========================================================================
    // LinearSearchService — by Name
    // =========================================================================

    @Test
    @DisplayName("TC-05 [Linear/Name] Case-insensitive match")
    void linearByName_caseInsensitiveMatch() {
        LinearSearchService svc = new LinearSearchService(catalogue);
        Product result = svc.searchByName("LAPTOP STAND");
        assertNotNull(result);
        assertEquals(375, result.getProductId());
    }

    @Test
    @DisplayName("TC-06 [Linear/Name] Not found — returns null")
    void linearByName_notFound() {
        LinearSearchService svc = new LinearSearchService(catalogue);
        assertNull(svc.searchByName("Invisible Gadget"));
    }

    @Test
    @DisplayName("TC-07 [Linear/Name] Null input — returns null without exception")
    void linearByName_nullInput() {
        LinearSearchService svc = new LinearSearchService(catalogue);
        assertDoesNotThrow(() -> assertNull(svc.searchByName(null)));
    }

    // =========================================================================
    // BinarySearchService — by ID
    // =========================================================================

    @Test
    @DisplayName("TC-08 [Binary/ID] Found at mid on first probe — best case O(1)")
    void binaryById_foundAtMid() {
        // Sorted order: 11,23,44,67,88,105,200,312,375,450
        // mid index = 4 → productId = 88
        BinarySearchService svc = new BinarySearchService(catalogue);
        Product result = svc.searchById(88);
        assertNotNull(result);
        assertEquals("Stainless Steel Bottle", result.getProductName());
    }

    @Test
    @DisplayName("TC-09 [Binary/ID] Found in the left half")
    void binaryById_foundLeftHalf() {
        BinarySearchService svc = new BinarySearchService(catalogue);
        Product result = svc.searchById(23);
        assertNotNull(result);
        assertEquals("Running Shoes", result.getProductName());
    }

    @Test
    @DisplayName("TC-10 [Binary/ID] Found in the right half")
    void binaryById_foundRightHalf() {
        BinarySearchService svc = new BinarySearchService(catalogue);
        Product result = svc.searchById(375);
        assertNotNull(result);
        assertEquals("Laptop Stand", result.getProductName());
    }

    @Test
    @DisplayName("TC-11 [Binary/ID] Not found — returns null")
    void binaryById_notFound() {
        BinarySearchService svc = new BinarySearchService(catalogue);
        assertNull(svc.searchById(9999));
    }

    @Test
    @DisplayName("TC-12 [Binary/ID] Single-element array — hit")
    void binaryById_singleElementHit() {
        Product[] single = {new Product(1, "Solo Product", "Test")};
        BinarySearchService svc = new BinarySearchService(single);
        assertNotNull(svc.searchById(1));
    }

    @Test
    @DisplayName("TC-13 [Binary/ID] Single-element array — miss")
    void binaryById_singleElementMiss() {
        Product[] single = {new Product(1, "Solo Product", "Test")};
        BinarySearchService svc = new BinarySearchService(single);
        assertNull(svc.searchById(99));
    }

    // =========================================================================
    // BinarySearchService — sort guarantee
    // =========================================================================

    @Test
    @DisplayName("TC-14 [Binary] Constructor sorts catalogue by productId ascending")
    void binarySearch_arraySortedAscending() {
        BinarySearchService svc = new BinarySearchService(catalogue);
        Product[] sorted = svc.getSortedProducts();
        for (int i = 0; i < sorted.length - 1; i++) {
            assertTrue(sorted[i].getProductId() < sorted[i + 1].getProductId(),
                    "Array not sorted at index " + i + ": "
                    + sorted[i].getProductId() + " >= " + sorted[i + 1].getProductId());
        }
    }

    // =========================================================================
    // Product model validation
    // =========================================================================

    @Test
    @DisplayName("TC-15 [Product] Non-positive productId throws IllegalArgumentException")
    void product_invalidId() {
        assertThrows(IllegalArgumentException.class,
                () -> new Product(0, "Bad Product", "Test"));
        assertThrows(IllegalArgumentException.class,
                () -> new Product(-5, "Bad Product", "Test"));
    }

    @Test
    @DisplayName("TC-16 [Product] Blank productName throws IllegalArgumentException")
    void product_blankName() {
        assertThrows(IllegalArgumentException.class,
                () -> new Product(1, "   ", "Test"));
        assertThrows(IllegalArgumentException.class,
                () -> new Product(1, null, "Test"));
    }
}
