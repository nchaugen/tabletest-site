---
title: "Basic Usage"
weight: 1
---

This guide covers the fundamental concepts of TableTest: table structure, value formats, and execution model.

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

See [Scenario Names](/reference/advanced-features/#scenario-names) for more details.

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

## Value Formats

TableTest supports four value formats: single values, lists, sets, and maps. These can be nested to create complex data structures.

### Single Values

Single values can appear with or without quotes. Surrounding single (`'`) or double (`"`) quotes are required when the value contains a `|` character, or starts with `[` or `{`. Whitespace around unquoted values is trimmed. To preserve leading or trailing whitespace, use quotes.

Empty values are represented by adjacent quote pairs (`""` or `''`).

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
{{< codefile file="examples/src/test/java/guide/ValueFormatsTest.java" id="single-values" >}}
{{< /tab >}}
{{< tab >}}
{{< codefile file="examples/src/test/kotlin/guide/ValueFormatsKtTest.kt" id="single-values" >}}
{{< /tab >}}
{{< /tabs >}}

When single values appear as elements inside collections (lists, sets, or maps), the characters `,`, `:`, `]`, and `}` also require quoting.

### Lists

Lists are enclosed in square brackets with comma-separated elements. Lists can contain single values or compound values (nested lists, sets, or maps). Empty lists are represented by `[]`.

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
{{< codefile file="examples/src/test/java/guide/ValueFormatsTest.java" id="lists" >}}
{{< /tab >}}
{{< tab >}}
{{< codefile file="examples/src/test/kotlin/guide/ValueFormatsKtTest.kt" id="lists" >}}
{{< /tab >}}
{{< /tabs >}}

### Sets

Sets are enclosed in curly braces with comma-separated elements. Sets can contain single values or compound values. Empty sets are represented by `{}`.

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
{{< codefile file="examples/src/test/java/guide/ValueFormatsTest.java" id="sets" >}}
{{< /tab >}}
{{< tab >}}
{{< codefile file="examples/src/test/kotlin/guide/ValueFormatsKtTest.kt" id="sets" >}}
{{< /tab >}}
{{< /tabs >}}

{{% details title="Sets vs Value Sets" closed="true" %}}

Curly braces have a dual role in TableTest. When the test parameter is declared as a `Set` type, the value is passed as a single set argument. When the parameter is *not* a `Set`, the values are expanded into separate test invocations — one per element. See [Value Sets](/reference/advanced-features/#value-sets) for details.

{{% /details %}}

### Maps

Maps use square brackets with comma-separated key-value pairs, where colons separate keys and values. Keys must be unquoted single values and cannot contain `,`, `:`, `|`, `[`, `]`, `{`, or `}`. Values can be single (unquoted or quoted) or compound (list, set, or map). Empty maps are represented by `[:]`.

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
{{< codefile file="examples/src/test/java/guide/ValueFormatsTest.java" id="maps" >}}
{{< /tab >}}
{{< tab >}}
{{< codefile file="examples/src/test/kotlin/guide/ValueFormatsKtTest.kt" id="maps" >}}
{{< /tab >}}
{{< /tabs >}}

### Nested Structures

Lists, sets, and maps can be nested to create complex data structures. TableTest converts nested values recursively using generic type information from the test method parameter.

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
{{< codefile file="examples/src/test/java/guide/ValueFormatsTest.java" id="nested" >}}
{{< /tab >}}
{{< tab >}}
{{< codefile file="examples/src/test/kotlin/guide/ValueFormatsKtTest.kt" id="nested" >}}
{{< /tab >}}
{{< /tabs >}}

### Null Values

A blank cell represents the absence of a value — there is no `null` keyword. Simply leave the cell empty.

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
{{< codefile file="examples/src/test/java/guide/ValueFormatsTest.java" id="null-values" >}}
{{< /tab >}}
{{< tab >}}
{{< codefile file="examples/src/test/kotlin/guide/ValueFormatsKtTest.kt" id="null-values" >}}
{{< /tab >}}
{{< /tabs >}}

See [Blank Cells](/reference/type-conversion/#blank-cells) for how blank cells are converted to different parameter types.

## Next Steps

- **[Type Conversion](/reference/type-conversion/)** — Built-in and custom `@TypeConverter` methods
- **[Advanced Features](/reference/advanced-features/)** — Scenario names, value sets, external files, parameter resolvers
