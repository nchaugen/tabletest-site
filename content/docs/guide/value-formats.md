---
title: "Value Formats"
weight: 2
---

TableTest supports four value formats: single values, lists, sets, and maps. These can be nested to create complex data structures.

## Single Values

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

## Lists

Lists are enclosed in square brackets with comma-separated elements. Lists can contain single values or compound values (nested lists, sets, or maps). Empty lists are represented by `[]`.

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
{{< codefile file="examples/src/test/java/guide/ValueFormatsTest.java" id="lists" >}}
{{< /tab >}}
{{< tab >}}
{{< codefile file="examples/src/test/kotlin/guide/ValueFormatsKtTest.kt" id="lists" >}}
{{< /tab >}}
{{< /tabs >}}

## Sets

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

Curly braces have a dual role in TableTest. When the test parameter is declared as a `Set` type, the value is passed as a single set argument. When the parameter is *not* a `Set`, the values are expanded into separate test invocations — one per element. See [Value Sets](/docs/guide/advanced-features/#value-sets) for details.

{{% /details %}}

## Maps

Maps use square brackets with comma-separated key-value pairs, where colons separate keys and values. Keys must be unquoted single values and cannot contain `,`, `:`, `|`, `[`, `]`, `{`, or `}`. Values can be single (unquoted or quoted) or compound (list, set, or map). Empty maps are represented by `[:]`.

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
{{< codefile file="examples/src/test/java/guide/ValueFormatsTest.java" id="maps" >}}
{{< /tab >}}
{{< tab >}}
{{< codefile file="examples/src/test/kotlin/guide/ValueFormatsKtTest.kt" id="maps" >}}
{{< /tab >}}
{{< /tabs >}}

## Nested Structures

Lists, sets, and maps can be nested to create complex data structures. TableTest converts nested values recursively using generic type information from the test method parameter.

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
{{< codefile file="examples/src/test/java/guide/ValueFormatsTest.java" id="nested" >}}
{{< /tab >}}
{{< tab >}}
{{< codefile file="examples/src/test/kotlin/guide/ValueFormatsKtTest.kt" id="nested" >}}
{{< /tab >}}
{{< /tabs >}}

## Null Values

Blank cells translate to `null` for all parameter types except primitives. For primitives, a blank cell causes an exception as they cannot represent `null`.

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
{{< codefile file="examples/src/test/java/guide/ValueFormatsTest.java" id="null-values" >}}
{{< /tab >}}
{{< tab >}}
{{< codefile file="examples/src/test/kotlin/guide/ValueFormatsKtTest.kt" id="null-values" >}}
{{< /tab >}}
{{< /tabs >}}

There is no `null` keyword — simply leave the cell blank.

## Next Steps

Learn how TableTest converts these values to method parameters: [Type Conversion](/docs/guide/type-conversion/)
