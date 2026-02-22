package guide;

import org.junit.jupiter.api.io.TempDir;
import org.tabletest.junit.Scenario;
import org.tabletest.junit.TableTest;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BasicUsageTest {

    // #region header-row
    @TableTest("""
        Input Value | Result?
        5           | 10
        """)
    void headerRow(int input, int expected) {
        assertEquals(expected, input * 2);
    }
    // #endregion header-row

    // #region header-row-scenario
    @TableTest("""
        Scenario | Input Value | Result?
        Doubles  | 5           | 10
        """)
    void headerRowScenario(int input, int expected) {
        assertEquals(expected, input * 2);
    }
    // #endregion header-row-scenario

    // #region data-rows
    @TableTest("""
        Value | Result
        1     | 2
        3     | 6
        5     | 10
        """)
    void testDouble(int value, int result) {
        assertEquals(result, value * 2);
    }
    // #endregion data-rows

    // #region implicit-scenario
    @TableTest("""
        Situation    | Input | Expected
        Small number | 5     | 10
        Large number | 100   | 200
        Zero         | 0     | 0
        """)
    void implicitScenario(int input, int expected) {
        assertEquals(expected, input * 2);
    }
    // #endregion implicit-scenario

    // #region explicit-scenario
    @TableTest(value = """
        Input | Description  | Expected
        5     | Small number | 10
        100   | Large number | 200
        """)
    void explicitScenario(int input, @Scenario String description, int expected) {
        assertEquals(expected, input * 2);
    }
    // #endregion explicit-scenario

    // #region junit-params
    @TableTest("""
        Scenario     | Input | Expected
        Small number | 5     | 10
        Large number | 100   | 200
        Zero         | 0     | 0
        """)
    void junitParams(@Scenario String scenario, int input, int expected, @TempDir Path tempDir) {
        assertEquals(expected, input * 2);
    }
    // #endregion junit-params

    // #region no-scenario
    @TableTest("""
        Input | Expected
        5     | 10
        100   | 200
        """)
    void noScenario(int input, int expected) {
        assertEquals(expected, input * 2);
    }
    // #endregion no-scenario

    // #region comments-blank-lines
    @TableTest("""
        Scenario       | Input | Expected
        
        // Positive numbers
        Small positive | 5     | 10
        Large positive | 100   | 200
        
        // Negative numbers
        Small negative | -5    | -10
        Large negative | -100  | -200
        
        // Edge cases
        Zero           | 0     | 0
        """)
    void commentsAndBlankLines(int input, int expected) {
        assertEquals(expected, input * 2);
    }
    // #endregion comments-blank-lines

    // #region one-execution-per-row
    @TableTest("""
        Scenario | Value
        First    | 1
        Second   | 2
        Third    | 3
        """)
    void oneExecutionPerRow(int value) {
        System.out.println("Value: " + value);
    }
    // #endregion one-execution-per-row

    // #region independent-executions
    private int counter = 0;

    @TableTest("""
        Scenario | Value
        First    | 1
        Second   | 2
        """)
    void independentExecutions(int value) {
        assertEquals(1, ++counter);  // counter will initialise to 0 for each row
    }
    // #endregion independent-executions

    // #region simple-values
    @TableTest("""
        Scenario | String | Int | Long | Double | Boolean
        Example  | hello  | 42  | 1000 | 3.14   | true
        """)
    void simpleValues(String s, int i, long l, double d, boolean b) {
        // Automatic conversion
    }
    // #endregion simple-values

    // #region string-values
    @TableTest("""
        Value              | Length?
        hello              | 5
        hello world        | 11
        "hello | world"    | 13
        "'"                | 1
        '"'                | 1
        """)
    void stringValues(String value, int expectedLength) {
        assertEquals(expectedLength, value.length());
    }
    // #endregion string-values

    // #region null-values
    @TableTest("""
        Scenario | Value
        Present  | hello
        Absent   |
        """)
    void nullValues(String value) {
        // Row 2: value == null
    }
    // #endregion null-values
}
