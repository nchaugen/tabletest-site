package guide;

import org.tabletest.junit.TableTest;
import org.tabletest.junit.TypeConverter;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RealisticExampleDiscountTest {

    record PercentageDiscount(int percentage) {}

    // #region discount-converter
    @TypeConverter
    public static PercentageDiscount parseDiscount(String input) {
        String digits = input.substring(0, input.length() - 1);
        return new PercentageDiscount(Integer.parseInt(digits));
    }
    // #endregion discount-converter

    // #region discount-test
    @TableTest("""
        Purchases last 30 days | Discount?
        {0, 1, 2, 3}           | 0%
        {4, 5, 6, 7, 8}        | 5%
        {9, 10, 11, 12, 13}    | 10%
        {14, 15, 16, 17, 18}   | 15%
        {19, 20, 21, 22, 23}   | 20%
        {24, 25, 26, 27, 28}   | 25%
        {29, 30, 31, 32, 33}   | 30%
        {34, 35, 36, 37, 38}   | 35%
        {39, 40, 100, 1000}    | 40%
        """)
    void next_purchase_discount(int purchasesLast30Days,
                                PercentageDiscount expectedDiscount) {
        assertEquals(
            expectedDiscount.percentage(),
            LevelledDiscount.calculateDiscountPercentage(purchasesLast30Days)
        );
    }
    // #endregion discount-test

    static class LevelledDiscount {
        static int calculateDiscountPercentage(int purchasesLast30Days) {
            int nextTicketNumber = purchasesLast30Days + 1;
            int tier = nextTicketNumber / 5;
            return Math.min(tier * 5, 40);
        }
    }
}
