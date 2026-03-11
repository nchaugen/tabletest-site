package guide;

import org.tabletest.junit.TableTest;
import org.tabletest.junit.TypeConverter;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TypeConversionDiscountTest {

    // #region discount-converter
    record Discount(int percentage) {}

    @TypeConverter
    public static Discount parseDiscount(String input) {
        String digits = input.replace("%", "").trim();
        return new Discount(Integer.parseInt(digits));
    }
    // #endregion discount-converter

    // #region discount-test
    @TableTest("""
        Purchases | Discount?
        5         | 10%
        15        | 20%
        40        | 40%
        """)
    void testDiscount(int purchases, Discount discount) {
        assertEquals(discount, calculateDiscount(purchases));
    }
    // #endregion discount-test

    // #region collections-custom-types
    @TableTest("""
        Discounts       | Best?
        [10%, 20%, 30%] | 30%
        [5%, 15%]       | 15%
        """)
    void testCollectionsWithCustomTypes(List<Discount> discounts, Discount best) {
        assertEquals(best, bestDiscount(discounts));
    }
    // #endregion collections-custom-types

    private Discount bestDiscount(List<Discount> discounts) {
        return discounts.stream()
            .max(Comparator.comparingInt(Discount::percentage))
            .orElseThrow();
    }

    private Discount calculateDiscount(int purchases) {
        int percentage = Math.min((purchases / 5 + 1) * 5, 40);
        return new Discount(percentage);
    }
}
