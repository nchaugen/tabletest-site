---
title: "Type Conversion"
weight: 3
---

TableTest converts values from table cells to the types required by test method parameters. Understanding the conversion strategy helps you work with custom types and complex data effectively.

## Conversion Strategy

TableTest uses a two-tier conversion approach:

1. **Custom converter methods** — annotated with `@TypeConverter` in your test class or external sources
2. **JUnit built-in converters** — standard type conversion as a fallback

Custom converters take priority, allowing you to override built-in conversion when needed.

## Built-in Conversion

Out of the box, TableTest converts single values to many standard types using JUnit's built-in type converters. See the [JUnit documentation](https://docs.junit.org/current/writing-tests/parameterized-classes-and-tests.html#argument-conversion-implicit) for the full list.

```java
@TableTest("""
    Number | Text | Date       | Class
    1      | abc  | 2025-01-20 | java.lang.Integer
    """)
void singleValues(short number, String text, LocalDate date, Class<?> type) {
    // All converted automatically
}
```

Built-in conversion also applies to elements in lists and sets, and to values in maps, when the test method parameter is a parameterised type. Map keys remain `String` type and are not converted.

```java
@TableTest("""
    Grades                                       | Highest Grade?
    [Alice: [95, 87, 92], Bob: [78, 85, 90]]     | 95
    [Charlie: [98, 89, 91], David: [45, 60, 70]] | 98
    """)
void testParameterizedTypes(Map<String, List<Integer>> grades,
                            int expectedHighestGrade) {
    // Nested values converted recursively
}
```

## Custom Converter Methods

When you need to convert to a type that isn't covered by built-in conversion, define a custom converter method annotated with `@TypeConverter`.

```java
public class DiscountTest {
    @TableTest("""
        Purchases | Discount?
        5         | 10%
        15        | 20%
        40        | 40%
        """)
    void testDiscount(int purchases, Discount discount) {
        assertEquals(discount, calculator.calculateDiscount(purchases));
    }

    @TypeConverter
    public static Discount parseDiscount(String input) {
        String digits = input.replace("%", "").trim();
        return new Discount(Integer.parseInt(digits));
    }
}
```

### Converter Method Rules

A valid custom converter method must:

1. Be annotated with `@TypeConverter`
2. Be a `public static` method in a `public` class
3. Accept exactly one parameter
4. Return an object of the target parameter type
5. Be the only `@TypeConverter` method matching the above criteria in the class

There is no specific naming pattern — any method fulfilling these requirements will be used.

### Converter Parameter Types

The converter parameter doesn't have to be `String`. It can be any type that TableTest knows how to convert. TableTest will recursively convert the parsed value to match the converter's parameter type.

```java
@TypeConverter
public static Temperature fromCelsius(double celsius) {
    return new Temperature(celsius);
}

@TableTest("""
    Celsius | Fahrenheit?
    0.0     | 32.0
    100.0   | 212.0
    """)
void test(Temperature celsius, double fahrenheit) {
    // Built-in conversion converts String → double
    // fromCelsius converts double → Temperature
}
```

### Overriding Built-in Conversion

Since custom converters take priority over built-in conversion, you can override the built-in conversion for specific types:

```java
@TableTest("""
    This Date  | Other Date | Is Before?
    today      | tomorrow   | true
    today      | yesterday  | false
    2024-02-29 | 2024-03-01 | true
    """)
void testIsBefore(LocalDate thisDate, LocalDate otherDate, boolean expectedIsBefore) {
    assertEquals(expectedIsBefore, thisDate.isBefore(otherDate));
}

@TypeConverter
public static LocalDate parseLocalDate(String input) {
    return switch (input) {
        case "yesterday" -> LocalDate.parse("2025-06-06");
        case "today" -> LocalDate.parse("2025-06-07");
        case "tomorrow" -> LocalDate.parse("2025-06-08");
        default -> LocalDate.parse(input);
    };
}
```

## Custom Converter Sources

To reuse converter methods across test classes, use the `@TypeConverterSources` annotation to list classes containing converters:

```java
@TypeConverterSources({SharedConverters.class, MoreConverters.class})
public class ExampleTest {
    // TableTest methods can use converters from the listed classes
}
```

## Collections with Custom Types

Custom converters work with collections — TableTest applies the converter to each element:

```java
@TypeConverter
public static Discount parseDiscount(String input) {
    String digits = input.replace("%", "").trim();
    return new Discount(Integer.parseInt(digits));
}

@TableTest("""
    Discounts        | Best?
    [10%, 20%, 30%]  | 30%
    [5%, 15%]        | 15%
    """)
void test(List<Discount> discounts, Discount best) {
    assertEquals(best, DiscountCalculator.selectBest(discounts));
}
```

## Kotlin Converters

For tests written in Kotlin, there are two locations to declare `@TypeConverter` methods local to the test class:

1. In the **companion object** of the test class, using `@JvmStatic` in addition to `@TypeConverter`
2. At **package level** in the file containing the test class

Dedicated converter source classes in Kotlin should be declared as `object` with methods annotated with both `@JvmStatic` and `@TypeConverter`:

```kotlin
object KotlinTypeConverterSource {
    @JvmStatic
    @TypeConverter
    fun toStudentGrades(input: Map<String, List<Int>>): StudentGrades {
        // implementation
    }
}
```

Usage:

```kotlin
@TypeConverterSources(KotlinTypeConverterSource::class)
class ExampleTest {
    // TableTest methods
}
```

## Next Steps

Explore value sets, external table files, and more: [Advanced Features](/docs/guide/advanced-features/)
