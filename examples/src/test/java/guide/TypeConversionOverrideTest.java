package guide;

import org.tabletest.junit.TableTest;
import org.tabletest.junit.TypeConverter;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TypeConversionOverrideTest {

    // #region override-builtin-converter
    @TypeConverter
    public static LocalDate parseLocalDate(String input) {
        return switch (input) {
            case "yesterday" -> LocalDate.parse("2025-06-06");
            case "today" -> LocalDate.parse("2025-06-07");
            case "tomorrow" -> LocalDate.parse("2025-06-08");
            default -> LocalDate.parse(input);
        };
    }
    // #endregion override-builtin-converter

    // #region override-builtin-test
    @TableTest("""
        This Date  | Other Date | Is Before?
        today      | tomorrow   | true
        today      | yesterday  | false
        2024-02-29 | 2024-03-01 | true
        """)
    void testIsBefore(LocalDate thisDate, LocalDate otherDate, boolean expectedIsBefore) {
        assertEquals(expectedIsBefore, thisDate.isBefore(otherDate));
    }
    // #endregion override-builtin-test
}
