package getting_started;

import org.tabletest.junit.TableTest;

import java.time.Year;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IntroductionTest {

    // #region traditional-tests
    @Test
    public void testLeapYearNotDivisibleBy4() {
        assertFalse(Year.isLeap(2001));
    }

    @Test
    public void testLeapYearDivisibleBy4() {
        assertTrue(Year.isLeap(2004));
    }

    @Test
    public void testLeapYearDivisibleBy100ButNotBy400() {
        assertFalse(Year.isLeap(2100));
    }

    @Test
    public void testLeapYearDivisibleBy400() {
        assertTrue(Year.isLeap(2000));
    }
    // #endregion traditional-tests

    // #region table-test
    @TableTest("""
        Scenario                        | Year | Is Leap Year?
        Not divisible by 4              | 2001 | false
        Divisible by 4                  | 2004 | true
        Divisible by 100 but not by 400 | 2100 | false
        Divisible by 400                | 2000 | true
        """)
    public void testLeapYear(int year, boolean isLeapYear) {
        assertEquals(isLeapYear, Year.isLeap(year));
    }
    // #endregion table-test

    // #region value-sets
    @TableTest("""
        Scenario                        | Year               | Is Leap Year?
        Not divisible by 4              | {1, 2001, 30001}   | false
        Divisible by 4                  | {4, 2004, 30008}   | true
        Divisible by 100 but not by 400 | {100, 2100, 30300} | false
        Divisible by 400                | {400, 2000, 30000} | true
        """)
    public void testLeapYearValueSets(int year, boolean isLeapYear) {
        assertEquals(isLeapYear, Year.isLeap(year));
    }
    // #endregion value-sets
}
