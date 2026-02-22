package guide

import org.tabletest.junit.TableTest
import org.tabletest.junit.TypeConverter
import java.time.LocalDate
import kotlin.test.assertEquals

class TypeConversionOverrideKtTest {

    // #region override-builtin-converter
    companion object {
        @JvmStatic
        @TypeConverter
        fun parseLocalDate(input: String): LocalDate =
            when (input) {
                "yesterday" -> LocalDate.parse("2025-06-06")
                "today" -> LocalDate.parse("2025-06-07")
                "tomorrow" -> LocalDate.parse("2025-06-08")
                else -> LocalDate.parse(input)
            }
    }
    // #endregion override-builtin-converter

    // #region override-builtin-test
    @TableTest(
        """
        This Date  | Other Date | Is Before?
        today      | tomorrow   | true
        today      | yesterday  | false
        2024-02-29 | 2024-03-01 | true
        """
    )
    fun testIsBefore(thisDate: LocalDate, otherDate: LocalDate, expectedIsBefore: Boolean) {
        assertEquals(expectedIsBefore, thisDate.isBefore(otherDate))
    }
    // #endregion override-builtin-test
}
