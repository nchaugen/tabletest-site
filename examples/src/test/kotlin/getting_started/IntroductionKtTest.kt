package getting_started

import org.junit.jupiter.api.Test
import org.tabletest.junit.TableTest
import java.time.Year
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class IntroductionKtTest {

    // #region traditional-tests
    @Test
    fun testLeapYearNotDivisibleBy4() {
        assertFalse(Year.isLeap(2001))
    }

    @Test
    fun testLeapYearDivisibleBy4() {
        assertTrue(Year.isLeap(2004))
    }

    @Test
    fun testLeapYearDivisibleBy100ButNotBy400() {
        assertFalse(Year.isLeap(2100))
    }

    @Test
    fun testLeapYearDivisibleBy400() {
        assertTrue(Year.isLeap(2000))
    }
    // #endregion traditional-tests

    // #region table-test
    @TableTest(
        """
        Scenario                        | Year | Is Leap Year?
        Not divisible by 4              | 2001 | false
        Divisible by 4                  | 2004 | true
        Divisible by 100 but not by 400 | 2100 | false
        Divisible by 400                | 2000 | true
        """
    )
    fun testLeapYear(year: Int, isLeapYear: Boolean) {
        assertEquals(isLeapYear, Year.isLeap(year.toLong()))
    }
    // #endregion table-test

    // #region value-sets
    @TableTest(
        """
        Scenario                        | Year               | Is Leap Year?
        Not divisible by 4              | {1, 2001, 30001}   | false
        Divisible by 4                  | {4, 2004, 30008}   | true
        Divisible by 100 but not by 400 | {100, 2100, 30300} | false
        Divisible by 400                | {400, 2000, 30000} | true
        """
    )
    fun testLeapYearValueSets(year: Int, isLeapYear: Boolean) {
        assertEquals(isLeapYear, Year.isLeap(year.toLong()))
    }
    // #endregion value-sets
}
