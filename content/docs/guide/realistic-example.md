---
title: "Realistic Example"
weight: 5
draft: true
---

# Realistic Example: Discount Calculation System

This guide demonstrates TableTest solving a real-world problem: a public transport discount system. You'll see custom type conversion, temporal logic, value sets, and table design in action.

## The Business Problem

Norwegian transit company Ruter offers tiered discounts based on purchase frequency:

> "The more you travel, the cheaper the tickets will become. Discounts increase stepwise based on the number of ticket purchases in the last 30 days, capped at 40%."

**Discount Tiers:**
- Purchases 1-4: 0% discount
- Purchases 5-9: 10% discount
- Purchases 10-19: 20% discount
- Purchases 20-39: 30% discount
- Purchases 40+: 40% discount

## Initial Implementation

### The Discount Type

First, define a `PercentageDiscount` type:

```java
public record PercentageDiscount(int value) implements Comparable<PercentageDiscount> {
    public PercentageDiscount {
        if (value < 0 || value > 100) {
            throw new IllegalArgumentException("Discount must be between 0 and 100");
        }
    }

    @Override
    public int compareTo(PercentageDiscount other) {
        return Integer.compare(this.value, other.value);
    }

    @Override
    public String toString() {
        return value + "%";
    }
}
```

### Custom Type Conversion

Create a factory method to convert strings like "10%" to `PercentageDiscount`:

```java
public static PercentageDiscount parseDiscount(String input) {
    if (input == null || input.isBlank()) {
        return new PercentageDiscount(0);
    }
    String digits = input.replace("%", "").trim();
    return new PercentageDiscount(Integer.parseInt(digits));
}
```

Now TableTest can convert discount values automatically.

### The Business Logic

Implement the discount calculation:

```java
public class DiscountCalculator {
    public PercentageDiscount calculateDiscount(int purchaseCount) {
        if (purchaseCount < 5) return new PercentageDiscount(0);
        if (purchaseCount < 10) return new PercentageDiscount(10);
        if (purchaseCount < 20) return new PercentageDiscount(20);
        if (purchaseCount < 40) return new PercentageDiscount(30);
        return new PercentageDiscount(40);
    }
}
```

## Testing with TableTest

### Basic Test

Start with boundary values for each tier:

```java
@TableTest("""
    Scenario              | Purchases | Discount
    No discount tier      | 4         | 0%
    10% discount tier     | 9         | 10%
    20% discount tier     | 19        | 20%
    30% discount tier     | 39        | 30%
    40% discount tier     | 40        | 40%
    """)
void testDiscountTiers(int purchases, PercentageDiscount discount) {
    DiscountCalculator calculator = new DiscountCalculator();
    assertEquals(discount, calculator.calculateDiscount(purchases));
}
```

This test verifies the upper boundary of each tier.

### Comprehensive Coverage with Value Sets

Use value sets to test multiple values per tier:

```java
@TableTest("""
    Scenario              | Purchases      | Discount
    No discount tier      | {1, 2, 3, 4}   | 0%
    10% discount tier     | {5, 6, 7, 8, 9}| 10%
    20% discount tier     | {10, 15, 19}   | 20%
    30% discount tier     | {20, 25, 39}   | 30%
    40% discount tier     | {40, 50, 100}  | 40%
    """)
void testDiscountTiersComprehensive(int purchases, PercentageDiscount discount) {
    DiscountCalculator calculator = new DiscountCalculator();
    assertEquals(discount, calculator.calculateDiscount(purchases));
}
```

This single table produces **17 test executions**, thoroughly covering all tiers with minimal code.

## Adding Temporal Logic

Real discount systems consider purchase timing. Let's add a 30-day window requirement.

### Enhanced Business Logic

