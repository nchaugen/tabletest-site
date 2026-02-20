---
title: "@TableTest Annotation"
weight: 1
draft: true
---

# @TableTest Annotation Reference

Complete specification of the `@TableTest` annotation.

## Basic Usage

```java
@TableTest("""
    Column1 | Column2
    value1  | value2
    """)
void testMethod(String column1, String column2) {
    // Test implementation
}
```

## Attributes

### value

**Type:** `String`
**Required:** Yes (unless `resource` is specified)
**Description:** The table content as an inline string

```java
@TableTest("""
    Input | Expected
    1     | 2
    3     | 6
    """)
```

Use Java text blocks (`"""`) or regular strings with escape sequences.

### resource

**Type:** `String`
**Required:** No (mutually exclusive with `value`)
**Description:** Classpath resource containing table data

```java
@TableTest(resource = "/test-data/inputs.txt")
```

File must be in `src/test/resources` and use the same pipe-delimited format.

**Note:** Cannot specify both `value` and `resource`.

### scenarioColumn

**Type:** `String`
**Required:** No
**Default:** First column (leftmost)
**Description:** Which column provides test scenario descriptions

```java
@TableTest(value = """
    Input | Description  | Expected
    1     | Small number | 2
    100   | Large number | 200
    """,
    scenarioColumn = "Description")
```

**Special value:** Empty string (`""`) disables scenario column:

```java
@TableTest(value = """
    Input | Expected
    1     | 2
    """,
    scenarioColumn = "")  // No scenario descriptions
```

### trimValues

**Type:** `boolean`
**Required:** No
**Default:** `true`
**Description:** Whether to trim whitespace from cell values

```java
@TableTest(value = """
    Input
      hello
    """,
    trimValues = true)   // "hello" (trimmed)

@TableTest(value = """
    Input
      hello
    """,
    trimValues = false)  // "  hello" (preserved)
```

## Annotation Target

`@TableTest` can only be applied to **methods**:

```java
@TableTest("""...""")
void testMethod() { }  // Valid

@TableTest("""...""")  // Invalid - not on method
class TestClass { }
```

## Retention and Inheritance

**Retention:** `RUNTIME` - Available via reflection during test execution

**Inheritance:** Not inherited - subclasses must re-declare `@TableTest`

## Compatibility with Other Annotations

### @DisplayName

Customize test class or method display name:

```java
@DisplayName("Discount Calculation Tests")
@TableTest("""
    Purchases | Discount
    5         | 10%
    """)
void testDiscount(int purchases, String discount) {
    // Appears as "Discount Calculation Tests[5]"
}
```

### @Tag

Add tags for selective test execution:

```java
@Tag("integration")
@TableTest("""
    Input | Expected
    1     | 2
    """)
void testIntegration(int input, int expected) {
    // Can run with --tags=integration
}
```

### @Disabled

Disable test temporarily:

```java
@Disabled("Under investigation")
@TableTest("""
    Input | Expected
    1     | 2
    """)
void testDisabled(int input, int expected) {
    // Won't execute
}
```

### @ExtendWith

Register JUnit extensions:

```java
@ExtendWith(DatabaseExtension.class)
class MyTest {
    @TableTest("""
        Query | Count
        ...   | 5
        """)
    void test(String query, int count, Database db) {
        // db injected by DatabaseExtension
    }
}
```

### @ParameterizedTest

**Not compatible** - `@TableTest` replaces `@ParameterizedTest`:

```java
// Wrong - don't combine
@ParameterizedTest
@TableTest("""...""")
void test() { }

// Correct - use TableTest alone
@TableTest("""...""")
void test() { }
```

## Method Requirements

### Access Modifier

Test methods must be non-private:

```java
@TableTest("""...""")
public void test() { }     // Valid

@TableTest("""...""")
void test() { }            // Valid (package-private)

@TableTest("""...""")
private void test() { }    // Invalid - won't execute
```

### Return Type

Test methods should return `void`:

```java
@TableTest("""...""")
void test() { }            // Valid

@TableTest("""...""")
int test() { return 0; }   // Technically valid, but discouraged
```

### Parameters

Method parameters must match table columns (except injected parameters):

```java
@TableTest("""
    Column1 | Column2
    value1  | value2
    """)
void test(String column1,    // Matches Column1
          String column2) {  // Matches Column2
    // Valid
}

@TableTest("""
    Column1
    value1
    """)
void test(String column1,
          String column2) {  // No matching column - ERROR
    // Invalid
}
```

**Exception:** JUnit-injected parameters (TestInfo, etc.) don't need matching columns.

### Static Methods

`@TableTest` doesn't work on static methods:

```java
@TableTest("""...""")
static void test() { }  // Won't execute
```

## Examples

### Minimal

```java
@TableTest("""
    Input
    value
    """)
void test(String input) { }
```

### With Custom Scenario Column

```java
@TableTest(value = """
    ID | Description       | Value
    1  | First test case   | 100
    2  | Second test case  | 200
    """,
    scenarioColumn = "Description")
void test(int id, int value) { }
```

### With External File

```java
@TableTest(resource = "/data/test-cases.txt")
void test(String input, String expected) { }
```

### With Multiple Annotations

```java
@DisplayName("Integration Tests")
@Tag("integration")
@ExtendWith(SpringExtension.class)
@TableTest("""
    Input | Expected
    1     | 2
    """)
void test(String input, String expected) { }
```

### No Scenario Column

```java
@TableTest(value = """
    Input | Expected
    1     | 2
    5     | 10
    """,
    scenarioColumn = "")
void test(int input, int expected) {
    // Scenarios appear as: test[1], test[2]
}
```

## Execution Model

For each row in the table:

1. TableTest parses the row
2. Converts values to parameter types
3. Invokes the test method with those parameters
4. Reports pass/fail for that scenario

JUnit lifecycle hooks (`@BeforeEach`, `@AfterEach`) execute for each row.

## Error Conditions

**No matching column:**
```
Method parameter 'foo' has no matching table column
```

**Both value and resource:**
```
Cannot specify both 'value' and 'resource' attributes
```

**Invalid table syntax:**
```
Malformed table at row 3: expected 4 columns, found 3
```

**Conversion failure:**
```
Could not convert 'abc' to type int
```

## See Also

- [Value Syntax](/docs/reference/value-syntax/) - Table format specifications
- [Conversion Rules](/docs/reference/conversion-rules/) - Type conversion details
- [User Guide](/docs/guide/) - Practical usage examples
