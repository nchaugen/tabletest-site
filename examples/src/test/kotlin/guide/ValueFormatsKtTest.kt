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
        Scores                           | Top scorer?
        [Alice: [90, 80], Bob: [70, 60]] | Alice
        [Alice: [75, 85], Bob: [95, 90]] | Bob
        """
    )
    fun testNestedParameterizedTypes(
        scores: Map<String, List<Int>>,
        expectedTopScorer: String
    ) {
        assertEquals(expectedTopScorer, topScorer(scores))
    }
    // #endregion nested

    private fun topScorer(scores: Map<String, List<Int>>): String =
        scores.maxBy { it.value.sum() }.key

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
