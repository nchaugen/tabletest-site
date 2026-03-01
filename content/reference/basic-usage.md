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

```
@TableTest("""
    Column1 | Column2 | Column3
    value1  | value2  | value3
    value4  | value5  | value6
    """)
```

### Header Row

The first row defines column names. Columns map to method parameters by order, so you can use readable header names with whitespace and punctuation:

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
{{< codefile file="examples/src/test/java/guide/BasicUsageTest.java" id="header-row" >}}
{{< /tab >}}
{{< tab >}}
{{< codefile file="examples/src/test/kotlin/guide/BasicUsageKtTest.kt" id="header-row" >}}
{{< /tab >}}
{{< /tabs >}}

If there is one additional column than parameters, TableTest will use the leftmost column as the scenario column. In this situation, mapping to parameters will start from the second column:

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
{{< codefile file="examples/src/test/java/guide/BasicUsageTest.java" id="header-row-scenario" >}}
{{< /tab >}}
{{< tab >}}
{{< codefile file="examples/src/test/kotlin/guide/BasicUsageKtTest.kt" id="header-row-scenario" >}}
{{< /tab >}}
{{< /tabs >}}

See [Scenario Names](#scenario-names) section below for more details.

### Data Rows

Each data row represents one test execution. A table with 3 data rows produces 3 test executions.

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
{{< codefile file="examples/src/test/java/guide/BasicUsageTest.java" id="data-rows" >}}
{{< /tab >}}
{{< tab >}}
{{< codefile file="examples/src/test/kotlin/guide/BasicUsageKtTest.kt" id="data-rows" >}}
{{< /tab >}}
{{< /tabs >}}

### Pipe Delimiters

Pipes (`|`) separate columns. Pipes are only used between columns, not at the start/end:

```
  A | B | C      // three columns ("A", "B", "C")
| A | B | C |    // five columns (null, "A", "B", "C", null)
```

Consistent formatting improves readability. The [IntelliJ plugin](https://plugins.jetbrains.com/plugin/27334-tabletest), [VS Code extension](https://marketplace.visualstudio.com/items?itemName=tabletest.tabletest), and [Formatter](https://github.com/nchaugen/tabletest-formatter) handle this automatically.


## Scenario Names

TableTest supports providing a description for each row through a *scenario column*. Scenario names help explain what is being tested and appear in test reports.

### Implicit Scenario Column

When the table contains one additional column than parameters, the leftmost column implicitly becomes the scenario column. The header name for this column can be anything you like.

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
{{< codefile file="examples/src/test/java/guide/BasicUsageTest.java" id="implicit-scenario" >}}
{{< /tab >}}
{{< tab >}}
{{< codefile file="examples/src/test/kotlin/guide/BasicUsageKtTest.kt" id="implicit-scenario" >}}
{{< /tab >}}
{{< /tabs >}}

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

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
{{< codefile file="examples/src/test/java/guide/BasicUsageTest.java" id="explicit-scenario" >}}
{{< /tab >}}
{{< tab >}}
{{< codefile file="examples/src/test/kotlin/guide/BasicUsageKtTest.kt" id="explicit-scenario" >}}
{{< /tab >}}
{{< /tabs >}}

### Scenario Column When Using JUnit-Supplied Parameters

If your test method has additional parameters supplied by JUnit like `TestInfo` or `@TempDir`, you will need to provide a parameter for the scenario column and annotate it with `@Scenario`, even if it is the leftmost one. The TableTest parameters must come first:

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
{{< codefile file="examples/src/test/java/guide/BasicUsageTest.java" id="junit-params" >}}
{{< /tab >}}
{{< tab >}}
{{< codefile file="examples/src/test/kotlin/guide/BasicUsageKtTest.kt" id="junit-params" >}}
{{< /tab >}}
{{< /tabs >}}

### No Scenario Column

When there is no implicit or explicit scenario column, TableTest will use the column values as a test description.

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
{{< codefile file="examples/src/test/java/guide/BasicUsageTest.java" id="no-scenario" >}}
{{< /tab >}}
{{< tab >}}
{{< codefile file="examples/src/test/kotlin/guide/BasicUsageKtTest.kt" id="no-scenario" >}}
{{< /tab >}}
{{< /tabs >}}

When running this test:
```
✓ ExampleTest
  ✓ testDouble (int, int)
    ✓ [1] 5, 10
    ✓ [2] 100, 200
```


## Comments and Blank Lines

Use comments and blank lines to organize tables:

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
{{< codefile file="examples/src/test/java/guide/BasicUsageTest.java" id="comments-blank-lines" >}}
{{< /tab >}}
{{< tab >}}
{{< codefile file="examples/src/test/kotlin/guide/BasicUsageKtTest.kt" id="comments-blank-lines" >}}
{{< /tab >}}
{{< /tabs >}}

- **Blank lines** – Ignored, use for visual grouping
- **Comments** – Lines starting with `//` are ignored

## Execution Model

TableTests are JUnit parameterized tests under the hood. They follow the standard JUnit execution model.

### One Execution Per Row

Each data row triggers one method invocation:

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
{{< codefile file="examples/src/test/java/guide/BasicUsageTest.java" id="one-execution-per-row" >}}
{{< /tab >}}
{{< tab >}}
{{< codefile file="examples/src/test/kotlin/guide/BasicUsageKtTest.kt" id="one-execution-per-row" >}}
{{< /tab >}}
{{< /tabs >}}

### Independent Executions

Each execution is independent. State doesn't carry over between rows:

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
{{< codefile file="examples/src/test/java/guide/BasicUsageTest.java" id="independent-executions" >}}
{{< /tab >}}
{{< tab >}}
{{< codefile file="examples/src/test/kotlin/guide/BasicUsageKtTest.kt" id="independent-executions" >}}
{{< /tab >}}
{{< /tabs >}}

**Best practice:** Keep tests stateless. Each row should be independently verifiable.

### Test Lifecycle

For each row:

1. TableTest reads the row values
2. Converts values to parameter types
3. Asks JUnit to run the test method with those parameters

JUnit lifecycle methods (`@BeforeEach`, `@AfterEach`) run for each row.

## Simple Values

TableTest automatically converts simple string values to common types:

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
{{< codefile file="examples/src/test/java/guide/BasicUsageTest.java" id="simple-values" >}}
{{< /tab >}}
{{< tab >}}
{{< codefile file="examples/src/test/kotlin/guide/BasicUsageKtTest.kt" id="simple-values" >}}
{{< /tab >}}
{{< /tabs >}}

Supported primitive types:
- `int`, `long`, `short`, `byte`
- `float`, `double`
- `boolean`
- `char`
- Wrapper types: `Integer`, `Long`, etc.

TableTest supports all the [type converters built-in to JUnit](https://docs.junit.org/current/writing-tests/parameterized-classes-and-tests.html#argument-conversion-implicit).

### String Values

Strings don't need quotes unless they contain special characters:

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
{{< codefile file="examples/src/test/java/guide/BasicUsageTest.java" id="string-values" >}}
{{< /tab >}}
{{< tab >}}
{{< codefile file="examples/src/test/kotlin/guide/BasicUsageKtTest.kt" id="string-values" >}}
{{< /tab >}}
{{< /tabs >}}

### Null Values

An empty cell will be passed as `null`:

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
{{< codefile file="examples/src/test/java/guide/BasicUsageTest.java" id="null-values" >}}
{{< /tab >}}
{{< tab >}}
{{< codefile file="examples/src/test/kotlin/guide/BasicUsageKtTest.kt" id="null-values" >}}
{{< /tab >}}
{{< /tabs >}}

**Note:** Primitive Java types can't be null. Use wrapper types for nullable numbers:

```java
void test(Integer value) {  // Not int - Integer allows null
    // ...
}
```

## Next Steps

Now that you understand the basics:

- **[Value Formats](/reference/value-formats/)** — Lists, sets, maps, nested structures, and quoting rules
- **[Type Conversion](/reference/type-conversion/)** — Built-in and custom `@TypeConverter` methods
- **[Advanced Features](/reference/advanced-features/)** — Value sets, external files, parameter resolvers
