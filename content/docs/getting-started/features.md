---
title: "Features Overview"
weight: 4
---

A quick tour of what TableTest can do beyond the basics. Each section shows a brief example — follow the links to the [User Guide](/docs/guide/) for full details.

## Table Format

Tests are expressed as pipe-delimited tables inside a `@TableTest` annotation. The first row is the header; subsequent rows are test cases. Each data row invokes the test method with cell values as arguments.

```java
@TableTest("""
    Scenario                              | Year | Is leap year?
    Years not divisible by 4              | 2001 | false
    Years divisible by 4                  | 2004 | true
    Years divisible by 100 but not by 400 | 2100 | false
    Years divisible by 400                | 2000 | true
    """)
public void leapYearCalculation(Year year, boolean expectedResult) {
    assertEquals(expectedResult, year.isLeap(), "Year " + year);
}
```

See [Basic Usage](/docs/guide/basic-usage/) for a detailed walkthrough of table structure, column mapping, and execution.

## Collections

Tables can contain lists, sets, and maps as cell values. Nested structures are also supported. Values are automatically converted to the parameterised type of the corresponding test parameter.

```java
@TableTest("""
    List      | size? | sum?
    []        | 0     | 0
    [1]       | 1     | 1
    [3, 2, 1] | 3     | 6
    """)
void integerList(List<Integer> list, int expectedSize, int expectedSum) {
    assertEquals(expectedSize, list.size());
    assertEquals(expectedSum, list.stream().mapToInt(Integer::intValue).sum());
}
```

Sets use curly braces (`{1, 2, 3}`), maps use square brackets with colons (`[a: 1, b: 2]`), and these can be freely nested — for example `[Alice: [95, 87], Bob: [78, 85]]` for a `Map<String, List<Integer>>`.

## Type Conversion

Cell values are automatically converted to the type of the corresponding test parameter. Built-in conversion covers primitives, strings, dates, enums, and other standard Java types via JUnit's implicit converters.

```java
@TableTest("""
    Number | Text | Date       | Class
    1      | abc  | 2025-01-20 | java.lang.Integer
    """)
void singleValues(short number, String text, LocalDate date, Class<?> type) {
    // all values converted automatically
}
```

For custom domain types, annotate a `public static` method with `@TypeConverter`. TableTest will use it to convert values to the target type:

```java
@TypeConverter
public static LocalDate convertToDate(String input) {
    return switch (input) {
        case "today" -> LocalDate.now();
        case "tomorrow" -> LocalDate.now().plusDays(1);
        default -> LocalDate.parse(input);
    };
}
```

Custom converters can be shared across test classes using the `@TypeConverterSources` annotation.

## Value Sets

When a cell uses set syntax (`{...}`) but the corresponding parameter is not a `Set`, TableTest expands it into multiple test invocations — one per value. This is useful for expressing that several inputs share the same expected result.

```java
@TableTest("""
    Scenario                              | Example years      | Is leap year?
    Years not divisible by 4              | {2001, 2002, 2003} | false
    Years divisible by 4                  | {2004, 2008, 2012} | true
    Years divisible by 100 but not by 400 | {2100, 2200, 2300} | false
    Years divisible by 400                | {2000, 2400, 2800} | true
    """)
public void testLeapYear(Year year, boolean expectedResult) {
    assertEquals(expectedResult, year.isLeap(), "Year " + year);
}
```

The table above generates 12 test invocations (3 values per row, 4 rows). Multiple value sets in the same row produce a Cartesian product of all combinations.

## Scenario Names

A descriptive first column names each test case and is used as the test display name. This makes test output and failure messages immediately understandable.

```java
@TableTest("""
    Scenario     | Input | Output?
    Basic case   | 1     | one
    Edge case    | 0     | zero
    """)
void test(int input, String output) {
    // "Basic case" and "Edge case" appear as display names
}
```

The scenario column is excluded from parameter mapping — there is no need to declare a method parameter for it unless you want to access its value inside the test.

## External Table Files

Large tables can be stored in separate files and loaded with the `resource` attribute. The file is located as a classpath resource relative to the test class.

```java
@TableTest(resource = "/test-data/large-dataset.table")
void testExternalTable(int a, int b, int sum) {
    assertEquals(sum, a + b);
}
```

This keeps test methods concise and makes it easy to share table data across tests or generate it from external sources.

---

This overview covers the highlights. Dive deeper in the [User Guide](/docs/guide/) — starting with [Value Formats](/docs/guide/value-formats/).
