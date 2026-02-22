package getting_started

import org.tabletest.junit.TableTest
import kotlin.test.assertEquals

// #region verification
class InstallationKtTest {
    @TableTest(
        """
        Input | Expected
        1     | 1
        2     | 2
        3     | 3
        """
    )
    fun verifyTableTestWorks(input: Int, expected: Int) {
        assertEquals(expected, input)
    }
}
// #endregion verification
