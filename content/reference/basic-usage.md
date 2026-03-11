---
title: "Basic Usage"
weight: 1
---

This guide covers the fundamental concepts of TableTest: table structure, value formats, and execution model.

## Table Structure

A TableTest table is a pipe-delimited text block. Columns are separated by pipes (`|`), and each line is a row. Pipes are only used *between* columns — not at the start or end of a line.

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

### Scenario Column

If there is one additional column than parameters, TableTest will use the leftmost column as the scenario column. Scenario values appear as test display names in reports and are not passed to the method:

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
{{< codefile file="examples/src/test/java/guide/BasicUsageTest.java" id="header-row-scenario" >}}
{{< /tab >}}
{{< tab >}}
{{< codefile file="examples/src/test/kotlin/guide/BasicUsageKtTest.kt" id="header-row-scenario" >}}
{{< /tab >}}
{{< /tabs >}}

See [Scenario Names](/reference/advanced-features/#scenario-names) for explicit scenario columns and other options.


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

Single values are converted to the corresponding parameter type — primitives, strings, enums, dates, and other types supported by [JUnit's built-in converters](https://docs.junit.org/current/writing-tests/parameterized-classes-and-tests.html#argument-conversion-implicit).

Values can appear with or without quotes. Surrounding single (`'`) or double (`"`) quotes are required when the value contains characters that could be confused with table syntax (such as `|`, `[`, or `{`). Whitespace around unquoted values is trimmed. To preserve leading or trailing whitespace, use quotes.

Empty values are represented by adjacent quote pairs (`""` or `''`).

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
{{< codefile file="examples/src/test/java/guide/ValueFormatsTest.java" id="single-values" >}}
{{< /tab >}}
{{< tab >}}
{{< codefile file="examples/src/test/kotlin/guide/ValueFormatsKtTest.kt" id="single-values" >}}
{{< /tab >}}
{{< /tabs >}}

When single values appear as elements inside collections (lists, sets, or maps), collection syntax characters also require quoting.

### Lists

Lists convert to `List` or array parameter types. Lists are enclosed in square brackets with comma-separated elements. Lists can contain single values or compound values (nested lists, sets, or maps). Empty lists are represented by `[]`.

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
{{< codefile file="examples/src/test/java/guide/ValueFormatsTest.java" id="lists" >}}
{{< /tab >}}
{{< tab >}}
{{< codefile file="examples/src/test/kotlin/guide/ValueFormatsKtTest.kt" id="lists" >}}
{{< /tab >}}
{{< /tabs >}}

### Sets

Sets convert to `Set` parameter types. When the parameter is *not* a `Set`, curly braces denote a [value set](/reference/advanced-features/#value-sets) instead — each element becomes a separate test invocation. Sets are enclosed in curly braces with comma-separated elements. Sets can contain single values or compound values. Empty sets are represented by `{}`.

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
{{< codefile file="examples/src/test/java/guide/ValueFormatsTest.java" id="sets" >}}
{{< /tab >}}
{{< tab >}}
{{< codefile file="examples/src/test/kotlin/guide/ValueFormatsKtTest.kt" id="sets" >}}
{{< /tab >}}
{{< /tabs >}}

### Maps

Maps convert to `Map` parameter types. Maps use square brackets with comma-separated key-value pairs, where colons separate keys and values. Keys must be unquoted single values without table or collection syntax characters. Values can be single (unquoted or quoted) or compound (list, set, or map). Empty maps are represented by `[:]`.

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

A blank cell converts to `null` for all object types, including `String`, wrapper types, and object arrays. Primitive types (`int`, `boolean`, etc.) and primitive arrays (`int[]`, etc.) cannot represent `null` — use their wrapper equivalents (`Integer`, `Boolean[]`) when a parameter may be blank.

## Next Steps

- **[Type Conversion](/reference/type-conversion/)** — Built-in and custom `@TypeConverter` methods
- **[Advanced Features](/reference/advanced-features/)** — Scenario names, value sets, external files, parameter resolvers
