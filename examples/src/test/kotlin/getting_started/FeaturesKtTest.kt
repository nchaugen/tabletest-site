package getting_started

import org.tabletest.junit.TableTest
import org.tabletest.junit.TypeConverter
import java.time.LocalDate
import java.time.Year
import kotlin.test.assertEquals

class FeaturesKtTest {

    // #region table-format
    @TableTest(
        """
        Scenario                              | Year | Is leap year?
        Years not divisible by 4              | 2001 | false
        Years divisible by 4                  | 2004 | true
        Years divisible by 100 but not by 400 | 2100 | false
        Years divisible by 400                | 2000 | true
        """
    )
    fun leapYearCalculation(year: Year, expectedResult: Boolean) {
        assertEquals(expectedResult, year.isLeap, "Year $year")
    }
    // #endregion table-format

    // #region collections
    @TableTest(
        """
        List      | size? | sum?
        []        | 0     | 0
        [1]       | 1     | 1
        [3, 2, 1] | 3     | 6
        """
    )
    fun integerList(list: List<Int>, expectedSize: Int, expectedSum: Int) {
        assertEquals(expectedSize, list.size)
        assertEquals(expectedSum, list.sum())
    }
    // #endregion collections

    // #region type-conversion
    @TableTest(
        """
        Number | Text | Date       | Class
        1      | abc  | 2025-01-20 | java.lang.Integer
        """
    )
    fun singleValues(number: Short, text: String, date: LocalDate, type: Class<*>) {
        // all values converted automatically
    }
    // #endregion type-conversion

    companion object {
        // #region custom-converter
        @JvmStatic
        @TypeConverter
        fun convertToDate(input: String): LocalDate =
            when (input) {
                "today" -> LocalDate.now()
                "tomorrow" -> LocalDate.now().plusDays(1)
                else -> LocalDate.parse(input)
            }
        // #endregion custom-converter
    }

    // #region value-sets
    @TableTest(
        """
        Scenario                              | Example years      | Is leap year?
        Years not divisible by 4              | {2001, 2002, 2003} | false
        Years divisible by 4                  | {2004, 2008, 2012} | true
        Years divisible by 100 but not by 400 | {2100, 2200, 2300} | false
        Years divisible by 400                | {2000, 2400, 2800} | true
        """
    )
    fun testLeapYear(year: Year, expectedResult: Boolean) {
        assertEquals(expectedResult, year.isLeap, "Year $year")
    }
    // #endregion value-sets

    // #region scenario-names
    @TableTest(
        """
        Scenario   | Input | Output?
        Basic case | 1     | one
        Edge case  | 0     | zero
        """
    )
    fun scenarioNames(input: Int, output: String) {
        val result = when (input) {
            0 -> "zero"
            1 -> "one"
            else -> input.toString()
        }
        assertEquals(output, result)
    }
    // #endregion scenario-names
}
