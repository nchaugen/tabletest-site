---
title: "Type Conversion"
weight: 2
---

TableTest converts values from table cells to the types required by test method parameters. Understanding the conversion strategy helps you work with custom types and complex data effectively.

## Conversion Strategy

TableTest uses a two-tier conversion approach:

1. **Custom converter methods** — annotated with `@TypeConverter` in your test class or external sources
2. **JUnit built-in converters** — standard type conversion as a fallback

Custom converters take priority, allowing you to override built-in conversion when needed.

## Built-in Conversion

### Single Values

The parameter type controls conversion. A cell value like `42` converts to `int`, `long`, `double`, or any other numeric type depending on what the parameter declares. The commonly-used built-in conversions include:

- **Numbers** — `int`, `long`, `short`, `byte`, `float`, `double`, and their wrapper types. The cell value is parsed according to the target type.
- **Boolean and char** — `true`/`false` for booleans, single characters for `char`.
- **Enums** — Converted by name (e.g. `MONDAY` for `DayOfWeek`).
- **Time types** — `LocalDate`, `LocalDateTime`, `LocalTime`, `Instant`, `Duration`, `Period`, and other `java.time` types, parsed in their standard ISO formats.
- **Paths and URIs** — `Path`, `File`, `URI`, and `URL`.

