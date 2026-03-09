package guide;

import org.tabletest.junit.TableTest;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TypeConversionTest {

    // #region built-in
    @TableTest("""
        Number | Text | Date       | Class
        1      | abc  | 2025-01-20 | java.lang.Integer
        """)
    void singleValues(short number, String text, LocalDate date, Class<?> type) {
        // All converted automatically
    }
    // #endregion built-in

    // #region parameterized-types
    @TableTest("""
        Grades                                       | Highest Grade?
        [Alice: [95, 87, 92], Bob: [78, 85, 90]]     | 95
        [Charlie: [98, 89, 91], David: [45, 60, 70]] | 98
        """)
    void testParameterizedTypes(Map<String, List<Integer>> grades,
                                int expectedHighestGrade) {
        int highest = grades.values().stream()
            .flatMapToInt(g -> g.stream().mapToInt(Integer::intValue))
            .max()
            .orElse(0);
        assertEquals(expectedHighestGrade, highest);
    }
    // #endregion parameterized-types
}
