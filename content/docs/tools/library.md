---
title: "Core Library"
weight: 1
draft: true
---

# TableTest Core Library

The core TableTest library is a JUnit 5 extension that enables data-driven testing with the `@TableTest` annotation. It's the foundation of the TableTest ecosystem.

## Overview

TableTest extends JUnit to allow developers to express system behaviour through multiple examples organized in table format. It reduces test code verbosity while improving maintainability and readability.

## Installation

Add TableTest as a test dependency in your project:

**Maven:**
```xml
<dependency>
    <groupId>io.github.nchaugen</groupId>
    <artifactId>tabletest-junit</artifactId>
    <version>{{< param currentVersion >}}</version>
    <scope>test</scope>
</dependency>
```

**Gradle:**
```groovy
testImplementation 'io.github.nchaugen:tabletest-junit:{{< param currentVersion >}}'
```

## Requirements

- Java 21 or above
- JUnit 5.11 or above

## Key Features

### Readable Table Syntax

Express multiple test cases in a clear, pipe-separated table format:

```java
@TableTest("""
    Scenario         | Input | Expected
    Positive number  | 5     | true
    Negative number  | -3    | false
    Zero             | 0     | false
    """)
void testIsPositive(int input, boolean expected) {
    assertEquals(expected, input > 0);
}
```

### Automatic Type Conversion

TableTest automatically converts string values to method parameter types using:

1. **Factory methods** - Custom conversion logic you define
2. **Built-in converters** - Primitives, strings, dates, enums, and more

```java
@TableTest("""
    Date       | Days After Today?
    2024-01-15 | -350
    2025-12-31 | 358
    """)
void testDateDifference(LocalDate date, int daysAfterToday) {
    // date is automatically converted from string to LocalDate
    long diff = ChronoUnit.DAYS.between(LocalDate.now(), date);
    assertEquals(daysAfterToday, diff, 5); // within 5 days tolerance
}
```

### Value Collections

Support for lists, sets, and maps directly in table cells:

```java
@TableTest("""
    Numbers    | Sum
    [1, 2, 3]  | 6
    [10, 20]   | 30
    []         | 0
    """)
void testSum(List<Integer> numbers, int sum) {
    assertEquals(sum, numbers.stream().mapToInt(Integer::intValue).sum());
}
```

### Value Sets

Test multiple inputs with the same expected outcome using set notation:

```java
@TableTest("""
    Scenario    | Year               | Leap?
    Leap years  | {2000, 2004, 2008} | true
    Not leap    | {2001, 2100, 2200} | false
    """)
void testLeapYear(int year, boolean isLeap) {
    assertEquals(isLeap, Year.isLeap(year));
}
```

The test executes once for each value in the set.

### Nested Structures

Compose complex data structures:

```java
@TableTest("""
    Student Grades                               | Highest
    [Alice: [95, 87, 92], Bob: [78, 85, 90]]    | 95
    [Charlie: [98, 89, 91], David: [70, 80]]    | 98
    """)
void testHighestGrade(Map<String, List<Integer>> grades, int highest) {
    int max = grades.values().stream()
        .flatMap(List::stream)
        .mapToInt(Integer::intValue)
        .max()
        .orElse(0);
    assertEquals(highest, max);
}
```

### Scenario Names

The first column serves as a scenario description, improving test readability and appearing in test reports:

```
✓ testLeapYear[Leap years: 2000]
✓ testLeapYear[Leap years: 2004]
✓ testLeapYear[Not leap: 2001]
```

### External Table Files

Store large tables in external files:

```java
@TableTest(resource = "/test-data/large-dataset.txt")
void testWithExternalData(String input, String expected) {
    // Test implementation
}
```

### Custom Conversion

Define factory methods for custom type conversion:

```java
public static Discount parseDiscount(String input) {
    String digits = input.substring(0, input.length() - 1); // Remove '%'
    return new Discount(Integer.parseInt(digits));
}

@TableTest("""
    Purchases | Discount
    5         | 10%
    15        | 20%
    """)
void testDiscount(int purchases, Discount discount) {
    // discount is converted using parseDiscount method
}
```

## Framework Integration

TableTest works seamlessly with popular frameworks:

### Spring Boot

```java
@SpringBootTest
class MySpringTest {
    @Autowired
    private MyService service;

    @TableTest("""
        Input | Expected
        foo   | FOO
        bar   | BAR
        """)
    void testService(String input, String expected) {
        assertEquals(expected, service.process(input));
    }
}
```

### Quarkus

```java
@QuarkusTest
class MyQuarkusTest {
    @Inject
    MyBean bean;

    @TableTest("""
        Value | Result
        1     | 2
        5     | 10
        """)
    void testBean(int value, int result) {
        assertEquals(result, bean.double(value));
    }
}
```

## Language Support

TableTest fully supports both **Java** and **Kotlin**:

**Java:**
```java
@TableTest("""
    Input | Output
    hello | HELLO
    """)
void test(String input, String output) {
    assertEquals(output, input.toUpperCase());
}
```

**Kotlin:**
```kotlin
@TableTest("""
    Input | Output
    hello | HELLO
    """)
fun test(input: String, output: String) {
    assertEquals(output, input.uppercase())
}
```

## Resources

- **GitHub:** [github.com/nchaugen/tabletest](https://github.com/nchaugen/tabletest)
- **Maven Central:** [tabletest-junit](https://central.sonatype.com/artifact/io.github.nchaugen/tabletest-junit)
- **User Guide:** Complete documentation in the [User Guide](/docs/guide/)
- **Examples:** Sample projects in the [repository](https://github.com/nchaugen/tabletest/tree/main/examples)

## License

Apache License 2.0 - Free for personal and commercial use.

---

**Next:** Enhance your development experience with the [IntelliJ Plugin →](/docs/tools/intellij-plugin/)
