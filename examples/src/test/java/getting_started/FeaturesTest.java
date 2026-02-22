package getting_started;

import org.tabletest.junit.TableTest;
import org.tabletest.junit.TypeConverter;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FeaturesTest {

    // #region table-format
    @TableTest("""
        Scenario                              | Year | Is leap year?
        Years not divisible by 4              | 2001 | false
        Years divisible by 4                  | 2004 | true
        Years divisible by 100 but not by 400 | 2100 | false
        Years divisible by 400                | 2000 | true
        """)
    public void leapYearCalculation(Year year, boolean expectedResult) {
        assertEquals(expectedResult, year.isLeap(), "Year " + year);
    }
    // #endregion table-format

    // #region collections
    @TableTest("""
        List      | size? | sum?
        []        | 0     | 0
        [1]       | 1     | 1
        [3, 2, 1] | 3     | 6
        """)
    void integerList(List<Integer> list, int expectedSize, int expectedSum) {
        assertEquals(expectedSize, list.size());
        assertEquals(expectedSum, list.stream().mapToInt(Integer::intValue).sum());
    }
    // #endregion collections

    // #region type-conversion
    @TableTest("""
        Number | Text | Date       | Class
        1      | abc  | 2025-01-20 | java.lang.Integer
        """)
    void singleValues(short number, String text, LocalDate date, Class<?> type) {
        // all values converted automatically
    }
    // #endregion type-conversion

    // #region custom-converter
    @TypeConverter
    public static LocalDate convertToDate(String input) {
        return switch (input) {
            case "today" -> LocalDate.now();
            case "tomorrow" -> LocalDate.now().plusDays(1);
            default -> LocalDate.parse(input);
        };
    }
    // #endregion custom-converter

    // #region value-sets
    @TableTest("""
        Scenario                              | Example years      | Is leap year?
        Years not divisible by 4              | {2001, 2002, 2003} | false
        Years divisible by 4                  | {2004, 2008, 2012} | true
        Years divisible by 100 but not by 400 | {2100, 2200, 2300} | false
        Years divisible by 400                | {2000, 2400, 2800} | true
        """)
    public void testLeapYear(Year year, boolean expectedResult) {
        assertEquals(expectedResult, year.isLeap(), "Year " + year);
    }
    // #endregion value-sets

    // #region scenario-names
    @TableTest("""
        Scenario   | Input | Output?
        Basic case | 1     | one
        Edge case  | 0     | zero
        """)
    void scenarioNames(int input, String output) {
        String result = switch (input) {
            case 0 -> "zero";
            case 1 -> "one";
            default -> String.valueOf(input);
        };
        assertEquals(output, result);
    }
    // #endregion scenario-names
}
