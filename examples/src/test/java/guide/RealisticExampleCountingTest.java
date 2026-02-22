package guide;

import org.tabletest.junit.TableTest;
import org.tabletest.junit.TypeConverter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RealisticExampleCountingTest {

    record Purchase(LocalDateTime timestamp) {}

    // #region relative-timestamp-converter
    private static final LocalDateTime TIME_NOW = LocalDateTime.now();

    @TypeConverter
    public static LocalDateTime parseRelativeTimestamp(String input) {
        Matcher matcher = T_MINUS_PATTERN.matcher(input);
        if (!matcher.matches()) {
            throw new IllegalArgumentException(
                "Invalid relative timestamp: " + input);
        }
        return TIME_NOW
            .minusDays(intOrZero(matcher, 1))
            .minusHours(intOrZero(matcher, 2))
            .minusMinutes(intOrZero(matcher, 3))
            .minusSeconds(intOrZero(matcher, 4));
    }
    // #endregion relative-timestamp-converter

    @TypeConverter
    public static Purchase createPurchase(LocalDateTime timestamp) {
        return new Purchase(timestamp);
    }

    private static final Pattern T_MINUS_PATTERN = Pattern.compile(
        "T-(?:(\\d+)d)?(?:(\\d+)h)?(?:(\\d+)m)?(?:(\\d+)s)?");

    private static int intOrZero(Matcher matcher, int group) {
        String value = matcher.group(group);
        return value != null ? Integer.parseInt(value) : 0;
    }

    // #region counting-test
    @TableTest("""
        Scenario              | Purchases        | Count?
        No purchases          | []               | 0
        Purchase too old      | [T-60d]          | 0
        Purchase just inside  | [T-29d23h59m59s] | 1
        Purchase just outside | [T-30d]          | 0
        All purchases inside  | [T-28d, T-27d]   | 2
        One inside, one not   | [T-45d, T-15d]   | 1
        """)
    void count_purchases_last_30_days(List<Purchase> purchases,
                                      int expectedCount) {
        assertEquals(expectedCount,
            countPurchasesLast30Days(TIME_NOW, purchases));
    }
    // #endregion counting-test

    private static long countPurchasesLast30Days(LocalDateTime timeNow,
                                                  List<Purchase> purchases) {
        LocalDateTime thirtyDaysAgo = timeNow.minusDays(30);
        return purchases.stream()
            .filter(p -> p.timestamp().isAfter(thirtyDaysAgo))
            .count();
    }
}
