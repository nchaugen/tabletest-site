package guide;

import org.tabletest.junit.TableTest;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ValueFormatsTest {

    // #region single-values
    @TableTest("""
        Value                  | Length?
        Hello, world!          | 13
        "cat file.txt | wc -l" | 20
        "[]"                   | 2
        ''                     | 0
        """)
    void testString(String value, int expectedLength) {
        assertEquals(expectedLength, value.length());
    }
    // #endregion single-values

    // #region lists
    @TableTest("""
        List      | Size? | Sum?
        []        | 0     | 0
        [1]       | 1     | 1
        [3, 2, 1] | 3     | 6
        """)
    void integerList(List<Integer> list, int expectedSize, int expectedSum) {
        assertEquals(expectedSize, list.size());
        assertEquals(expectedSum, list.stream().mapToInt(Integer::intValue).sum());
    }
    // #endregion lists

    // #region sets
    @TableTest("""
        Set       | Size?
        {1, 2, 3} | 3
        {Hello}   | 1
        {}        | 0
        """)
    void testSet(Set<String> set, int expectedSize) {
        assertEquals(expectedSize, set.size());
    }
    // #endregion sets

    // #region maps
    @TableTest("""
        Map                        | Size?
        [one: 1, two: 2, three: 3] | 3
        [:]                        | 0
        """)
    void testMap(Map<String, Integer> map, int expectedSize) {
        assertEquals(expectedSize, map.size());
    }
    // #endregion maps

    // #region nested
    @TableTest("""
        Scores                           | Top scorer?
        [Alice: [90, 80], Bob: [70, 60]] | Alice
        [Alice: [75, 85], Bob: [95, 90]] | Bob
        """)
    void testNestedParameterizedTypes(
        Map<String, List<Integer>> scores,
        String expectedTopScorer
    ) {
        assertEquals(expectedTopScorer, topScorer(scores));
    }
    // #endregion nested

    private String topScorer(Map<String, List<Integer>> scores) {
        return scores.entrySet().stream()
            .max(Map.Entry.comparingByValue(
                (a, b) -> Integer.compare(a.stream().mapToInt(Integer::intValue).sum(),
                    b.stream().mapToInt(Integer::intValue).sum())))
            .map(Map.Entry::getKey).orElseThrow();
    }

    // #region null-values
    @TableTest("""
        String | Integer | List | Map | Set
               |         |      |     |
        """)
    void blankConvertsToNull(
        String string, Integer integer, List<?> list,
        Map<String, ?> map, Set<?> set
    ) {
        assertNull(string);
        assertNull(integer);
        assertNull(list);
        assertNull(map);
        assertNull(set);
    }
    // #endregion null-values
}
