---
title: "Advanced Features"
weight: 4
draft: true
---

# Advanced Features

TableTest provides powerful features for comprehensive testing: value sets for testing multiple inputs, external table files for large datasets, parameter resolvers for dependency injection, and escape sequences for special characters.

## Value Sets

Value sets allow you to test multiple inputs with the same expected outcome, expressed compactly within a single row.

### Basic Value Sets

Use curly braces to define a set of values for a column:

```java
@TableTest("""
    Scenario         | Year               | Is Leap Year?
    Leap years       | {2000, 2004, 2008} | true
    Non-leap years   | {2001, 2100, 2200} | false
    """)
void testLeapYear(int year, boolean isLeapYear) {
    assertEquals(isLeapYear, Year.isLeap(year));
}
```

This table produces **6 test executions**:
- `testLeapYear[Leap years: 2000]`
- `testLeapYear[Leap years: 2004]`
- `testLeapYear[Leap years: 2008]`
- `testLeapYear[Non-leap years: 2001]`
- `testLeapYear[Non-leap years: 2100]`
- `testLeapYear[Non-leap years: 2200]`

### Multiple Value Sets

When multiple columns contain value sets, TableTest generates the **Cartesian product** - all combinations:

```java
@TableTest("""
    Numbers      | Operators | Results
    {1, 2}       | {+, *}    | {2, 3, 2, 4}
    """)
void testOperations(int number, String operator, int result) {
    // Generates 4 executions:
    // 1, +, 2
    // 1, *, 2
    // 2, +, 3
    // 2, *, 4
}
```

**Warning:** Cartesian products grow quickly. `{1, 2, 3}` × `{a, b, c}` × `{x, y, z}` = 27 executions.

### Value Sets with Collections

Value sets can contain collections:

```java
@TableTest("""
    Lists            | Sum
    {[1,2], [3,4,5]} | {3, 12}
    """)
void testSum(List<Integer> list, int sum) {
    // 2 executions:
    // [1, 2], 3
    // [3, 4, 5], 12
}
```

### Benefits of Value Sets

**Conciseness:** Express multiple related test cases in one row instead of duplicating logic across many rows.

**Clarity:** Group inputs by their expected outcome, making patterns visible.

**Coverage:** Easy to add more test cases without expanding the table vertically.

**Before (repetitive):**
```java
@TableTest("""
    Scenario | Year | Leap?
    2000     | 2000 | true
    2004     | 2004 | true
    2008     | 2008 | true
    2012     | 2012 | true
    """)
```

**After (concise):**
```java
@TableTest("""
    Scenario    | Year               | Leap?
    Leap years  | {2000,2004,2008,2012} | true
    """)
```

## External Table Files

For large datasets, store tables in external files and reference them:

```java
@TableTest(resource = "/test-data/large-dataset.txt")
void testWithExternalFile(String input, String expected) {
    assertEquals(expected, process(input));
}
```

### File Format

External files use the same pipe-delimited format:

**test-data/large-dataset.txt:**
```
Scenario | Input | Expected
Case 1   | foo   | FOO
Case 2   | bar   | BAR
Case 3   | baz   | BAZ
...
(hundreds more rows)
```

### Benefits

**Readability:** Keep test methods focused, not cluttered with massive inline tables.

**Reusability:** Share table files across multiple test methods or classes.

**Version Control:** Large tables in separate files create cleaner diffs and easier reviews.

**Data Management:** Edit large datasets with specialized tools or scripts.

### File Location

Place test data files in `src/test/resources`:

```
src/
  test/
    resources/
      test-data/
        users.txt
        transactions.txt
    java/
      com/example/
        MyTest.java
```

Reference with classpath-relative paths:

```java
@TableTest(resource = "/test-data/users.txt")
```

## Parameter Resolvers

TableTest integrates with JUnit's `ParameterResolver` system, allowing dependency injection into test methods.

### Built-in Resolvers

Standard JUnit resolvers work automatically:

```java
@TableTest("""
    Input | Expected
    hello | HELLO
    world | WORLD
    """)
void test(String input, String expected, TestInfo testInfo) {
    // testInfo injected by JUnit, not from table
    System.out.println("Running: " + testInfo.getDisplayName());
    assertEquals(expected, input.toUpperCase());
}
```

Supported injection types:
- `TestInfo`
- `TestReporter`
- `RepetitionInfo`

### Custom Resolvers

Create custom parameter resolvers for dependency injection:

```java
public class DatabaseResolver implements ParameterResolver {
    @Override
    public boolean supportsParameter(ParameterContext parameterContext,
                                   ExtensionContext extensionContext) {
        return parameterContext.getParameter().getType() == Database.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext,
                                  ExtensionContext extensionContext) {
        return new Database("jdbc:h2:mem:test");
    }
}

@ExtendWith(DatabaseResolver.class)
class MyTest {
    @TableTest("""
        Query          | Count
        SELECT * ...   | 5
        """)
    void test(String query, int count, Database db) {
        // db injected by DatabaseResolver
        assertEquals(count, db.execute(query).size());
    }
}
```

