package guide;

import org.tabletest.junit.TableTest;
import org.tabletest.junit.TypeConverter;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TypeConversionTemperatureTest {

    record Temperature(double celsius) {
        double toFahrenheit() {
            return celsius * 9.0 / 5.0 + 32.0;
        }
    }

    // #region converter-parameter-types
    @TypeConverter
    public static Temperature fromCelsius(double celsius) {
        return new Temperature(celsius);
    }
    // #endregion converter-parameter-types

    // #region converter-parameter-types-test
    @TableTest("""
        Celsius | Fahrenheit?
        0.0     | 32.0
        100.0   | 212.0
        """)
    void testTemperature(Temperature celsius, double fahrenheit) {
        assertEquals(fahrenheit, celsius.toFahrenheit(), 0.01);
    }
    // #endregion converter-parameter-types-test
}
