package guide

import org.tabletest.junit.TableTest
import org.tabletest.junit.TypeConverter
import kotlin.test.assertEquals

class TypeConversionDiscountKtTest {

    // #region discount-converter
    data class Discount(val percentage: Int)

    companion object {
        @JvmStatic
        @TypeConverter
        fun parseDiscount(input: String): Discount {
            val digits = input.replace("%", "").trim()
            return Discount(digits.toInt())
        }
    }
    // #endregion discount-converter

    // #region discount-test
    @TableTest(
        """
        Purchases | Discount?
        5         | 10%
        15        | 20%
        40        | 40%
        """
    )
    fun testDiscount(purchases: Int, discount: Discount) {
        assertEquals(discount, calculateDiscount(purchases))
    }
    // #endregion discount-test

    // #region collections-custom-types
    @TableTest(
        """
        Discounts       | Best?
        [10%, 20%, 30%] | 30%
        [5%, 15%]       | 15%
        """
    )
    fun testCollectionsWithCustomTypes(discounts: List<Discount>, best: Discount) {
        val actual = discounts.maxBy { it.percentage }
        assertEquals(best, actual)
    }
    // #endregion collections-custom-types

    private fun calculateDiscount(purchases: Int): Discount {
        val percentage = minOf((purchases / 5 + 1) * 5, 40)
        return Discount(percentage)
    }
}
