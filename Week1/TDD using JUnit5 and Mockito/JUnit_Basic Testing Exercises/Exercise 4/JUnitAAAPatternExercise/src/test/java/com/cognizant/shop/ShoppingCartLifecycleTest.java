package com.cognizant.shop;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * ShoppingCartLifecycleTest – demonstrates the FULL JUnit 4 fixture lifecycle:
 *
 * <pre>
 *  @BeforeClass  setUpClass()   ← once before ALL tests in this class
 *
 *      @Before   setUp()        ← before each @Test
 *          @Test testMethod()
 *      @After    tearDown()     ← after  each @Test
 *
 *      @Before   setUp()
 *          @Test testMethod2()
 *      @After    tearDown()
 *
 *  @AfterClass   tearDownClass() ← once after ALL tests in this class
 * </pre>
 *
 * <h2>When to use each annotation</h2>
 * <table border="1" cellpadding="4">
 *   <tr><th>Annotation</th>   <th>Runs</th>            <th>Typical use</th></tr>
 *   <tr><td>@BeforeClass</td> <td>Once (class start)</td> <td>Start DB connection, load config</td></tr>
 *   <tr><td>@Before</td>      <td>Before each test</td>   <td>Create fresh object under test</td></tr>
 *   <tr><td>@After</td>       <td>After each test</td>    <td>Release per-test resources</td></tr>
 *   <tr><td>@AfterClass</td>  <td>Once (class end)</td>   <td>Close DB connection, cleanup</td></tr>
 * </table>
 *
 * <p><b>Rule:</b> {@code @BeforeClass} / {@code @AfterClass} methods
 * <em>must</em> be {@code public static}.
 */
public class ShoppingCartLifecycleTest {

    // =========================================================================
    // Class-level shared state (simulates an expensive one-time resource,
    // e.g., a database connection or a product catalogue loaded from disk)
    // =========================================================================
    private static String sharedCatalogueId;   // set once in @BeforeClass

    // Instance-level state — fresh per test
    private ShoppingCart cart;

    // =========================================================================
    // @BeforeClass — executes ONCE before the first test method in this class
    // =========================================================================

    /**
     * Simulates loading a shared, expensive resource once for the entire
     * test class (e.g., reading a product catalogue from disk).
     * Must be {@code public static}.
     */
    @BeforeClass
    public static void setUpClass() {
        sharedCatalogueId = "CATALOGUE-2024-Q1";
        System.out.println("\n[BeforeClass] Shared catalogue loaded: " + sharedCatalogueId);
        System.out.println("[BeforeClass] (Runs ONCE before all tests in this class)\n");
    }

    // =========================================================================
    // @AfterClass — executes ONCE after the last test method in this class
    // =========================================================================

    /**
     * Simulates releasing the shared resource after all tests have run.
     * Must be {@code public static}.
     */
    @AfterClass
    public static void tearDownClass() {
        System.out.println("\n[AfterClass] Releasing shared catalogue: " + sharedCatalogueId);
        sharedCatalogueId = null;
        System.out.println("[AfterClass] (Runs ONCE after all tests in this class)");
    }

    // =========================================================================
    // @Before — executes before EVERY @Test method
    // =========================================================================

    /**
     * Creates a fresh {@link ShoppingCart} pre-loaded with two items,
     * representing an "in-progress" cart state.
     */
    @Before
    public void setUp() {
        cart = new ShoppingCart();
        cart.addItem(new ShoppingCart.Item("Notebook",   200.00, 3));   // subtotal  600
        cart.addItem(new ShoppingCart.Item("Pen Set",     50.00, 5));   // subtotal  250
        // gross total = 850
        System.out.println("[Before] Fresh ShoppingCart created with 2 items. Total = 850");
    }

    // =========================================================================
    // @After — executes after EVERY @Test method
    // =========================================================================

    /**
     * Clears cart state after each test to prevent cross-test contamination.
     */
    @After
    public void tearDown() {
        int countBefore = cart.getItemCount();
        cart.clear();
        System.out.println("[After]  Cart cleared (" + countBefore + " item(s) removed).");
        cart = null;
    }

    // =========================================================================
    // Test Methods — all structured with AAA
    // =========================================================================

    /**
     * TC-01 · Verify the @Before fixture loaded items correctly.
     */
    @Test
    public void testFixture_CartPreloadedBySetUp() {
        // ARRANGE – handled entirely by @Before

        // ACT
        int count = cart.getItemCount();

        // ASSERT
        assertEquals("@Before should have added exactly 2 items to the cart",
                2, count);
    }

    /**
     * TC-02 · Pre-loaded total should be 850 before any test mutation.
     */
    @Test
    public void testFixture_PreloadedTotalIsCorrect() {
        // ARRANGE – cart has Notebook(200×3=600) + Pen Set(50×5=250) from @Before

        // ACT
        double total = cart.getTotal();

        // ASSERT
        assertEquals("Pre-loaded total should be 850.00", 850.00, total, 0.001);
    }

    /**
     * TC-03 · Adding a new item on top of the fixture does not affect other tests
     *          because @Before and @After isolate each test's state.
     */
    @Test
    public void testIsolation_AddItemDoesNotAffectOtherTests() {
        // ARRANGE
        ShoppingCart.Item stapler = new ShoppingCart.Item("Stapler", 120.00, 1);

        // ACT
        cart.addItem(stapler);

        // ASSERT
        assertEquals("Cart should have 3 items after adding stapler to fixture",
                3, cart.getItemCount());
        assertEquals("Total should be 970 after adding stapler (850 + 120)",
                970.00, cart.getTotal(), 0.001);
    }

    /**
     * TC-04 · @BeforeClass data is available to all instance test methods.
     */
    @Test
    public void testSharedResource_CatalogueIdIsAvailable() {
        // ARRANGE – sharedCatalogueId set in @BeforeClass

        // ACT
        String catalogueId = sharedCatalogueId;

        // ASSERT
        assertNotNull("Shared catalogue ID from @BeforeClass must not be null",
                catalogueId);
        assertEquals("Catalogue ID should match what was set in @BeforeClass",
                "CATALOGUE-2024-Q1", catalogueId);
    }

    /**
     * TC-05 · Removing an item from the fixture cart and verifying state.
     */
    @Test
    public void testRemoveItem_AAAPatternWithFixtureState() {
        // ARRANGE – cart has Notebook and Pen Set from @Before

        // ACT
        boolean removed = cart.removeItem("Pen Set");

        // ASSERT
        assertFalse("Cart must not be empty — Notebook remains", cart.isEmpty());
        assertEquals("Only Notebook should remain (1 item)", 1, cart.getItemCount());
        assertEquals("Total after removing Pen Set should be 600.00 (Notebook only)",
                600.00, cart.getTotal(), 0.001);
    }

    /**
     * TC-06 · Discount applied to the fixture cart.
     */
    @Test
    public void testDiscount_AAAPatternWithFixtureTotal() {
        // ARRANGE – total from @Before is 850.00; apply 20% discount

        // ACT
        double discounted = cart.getTotalAfterDiscount(20.0);

        // ASSERT
        assertEquals("20% discount on 850 should yield 680.00",
                680.00, discounted, 0.001);
    }
}
