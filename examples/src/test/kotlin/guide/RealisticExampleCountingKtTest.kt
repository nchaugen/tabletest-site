package guide

import org.tabletest.junit.TableTest
import org.tabletest.junit.TypeConverter
import java.time.LocalDateTime
import kotlin.test.assertEquals

class RealisticExampleCountingKtTest {

    data class Purchase(val timestamp: LocalDateTime)

    companion object {
        private val TIME_NOW: LocalDateTime = LocalDateTime.now()

        private val T_MINUS_PATTERN = Regex("""T-(?:(\d+)d)?(?:(\d+)h)?(?:(\d+)m)?(?:(\d+)s)?""")

        // #region relative-timestamp-converter
        @JvmStatic
        @TypeConverter
        fun parseRelativeTimestamp(input: String): LocalDateTime {
            val match = T_MINUS_PATTERN.matchEntire(input)
                ?: throw IllegalArgumentException("Invalid relative timestamp: $input")
            return TIME_NOW
                .minusDays(match.groupValues[1].toLongOrNull() ?: 0)
                .minusHours(match.groupValues[2].toLongOrNull() ?: 0)
                .minusMinutes(match.groupValues[3].toLongOrNull() ?: 0)
                .minusSeconds(match.groupValues[4].toLongOrNull() ?: 0)
        }
        // #endregion relative-timestamp-converter

        @JvmStatic
        @TypeConverter
        fun createPurchase(timestamp: LocalDateTime): Purchase = Purchase(timestamp)
    }

    // #region counting-test
    @TableTest(
        """
        Scenario              | Purchases        | Count?
        No purchases          | []               | 0
        Purchase too old      | [T-60d]          | 0
        Purchase just inside  | [T-29d23h59m59s] | 1
        Purchase just outside | [T-30d]          | 0
        All purchases inside  | [T-28d, T-27d]   | 2
        One inside, one not   | [T-45d, T-15d]   | 1
        """
    )
    fun count_purchases_last_30_days(purchases: List<Purchase>,
                                     expectedCount: Int) {
        assertEquals(expectedCount,
            countPurchasesLast30Days(TIME_NOW, purchases))
    }
    // #endregion counting-test

    private fun countPurchasesLast30Days(timeNow: LocalDateTime,
                                         purchases: List<Purchase>): Int {
        val thirtyDaysAgo = timeNow.minusDays(30)
        return purchases.count { it.timestamp.isAfter(thirtyDaysAgo) }
    }
}