See the [JUnit documentation](https://docs.junit.org/current/writing-tests/parameterized-classes-and-tests.html#argument-conversion-implicit) for the full list.

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
{{< codefile file="examples/src/test/java/guide/TypeConversionTest.java" id="built-in" >}}
{{< /tab >}}
{{< tab >}}
{{< codefile file="examples/src/test/kotlin/guide/TypeConversionKtTest.kt" id="built-in" >}}
{{< /tab >}}
{{< /tabs >}}

### Collections and Arrays

When the parameter is a parameterised type like `List<Integer>` or `Map<String, LocalDate>`, TableTest uses the generic type information to convert each element individually. Map keys remain `String` type and are not converted.

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
{{< codefile file="examples/src/test/java/guide/TypeConversionTest.java" id="parameterized-types" >}}
{{< /tab >}}
{{< tab >}}
{{< codefile file="examples/src/test/kotlin/guide/TypeConversionKtTest.kt" id="parameterized-types" >}}
{{< /tab >}}
{{< /tabs >}}

The same `[a, b, c]` list syntax also works for array parameters — both object arrays (`String[]`) and primitive arrays (`int[]`). The parameter type determines the result.

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
{{< codefile file="examples/src/test/java/guide/TypeConversionTest.java" id="array-types" >}}
{{< /tab >}}
{{< tab >}}
{{< codefile file="examples/src/test/kotlin/guide/TypeConversionKtTest.kt" id="array-types" >}}
{{< /tab >}}
{{< /tabs >}}

## Custom Converter Methods

When you need to convert to a type that isn't covered by built-in conversion, define a custom converter method annotated with `@TypeConverter`.

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
{{< codefile file="examples/src/test/java/guide/TypeConversionDiscountTest.java" id="discount-converter" >}}
{{< /tab >}}
{{< tab >}}
{{< codefile file="examples/src/test/kotlin/guide/TypeConversionDiscountKtTest.kt" id="discount-converter" >}}
{{< /tab >}}
{{< /tabs >}}

Then use it in a test:

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
{{< codefile file="examples/src/test/java/guide/TypeConversionDiscountTest.java" id="discount-test" >}}
{{< /tab >}}
{{< tab >}}
{{< codefile file="examples/src/test/kotlin/guide/TypeConversionDiscountKtTest.kt" id="discount-test" >}}
{{< /tab >}}
{{< /tabs >}}

Custom converters also work with collections — TableTest applies the converter to each element:

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
{{< codefile file="examples/src/test/java/guide/TypeConversionDiscountTest.java" id="collections-custom-types" >}}
{{< /tab >}}
{{< tab >}}
{{< codefile file="examples/src/test/kotlin/guide/TypeConversionDiscountKtTest.kt" id="collections-custom-types" >}}
{{< /tab >}}
{{< /tabs >}}

### Converter Method Rules

A valid custom converter method must:

1. Be annotated with `@TypeConverter`
2. Be a `public static` method in a `public` class (see [Kotlin converters](#kotlin-converters) below)
3. Accept exactly one parameter
4. Return an object of the target parameter type
5. Be the only `@TypeConverter` method matching the above criteria in the class

There is no specific naming pattern — any method fulfilling these requirements will be used.

### Kotlin Converters

In Kotlin, `@TypeConverter` methods require `@JvmStatic` and can be declared in two locations:

1. In the **companion object** of the test class (as shown in the examples above)
2. At **package level** in the file containing the test class

### Converter Parameter Types

The converter parameter doesn't have to be `String`. It can be any type that TableTest knows how to convert. When the converter's parameter type doesn't match the parsed value, TableTest recursively attempts to convert the value to match — using custom converters or built-in conversion as needed. In the example below, the converter accepts a `double` — TableTest first converts the cell value from string to `double` using built-in conversion, then passes the result to `fromCelsius`.

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
{{< codefile file="examples/src/test/java/guide/TypeConversionTemperatureTest.java" id="converter-parameter-types" >}}
{{< /tab >}}
{{< tab >}}
{{< codefile file="examples/src/test/kotlin/guide/TypeConversionTemperatureKtTest.kt" id="converter-parameter-types" >}}
{{< /tab >}}
{{< /tabs >}}

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
{{< codefile file="examples/src/test/java/guide/TypeConversionTemperatureTest.java" id="converter-parameter-types-test" >}}
{{< /tab >}}
{{< tab >}}
{{< codefile file="examples/src/test/kotlin/guide/TypeConversionTemperatureKtTest.kt" id="converter-parameter-types-test" >}}
{{< /tab >}}
{{< /tabs >}}

### Overriding Built-in Conversion

Since custom converters take priority over built-in conversion, you can override the built-in conversion for specific types:

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
{{< codefile file="examples/src/test/java/guide/TypeConversionOverrideTest.java" id="override-builtin-converter" >}}
{{< /tab >}}
{{< tab >}}
{{< codefile file="examples/src/test/kotlin/guide/TypeConversionOverrideKtTest.kt" id="override-builtin-converter" >}}
{{< /tab >}}
{{< /tabs >}}

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
{{< codefile file="examples/src/test/java/guide/TypeConversionOverrideTest.java" id="override-builtin-test" >}}
{{< /tab >}}
{{< tab >}}
{{< codefile file="examples/src/test/kotlin/guide/TypeConversionOverrideKtTest.kt" id="override-builtin-test" >}}
{{< /tab >}}
{{< /tabs >}}

### JUnit Explicit Argument Conversion

JUnit's [explicit argument conversion](https://docs.junit.org/current/writing-tests/parameterized-classes-and-tests.html#argument-conversion-explicit) with `@ConvertWith` can also be used with TableTest methods. Note that the `ArgumentConverter` receives the parsed value (not the raw string).

## Custom Converter Sources

To reuse converter methods across test classes, use the `@TypeConverterSources` annotation to list classes containing converters:

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
```java
@TypeConverterSources({SharedConverters.class, MoreConverters.class})
public class ExampleTest {
    // TableTest methods can use converters from the listed classes
}
```
{{< /tab >}}
{{< tab >}}
```kotlin
@TypeConverterSources(SharedConverters::class, MoreConverters::class)
class ExampleTest {
    // TableTest methods can use converters from the listed classes
}
```
{{< /tab >}}
{{< /tabs >}}

A converter source class is a regular class with `public static` `@TypeConverter` methods. In Kotlin, use an `object` declaration with `@JvmStatic`:

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
```java
public class SharedConverters {
    @TypeConverter
    public static Discount toDiscount(double rate) {
        return new Discount(rate / 100);
    }
}
```
{{< /tab >}}
{{< tab >}}
```kotlin
object SharedConverters {
    @JvmStatic
    @TypeConverter
    fun toDiscount(rate: Double): Discount =
        Discount(rate / 100)
}
```
{{< /tab >}}
{{< /tabs >}}

### Converter Search Order

TableTest searches for a matching custom converter in the test class first, then in classes listed by `@TypeConverterSources`, and stops at the first match. This means a converter defined in the test class always takes precedence over one in a shared source.

## Next Steps

Explore value sets, external table files, and more: [Advanced Features](/reference/advanced-features/)
