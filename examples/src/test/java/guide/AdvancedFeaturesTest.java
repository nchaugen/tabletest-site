package guide;

import org.junit.jupiter.api.TestInfo;
import org.tabletest.junit.Scenario;
import org.tabletest.junit.TableTest;

import java.time.Year;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AdvancedFeaturesTest {

    // #region value-sets
    @TableTest("""
        Scenario                              | Example years      | Is leap year?
        Years not divisible by 4              | {2001, 2002, 2003} | false
        Years divisible by 4                  | {2004, 2008, 2012} | true
        Years divisible by 100 but not by 400 | {2100, 2200, 2300} | false
        Years divisible by 400                | {2000, 2400, 2800} | true
        """)
    public void testLeapYear(Year year, boolean expectedResult) {
        assertEquals(expectedResult, year.isLeap(), "Year " + year);
    }
    // #endregion value-sets

    // #region cartesian-product
    @TableTest("""
        Scenario       | x         | y       | even sum?
        Even plus even | {2, 4, 6} | {8, 10} | true
        Odd plus even  | {1, 3, 5} | {6, 8}  | false
        """)
    void testEvenOddSums(int x, int y, boolean expectedResult) {
        boolean isEvenSum = (x + y) % 2 == 0;
        assertEquals(expectedResult, isEvenSum);
    }
    // #endregion cartesian-product

    // #region sets-as-parameters
    @TableTest("""
        Values       | Size?
        {1, 2, 3}    | 3
        {a, b, c, d} | 4
        {}           | 0
        """)
    void testSetParameter(Set<String> values, int expectedSize) {
        assertEquals(expectedSize, values.size());
    }
    // #endregion sets-as-parameters

    // #region comments
    @TableTest("""
        String      | Length?
        
        Hello world | 11
        
        // The next row is currently disabled
        // "World, hello" | 12
        
        // Special characters must be quoted
        '|'         | 1
        '[:]'       | 3
        """)
    void testComment(String string, int expectedLength) {
        assertEquals(expectedLength, string.length());
    }
    // #endregion comments

    // #region external-table
    @TableTest(resource = "/external.table")
    void testExternalTable(int a, int b, int sum) {
        assertEquals(sum, a + b);
    }
    // #endregion external-table

    // #region parameter-resolvers
    @TableTest("""
        Scenario | value | double?
        Zero     | 0     | 0
        Two      | 2     | 4
        """)
    void testDoubleValue(@Scenario String scenario, int value,
                         int expectedResult, TestInfo info) {
        assertEquals(expectedResult, 2 * value);
        assertNotNull(info);
    }
    // #endregion parameter-resolvers

    // #region escape-sequences-java
    @TableTest("""
        Scenario                                | Input      | Length?
        Tab character processed by compiler     | a\tb       | 3
        Quote marks processed by compiler       | Say \"hi\" | 8
        Backslash processed by compiler         | path\\file | 9
        Unicode character processed by compiler | \u0041B    | 2
        Octal character processed by compiler   | \101B      | 2
        """)
    void testEscapeSequences(String input, int expectedLength) {
        assertEquals(expectedLength, input.length());
    }
    // #endregion escape-sequences-java
}