```java
public class DiscountCalculator {
    private final Clock clock;

    public DiscountCalculator(Clock clock) {
        this.clock = clock;
    }

    public PercentageDiscount calculateDiscount(List<Purchase> purchases) {
        LocalDate thirtyDaysAgo = LocalDate.now(clock).minusDays(30);

        long recentPurchases = purchases.stream()
            .filter(p -> p.date().isAfter(thirtyDaysAgo))
            .count();

        return discountForCount((int) recentPurchases);
    }

    private PercentageDiscount discountForCount(int count) {
        if (count < 5) return new PercentageDiscount(0);
        if (count < 10) return new PercentageDiscount(10);
        if (count < 20) return new PercentageDiscount(20);
        if (count < 40) return new PercentageDiscount(30);
        return new PercentageDiscount(40);
    }
}
```

### Readable Temporal Notation

Create a notation for relative timestamps:

```java
public static LocalDate parseRelativeDate(String input) {
    // "T" means "today"
    // "T-30d" means "30 days ago"
    // "T-45d" means "45 days ago"

    if (input.equals("T")) {
        return LocalDate.now();
    }

    if (input.startsWith("T-") && input.endsWith("d")) {
        String daysStr = input.substring(2, input.length() - 1);
        int days = Integer.parseInt(daysStr);
        return LocalDate.now().minusDays(days);
    }

    // Fall back to ISO-8601
    return LocalDate.parse(input);
}
```

This makes temporal test data readable and maintainable. Instead of:

```
2024-01-15  // What does this date mean?
```

You write:

```
T-30d       // 30 days ago - clear intent
```

### Purchase Record

Define a purchase record:

```java
public record Purchase(String id, LocalDate date) {}

public static Purchase parsePurchase(String input) {
    // Format: "id:date" e.g., "P1:T-10d"
    String[] parts = input.split(":");
    String id = parts[0];
    LocalDate date = parseRelativeDate(parts[1]);
    return new Purchase(id, date);
}
```

### Time-Aware Test

Test the 30-day window logic:

```java
@TableTest("""
    Scenario                  | Purchases                                        | Discount
    All recent                | [P1:T-5d, P2:T-10d, P3:T-15d, P4:T-20d, P5:T-25d]| 10%
    Some expired              | [P1:T-5d, P2:T-35d, P3:T-40d, P4:T-50d]          | 0%
    Just qualifying           | [P1:T-1d, P2:T-2d, P3:T-3d, P4:T-4d, P5:T-29d]   | 10%
    Just outside window       | [P1:T-31d, P2:T-32d, P3:T-35d, P4:T-40d]         | 0%
    Mixed recent and expired  | [P1:T-5d, P2:T-10d, P3:T-35d, P4:T-40d]          | 0%
    High volume recent        | [P1:T-1d, P2:T-5d, P3:T-10d, P4:T-15d, P5:T-20d, | 10%
                              |  P6:T-25d, P7:T-28d, P8:T-29d, P9:T-30d]         |
    """)
void testDiscountWithTimeWindow(List<Purchase> purchases, PercentageDiscount discount,
                                TestInfo testInfo) {
    Clock fixedClock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
    DiscountCalculator calculator = new DiscountCalculator(fixedClock);

    assertEquals(discount, calculator.calculateDiscount(purchases),
        "Failed: " + testInfo.getDisplayName());
}
```

This test clearly shows how the 30-day window affects discount calculation.

## Design Lessons

### 1. Invest in Readability

The relative date notation (`T-30d`) makes temporal logic immediately clear. Without it:

```java
// Hard to understand
[P1:2024-01-15, P2:2024-01-10, P3:2023-12-20]
```

With notation:

```java
// Intent is clear
[P1:T-5d, P2:T-10d, P3:T-40d]
```

The small investment in `parseRelativeDate` pays off in test clarity.

### 2. Inject Time

Make time a parameter to enable testing:

```java
// Bad: Hard to test
LocalDate now = LocalDate.now();  // Always "now"

// Good: Testable
LocalDate now = LocalDate.now(clock);  // Controllable
```

Pass a `Clock` to your logic, allowing tests to fix time and verify temporal behavior reliably.

### 3. Start Simple, Expand

Begin with basic test cases:

1. **Foundation:** Test tier boundaries
2. **Expansion:** Add value sets for coverage
3. **Complexity:** Introduce temporal logic
4. **Edge cases:** Test window boundaries

