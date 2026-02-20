---
title: "Your First TableTest"
weight: 3
---

Let's write a complete table-driven test from scratch. We'll test a simple calculator addition method to demonstrate the fundamentals.

{{% steps %}}

### Create the Class Under Test

First, create a simple calculator class:

```java
public class Calculator {
    public int add(int a, int b) {
        return a + b;
    }
}
```

### Create the Test Class

Create a JUnit test class and import the necessary classes:

```java
import org.tabletest.junit.TableTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorTest {
    private final Calculator calculator = new Calculator();

    // Tests will go here
}
```

### Write Your First TableTest

Add a test method with the `@TableTest` annotation:

```java
@TableTest("""
    Scenario              | A  | B  | Sum?
    Two positive numbers  | 2  | 3  | 5
    Positive and negative | 5  | -3 | 2
    Two negative numbers  | -4 | -6 | -10
    Zero and positive     | 0  | 7  | 7
    """)
public void testAddition(int a, int b, int expectedSum) {
    int result = calculator.add(a, b);
    assertEquals(expectedSum, result);
}
```

Let's break this down:

**The Table Structure**

```
Scenario              | A | B | Sum?
Two positive numbers  | 2 | 3 | 5
```

- **First row:** Column headers (pipe-separated)
- **First column:** Scenario descriptions (not passed as parameter)
- **Remaining columns:** Test data mapped to method parameters
- **Subsequent rows:** Test cases (one execution per row)

**Method Parameters**

```java
public void testAddition(int a, int b, int expectedSum)
```

The method parameters match the table columns (except the scenario column) based on order. TableTest automatically converts string values to the parameter types.

**The Assertion**

```java
int result = calculator.add(a, b);
assertEquals(expectedSum, result);
```

Standard JUnit assertions. The test logic executes once per table row.

### Run the Test

Run the test using your IDE or build tool:

{{< tabs items="Maven,Gradle" >}}
{{< tab >}}
```bash
mvn test
```
{{< /tab >}}
{{< tab >}}
```bash
./gradlew test
```
{{< /tab >}}
{{< /tabs >}}

You should see **four test executions** – one for each row in the table. In your IDE's test runner, they'll appear as:

```
✓ CalculatorTest
  ✓ testAddition (int, int, int)
    ✓ [1] Two positive numbers
    ✓ [2] Positive and negative
    ✓ [3] Two negative numbers
    ✓ [4] Zero and positive
```

The scenario description becomes the display name, making it easy to identify which case passed or failed.

{{% /steps %}}

## Understanding What Happens

When you run this test, TableTest will:

1. **Parse the table** – Extract headers and data rows
2. **Map columns to parameters** – By order, ignoring the additional scenario column
3. **Convert values** – Transforms string `"2"` to int `2`, etc.
4. **Pass converted values to JUnit** – Making JUnit run the test once for each row 

## What You've Learned

✓ How to structure a TableTest table\
✓ How columns map to method parameters\
✓ How scenario descriptions work\
✓ How to run and interpret results

## Next Steps

You now know the basics! Let's explore [what to learn next&nbsp;→](/docs/getting-started/next-steps/)
