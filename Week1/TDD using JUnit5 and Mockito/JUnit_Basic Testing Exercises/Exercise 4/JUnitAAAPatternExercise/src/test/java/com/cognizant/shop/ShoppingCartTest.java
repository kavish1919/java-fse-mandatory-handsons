package com.cognizant.shop;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * ShoppingCartTest – Exercise 4: AAA Pattern with @Before and @After.
 *
 * <hr>
 * <h2>The Arrange-Act-Assert (AAA) Pattern</h2>
 * <p>Every test method in this class is structured in exactly three phases:
 *
 * <pre>
 *   // ARRANGE – set up all preconditions and inputs
 *   // ACT     – invoke the method under test (exactly one action)
 *   // ASSERT  – verify the expected outcome
 * </pre>
 *
 * <p>This structure makes each test self-documenting:
 * <ul>
 *   <li><b>Arrange</b> – "given these preconditions…"</li>
 *   <li><b>Act</b>     – "when I do this…"</li>
 *   <li><b>Assert</b>  – "then I expect…"</li>
 * </ul>
 *
 * <hr>
 * <h2>Test Fixture Lifecycle (@Before / @After)</h2>
 *
 * <pre>
 *   @Before setUp()    ← runs before EVERY @Test method
 *       @Test method
 *   @After  tearDown() ← runs after  EVERY @Test method
 * </pre>
 *
 * <p>Using {@code @Before} guarantees test isolation: each test starts
 * with a fresh, predictable {@link ShoppingCart} regardless of the order
 * in which JUnit executes tests.
 */
public class ShoppingCartTest {

    // =========================================================================
    // Test Fixture — shared objects declared as fields
    // =========================================================================

    /** The cart under test — recreated before every test method. */
    private ShoppingCart cart;

    /** Reusable items pre-defined at the field level for readability. */
    private ShoppingCart.Item laptop;
    private ShoppingCart.Item mouse;
    private ShoppingCart.Item keyboard;

    // =========================================================================
    // @Before – Setup: runs before EVERY @Test method
    // =========================================================================

    /**
     * Creates a fresh {@link ShoppingCart} and three standard {@link ShoppingCart.Item}
     * objects before each test, ensuring complete test isolation.
     */
    @Before
    public void setUp() {
        // Initialise the cart fixture
        cart = new ShoppingCart();

        // Pre-build reusable items (Arrange helpers)
        laptop   = new ShoppingCart.Item("Laptop",   75000.00, 1);
        mouse    = new ShoppingCart.Item("Mouse",      850.00, 2);   // qty = 2 → subtotal 1700
        keyboard = new ShoppingCart.Item("Keyboard",  1500.00, 1);

        System.out.println("[setUp]    ShoppingCart and items initialised.");
    }

    // =========================================================================
    // @After – Teardown: runs after EVERY @Test method
    // =========================================================================

    /**
     * Clears the cart and nulls references after each test to release
     * memory and prevent accidental state leakage.
     */
    @After
    public void tearDown() {
        cart.clear();
        cart     = null;
        laptop   = null;
        mouse    = null;
        keyboard = null;
        System.out.println("[tearDown] ShoppingCart cleared and references nulled.");
    }

    // =========================================================================
    // Tests – each follows strict AAA layout
    // =========================================================================

    /**
     * TC-01 · A newly created cart should be empty.
     */
    @Test
    public void testNewCartIsEmpty() {
        // ARRANGE – cart created fresh in @Before; nothing additional needed

        // ACT
        boolean result = cart.isEmpty();

        // ASSERT
        assertTrue("A freshly created cart must report isEmpty() = true", result);
    }

    /**
     * TC-02 · Adding one item must increase the item count to 1.
     */
    @Test
    public void testAddItem_IncreasesItemCount() {
        // ARRANGE
        // cart is empty (ensured by @Before)

        // ACT
        cart.addItem(laptop);

        // ASSERT
        assertEquals("Item count should be 1 after adding one item",
                1, cart.getItemCount());
    }

    /**
     * TC-03 · Adding three items must set item count to 3.
     */
    @Test
    public void testAddMultipleItems_CorrectCount() {
        // ARRANGE
        // cart starts empty; items pre-built in @Before

        // ACT
        cart.addItem(laptop);
        cart.addItem(mouse);
        cart.addItem(keyboard);

        // ASSERT
        assertEquals("Item count should be 3 after adding three items",
                3, cart.getItemCount());
    }

    /**
     * TC-04 · Total should equal the sum of all item subtotals.
     *          laptop=75000, mouse=850×2=1700, keyboard=1500 → total=78200
     */
    @Test
    public void testGetTotal_ReturnsCorrectSum() {
        // ARRANGE
        cart.addItem(laptop);    // 75000.00
        cart.addItem(mouse);     //  1700.00  (850 × 2)
        cart.addItem(keyboard);  //  1500.00

        // ACT
        double total = cart.getTotal();

        // ASSERT
        assertEquals("Total should be 78200.00 for the three test items",
                78200.00, total, 0.001);
    }

