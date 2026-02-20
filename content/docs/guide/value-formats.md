---
title: "Value Formats"
weight: 2
---

TableTest supports four value formats: single values, lists, sets, and maps. These can be nested to create complex data structures.

## Single Values

Single values can appear with or without quotes. Surrounding single (`'`) or double (`"`) quotes are required when the value contains a `|` character, or starts with `[` or `{`. Whitespace around unquoted values is trimmed. To preserve leading or trailing whitespace, use quotes.

Empty values are represented by adjacent quote pairs (`""` or `''`).

```java
@TableTest("""
    Value                  | Length?
    Hello, world!          | 13
    "cat file.txt | wc -l" | 20
    "[]"                   | 2
    ''                     | 0
    """)
void testString(String value, int expectedLength) {
    assertEquals(expectedLength, value.length());
}
```

When single values appear as elements inside collections (lists, sets, or maps), the characters `,`, `:`, `]`, and `}` also require quoting.

## Lists

Lists are enclosed in square brackets with comma-separated elements. Lists can contain single values or compound values (nested lists, sets, or maps). Empty lists are represented by `[]`.

```java
@TableTest("""
    List      | Size? | Sum?
    []        | 0     | 0
    [1]       | 1     | 1
    [3, 2, 1] | 3     | 6
    """)
void integerList(List<Integer> list, int expectedSize, int expectedSum) {
    assertEquals(expectedSize, list.size());
    assertEquals(expectedSum, list.stream().mapToInt(Integer::intValue).sum());
}
```

## Sets

Sets are enclosed in curly braces with comma-separated elements. Sets can contain single values or compound values. Empty sets are represented by `{}`.

```java
@TableTest("""
    Set              | Size?
    {1, 2, 3, 2, 1}  | 3
    {Hello, Hello}   | 1
    {}               | 0
    """)
void testSet(Set<String> set, int expectedSize) {
    assertEquals(expectedSize, set.size());
}
```

{{% details title="Sets vs Value Sets" closed="true" %}}

Curly braces have a dual role in TableTest. When the test parameter is declared as a `Set` type, the value is passed as a single set argument. When the parameter is *not* a `Set`, the values are expanded into separate test invocations — one per element. See [Value Sets](/docs/guide/advanced-features/#value-sets) for details.

{{% /details %}}

## Maps

Maps use square brackets with comma-separated key-value pairs, where colons separate keys and values. Keys must be unquoted single values and cannot contain `,`, `:`, `|`, `[`, `]`, `{`, or `}`. Values can be single (unquoted or quoted) or compound (list, set, or map). Empty maps are represented by `[:]`.

```java
@TableTest("""
    Map                        | Size?
    [one: 1, two: 2, three: 3] | 3
    [:]                        | 0
    """)
void testMap(Map<String, Integer> map, int expectedSize) {
    assertEquals(expectedSize, map.size());
}
```

## Nested Structures

Lists, sets, and maps can be nested to create complex data structures. TableTest converts nested values recursively using generic type information from the test method parameter.

```java
@TableTest("""
    Student grades                                                  | Highest grade? | Average grade? | Pass count?
    [Alice: [95, 87, 92], Bob: [78, 85, 90], Charlie: [98, 89, 91]] | 98             | 89.4           | 3
    [David: [45, 60, 70], Emma: [65, 70, 75], Frank: [82, 78, 60]]  | 82             | 67.2           | 2
    [:]                                                             | 0              | 0.0            | 0
    """)
void testNestedParameterizedTypes(
    Map<String, List<Integer>> studentGrades,
    int expectedHighestGrade,
    double expectedAverageGrade,
    int expectedPassCount
) {
    Students students = fromGradesMap(studentGrades);
    assertEquals(expectedHighestGrade, students.highestGrade());
    assertEquals(expectedAverageGrade, students.averageGrade(), 0.1);
    assertEquals(expectedPassCount, students.passCount());
}
```

## Null Values

Blank cells translate to `null` for all parameter types except primitives. For primitives, a blank cell causes an exception as they cannot represent `null`.

```java
@TableTest("""
    String | Integer | List | Map | Set
           |         |      |     |
    """)
void blankConvertsToNull(String string, Integer integer, List<?> list,
                         Map<String, ?> map, Set<?> set) {
    assertNull(string);
    assertNull(integer);
    assertNull(list);
    assertNull(map);
    assertNull(set);
}
```

There is no `null` keyword — simply leave the cell blank.

## Next Steps

Learn how TableTest converts these values to method parameters: [Type Conversion](/docs/guide/type-conversion/)
