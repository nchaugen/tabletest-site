---
title: "Conversion Rules"
weight: 3
draft: true
---

# Type Conversion Rules Reference

Complete specification of how TableTest converts string values to method parameter types.

## Conversion Strategy

TableTest uses a two-tier conversion approach:

1. **Factory methods** (custom conversion)
2. **Built-in converters** (JUnit implicit converters + TableTest extensions)

Factory methods take priority. If no factory method succeeds, built-in converters are tried.

## Factory Methods

### Definition

A factory method is a public static method that converts a value to the target type.

### Requirements

1. **Public** - Must be accessible
2. **Static** - No instance required
3. **Single parameter** - Takes one argument
4. **Returns target type** - Produces the parameter type

### Signature

```java
public static TargetType methodName(SourceType input) {
    return /* conversion logic */;
}
```

### Discovery Order

TableTest searches for factory methods in this order:

1. **Test class** - Methods in the test class itself
2. **@FactorySources classes** - Classes specified in `@FactorySources` annotation
3. **Target type** - Static methods in the target type class

### Naming Conventions

Common factory method names (all recognized):

- `parse<Type>` - e.g., `parseDiscount`
- `from<Type>` - e.g., `fromString`
- `valueOf` - Standard Java convention
- `of` - Short form (e.g., `Optional.of`)

TableTest doesn't require specific names - any public static method matching the signature works.

### Example

```java
public class MyTest {
    // Factory method in test class
    public static Discount parseDiscount(String input) {
        return new Discount(Integer.parseInt(input.replace("%", "")));
    }

    @TableTest("""
        Purchases | Discount
        5         | 10%
        """)
    void test(int purchases, Discount discount) {
        // "10%" converted via parseDiscount
    }
}
```

### Factory Parameter Types

Factory methods can accept any convertible type:

```java
// String input (most common)
public static Temperature fromCelsius(String input) { }

// Numeric input
public static Temperature fromCelsius(double input) { }

// Custom type input
public static AdvancedDiscount from(Discount discount) { }
```

TableTest converts the source value to the factory parameter type first, then invokes the factory.

## Built-in Converters

### Primitives

**Supported types:**
- `int` / `Integer`
- `long` / `Long`
- `short` / `Short`
- `byte` / `Byte`
- `float` / `Float`
- `double` / `Double`
- `boolean` / `Boolean`
- `char` / `Character`

**Rules:**
- Numeric strings → numbers: `"42"` → `42`
- Boolean strings → boolean: `"true"` → `true`, `"false"` → `false`
- Single character → char: `"A"` → `'A'`

**Examples:**
```
42       → int
3.14     → double
true     → boolean
A        → char
```

### Strings

**Rule:** Direct assignment, no conversion

```
hello    → "hello"
""       → "" (empty string)
```

### Enums

**Rule:** Match by enum constant name (case-sensitive)

```java
enum Status { PENDING, ACTIVE, CLOSED }

"PENDING"  → Status.PENDING
"ACTIVE"   → Status.ACTIVE
```

**Error if no match:**
```
"INVALID"  → IllegalArgumentException
```

### Temporal Types

**Supported types:**
- `LocalDate`
- `LocalTime`
- `LocalDateTime`
- `Instant`
- `ZonedDateTime`
- `OffsetDateTime`
- `Duration`
- `Period`

**Format:** ISO-8601

**Examples:**
```
2024-01-15          → LocalDate
14:30:00            → LocalTime
2024-01-15T14:30:00 → LocalDateTime
PT2H30M             → Duration
P1Y2M3D             → Period
```

### File System

**Supported types:**
- `File`
- `Path`

**Rule:** String → file path

```
/path/to/file  → Path.of("/path/to/file")
```

### URIs and URLs

**Supported types:**
- `URI`
- `URL`

**Rule:** String → URI/URL

```
https://example.com  → URI / URL
```

### UUID

**Rule:** String → UUID

```
550e8400-e29b-41d4-a716-446655440000  → UUID
```

### Class

**Rule:** Fully qualified class name → Class object

```
java.lang.String  → String.class
```

## Collection Conversion

### Lists

**Parameter type:** `List<T>`

**Rule:** Parse list syntax `[e1, e2, e3]`, convert each element to type `T`

**Examples:**
```java
void test(List<Integer> numbers) {
    // [1, 2, 3] → List.of(1, 2, 3)
}

void test(List<String> words) {
    // [hello, world] → List.of("hello", "world")
}
```

### Sets

**Parameter type:** `Set<T>`

**Rule:** Parse set syntax `{e1, e2, e3}`, convert each element to type `T`, remove duplicates

**Examples:**
```java
void test(Set<Integer> numbers) {
    // {1, 2, 3} → Set.of(1, 2, 3)
}

void test(Set<String> words) {
    // {hello, world} → Set.of("hello", "world")
}
```

### Maps

**Parameter type:** `Map<K, V>`

**Rule:** Parse map syntax `[k1: v1, k2: v2]`, convert keys to type `K`, values to type `V`

**Examples:**
```java
void test(Map<String, Integer> counts) {
    // [a: 1, b: 2] → Map.of("a", 1, "b", 2)
}

void test(Map<Integer, String> labels) {
    // [1: first, 2: second] → Map.of(1, "first", 2, "second")
}
```

## Nested Conversion

