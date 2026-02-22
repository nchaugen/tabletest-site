package guide

import org.junit.jupiter.api.TestInfo
import org.tabletest.junit.Scenario
import org.tabletest.junit.TableTest
import java.time.Year
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class AdvancedFeaturesKtTest {

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

    // #region cartesian-product
    @TableTest(
        """
        Scenario       | x         | y       | even sum?
        Even plus even | {2, 4, 6} | {8, 10} | true
        Odd plus even  | {1, 3, 5} | {6, 8}  | false
        """
    )
    fun testEvenOddSums(x: Int, y: Int, expectedResult: Boolean) {
        val isEvenSum = (x + y) % 2 == 0
        assertEquals(expectedResult, isEvenSum)
    }
    // #endregion cartesian-product

    // #region sets-as-parameters
    @TableTest(
        """
        Values       | Size?
        {1, 2, 3}    | 3
        {a, b, c, d} | 4
        {}           | 0
        """
    )
    fun testSetParameter(values: Set<String>, expectedSize: Int) {
        assertEquals(expectedSize, values.size)
    }
    // #endregion sets-as-parameters

    // #region comments
    @TableTest(
        """
        String      | Length?
        
        Hello world | 11
        
        // The next row is currently disabled
        // "World, hello" | 12
        
        // Special characters must be quoted
        '|'         | 1
        '[:]'       | 3
        """
    )
    fun testComment(string: String, expectedLength: Int) {
        assertEquals(expectedLength, string.length)
    }
    // #endregion comments

    // #region external-table
    @TableTest(resource = "/external.table")
    fun testExternalTable(a: Int, b: Int, sum: Int) {
        assertEquals(sum, a + b)
    }
    // #endregion external-table

    // #region parameter-resolvers
    @TableTest(
        """
        Scenario | value | double?
        Zero     | 0     | 0
        Two      | 2     | 4
        """
    )
    fun testDoubleValue(@Scenario scenario: String, value: Int,
                        expectedResult: Int, info: TestInfo) {
        assertEquals(expectedResult, 2 * value)
        assertNotNull(info)
    }
    // #endregion parameter-resolvers

    // #region escape-sequences-kotlin
    @TableTest(
        """
        Scenario                                    | Input      | Length?
        Tab character NOT processed by compiler     | a\tb       | 4
        Quote marks NOT processed by compiler       | Say \"hi\" | 10
        Backslash NOT processed by compiler         | path\\file | 10
        Unicode character NOT processed by compiler | \u0041B    | 7
        Octal character NOT processed by compiler   | \101B      | 5
        """
    )
    fun testEscapeSequences(input: String, expectedLength: Int) {
        assertEquals(expectedLength, input.length)
    }
    // #endregion escape-sequences-kotlin
}
