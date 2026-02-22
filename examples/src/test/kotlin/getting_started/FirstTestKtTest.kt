package getting_started

import org.tabletest.junit.TableTest
import kotlin.test.assertEquals

class FirstTestKtTest {

    // #region calculator
    class Calculator {
        fun add(a: Int, b: Int): Int = a + b
    }
    // #endregion calculator

    // #region test-class
    private val calculator = Calculator()
    // #endregion test-class

    // #region test-addition
    @TableTest(
        """
        Scenario              | A  | B  | Sum?
        Two positive numbers  | 2  | 3  | 5
        Positive and negative | 5  | -3 | 2
        Two negative numbers  | -4 | -6 | -10
        Zero and positive     | 0  | 7  | 7
        """
    )
    fun testAddition(a: Int, b: Int, expectedSum: Int) {
        val result = calculator.add(a, b)
        assertEquals(expectedSum, result)
    }
    // #endregion test-addition
}
