---
title: "What is TableTest?"
weight: 1
---

TableTest is a JUnit extension that enables *table-driven testing* – a technique where you express system behaviour through multiple examples organized in a readable table format.

## The Problem: Repetitive Test Code

Much of software development involves evaluating inputs against rules: validating fields, classifying data, and triggering actions when thresholds are crossed. Traditional unit testing often produces repetitive methods with nearly identical structure.

Consider testing leap year logic:

```java
@Test
public void testLeapYearNotDivisibleBy4() {
    assertFalse(Year.isLeap(2001));
}

@Test
public void testLeapYearDivisibleBy4() {
    assertTrue(Year.isLeap(2004));
}

@Test
public void testLeapYearDivisibleBy100ButNotBy400() {
    assertFalse(Year.isLeap(2100));
}

@Test
public void testLeapYearDivisibleBy400() {
    assertTrue(Year.isLeap(2000));
}
```

Four test methods, each testing a single scenario. As requirements grow, so does the repetition. What if we need to test 10 scenarios? 20? The code becomes unwieldy.

## The Solution: Separate Data from Logic

Table-driven testing consolidates test logic into a single parameterized method that executes multiple data rows:

```java
@TableTest("""
    Scenario                        | Year | Is Leap Year?
    Not divisible by 4              | 2001 | false
    Divisible by 4                  | 2004 | true
    Divisible by 100 but not by 400 | 2100 | false
    Divisible by 400                | 2000 | true
    """)
public void testLeapYear(int year, boolean isLeapYear) {
    assertEquals(isLeapYear, Year.isLeap(year));
}
```

The test logic appears once. The test data – our examples – lives in the table. Each row represents one scenario that will be executed.

## Key Benefits

### 1. Reduces Maintenance Burden

When implementation changes, you modify the test logic in one place. Adding new test cases is as simple as adding a row to the table rather than writing an entirely new method.

### 2. Improves Readability

Tables are instantly scannable. You can see patterns, identify missing edge cases, and understand coverage at a glance. The structure makes test intent obvious.

### 3. Creates Living Documentation

Well-structured tables serve as useful descriptions of how the system works. They facilitate discussions with non-technical stakeholders about system behaviour and edge cases, becoming a shared language between technical and business teams.

### 4. Enables Comprehensive Testing

Tables make it easy to test multiple inputs with the same expected outcome. Using value sets, you can group scenarios:

```java
@TableTest("""
    Scenario                        | Year               | Is Leap Year?
    Not divisible by 4              | {1, 2001, 30001}   | false
    Divisible by 4                  | {4, 2004, 30008}   | true
    Divisible by 100 but not by 400 | {100, 2100, 30300} | false
    Divisible by 400                | {400, 2000, 30000} | true
    """)
public void testLeapYear(int year, boolean isLeapYear) {
    assertEquals(isLeapYear, Year.isLeap(year));
}
```

This single table now tests 12 different years – triple the coverage with no additional code.

## When to Use TableTest

TableTest excels when:

- **Testing the same logic with multiple inputs** – Different data, same assertion structure
- **You have 2+ similar test methods** – Methods that differ only in their input values
- **Business rules involve multiple cases** – Discount tiers, validation rules, state transitions
- **Adding test cases should be simple** – New scenarios should require adding a row, not writing code

## When Not to Use TableTest

Standard `@Test` methods work better when:

- **Testing a single scenario** – One input, one expected output
- **Test logic differs significantly** – Different assertions or setup for each case
- **Complex setup varies per test** – Mocking behaviour changes between tests
- **Tests aren't data-driven** – Testing interactions, exceptions, or behaviour rather than data transformations

## Next Steps

Now that you understand what TableTest is and when to use it, let's [set it up in your project&nbsp;→](/docs/getting-started/installation/)