### Mixing Table Data and Injection

Parameters without matching columns are resolved via JUnit:

```java
@TableTest("""
    Input | Expected
    1     | 2
    """)
void test(String input, String expected,
          TestInfo info,      // Injected
          Database db) {      // Injected
    // Only 'input' and 'expected' come from table
}
```

TableTest tries to match parameters to table columns first. Unmatched parameters fall back to JUnit resolution.

## Escape Sequences

Handle special characters in table cells using escape sequences.

### Java Escape Sequences

In Java text blocks, use standard Java escapes:

```java
@TableTest("""
    Input
    "hello\nworld"   // Newline
    "tab\there"      // Tab
    "quote\\"char"   // Escaped quote
    """)
void test(String input) {
    // Standard Java escaping
}
```

### Kotlin Raw Strings

Kotlin raw strings require different escaping:

```kotlin
@TableTest("""
    Input
    "hello
    world"           // Newline (literal)
    "quote""char"    // Escaped quote (double quote)
    """)
fun test(input: String) {
    // Kotlin raw string rules
}
```

### Quoting Special Characters

Quote values containing table delimiters:

```java
@TableTest("""
    Value
    "pipe | character"     // Contains pipe
    "comma, value"         // Contains comma
    "colon: value"         // Contains colon
    "bracket [value]"      // Contains brackets
    """)
void test(String value) {
    // Quotes prevent parsing as special syntax
}
```

## Null Handling

### Explicit Null

Use the `null` keyword:

```java
@TableTest("""
    Input  | Output
    hello  | HELLO
    null   | null
    """)
void test(String input, String output) {
    assertEquals(output, input == null ? null : input.toUpperCase());
}
```

### Empty Cell as Null

Empty cells become `null` for non-primitive types:

```java
@TableTest("""
    Value
    present
            // Empty cell
    """)
void test(String value) {
    // Row 2: value == null
}
```

**Important:** Primitive types (`int`, `boolean`, etc.) cannot be null. Use wrapper types (`Integer`, `Boolean`) for nullable parameters.

### Null vs Empty Collections

Distinguish between `null` and empty:

```java
@TableTest("""
    List  | Description
    []    | Empty list
    null  | Null reference
    """)
void test(List<Integer> list, String description) {
    if (list == null) {
        assertEquals("Null reference", description);
    } else if (list.isEmpty()) {
        assertEquals("Empty list", description);
    }
}
```

## Comments and Organization

### Inline Comments

Lines starting with `//` are ignored:

```java
@TableTest("""
    Scenario | Value

    // Positive test cases
    Small    | 5
    Large    | 100

    // Negative test cases
    Negative | -5

    // Edge cases
    Zero     | 0
    """)
```

### Blank Lines

Use blank lines to visually group related test cases:

```java
@TableTest("""
    Scenario | Value

    Basic cases
    Simple   | 1
    Medium   | 50

    Edge cases
    Minimum  | 0
    Maximum  | 100
    """)
```

Comments and blank lines don't affect execution - they're purely for organization and readability.

## Display Names

### Test Display Names

Customize how tests appear in reports using `@DisplayName`:

```java
@DisplayName("Discount Calculation Tests")
@TableTest("""
    Purchases | Discount
    5         | 10%
    15        | 20%
    """)
void testDiscount(int purchases, String discount) {
    // ...
}
```

Output:
```
✓ Discount Calculation Tests[5]
✓ Discount Calculation Tests[15]
```

### Scenario Column Customization

Change which column provides scenario descriptions:

```java
@TableTest(value = """
    Input | Description     | Expected
    5     | Small number    | 10
    100   | Large number    | 200
    """,
    scenarioColumn = "Description")
```

Or disable scenario names:

```java
@TableTest(value = """...""", scenarioColumn = "")
```

## Performance Considerations

### Large Tables

For tables with hundreds of rows:
- Consider external files for maintainability
- Use value sets to reduce row count
- Monitor test execution time

### Value Set Cartesian Products

Be mindful of combinatorial explosion:

```java
// Generates 1000 executions!
{1..10} × {1..10} × {1..10}
```

If you need comprehensive coverage, ensure test execution is fast or consider parameterized generation strategies.

### External File Caching

TableTest caches external files per test run. Multiple test methods referencing the same file share one file read.

## Best Practices

**Use value sets for related cases:** Group inputs with identical expected outcomes.

**Extract large tables:** Keep test methods readable by moving large datasets to external files.

**Comment generously:** Explain complex scenarios and edge cases with inline comments.

**Test resolvers independently:** Custom parameter resolvers are regular JUnit extensions - test them separately.

**Handle nulls explicitly:** Don't rely on empty cell interpretation - use `null` keyword for clarity.

## Next Steps

See all these features in action: [Realistic Example →](/docs/guide/realistic-example/)