    /**
     * TC-05 · Total of an empty cart must be zero.
     */
    @Test
    public void testGetTotal_EmptyCart_ReturnsZero() {
        // ARRANGE – cart is empty from @Before

        // ACT
        double total = cart.getTotal();

        // ASSERT
        assertEquals("Empty cart total must be 0.0", 0.0, total, 0.0);
    }

    /**
     * TC-06 · A 10% discount on a total of 78200 should give 70380.
     */
    @Test
    public void testGetTotalAfterDiscount_TenPercent() {
        // ARRANGE
        cart.addItem(laptop);    // 75000
        cart.addItem(mouse);     //  1700
        cart.addItem(keyboard);  //  1500
        // gross total = 78200; 10% off = 70380

        // ACT
        double discountedTotal = cart.getTotalAfterDiscount(10.0);

        // ASSERT
        assertEquals("10% discount on 78200 should yield 70380",
                70380.00, discountedTotal, 0.001);
    }

    /**
     * TC-07 · A 0% discount must leave the total unchanged.
     */
    @Test
    public void testGetTotalAfterDiscount_ZeroPercent_NoChange() {
        // ARRANGE
        cart.addItem(laptop);

        // ACT
        double discountedTotal = cart.getTotalAfterDiscount(0.0);

        // ASSERT
        assertEquals("0% discount should not change the total",
                75000.00, discountedTotal, 0.001);
    }

    /**
     * TC-08 · A 100% discount must make the total zero.
     */
    @Test
    public void testGetTotalAfterDiscount_HundredPercent_TotalIsZero() {
        // ARRANGE
        cart.addItem(laptop);

        // ACT
        double discountedTotal = cart.getTotalAfterDiscount(100.0);

        // ASSERT
        assertEquals("100% discount should reduce total to 0.0",
                0.0, discountedTotal, 0.001);
    }

    /**
     * TC-09 · Removing an existing item should return true and reduce count by 1.
     */
    @Test
    public void testRemoveItem_ExistingItem_ReturnsTrueAndDecreasesCount() {
        // ARRANGE
        cart.addItem(laptop);
        cart.addItem(mouse);

        // ACT
        boolean removed = cart.removeItem("Laptop");

        // ASSERT
        assertTrue("removeItem() should return true when item is found", removed);
        assertEquals("Item count should decrease to 1 after removing one item",
                1, cart.getItemCount());
    }

    /**
     * TC-10 · Removing a non-existent item should return false and leave count unchanged.
     */
    @Test
    public void testRemoveItem_NonExistentItem_ReturnsFalseAndCountUnchanged() {
        // ARRANGE
        cart.addItem(mouse);

        // ACT
        boolean removed = cart.removeItem("Invisible Product");

        // ASSERT
        assertFalse("removeItem() should return false when item is not found", removed);
        assertEquals("Item count should remain 1 after failed removal",
                1, cart.getItemCount());
    }

    /**
     * TC-11 · After clear(), the cart must be empty.
     */
    @Test
    public void testClear_MakesCartEmpty() {
        // ARRANGE
        cart.addItem(laptop);
        cart.addItem(mouse);
        cart.addItem(keyboard);

        // ACT
        cart.clear();

        // ASSERT
        assertTrue("Cart must be empty after calling clear()", cart.isEmpty());
        assertEquals("Item count must be 0 after clear()", 0, cart.getItemCount());
    }

    /**
     * TC-12 · Invalid discount (> 100) must throw IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetTotalAfterDiscount_InvalidPercent_ThrowsException() {
        // ARRANGE
        cart.addItem(laptop);

        // ACT — expected to throw
        cart.getTotalAfterDiscount(110.0);

        // ASSERT — handled by @Test(expected = ...)
    }

    /**
     * TC-13 · Adding a null item must throw IllegalArgumentException.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddItem_NullItem_ThrowsException() {
        // ARRANGE – nothing extra

        // ACT — expected to throw
        cart.addItem(null);

        // ASSERT — handled by @Test(expected = ...)
    }

    /**
     * TC-14 · removeItem() is case-insensitive.
     */
    @Test
    public void testRemoveItem_CaseInsensitiveMatch() {
        // ARRANGE
        cart.addItem(keyboard);     // name = "Keyboard"

        // ACT – use different casing
        boolean removed = cart.removeItem("KEYBOARD");

        // ASSERT
        assertTrue("removeItem() should match regardless of case", removed);
        assertTrue("Cart should be empty after removing the only item", cart.isEmpty());
    }
}
