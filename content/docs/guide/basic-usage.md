---
title: "Basic Usage"
weight: 1
---

This guide covers the fundamental concepts of TableTest: table structure, column mapping, scenario names, and execution model.

## Table Structure

A TableTest table consists of:

1. **Header row** – Column names (first row)
2. **Data rows** – Test cases (subsequent rows)
3. **Pipe delimiters** – Separate columns

```java
@TableTest("""
    Column1 | Column2 | Column3
    value1  | value2  | value3
    value4  | value5  | value6
    """)
```

### Header Row

The first row defines column names. Columns map to method parameters by order, so you can use readable header names with whitespace and punctuation:

```java
@TableTest("""
    Input Value | Result?
    5           | 10
    """)
void test(int input, int expected) {
    // First column maps to 'input' parameter
    // Second column maps to 'expected' parameter
}
```

If there is one additional column than parameters, TableTest will use the leftmost column as the scenario column. In this situation, mapping to parameters will start from the second column:

```java
@TableTest("""
    Scenario | Input Value | Result?
    Doubles  | 5           | 10
    """)
void test(int input, int expected) {
    // Second column maps to 'input' parameter
    // Third column maps to 'expected' parameter
}
```

See [Scenario Names](#scenario-names) section below for more details.

### Data Rows

Each data row represents one test execution. A table with 3 data rows produces 3 test executions.

```java
@TableTest("""
    Value | Result
    1     | 2
    3     | 6
    5     | 10
    """)
void testDouble(int value, int result) {
    // Executes 3 times:
    // 1. value=1, result=2
    // 2. value=3, result=6
    // 3. value=5, result=10
}
```

### Pipe Delimiters

Pipes (`|`) separate columns. Pipes are only used between columns, not at the start/end:

```
  A | B | C      // three columns ("A", "B", "C")
| A | B | C |    // five columns (null, "A", "B", "C", null)
```

Consistent formatting improves readability. The [IntelliJ plugin](https://plugins.jetbrains.com/plugin/27334-tabletest) and the [Formatter](https://github.com/nchaugen/tabletest-formatter) handle this automatically.


## Scenario Names

TableTest supports providing a description for each row through a *scenario column*. Scenario names help explain what is being tested and appear in test reports.

### Implicit Scenario Column

When the table contains one additional column than parameters, the leftmost column implicitly becomes the scenario column. The header name for this column can be anything you like.

```java
@TableTest("""
    Situation    | Input | Expected
    Small number | 5     | 10
    Large number | 100   | 200
    Zero         | 0     | 0
    """)
void testDouble(int input, int expected) {
    assertEquals(expected, input * 2);
}
```

Scenario names appear in test reports, making it easy to identify problematic cases:
```
✓ ExampleTest
  ✓ testDouble (int, int)
    ✓ [1] Small number
    ✓ [2] Large number
    ✗ [3] Zero
```

### Explicit Scenario Column

If you want another column to be the scenario column, you need to provide a parameter for all columns and annotate it with `@Scenario`:

```java
@TableTest(value = """
    Input | Description     | Expected
    5     | Small number    | 10
    100   | Large number    | 200
    """)
void test(int input, @Scenario String description, int expected) {
    // 'Description' is now the scenario column
}
```

### Scenario Column When Using JUnit-Supplied Parameters

If your test method has additional parameters supplied by JUnit like `TestInfo` or `@TempDir`, you will need to provide a parameter for the scenario column and annotate it with `@Scenario`, even if it is the leftmost one. The TableTest parameters must come first:

```java
@TableTest("""
    Scenario     | Input | Expected
    Small number | 5     | 10
    Large number | 100   | 200
    Zero         | 0     | 0
    """)
void testDouble(@Scenario String scenario, int input, int expected, @TempDir Path tempDir) {
    // 'Scenario' is now the scenario column
}
```

### No Scenario Column

When there is no implicit or explicit scenario column, TableTest will use the column values as a test description.

```java
@TableTest(value = """
    Input | Expected
    5     | 10
    100   | 200
    """)
void testDouble(int input, int expected) {
    // No scenario column
}
```
When running this test:
```
✓ ExampleTest
  ✓ testDouble (int, int)
    ✓ [1] 5, 10
    ✓ [2] 100, 200
```


## Comments and Blank Lines

Use comments and blank lines to organize tables:

```java
@TableTest("""
    Scenario              | Input | Expected

    // Positive numbers
    Small positive        | 5     | 10
    Large positive        | 100   | 200

    // Negative numbers
    Small negative        | -5    | -10
    Large negative        | -100  | -200

    // Edge cases
    Zero                  | 0     | 0
    """)
```

- **Blank lines** – Ignored, use for visual grouping
- **Comments** – Lines starting with `//` are ignored

## Execution Model

TableTests are JUnit parameterized tests under the hood. They follow the standard JUnit execution model.

### One Execution Per Row

Each data row triggers one method invocation:

```java
@TableTest("""
    Scenario | Value
    First    | 1
    Second   | 2
    Third    | 3
    """)
void test(int value) {
    System.out.println("Value: " + value);
}

// Output:
// Value: 1
// Value: 2
// Value: 3
```

### Independent Executions

Each execution is independent. State doesn't carry over between rows:

```java
private int counter = 0;

@TableTest("""
    Scenario | Value
    First    | 1
    Second   | 2
    """)
void test(int value) {
    assertEquals(1, ++counter);  // counter will initialise to 0 for each row
}
```

**Best practice:** Keep tests stateless. Each row should be independently verifiable.

### Test Lifecycle

For each row:

1. TableTest reads the row values
2. Converts values to parameter types
3. Asks JUnit to run the test method with those parameters

JUnit lifecycle methods (`@BeforeEach`, `@AfterEach`) run for each row.

## Simple Values

TableTest automatically converts simple string values to common types:

```java
@TableTest("""
    Scenario | String  | Int | Long | Double | Boolean
    Example  | hello   | 42  | 1000 | 3.14   | true
    """)
void test(String s, int i, long l, double d, boolean b) {
    // Automatic conversion
}
```

Supported primitive types:
- `int`, `long`, `short`, `byte`
- `float`, `double`
- `boolean`
- `char`
- Wrapper types: `Integer`, `Long`, etc.

TableTest supports all the [type converters built-in to JUnit](https://docs.junit.org/current/writing-tests/parameterized-classes-and-tests.html#argument-conversion-implicit). 

### String Values

Strings don't need quotes unless they contain special characters:

```java
@TableTest("""
    Value
    hello
    hello world
    // Quotes needed below - contains pipe
    "hello | world"
    // Quotes needed below - contains single quote
    "'"
    // Quotes needed below - contains double quote
    '"' 
    """)
```

### Null Values

An empty cell will be passed as `null`:

```java
@TableTest("""
    Scenario | Value
    Present  | hello
    Absent   |
    """)
void test(String value) {
    // Row 2: value == null
}
```

**Note:** Primitive types can't be null. Use wrapper types for nullable numbers:

```java
void test(Integer value) {  // Not int - Integer allows null
    // ...
}
```

## Next Steps

Now that you understand the basics, explore the full [User Guide on GitHub](https://github.com/nchaugen/tabletest/blob/main/USERGUIDE.md) to learn about value formats, type conversion, and advanced features.
