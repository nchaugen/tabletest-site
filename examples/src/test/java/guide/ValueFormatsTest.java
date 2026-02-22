package guide;

import org.tabletest.junit.TableTest;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
        Student grades                                                  | Pass?
        [Alice: [95, 87, 92], Bob: [78, 85, 90], Charlie: [98, 89, 91]] | {Alice, Bob, Charlie}
        [David: [45, 60, 70], Emma: [65, 70, 75], Frank: [82, 78, 60]]  | {Emma, Frank}
        [:]                                                             | {}
        """)
    void testNestedParameterizedTypes(
        Map<String, List<Integer>> studentGrades,
        Set<String> expectedPass
    ) {
        Set<String> pass = studentGrades.entrySet().stream()
            .filter(it ->
                it.getValue().stream()
                    .mapToInt(Integer::intValue)
                    .average().orElse(0) >= 60)
            .map(Map.Entry::getKey)
            .collect(Collectors.toSet());
        assertEquals(expectedPass, pass);
    }
    // #endregion nested

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
