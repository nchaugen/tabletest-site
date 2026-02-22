package guide

import org.tabletest.junit.TableTest
import org.tabletest.junit.TypeConverter
import kotlin.test.assertEquals

class TypeConversionTemperatureKtTest {

    data class Temperature(val celsius: Double) {
        fun toFahrenheit(): Double = celsius * 9.0 / 5.0 + 32.0
    }

    // #region converter-parameter-types
    companion object {
        @JvmStatic
        @TypeConverter
        fun fromCelsius(celsius: Double): Temperature = Temperature(celsius)
    }
    // #endregion converter-parameter-types

    // #region converter-parameter-types-test
    @TableTest(
        """
        Celsius | Fahrenheit?
        0.0     | 32.0
        100.0   | 212.0
        """
    )
    fun testTemperature(celsius: Temperature, fahrenheit: Double) {
        assertEquals(fahrenheit, celsius.toFahrenheit(), 0.01)
    }
    // #endregion converter-parameter-types-test
}
