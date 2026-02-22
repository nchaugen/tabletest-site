---
title: "Features Overview"
weight: 4
---

A quick tour of what TableTest can do beyond the basics. Each section shows a brief example — follow the links to the [User Guide](/docs/guide/) for full details.

## Table Format

Tests are expressed as pipe-delimited tables inside a `@TableTest` annotation. The first row is the header; subsequent rows are test cases. Each data row invokes the test method with cell values as arguments.

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
{{< codefile file="examples/src/test/java/getting_started/FeaturesTest.java" id="table-format" >}}
{{< /tab >}}
{{< tab >}}
{{< codefile file="examples/src/test/kotlin/getting_started/FeaturesKtTest.kt" id="table-format" >}}
{{< /tab >}}
{{< /tabs >}}

See [Basic Usage](/docs/guide/basic-usage/) for a detailed walkthrough of table structure, column mapping, and execution.

## Collections

Tables can contain lists, sets, and maps as cell values. Nested structures are also supported. Values are automatically converted to the parameterised type of the corresponding test parameter.

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
{{< codefile file="examples/src/test/java/getting_started/FeaturesTest.java" id="collections" >}}
{{< /tab >}}
{{< tab >}}
{{< codefile file="examples/src/test/kotlin/getting_started/FeaturesKtTest.kt" id="collections" >}}
{{< /tab >}}
{{< /tabs >}}

Sets use curly braces (`{1, 2, 3}`), maps use square brackets with colons (`[a: 1, b: 2]`), and these can be freely nested — for example `[Alice: [95, 87], Bob: [78, 85]]` for a `Map<String, List<Integer>>`.

## Type Conversion

Cell values are automatically converted to the type of the corresponding test parameter. Built-in conversion covers primitives, strings, dates, enums, and other standard Java types via JUnit's implicit converters.

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
{{< codefile file="examples/src/test/java/getting_started/FeaturesTest.java" id="type-conversion" >}}
{{< /tab >}}
{{< tab >}}
{{< codefile file="examples/src/test/kotlin/getting_started/FeaturesKtTest.kt" id="type-conversion" >}}
{{< /tab >}}
{{< /tabs >}}

For custom domain types, annotate a `public static` method with `@TypeConverter`. TableTest will use it to convert values to the target type:

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
{{< codefile file="examples/src/test/java/getting_started/FeaturesTest.java" id="custom-converter" >}}
{{< /tab >}}
{{< tab >}}
{{< codefile file="examples/src/test/kotlin/getting_started/FeaturesKtTest.kt" id="custom-converter" >}}
{{< /tab >}}
{{< /tabs >}}

Custom converters can be shared across test classes using the `@TypeConverterSources` annotation.

## Value Sets

When a cell uses set syntax (`{...}`) but the corresponding parameter is not a `Set`, TableTest expands it into multiple test invocations — one per value. This is useful for expressing that several inputs share the same expected result.

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
{{< codefile file="examples/src/test/java/getting_started/FeaturesTest.java" id="value-sets" >}}
{{< /tab >}}
{{< tab >}}
{{< codefile file="examples/src/test/kotlin/getting_started/FeaturesKtTest.kt" id="value-sets" >}}
{{< /tab >}}
{{< /tabs >}}

The table above generates 12 test invocations (3 values per row, 4 rows). Multiple value sets in the same row produce a Cartesian product of all combinations.

## Scenario Names

A descriptive first column names each test case and is used as the test display name. This makes test output and failure messages immediately understandable.

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
{{< codefile file="examples/src/test/java/getting_started/FeaturesTest.java" id="scenario-names" >}}
{{< /tab >}}
{{< tab >}}
{{< codefile file="examples/src/test/kotlin/getting_started/FeaturesKtTest.kt" id="scenario-names" >}}
{{< /tab >}}
{{< /tabs >}}

The scenario column is excluded from parameter mapping — there is no need to declare a method parameter for it unless you want to access its value inside the test.

## External Table Files

Large tables can be stored in separate files and loaded with the `resource` attribute. The file is located as a classpath resource relative to the test class.

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
```java
@TableTest(resource = "/test-data/large-dataset.table")
void testExternalTable(int a, int b, int sum) {
    assertEquals(sum, a + b);
}
```
{{< /tab >}}
{{< tab >}}
```kotlin
@TableTest(resource = "/test-data/large-dataset.table")
fun testExternalTable(a: Int, b: Int, sum: Int) {
    assertEquals(sum, a + b)
}
```
{{< /tab >}}
{{< /tabs >}}

This keeps test methods concise and makes it easy to share table data across tests or generate it from external sources.

---

This overview covers the highlights. Dive deeper in the [User Guide](/docs/guide/) — starting with [Value Formats](/docs/guide/value-formats/).
