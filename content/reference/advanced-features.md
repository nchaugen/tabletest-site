---
title: "Advanced Features"
weight: 3
---

TableTest provides features beyond the basics for expressing comprehensive examples: scenario names for labelling test cases, value sets for testing multiple inputs, comments for organising tables, external files for large datasets, parameter resolvers for dependency injection, and escape sequences for special characters.

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

## Value Sets

Value sets let you test multiple inputs with the same expected outcome, expressed compactly within a single row. When a cell contains a set (enclosed in curly braces) and the corresponding parameter is *not* declared as a `Set` type, TableTest expands it into separate test invocations — one per element.

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
{{< codefile file="examples/src/test/java/guide/AdvancedFeaturesTest.java" id="value-sets" >}}
{{< /tab >}}
{{< tab >}}
{{< codefile file="examples/src/test/kotlin/guide/AdvancedFeaturesKtTest.kt" id="value-sets" >}}
{{< /tab >}}
{{< /tabs >}}

This table produces **12 test executions** — three per row, one for each value in the set. With scenario names, the display name includes both the scenario and the actual value used, making it easy to pinpoint failures.

### Cartesian Product

When multiple columns contain value sets, TableTest generates the Cartesian product — all combinations of values:

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
{{< codefile file="examples/src/test/java/guide/AdvancedFeaturesTest.java" id="cartesian-product" >}}
{{< /tab >}}
{{< tab >}}
{{< codefile file="examples/src/test/kotlin/guide/AdvancedFeaturesKtTest.kt" id="cartesian-product" >}}
{{< /tab >}}
{{< /tabs >}}

Each row above produces 6 test executions (3 x-values times 2 y-values). Use value sets judiciously — the number of test cases grows multiplicatively with each additional set.

### Sets as Parameters

When the parameter *is* declared as a `Set` type, the entire set is passed as a single argument without expansion:

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
{{< codefile file="examples/src/test/java/guide/AdvancedFeaturesTest.java" id="sets-as-parameters" >}}
{{< /tab >}}
{{< tab >}}
{{< codefile file="examples/src/test/kotlin/guide/AdvancedFeaturesKtTest.kt" id="sets-as-parameters" >}}
{{< /tab >}}
{{< /tabs >}}

## Comments and Blank Lines

Lines starting with `//` (ignoring leading whitespace) are treated as comments and ignored. Blank lines are also ignored. Both can be used to organise and annotate your tables.

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
{{< codefile file="examples/src/test/java/guide/AdvancedFeaturesTest.java" id="comments" >}}
{{< /tab >}}
{{< tab >}}
{{< codefile file="examples/src/test/kotlin/guide/AdvancedFeaturesKtTest.kt" id="comments" >}}
{{< /tab >}}
{{< /tabs >}}

## External Table Files

For large datasets, store the table in an external file using the `resource` attribute. The file is located as a resource relative to the test class and is typically stored in the test `resources` directory.

By default, the file is assumed to use UTF-8 encoding. Specify a different encoding with the `encoding` attribute if needed.

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
{{< codefile file="examples/src/test/java/guide/AdvancedFeaturesTest.java" id="external-table" >}}
{{< /tab >}}
{{< tab >}}
{{< codefile file="examples/src/test/kotlin/guide/AdvancedFeaturesKtTest.kt" id="external-table" >}}
{{< /tab >}}
{{< /tabs >}}

A custom encoding can be specified:

```java
@TableTest(resource = "/custom-encoding.table", encoding = "ISO-8859-1")
void testExternalTableWithCustomEncoding(String string, int expectedLength) {
    assertEquals(expectedLength, string.length());
}
```

## Parameter Resolvers

TableTest methods can receive additional arguments provided by JUnit `ParameterResolver` extensions (such as `TestInfo`, `TestReporter`, etc.). These resolver-provided parameters **must be declared last**, after all table columns.

If the table includes a scenario name column and you use parameter resolvers, the scenario column now requires an explicit parameter with the `@Scenario` annotation:

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
{{< codefile file="examples/src/test/java/guide/AdvancedFeaturesTest.java" id="parameter-resolvers" >}}
{{< /tab >}}
{{< tab >}}
{{< codefile file="examples/src/test/kotlin/guide/AdvancedFeaturesKtTest.kt" id="parameter-resolvers" >}}
{{< /tab >}}
{{< /tabs >}}

## Escape Sequences

Escape sequence handling varies depending on the programming language.

### Java Text Blocks

In Java text blocks, all Java escape sequences (`\t`, `\"`, `\\`, `\uXXXX`, etc.) are processed by the Java compiler before TableTest receives the values:

{{< codefile file="examples/src/test/java/guide/AdvancedFeaturesTest.java" id="escape-sequences-java" >}}

### Java String Arrays

The same rules apply to Java string arrays — escape sequences are processed by the compiler:

{{< codefile file="examples/src/test/java/guide/AdvancedFeaturesTest.java" id="escape-sequences-java-array" >}}

### Kotlin Raw Strings

Using Kotlin raw strings, escape sequences are **not** processed. They remain as literal backslash characters:

{{< codefile file="examples/src/test/kotlin/guide/AdvancedFeaturesKtTest.kt" id="escape-sequences-kotlin" >}}

### External Files

Table files are read as raw text independent of the programming language, meaning escape sequences are **not** processed and remain literal.

If you need special characters in Kotlin or external table files, use actual characters instead of escape sequences, or use Kotlin regular strings for simple cases.

## Next Steps

See all these features in action: [Realistic Example](/learn/realistic-example/)
