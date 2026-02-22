package guide

import org.junit.jupiter.api.io.TempDir
import org.tabletest.junit.Scenario
import org.tabletest.junit.TableTest
import java.nio.file.Path
import kotlin.test.assertEquals

class BasicUsageKtTest {

    // #region header-row
    @TableTest(
        """
        Input Value | Result?
        5           | 10
        """
    )
    fun headerRow(input: Int, expected: Int) {
        assertEquals(expected, input * 2)
    }
    // #endregion header-row

    // #region header-row-scenario
    @TableTest(
        """
        Scenario | Input Value | Result?
        Doubles  | 5           | 10
        """
    )
    fun headerRowScenario(input: Int, expected: Int) {
        assertEquals(expected, input * 2)
    }
    // #endregion header-row-scenario

    // #region data-rows
    @TableTest(
        """
        Value | Result
        1     | 2
        3     | 6
        5     | 10
        """
    )
    fun testDouble(value: Int, result: Int) {
        assertEquals(result, value * 2)
    }
    // #endregion data-rows

    // #region implicit-scenario
    @TableTest(
        """
        Situation    | Input | Expected
        Small number | 5     | 10
        Large number | 100   | 200
        Zero         | 0     | 0
        """
    )
    fun implicitScenario(input: Int, expected: Int) {
        assertEquals(expected, input * 2)
    }
    // #endregion implicit-scenario

    // #region explicit-scenario
    @TableTest(
        value = """
        Input | Description  | Expected
        5     | Small number | 10
        100   | Large number | 200
        """
    )
    fun explicitScenario(input: Int, @Scenario description: String, expected: Int) {
        assertEquals(expected, input * 2)
    }
    // #endregion explicit-scenario

    // #region junit-params
    @TableTest(
        """
        Scenario     | Input | Expected
        Small number | 5     | 10
        Large number | 100   | 200
        Zero         | 0     | 0
        """
    )
    fun junitParams(@Scenario scenario: String, input: Int, expected: Int, @TempDir tempDir: Path) {
        assertEquals(expected, input * 2)
    }
    // #endregion junit-params

    // #region no-scenario
    @TableTest(
        """
        Input | Expected
        5     | 10
        100   | 200
        """
    )
    fun noScenario(input: Int, expected: Int) {
        assertEquals(expected, input * 2)
    }
    // #endregion no-scenario

    // #region comments-blank-lines
    @TableTest(
        """
        Scenario       | Input | Expected
        
        // Positive numbers
        Small positive | 5     | 10
        Large positive | 100   | 200
        
        // Negative numbers
        Small negative | -5    | -10
        Large negative | -100  | -200
        
        // Edge cases
        Zero           | 0     | 0
        """
    )
    fun commentsAndBlankLines(input: Int, expected: Int) {
        assertEquals(expected, input * 2)
    }
    // #endregion comments-blank-lines

    // #region one-execution-per-row
    @TableTest(
        """
        Scenario | Value
        First    | 1
        Second   | 2
        Third    | 3
        """
    )
    fun oneExecutionPerRow(value: Int) {
        println("Value: $value")
    }
    // #endregion one-execution-per-row

    // #region independent-executions
    private var counter = 0

    @TableTest(
        """
        Scenario | Value
        First    | 1
        Second   | 2
        """
    )
    fun independentExecutions(value: Int) {
        assertEquals(1, ++counter)  // counter will initialise to 0 for each row
    }
    // #endregion independent-executions

    // #region simple-values
    @TableTest(
        """
        Scenario | String | Int | Long | Double | Boolean
        Example  | hello  | 42  | 1000 | 3.14   | true
        """
    )
    fun simpleValues(s: String, i: Int, l: Long, d: Double, b: Boolean) {
        // Automatic conversion
    }
    // #endregion simple-values

    // #region string-values
    @TableTest(
        """
        Value           | Length?
        hello           | 5
        hello world     | 11
        "hello | world" | 13
        "'"             | 1
        '"'             | 1
        """
    )
    fun stringValues(value: String, expectedLength: Int) {
        assertEquals(expectedLength, value.length)
    }
    // #endregion string-values

    // #region null-values
    @TableTest(
        """
        Scenario | Value
        Present  | hello
        Absent   |
        """
    )
    fun nullValues(value: String?) {
        // Row 2: value == null
    }
    // #endregion null-values
}
