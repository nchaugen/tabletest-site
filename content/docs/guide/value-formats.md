---
title: "Value Formats"
weight: 2
draft: true
---

TableTest supports four value formats: single values, lists, sets, and maps. These can be nested to create complex data structures.

## Single Values

Single values are written directly without delimiters:

```java
@TableTest("""
    String | Integer | Boolean
    hello  | 42      | true
    world  | -10     | false
    """)
void test(String s, int i, boolean b) {
    // Simple value conversion
}
```

See [Basic Usage](/docs/guide/basic-usage/) for details on simple value conversion.

## Lists

Lists use square bracket syntax: `[item1, item2, item3]`

```java
@TableTest("""
    Numbers   | Sum?
    [1, 2, 3] | 6
    [10, 20]  | 30
    []        | 0
    """)
void testSum(List<Integer> numbers, int sum) {
    int actual = numbers.stream().mapToInt(Integer::intValue).sum();
    assertEquals(sum, actual);
}
```

### List Features

**Empty lists:**
```
[]
```

**Nested lists:**
```
[[1, 2], [3, 4], [5, 6]]
```

**Whitespace tolerance:**
```
[1,2,3]        // Valid
[1, 2, 3]      // Valid (more readable)
[ 1 , 2 , 3]   // Valid
```

**Null elements disallowed:**
```
[1,,2]   // Parse error
```

### List Parameter Types

TableTest infers element types from generic parameters:

```java
void test(List<Integer> numbers) {
    // Elements converted to Integer
}

void test(List<String> words) {
    // Elements remain as String
}

void test(List<LocalDate> dates) {
    // Elements converted to LocalDate
}
```

## Sets

Sets use curly brace syntax: `{item1, item2, item3}`

```java
@TableTest("""
    Unique Numbers | Count?
    {1, 2, 3}      | 3
    {1, 1, 2}      | 2
    {5}            | 1
    {}             | 0
    """)
void testUniqueCount(Set<Integer> numbers, int count) {
    assertEquals(count, numbers.size());
}
```

### Set Features

**Duplicate removal:**
```
{1, 2, 2, 3}  // Becomes {1, 2, 3}
```

**Empty sets:**
```
{}
```

**Ordered:**
TableTest uses ordered sets for consistent execution and reporting.

## Maps

Maps use key-value pair syntax: `[key1: value1, key2: value2]`

```java
@TableTest("""
    Prices                      | Total?
    [apple: 1.50, banana: 0.75] | 2.25
    [coffee: 3.00]              | 3.00
    []                          | 0.00
    """)
void testTotal(Map<String, Double> prices, double total) {
    double sum = prices.values().stream().mapToDouble(Double::doubleValue).sum();
    assertEquals(total, sum, 0.01);
}
```

### Map Features

**Empty maps:**
```
[:]
```

**Type inference:**
```java
void test(Map<String, Integer> map) {
    // Keys: String, Values: Integer
}

void test(Map<Integer, List<String>> map) {
    // Keys: Integer, Values: List<String>
}
```

**Whitespace tolerance (all valid):**
```
[a:1,b:2]          // Valid
[a: 1, b: 2]       // Valid (more readable)
[ a : 1 , b : 2 ]  // Valid
```

### Map Keys and Values

**String keys** (no quotes allowed):
```java
[apple: 1, banana: 2]
```

**Complex values:**
```java
[alice: [95, 87, 92], bob: [78, 85, 90]]  // Map<String, List<Integer>>
```

## Nested Structures

Combine formats to create complex data:

### List of Lists

```java
@TableTest("""
    Matrix        | Sum?
    [[1, 2], [3]] | 6
    [[5], [6, 7]] | 18
    """)
void test(List<List<Integer>> matrix, int sum) {
    int actual = matrix.stream()
        .flatMap(List::stream)
        .mapToInt(Integer::intValue)
        .sum();
    assertEquals(sum, actual);
}
```

### Map of Lists

