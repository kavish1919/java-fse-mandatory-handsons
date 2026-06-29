package com.cognizant.search.service;

import com.cognizant.search.model.Product;
import org.junit.jupiter.api.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive JUnit 5 tests for {@link SearchService}.
 *
 * <p>Each test is independent; the shared dataset is rebuilt before each test
 * via {@link #setUp()}.
 */
@DisplayName("SearchService Tests")
class SearchServiceTest {

    private SearchService service;
    private Product[] catalogue;
    private Product[] sorted;

    @BeforeEach
    void setUp() {
        service = new SearchService();
        catalogue = new Product[]{
            new Product(105, "Wireless Keyboard",    "Electronics"),
            new Product( 23, "Office Chair",         "Furniture"),
            new Product(312, "Running Shoes",        "Footwear"),
            new Product( 67, "Java Book",            "Books"),
            new Product(201, "USB-C Hub",            "Electronics"),
        };
        sorted = service.sortedCopy(catalogue);
    }

    // ------------------------------------------------------------------
    // Linear Search — Positive
    // ------------------------------------------------------------------

    @Test
    @DisplayName("Linear: finds an existing product by ID")
    void linearSearchFindsExistingProduct() {
        Optional<Product> result = service.linearSearch(catalogue, 67);
        assertTrue(result.isPresent());
        assertEquals("Java Book", result.get().getProductName());
    }

    @Test
    @DisplayName("Linear: finds the first element (best case)")
    void linearSearchFindsFirstElement() {
        Optional<Product> result = service.linearSearch(catalogue, 105);
        assertTrue(result.isPresent());
    }

    @Test
    @DisplayName("Linear: finds the last element (worst case)")
    void linearSearchFindsLastElement() {
        Optional<Product> result = service.linearSearch(catalogue, 201);
        assertTrue(result.isPresent());
    }

    // ------------------------------------------------------------------
    // Linear Search — Negative / Edge
    // ------------------------------------------------------------------

    @Test
    @DisplayName("Linear: returns empty for a missing product")
    void linearSearchReturnsEmptyForMissingProduct() {
        Optional<Product> result = service.linearSearch(catalogue, 999);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Linear: returns empty for an empty array")
    void linearSearchOnEmptyArrayReturnsEmpty() {
        Optional<Product> result = service.linearSearch(new Product[]{}, 1);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Linear: throws IllegalArgumentException for null array")
    void linearSearchThrowsForNullArray() {
        assertThrows(IllegalArgumentException.class,
                () -> service.linearSearch(null, 1));
    }

    // ------------------------------------------------------------------
    // Binary Search — Positive
    // ------------------------------------------------------------------

    @Test
    @DisplayName("Binary: finds an existing product in sorted array")
    void binarySearchFindsExistingProduct() {
        Optional<Product> result = service.binarySearch(sorted, 67);
        assertTrue(result.isPresent());
        assertEquals("Java Book", result.get().getProductName());
    }

    @Test
    @DisplayName("Binary: finds the smallest ID (first in sorted array)")
    void binarySearchFindsSmallestId() {
        Optional<Product> result = service.binarySearch(sorted, 23);
        assertTrue(result.isPresent());
    }

    @Test
    @DisplayName("Binary: finds the largest ID (last in sorted array)")
    void binarySearchFindsLargestId() {
        Optional<Product> result = service.binarySearch(sorted, 312);
        assertTrue(result.isPresent());
    }

    // ------------------------------------------------------------------
    // Binary Search — Negative / Edge
    // ------------------------------------------------------------------

    @Test
    @DisplayName("Binary: returns empty for a missing product")
    void binarySearchReturnsEmptyForMissingProduct() {
        Optional<Product> result = service.binarySearch(sorted, 999);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Binary: returns empty for an empty array")
    void binarySearchOnEmptyArrayReturnsEmpty() {
        Optional<Product> result = service.binarySearch(new Product[]{}, 1);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Binary: throws IllegalArgumentException for null array")
    void binarySearchThrowsForNullArray() {
        assertThrows(IllegalArgumentException.class,
                () -> service.binarySearch(null, 1));
    }

    // ------------------------------------------------------------------
    // Sorted copy
    // ------------------------------------------------------------------

    @Test
    @DisplayName("sortedCopy() must return array ordered ascending by productId")
    void sortedCopyIsOrdered() {
        for (int i = 0; i < sorted.length - 1; i++) {
            assertTrue(sorted[i].getProductId() < sorted[i + 1].getProductId(),
                    "Element at index " + i + " must have a smaller ID than element at " + (i + 1));
        }
    }

    @Test
    @DisplayName("sortedCopy() must not mutate the original array")
    void sortedCopyDoesNotMutateOriginal() {
        int firstIdBefore = catalogue[0].getProductId();
        service.sortedCopy(catalogue);
        assertEquals(firstIdBefore, catalogue[0].getProductId());
    }

    // ------------------------------------------------------------------
    // Product model validation
    // ------------------------------------------------------------------

    @Test
    @DisplayName("Product must reject non-positive productId")
    void productRejectsNonPositiveId() {
        assertThrows(IllegalArgumentException.class,
                () -> new Product(0, "Test", "Cat"));
        assertThrows(IllegalArgumentException.class,
                () -> new Product(-1, "Test", "Cat"));
    }
}