Conversion applies recursively to nested structures:

```java
void test(List<List<Integer>> matrix) {
    // [[1, 2], [3, 4]] → List.of(List.of(1, 2), List.of(3, 4))
}

void test(Map<String, List<Integer>> data) {
    // [a: [1, 2], b: [3, 4]] → Map.of("a", List.of(1, 2), "b", List.of(3, 4))
}
```

Each level of nesting independently converts using the same rules.

## Null Conversion

### Rules

1. **`null` keyword** → `null` reference
2. **Empty cell** → `null` for non-primitive types
3. **Primitives** → Cannot be `null` (use wrapper types)

### Examples

```java
void test(String value) {
    // "null" → null
    // "" (empty cell) → null
}

void test(Integer value) {
    // "null" → null
    // "" → null
}

void test(int value) {
    // "null" → Error! Primitives can't be null
    // "" → Error!
}
```

## Custom Conversion with @ConvertWith

### Usage

Specify a custom `ArgumentConverter` for a parameter:

```java
@TableTest("""
    Answer | Expected
    Yes    | true
    No     | false
    """)
void test(String answer,
          @ConvertWith(YesNoConverter.class) boolean expected) {
    // expected converted via YesNoConverter
}
```

### Converter Implementation

```java
public class YesNoConverter implements ArgumentConverter {
    @Override
    public Object convert(Object source, ParameterContext context) {
        if (source instanceof String str) {
            return str.equalsIgnoreCase("Yes");
        }
        throw new IllegalArgumentException("Cannot convert: " + source);
    }
}
```

### Priority

`@ConvertWith` takes precedence over factory methods and built-in converters.

## Factory Sources

### @FactorySources Annotation

Specify external classes containing factory methods:

```java
@FactorySources({MyConverters.class, OtherConverters.class})
class MyTest {
    @TableTest("""
        Value
        special
        """)
    void test(CustomType value) {
        // Factory methods searched in MyConverters and OtherConverters
    }
}
```

### Search Order with @FactorySources

1. Test class methods
2. Classes in `@FactorySources` (in declaration order)
3. Target type class methods

## Conversion Errors

### Common Errors

**No converter found:**
```
No converter found for parameter 'foo' of type CustomType
```

**Fix:** Define a factory method or use `@ConvertWith`

**Conversion failed:**
```
Could not convert 'abc' to type int
```

**Fix:** Ensure value format matches expected type

**Ambiguous conversion:**
```
Multiple converters found for type Foo
```

**Fix:** Use `@ConvertWith` to specify which converter to use

### Error Messages

TableTest provides detailed error messages including:
- The problematic value
- The target type
- The conversion method that failed
- Suggestions for fixing the issue

## Type Inference

TableTest infers generic types from method parameters:

```java
void test(List<Integer> numbers) {
    // Infers: List element type is Integer
    // [1, 2, 3] → each element converted to Integer
}

void test(Map<String, List<Double>> data) {
    // Infers: Map key type is String, value type is List<Double>
    // [a: [1.5, 2.5]] → "a" as String, [1.5, 2.5] as List<Double>
}
```

## Best Practices

### Factory Method Naming

Use clear, descriptive names:

```java
// Good
public static Temperature fromCelsius(double c)
public static Discount parsePercentage(String s)

// Less clear
public static Temperature make(double d)
public static Discount parse(String s)
```

### Error Handling

Throw descriptive exceptions:

```java
public static Temperature fromCelsius(double celsius) {
    if (celsius < -273.15) {
        throw new IllegalArgumentException(
            "Temperature below absolute zero: " + celsius);
    }
    return new Temperature(celsius);
}
```

### Reusable Converters

Place common factory methods in a shared utility class:

```java
public class TestConverters {
    public static Discount parseDiscount(String input) { }
    public static Temperature parseTemperature(String input) { }
}

@FactorySources(TestConverters.class)
class MyTest { }
```

### Test Factory Methods

Factory methods are regular code - test them:

```java
@Test
void testParseDiscount() {
    assertEquals(new Discount(10), parseDiscount("10%"));
    assertThrows(IllegalArgumentException.class,
        () -> parseDiscount("invalid"));
}
```

## Language-Specific Features

### Java

Standard static methods:

```java
public static MyType fromString(String input) {
    return new MyType(input);
}
```

### Kotlin

Use companion object with `@JvmStatic`:

```kotlin
class MyType(val value: String) {
    companion object {
        @JvmStatic
        fun fromString(input: String): MyType {
            return MyType(input)
        }
    }
}
```

The `@JvmStatic` annotation is required for Java interop.

## Summary

**Conversion priority:**
1. `@ConvertWith` converter (explicit)
2. Factory methods (custom)
3. Built-in converters (standard types)

**Factory method discovery:**
1. Test class
2. `@FactorySources` classes
3. Target type class

**Supported built-in types:**
- Primitives and wrappers
- Strings
- Enums
- Temporal types (ISO-8601)
- File system (File, Path)
- Network (URI, URL)
- UUID
- Collections (List, Set, Map)

## See Also

- [@TableTest Annotation](/docs/reference/annotation/) - Annotation specification
- [Value Syntax](/docs/reference/value-syntax/) - Value format rules
- [User Guide: Type Conversion](/docs/guide/type-conversion/) - Practical examples
