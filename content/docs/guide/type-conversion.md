---
title: "Type Conversion"
weight: 3
draft: true
---

# Type Conversion

TableTest converts string values from table cells to method parameter types. Understanding conversion rules helps you work with custom types and complex data effectively.

## Conversion Strategy

TableTest uses a two-tier conversion approach:

1. **Type converter methods** - Custom conversion logic
2. **JUnit implicit converters** - Built-in type conversion

This strategy prioritizes custom conversion while falling back to JUnit's robust conversion system.

## Automatic Conversion

Most standard Java types convert automatically without additional configuration:

### Primitives and Wrappers

```java
@TableTest("""
    Int | Long  | Double | Boolean | Char
    42  | 1000  | 3.14   | true    | A
    -5  | -2000 | -1.5   | false   | Z
    """)
void test(int i, long l, double d, boolean b, char c) {
    // Automatic conversion
}
```

Supported: `int`, `long`, `short`, `byte`, `float`, `double`, `boolean`, `char`, and wrapper types.

### Strings

Strings work without conversion:

```java
@TableTest("""
    Text
    hello
    world
    """)
void test(String text) {
    // Direct mapping
}
```

### Enums

Enum constants match by name:

```java
enum Status { PENDING, ACTIVE, CLOSED }

@TableTest("""
    Status
    PENDING
    ACTIVE
    CLOSED
    """)
void test(Status status) {
    // Converted to enum constant
}
```

### Temporal Types

Common date/time types:

```java
@TableTest("""
    Date       | Time     | DateTime
    2024-01-15 | 14:30:00 | 2024-01-15T14:30:00
    """)
void test(LocalDate date, LocalTime time, LocalDateTime dateTime) {
    // ISO-8601 format
}
```

Supported: `LocalDate`, `LocalTime`, `LocalDateTime`, `Instant`, `ZonedDateTime`, and more.

## Factory Methods

For types without automatic conversion, define **type converter methods** - public static methods that convert strings to your type. Annotate the methods with `@TypeConverter`.

### Basic Factory Method

```java
public class DiscountTest {
    @TableTest("""
        Purchases | Discount
        5         | 10%
        15        | 20%
        40        | 40%
        """)
    void testDiscount(int purchases, Discount discount) {
        // parseDiscount converts "10%" → Discount(10)
    }

    @TypeConverter
    public static Discount parseDiscount(String input) {
        String digits = input.substring(0, input.length() - 1); // Remove '%'
        return new Discount(Integer.parseInt(digits));
    }
}
```

### Type Converter Method Rules

A valid type converter method must be:

1. **Public in a public class** - Accessible to TableTest
2. **Static** - No instance required
3. **Single parameter** - Takes one argument (usually `String`)
4. **Returns target type** - Produces the parameter type
5. **Annotated** - With `@TypeConverter`

### Type Converter Method Variants

**Accept any convertible type:**

```java
@TypeConverter
public static Temperature fromCelsius(double celsius) {
    return new Temperature(celsius);
}

@TableTest("""
    Celsius | Fahrenheit
    0.0     | 32.0
    100.0   | 212.0
    """)
void test(Temperature celsius, double fahrenheit) {
    // built-in conversion converts String → double
    // fromCelsius converts double → Temperature
}
```

The type converter parameter doesn't have to be `String` - it can be any type TableTest knows how to convert.


## External Type Converter Sources

If type converter methods aren't in the test class, specify external sources with `@TypeConverterSources`:

```java
@TypeConverterSources(MyConverters.class)
class MyTest {
    @TableTest("""
        Value
        special-value
        """)
    void test(CustomType value) {
        // Uses converter from MyConverters class
    }
}
```

## Collections with Custom Types

Type converters work with collections:

```java
@TypeConverter
public static Discount parseDiscount(String input) {
    String digits = input.substring(0, input.length() - 1);
    return new Discount(Integer.parseInt(digits));
}

@TableTest("""
    Discounts        | Best
    [10%, 20%, 30%]  | 30%
    [5%, 15%]        | 15%
    """)
void test(List<Discount> discounts, Discount best) {
    // Each element converted via parseDiscount
    assertEquals(best, DiscountCalculator.selectBest(discounts));
}
```

