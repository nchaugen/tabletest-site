package guide

import org.tabletest.junit.TableTest
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ValueFormatsKtTest {

    // #region single-values
    @TableTest(
        """
        Value                  | Length?
        Hello, world!          | 13
        "cat file.txt | wc -l" | 20
        "[]"                   | 2
        ''                     | 0
        """
    )
    fun testString(value: String, expectedLength: Int) {
        assertEquals(expectedLength, value.length)
    }
    // #endregion single-values

    // #region lists
    @TableTest(
        """
        List      | Size? | Sum?
        []        | 0     | 0
        [1]       | 1     | 1
        [3, 2, 1] | 3     | 6
        """
    )
    fun integerList(list: List<Int>, expectedSize: Int, expectedSum: Int) {
        assertEquals(expectedSize, list.size)
        assertEquals(expectedSum, list.sum())
    }
    // #endregion lists

    // #region sets
    @TableTest(
        """
        Set       | Size?
        {1, 2, 3} | 3
        {Hello}   | 1
        {}        | 0
        """
    )
    fun testSet(set: Set<String>, expectedSize: Int) {
        assertEquals(expectedSize, set.size)
    }
    // #endregion sets

    // #region maps
    @TableTest(
        """
        Map                        | Size?
        [one: 1, two: 2, three: 3] | 3
        [:]                        | 0
        """
    )
    fun testMap(map: Map<String, Int>, expectedSize: Int) {
        assertEquals(expectedSize, map.size)
    }
    // #endregion maps

    // #region nested
    @TableTest(
        """
        Student grades                                                  | Pass?
        [Alice: [95, 87, 92], Bob: [78, 85, 90], Charlie: [98, 89, 91]] | {Alice, Bob, Charlie}
        [David: [45, 60, 70], Emma: [65, 70, 75], Frank: [82, 78, 60]]  | {Emma, Frank}
        [:]                                                             | {}
        """
    )
    fun testNestedParameterizedTypes(
        studentGrades: Map<String, List<Int>>,
        expectedPass: Set<String>
    ) {
        val pass = studentGrades.filter { it.value.average() >= 60 }.map { it.key }.toSet()
        assertEquals(expectedPass, pass)
    }
    // #endregion nested

    // #region null-values
    @TableTest(
        """
        String | Integer | List | Map | Set
               |         |      |     |
        """
    )
    fun blankConvertsToNull(
        string: String?, integer: Int?, list: List<Any?>?,
        map: Map<String, Any?>?, set: Set<Any?>?
    ) {
        assertNull(string)
        assertNull(integer)
        assertNull(list)
        assertNull(map)
        assertNull(set)
    }
    // #endregion null-values
}
