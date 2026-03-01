---
title: "Why TableTest?"
date: 2025-08-31
description: "How table-driven testing transforms repetitive test methods into scannable, self-documenting tables."
weight: 3
---

A lot of software is about evaluating inputs against rules: validating fields, classifying data, triggering actions when thresholds are crossed. This is the kind of logic that benefits most from thorough, readable tests.

Consider leap year detection — a small function with four distinct rules. Most developers would write it like this:

```java
@Test
void yearNotDivisibleBy4_isNotLeap() {
    assertFalse(Year.isLeap(2001));
}

@Test
void yearDivisibleBy4_isLeap() {
    assertTrue(Year.isLeap(2004));
}

@Test
void yearDivisibleBy100Not400_isNotLeap() {
    assertFalse(Year.isLeap(2100));
}

@Test
void yearDivisibleBy400_isLeap() {
    assertTrue(Year.isLeap(2000));
}
```

Four tests, four methods, four assertions. Each adds a new scenario by duplicating the structure of the others. The coverage is reasonable, but the signal-to-noise ratio is low. Reading these methods, you have to hold each assertion in mind and work out the pattern yourself.

## The Table-Driven Approach

Table-driven testing separates test logic from test data. A single parameterised test method runs once per row in a table:

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

The `Scenario` column gives each row a name. It doesn't require a matching parameter — TableTest recognises the extra column and uses it as a test display name. The remaining two columns map to `year` and `isLeapYear` by position.

TableTest converts the string values automatically. `"2001"` becomes `int 2001`; `"false"` becomes `boolean false`. No boilerplate conversion code needed.

## Growing Coverage by Adding Rows

The real payoff comes when you want more coverage. With individual test methods, each new scenario means a new method. With a table, it means a new row:

```java
@TableTest("""
    Scenario                        | Year  | Is Leap Year?
    Not divisible by 4              | 2001  | false
    Divisible by 4                  | 2004  | true
    Divisible by 100 but not by 400 | 2100  | false
    Divisible by 400                | 2000  | true
    Year 0 (ISO proleptic calendar) | 0     | true
    Far future leap                 | 2800  | true
    Very far future leap            | 30000 | true
    Very far future not leap        | 30100 | false
    Negative input                  | -1    | false
    """)
void leapYear(int year, boolean isLeapYear) {
    assertEquals(isLeapYear, Year.isLeap(year));
}
```

The test method stays the same. Nine scenarios, one method. A coverage gap you spotted while reviewing is just another row.

## Grouping Values with Sets

TableTest supports value sets: multiple inputs in a single cell, wrapped in curly braces. When a cell contains `{4, 2004, 30008}`, TableTest runs the test once for each value. The table stays compact even as coverage grows:

```java
@TableTest("""
    Scenario                        | Year               | Is Leap Year?
    Not divisible by 4              | {1, 2001, 30001}   | false
    Divisible by 4                  | {4, 2004, 30008}   | true
    Divisible by 100 but not by 400 | {100, 2100, 30300} | false
    Divisible by 400                | {400, 2000, 30000} | true
    Year 0 (ISO proleptic calendar) | 0                  | true
    Negative input                  | -1                 | false
    """)
void leapYear(int year, boolean isLeapYear) {
    assertEquals(isLeapYear, Year.isLeap(year));
}
```

Six rows, but the groupings make the rules visible at a glance. "Divisible by 4" covers 4, 2004, and 30008 — past, present, far future. The table communicates not just what's tested, but why those values belong together.

## Tables as Documentation

These tables can become useful descriptions of how the business and, consequently, the software work. The leap year table mirrors the logical rules in a way that a developer and a business expert could review together. When the rules change, the table changes — and the tests change with it.

This is the core premise of table-driven testing: test data is explicit, visible, and separate from test logic. Adding a scenario is a matter of adding a row. Coverage gaps are visible in the whitespace. And the table itself becomes a specification.

## Getting Started

[TableTest](https://github.com/nchaugen/tabletest) is a JUnit extension for Java and Kotlin. See the [installation guide](/learn/getting-started/installation/) and [your first test](/learn/getting-started/first-test/) to get up and running quickly.

For a more complete example showing custom types, type conversion, and relative timestamps, see the [realistic example](/learn/realistic-example/) in the documentation.
