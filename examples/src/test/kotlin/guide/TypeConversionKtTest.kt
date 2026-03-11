package guide

import org.tabletest.junit.TableTest
import java.time.LocalDate
import kotlin.test.assertEquals

class TypeConversionKtTest {

    // #region built-in
    @TableTest(
        """
        Number | Text | Date       | Class
        1      | abc  | 2025-01-20 | java.lang.Integer
        """
    )
    fun singleValues(number: Short, text: String, date: LocalDate, type: Class<*>) {
        // All converted automatically
    }
    // #endregion built-in

    // #region parameterized-types
    @TableTest(
        """
        Grades                                       | Highest Grade?
        [Alice: [95, 87, 92], Bob: [78, 85, 90]]     | 95
        [Charlie: [98, 89, 91], David: [45, 60, 70]] | 98
        """
    )
    fun testParameterizedTypes(grades: Map<String, List<Int>>,
                               expectedHighestGrade: Int) {
        assertEquals(expectedHighestGrade, highestGrade(grades))
    }
    // #endregion parameterized-types

    private fun highestGrade(grades: Map<String, List<Int>>): Int =
        grades.values.flatten().max()

    // #region array-types
    @TableTest(
        """
        Numbers   | Sum?
        [1, 2, 3] | 6
        [10, 20]  | 30
        []        | 0
        """
    )
    fun sumArray(numbers: IntArray, expectedSum: Int) {
        assertEquals(expectedSum, numbers.sum())
    }
    // #endregion array-types
}
