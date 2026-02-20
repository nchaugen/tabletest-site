---
title: "Advanced Features"
weight: 4
---

TableTest provides features beyond the basics for expressing comprehensive examples: value sets for testing multiple inputs, comments for organising tables, external files for large datasets, parameter resolvers for dependency injection, and escape sequences for special characters.

## Value Sets

Value sets let you test multiple inputs with the same expected outcome, expressed compactly within a single row. When a cell contains a set (enclosed in curly braces) and the corresponding parameter is *not* declared as a `Set` type, TableTest expands it into separate test invocations — one per element.

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

This table produces **12 test executions** — three per row, one for each value in the set. With scenario names, the display name includes both the scenario and the actual value used, making it easy to pinpoint failures.

### Cartesian Product

When multiple columns contain value sets, TableTest generates the Cartesian product — all combinations of values:

```java
@TableTest("""
    Scenario       | x         | y       | even sum?
    Even plus even | {2, 4, 6} | {8, 10} | true
    Odd plus even  | {1, 3, 5} | {6, 8}  | false
    """)
void testEvenOddSums(int x, int y, boolean expectedResult) {
    boolean isEvenSum = (x + y) % 2 == 0;
    assertEquals(expectedResult, isEvenSum);
}
```

Each row above produces 6 test executions (3 x-values times 2 y-values). Use value sets judiciously — the number of test cases grows multiplicatively with each additional set.

### Sets as Parameters

When the parameter *is* declared as a `Set` type, the entire set is passed as a single argument without expansion:

```java
@TableTest("""
    Values       | Size?
    {1, 2, 3}    | 3
    {a, b, c, d} | 4
    {}           | 0
    """)
void testSetParameter(Set<String> values, int expectedSize) {
    assertEquals(expectedSize, values.size());
}
```

## Comments and Blank Lines

Lines starting with `//` (ignoring leading whitespace) are treated as comments and ignored. Blank lines are also ignored. Both can be used to organise and annotate your tables.

```java
@TableTest("""
    String         | Length?

    Hello world    | 11

    // The next row is currently disabled
    // "World, hello" | 12

    // Special characters must be quoted
    '|'            | 1
    '[:]'          | 3
    """)
void testComment(String string, int expectedLength) {
    assertEquals(expectedLength, string.length());
}
```

## External Table Files

For large datasets, store the table in an external file using the `resource` attribute. The file is located as a resource relative to the test class and is typically stored in the test `resources` directory.

By default, the file is assumed to use UTF-8 encoding. Specify a different encoding with the `encoding` attribute if needed.

```java
@TableTest(resource = "/external.table")
void testExternalTable(int a, int b, int sum) {
    assertEquals(sum, a + b);
}

@TableTest(resource = "/custom-encoding.table", encoding = "ISO-8859-1")
void testExternalTableWithCustomEncoding(String string, int expectedLength) {
    assertEquals(expectedLength, string.length());
}
```

## Parameter Resolvers

TableTest methods can receive additional arguments provided by JUnit `ParameterResolver` extensions (such as `TestInfo`, `TestReporter`, etc.). These resolver-provided parameters **must be declared last**, after all table columns.

If the table includes a scenario name column and you use parameter resolvers, the scenario column now requires an explicit parameter with the `@Scenario` annotation:

```java
@TableTest("""
    Scenario | value | double?
    Zero     | 0     | 0
    Two      | 2     | 4
    """)
void testDoubleValue(@Scenario String scenario, int value,
                     int expectedResult, TestInfo info) {
    assertEquals(expectedResult, 2 * value);
    assertNotNull(info);
}
```

## Escape Sequences

Escape sequence handling varies depending on the programming language.

### Java Text Blocks

In Java text blocks, all Java escape sequences (`\t`, `\"`, `\\`, `\uXXXX`, etc.) are processed by the Java compiler before TableTest receives the values:

```java
@TableTest("""
    Scenario                                | Input      | Length?
    Tab character processed by compiler     | a\tb       | 3
    Quote marks processed by compiler       | Say \"hi\" | 8
    Backslash processed by compiler         | path\\file | 9
    Unicode character processed by compiler | \u0041B    | 2
    Octal character processed by compiler   | \101B      | 2
    """)
void testEscapeSequences(String input, int expectedLength) {
    assertEquals(expectedLength, input.length());
}
```

### Kotlin Raw Strings

Using Kotlin raw strings, escape sequences are **not** processed. They remain as literal backslash characters:

```kotlin
@TableTest(
    """
    Scenario                                    | Input      | Length?
    Tab character NOT processed by compiler     | a\tb       | 4
    Quote marks NOT processed by compiler       | Say \"hi\" | 10
    Backslash NOT processed by compiler         | path\\file | 10
    Unicode character NOT processed by compiler | \u0041B    | 7
    Octal character NOT processed by compiler   | \101B      | 5
    """)
fun testEscapeSequences(input: String, expectedLength: Int) {
    assertEquals(expectedLength, input.length)
}
```

### External Files

Table files are read as raw text independent of the programming language, meaning escape sequences are **not** processed and remain literal.

If you need special characters in Kotlin or external table files, use actual characters instead of escape sequences, or use Kotlin regular strings for simple cases.

## Next Steps

See all these features in action: [Realistic Example](/docs/guide/realistic-example/)
