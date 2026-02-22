package getting_started;

import org.tabletest.junit.TableTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FirstTestTest {

    // #region calculator
    public class Calculator {
        public int add(int a, int b) {
            return a + b;
        }
    }
    // #endregion calculator

    // #region test-class
    private final Calculator calculator = new Calculator();
    // #endregion test-class

    // #region test-addition
    @TableTest("""
        Scenario              | A  | B  | Sum?
        Two positive numbers  | 2  | 3  | 5
        Positive and negative | 5  | -3 | 2
        Two negative numbers  | -4 | -6 | -10
        Zero and positive     | 0  | 7  | 7
        """)
    public void testAddition(int a, int b, int expectedSum) {
        int result = calculator.add(a, b);
        assertEquals(expectedSum, result);
    }
    // #endregion test-addition
}
