package getting_started;

import org.tabletest.junit.TableTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

// #region verification
public class InstallationTest {
    @TableTest("""
        Input | Expected
        1     | 1
        2     | 2
        3     | 3
        """)
    public void verifyTableTestWorks(int input, int expected) {
        assertEquals(expected, input);
    }
}
// #endregion verification