```java
@TableTest("""
    Student Grades                           | Highest?
    [Alice: [95, 87, 92], Bob: [78, 85, 90]] | 95
    [Charlie: [98, 89, 91], David: [70, 80]] | 98
    """)
void test(Map<String, List<Integer>> grades, int highest) {
    int max = grades.values().stream()
        .flatMap(List::stream)
        .mapToInt(Integer::intValue)
        .max()
        .orElse(0);
    assertEquals(highest, max);
}
```

### List of Maps

```java
@TableTest("""
    Transactions                                            | Total?
    [[item: apple, price: 1.50]]                            | 1.50
    [[item: coffee, price: 3.00], [item: tea, price: 2.50]] | 5.50
    """)
void test(List<Map<String, Object>> transactions, double total) {
    double sum = transactions.stream()
        .mapToDouble(t -> (Double) t.get("price"))
        .sum();
    assertEquals(total, sum, 0.01);
}
```

## Quoting and Special Characters

### When to Quote

Quote values that contain special characters:

```java
@TableTest("""
    Value
    "hello, world"    // Contains comma
    "key: value"      // Contains colon
    "item | other"    // Contains pipe
    "text with spaces"// Spaces are OK unquoted, but quotes don't hurt
    """)
```

### Quote Types

Both single and double quotes work:

```java
'hello'    // Valid
"hello"    // Valid
```

### Escaping Quotes

Escape quotes within quoted strings:

**Java:**
```java
"say \"hello\""     // say "hello"
```

**Kotlin:**
```kotlin
"""say "hello""""   // say "hello"  (raw strings, no escaping needed)
```

## Empty Values

Different contexts interpret empty values differently:

```java
@TableTest("""
    List  | Set | Map | String
    []    | {}  | []  | ""
    """)
void test(List<Integer> list, Set<Integer> set,
          Map<String, Integer> map, String str) {
    // All empty/empty string
}
```

Context (parameter type) determines interpretation.

## Null Values

Use `null` keyword or empty cell:

```java
@TableTest("""
    Scenario | Value
    Present  | hello
    Absent   | null
    Empty    |
    """)
void test(String value) {
    // Row 2 and 3: value == null
}
```

**Collections:** `null` means null reference, not empty collection:

```java
@TableTest("""
    List  | Is Null?
    []    | false      // Empty list, not null
    null  | true       // Null reference
    """)
void test(List<Integer> list, boolean isNull) {
    assertEquals(isNull, list == null);
}
```

## Type Inference

TableTest infers types from generic parameters:

```java
// Method signature determines conversion:
void test(List<Integer> numbers) {
    // [1, 2, 3] → List<Integer>
}

void test(List<String> words) {
    // [hello, world] → List<String>
}

void test(Map<String, List<Integer>> data) {
    // [a: [1, 2], b: [3, 4]] → Map<String, List<Integer>>
}
```

If conversion fails (e.g., `"abc"` → `Integer`), test execution fails with a clear error message.

## Common Patterns

### Testing Multiple Cases

```java
@TableTest("""
    Scenario        | Input         | Expected
    Empty list      | []            | 0
    Single item     | [5]           | 5
    Multiple items  | [1, 2, 3, 4]  | 10
    """)
void testSum(List<Integer> input, int expected) {
    assertEquals(expected, input.stream().mapToInt(i -> i).sum());
}
```

### Configuration Maps

```java
@TableTest("""
    Config                                  | Valid?
    [debug: true, timeout: 30]             | true
    [debug: false]                         | true
    [invalid_key: value]                   | false
    """)
void testConfig(Map<String, String> config, boolean valid) {
    // Validate configuration
}
```

### Categorized Data

```java
@TableTest("""
    Category Counts           | Total
    [electronics: 10, books: 5] | 15
    [food: 20, drinks: 15]      | 35
    """)
void testInventory(Map<String, Integer> counts, int total) {
    assertEquals(total, counts.values().stream().mapToInt(i -> i).sum());
}
```

## Next Steps

Learn how TableTest converts these values to method parameters: [Type Conversion →](/docs/guide/type-conversion/)
