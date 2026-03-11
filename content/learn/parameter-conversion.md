---
title: "Parameter Conversion Explained"
date: 2025-09-06
description: "How TableTest converts table cell strings to the types your test methods need, and how to teach it about your domain types."
weight: 4
---

In the [previous article](/learn/readable-table-tests/), the leap year table had three columns: a scenario description, a year, and a boolean. The test method only had two parameters — `int year` and `boolean isLeapYear`. That extra column raises a question worth explaining.

## The Scenario Column

When a table has one more column than the test method has parameters, TableTest treats the leftmost column as a *scenario column*. It uses the values there as test display names — you see them in your IDE and in test reports — but no parameter is needed in the method signature.

```java
@TableTest("""
    Scenario                        | Year | Is Leap Year?
    Not divisible by 4              | 2001 | false
    Divisible by 4                  | 2004 | true
    Divisible by 100 but not by 400 | 2100 | false
    Divisible by 400                | 2000 | true
    """)
void leapYear(int year, boolean isLeapYear) {
    assertEquals(isLeapYear, Year.isLeap(year));
}
```

The header name doesn't have to be "Scenario" — it can be anything. If you want a different column to serve as the scenario column, or you need more control, see [Scenario Names](/reference/advanced-features/#scenario-names) in the documentation.

## Automatic Conversion

TableTest uses JUnit's built-in implicit type conversion to turn string values into the types your parameters expect. Numeric strings convert to numeric types (`int`, `long`, `double`), `"true"` and `"false"` convert to `boolean`, and standard library types like dates, times, and URLs are also supported. The [JUnit documentation](https://docs.junit.org/current/writing-tests/parameterized-classes-and-tests.html#argument-conversion-implicit) lists the full set.

Beyond JUnit's conversions, TableTest adds support for collection types. List syntax `[1, 2, 3]`, set syntax `{1, 2, 3}`, and map syntax `[key: value]` all work out of the box. When the parameter is a parameterised type like `List<Integer>`, the individual elements are also converted.

```java
@TableTest("""
    Scenario         | Inputs        | Total?
    Two numbers      | [10, 20]      | 30
    Three numbers    | [5, 15, 30]   | 50
    """)
void sumOfList(List<Integer> inputs, int total) {
    assertEquals(total, inputs.stream().mapToInt(i -> i).sum());
}
```

## Custom Type Converters

Automatic conversion handles standard types, but your tests often work with domain types. If TableTest encounters a type it can't convert to, it tells you with a clear error — that's your cue to add a `@TypeConverter` method.

A converter method is `public static`, annotated with `@TypeConverter`, and accepts a single parameter. The return type tells TableTest which type it converts to:

```java
@TypeConverter
public static PercentageDiscount toDiscount(String value) {
    // e.g. "15%" → new PercentageDiscount(15)
    int percentage = Integer.parseInt(value.replace("%", "").trim());
    return new PercentageDiscount(percentage);
}
```

Now table cells like `"0%"` and `"15%"` convert to `PercentageDiscount` automatically:

```java
@TableTest("""
    Scenario      | Purchases | Discount?
    No purchases  | 0         | 0%
    First tier    | 5         | 5%
    Second tier   | 10        | 10%
    """)
void discountTier(int purchases, PercentageDiscount expectedDiscount) {
    assertEquals(expectedDiscount, LevelledDiscount.calculate(purchases));
}
```

Custom converters also apply when the type appears inside a collection. If you have `@TypeConverter` for `PercentageDiscount`, then `List<PercentageDiscount>` works too.

### Converter Parameter Types

The converter's parameter doesn't have to be `String`. It can be any type TableTest already knows how to convert. If your domain type maps naturally from a `double`, accept a `double`:

```java
@TypeConverter
public static Temperature fromCelsius(double celsius) {
    return new Temperature(celsius);
}
```

TableTest converts the cell string to `double` first (using built-in conversion), then passes the result to your converter. This lets you avoid parsing boilerplate and compose conversions naturally.

### Overriding Built-in Conversion

Custom converters take priority over built-in conversion, so you can override the default for any type. A common use case is dates: instead of accepting the default ISO format, you could define a converter that recognises `"2025-09-06"` or relative notation like `"T-30d"`. See the [realistic example](/learn/realistic-example/#making-the-table-readable) for a full illustration of this technique.

## Sharing Converters Across Tests

Converter methods defined in the test class are available only in that class. To reuse them, extract them into a shared class and reference it with `@TypeConverterSources`:

```java
@TypeConverterSources(SharedConverters.class)
class DiscountTests {
    // All @TypeConverter methods from SharedConverters are available here
}
```

```java
public class SharedConverters {
    @TypeConverter
    public static PercentageDiscount toDiscount(String value) {
        int percentage = Integer.parseInt(value.replace("%", "").trim());
        return new PercentageDiscount(percentage);
    }
}
```

TableTest searches the test class first, then the listed sources, and uses the first match — so a local converter always wins over a shared one.

## Value Sets

Value sets let you provide multiple input values in a single cell, wrapped in curly braces. TableTest runs the test once for each value:

```java
@TableTest("""
    Scenario                        | Year               | Is Leap Year?
    Not divisible by 4              | {1, 2001, 30001}   | false
    Divisible by 4                  | {4, 2004, 30008}   | true
    Divisible by 100 but not by 400 | {100, 2100, 30300} | false
    Divisible by 400                | {400, 2000, 30000} | true
    """)
void leapYear(int year, boolean isLeapYear) {
    assertEquals(isLeapYear, Year.isLeap(year));
}
```

When multiple columns contain sets, TableTest runs all combinations — a cartesian product. The test display name shows the actual values used in each execution, so failures are easy to trace.

## What This Looks Like Together

These features compose well. A table can have a scenario column for readability, use automatic conversion for primitive inputs, rely on a shared custom converter for domain types, and use value sets to group related inputs. Each feature is independent — use as many or as few as the table needs.

For more detail on all of these topics, see:

- [Advanced Features](/reference/advanced-features/) — Scenario names, value sets, external table files
- [Basic Usage](/reference/basic-usage/) — Table syntax, value formats, quoting rules
- [Type Conversion](/reference/type-conversion/) — Full reference for all conversion options
- [Advanced Features](/reference/advanced-features/) — Value sets, external table files