Each iteration builds on the previous, maintaining working tests throughout.

### 4. Use Domain Types

`PercentageDiscount` is better than raw `int` because:

- **Type safety:** Can't accidentally pass a discount where a count is expected
- **Validation:** Enforces 0-100 range
- **Clarity:** `PercentageDiscount` is more expressive than `int`
- **Custom conversion:** Factory methods make tables readable

### 5. Tables as Documentation

Well-designed tables become living documentation of business rules. Stakeholders can review this test:

```java
@TableTest("""
    Scenario              | Purchases      | Discount
    No discount tier      | {1, 2, 3, 4}   | 0%
    10% discount tier     | {5, 6, 7, 8, 9}| 10%
    20% discount tier     | {10, 15, 19}   | 20%
    30% discount tier     | {20, 25, 39}   | 30%
    40% discount tier     | {40, 50, 100}  | 40%
    """)
```

And understand the discount structure without reading implementation code.

## Complete Example

Here's the full test class:

```java
public class DiscountCalculatorTest {
    private final DiscountCalculator calculator =
        new DiscountCalculator(Clock.systemDefaultZone());

    // Factory methods
    public static PercentageDiscount parseDiscount(String input) {
        String digits = input.replace("%", "").trim();
        return new PercentageDiscount(Integer.parseInt(digits));
    }

    public static Purchase parsePurchase(String input) {
        String[] parts = input.split(":");
        return new Purchase(parts[0], parseRelativeDate(parts[1]));
    }

    public static LocalDate parseRelativeDate(String input) {
        if (input.equals("T")) return LocalDate.now();
        if (input.startsWith("T-") && input.endsWith("d")) {
            int days = Integer.parseInt(input.substring(2, input.length() - 1));
            return LocalDate.now().minusDays(days);
        }
        return LocalDate.parse(input);
    }

    @DisplayName("Discount Tiers")
    @TableTest("""
        Scenario              | Purchases      | Discount
        No discount tier      | {1, 2, 3, 4}   | 0%
        10% discount tier     | {5, 6, 7, 8, 9}| 10%
        20% discount tier     | {10, 15, 19}   | 20%
        30% discount tier     | {20, 25, 39}   | 30%
        40% discount tier     | {40, 50, 100}  | 40%
        """)
    void testDiscountTiers(int purchases, PercentageDiscount discount) {
        assertEquals(discount, calculator.calculateDiscount(purchases));
    }

    @DisplayName("Time Window Tests")
    @TableTest("""
        Scenario                  | Purchases                          | Discount
        All recent                | [P1:T-5d, P2:T-10d, P3:T-15d,     | 10%
                                  |  P4:T-20d, P5:T-25d]               |
        Some expired              | [P1:T-5d, P2:T-35d, P3:T-40d]     | 0%
        Just qualifying           | [P1:T-1d, P2:T-2d, P3:T-3d,       | 10%
                                  |  P4:T-4d, P5:T-29d]                |
        Just outside window       | [P1:T-31d, P2:T-32d, P3:T-35d]    | 0%
        """)
    void testDiscountWithTimeWindow(List<Purchase> purchases,
                                   PercentageDiscount discount) {
        assertEquals(discount, calculator.calculateDiscount(purchases));
    }
}
```

## Key Takeaways

1. **Custom types enhance clarity** - `PercentageDiscount` and `Purchase` make intent explicit
2. **Factory methods enable custom conversion** - `parseDiscount`, `parsePurchase` make tables readable
3. **Value sets increase coverage** - Test multiple values per tier without code duplication
4. **Readable notation matters** - `T-30d` is clearer than absolute dates
5. **Inject dependencies** - Pass `Clock` to enable testable temporal logic
6. **Tables document behavior** - Well-designed tables serve as executable specifications

## Next Steps

For formal specifications and detailed rules, see the [Reference](/docs/reference/) section.

For more examples, check the [TableTest repository](https://github.com/nchaugen/tabletest/tree/main/examples).