TableTest applies the type converter to each element.

## Nested Conversion

Complex nested structures convert recursively:

```java
@TypeConverter
public static Price parsePrice(String input) {
    return new Price(Double.parseDouble(input.replace("$", "")));
}

@TableTest("""
    Product Prices                                | Total
    [apple: [$1.50, $1.25], banana: [$0.75]]      | $3.50
    [coffee: [$3.00, $5.00], tea: [$2.50, $3.00]] | $13.50
    """)
void test(Map<String, List<Price>> prices, Price total) {
    // Keys: String (automatic)
    // Values: List<Price> (via built-in and parsePrice)
    assertEquals(total, PriceCalculator.calculateTotal(prices));
}
```

## Value Sets and Conversion

Type converters work with [value sets](/docs/guide/advanced-features/#value-sets):

```java
@TypeConverter
public static EmailAddress parseEmail(String input) {
    return new EmailAddress(input);
}

@TableTest("""
    Scenario       | Emails                            | Is Valid?
    Valid emails   | {alice@example.com, bob@test.org} | true
    Invalid emails | {admin@, @site.com}               | false
    """)
void test(EmailAddress email, boolean expectedValidity) {
    // Each email converted via parseEmail
    assertEquals(expectedValidity, EmailValidator.isValid(email));
}
```

## Conversion Errors

When conversion fails, TableTest provides clear error messages:

```java
@TableTest("""
    Number
    abc      // Can't convert to int
    """)
void test(int number) {
    // Error: "Could not convert 'abc' to type int"
}
```

Error messages include:
- The problematic value
- The target type
- The conversion method that failed

## Best Practices

### Descriptive Factory Names

Use clear, intention-revealing names:

```java
// Good
public static Temperature fromCelsius(double c) { ... }
public static Discount fromPercentage(String s) { ... }

// Less clear
public static Temperature create(double d) { ... }
public static Discount parse(String s) { ... }
```

### Handle Edge Cases

Factory methods should handle expected edge cases gracefully:

```java
public static Discount parseDiscount(String input) {
    if (input == null || input.isEmpty()) {
        return Discount.NONE;
    }
    if (input.equals("FREE")) {
        return Discount.FULL;
    }
    String digits = input.replace("%", "").trim();
    return new Discount(Integer.parseInt(digits));
}
```

### Fail Fast

When conversion can't succeed, throw descriptive exceptions:

```java
public static Temperature fromCelsius(double celsius) {
    if (celsius < -273.15) {
        throw new IllegalArgumentException(
            "Temperature below absolute zero: " + celsius);
    }
    return new Temperature(celsius);
}
```

### Test Factory Methods

Factory methods are regular Java methods - test them separately:

```java
@Test
void testParseDiscount() {
    assertEquals(new Discount(10), parseDiscount("10%"));
    assertEquals(new Discount(0), parseDiscount("0%"));
    assertThrows(IllegalArgumentException.class,
        () -> parseDiscount("invalid"));
}
```

## Language-Specific Features

### Java Factory Methods

Use standard static methods:

```java
public static MyType fromString(String input) {
    return new MyType(input);
}
```

### Kotlin Factory Methods

Use companion object functions:

```kotlin
class MyType(val value: String) {
    companion object {
        @JvmStatic
        fun fromString(input: String): MyType {
            return MyType(input)
        }
    }
}
```

The `@JvmStatic` annotation is required for TableTest to find the method.

## Troubleshooting

**Factory method not found:**
- Ensure method is `public static`
- Check method name follows conventions
- Verify parameter and return types match
- Use `@FactorySources` if method is in another class

**Conversion fails:**
- Check input format matches factory expectations
- Verify factory handles null and edge cases
- Test factory method independently
- Review error message for specific cause

**Ambiguous conversion:**
- Multiple factories can convert the same type
- TableTest tries them in order until one succeeds
- If ambiguity causes problems, use `@ConvertWith` for explicitness

## Next Steps

Explore powerful testing techniques: [Advanced Features →](/docs/guide/advanced-features/)
