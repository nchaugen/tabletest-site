---
title: "Common Mistakes"
weight: 6
---

### Missing Column

```java
@TableTest("""
    Scenario | A | B
    Test     | 2 | 3
    """)
public void test(int a, int b, int sum) {  // 'sum' has no matching column!
    // ...
}
```
This will make TableTest attempt to use the Scenario column as the value of the first parameter, then failing to convert the string `"Test"` to an int, causing a `TableTestException` at runtime.

TableTest will only leave the first column unmapped if there is one additional column than parameters.

**Fix:** Ensure every parameter (except injected ones like `TestInfo`) has a matching column.

### Wrong Type

```java
@TableTest("""
    Number
    abc
    """)
public void test(int number) {  // Can't convert "abc" to int!
    // ...
}
```
Similar to the example above, TableTest will fail to convert the string `"abc"` to an int, causing a `TableTestException` at runtime.

**Fix:** Ensure values can be converted to parameter types, or provide a custom factory method.

### Misaligned Columns

```java
@TableTest("""
    A | B | Sum
    2 |     3 | 5
    """)
```
This is not a mistake. TableTest doesn't mind and will parse the table correctly.

**Fix:** To make it easier to read, the IntelliJ plugin (and TableTest Formatter) helps maintain alignment.

