---
title: "Realistic Example"
weight: 5
---

This guide walks through designing TableTests for a realistic problem: a public transport levelled discount system. You'll see how to develop tests iteratively and how investing in readable notation pays off.

## The Business Problem

Norwegian transit company Ruter offers levelled discounts on single tickets based on purchase frequency:

> "The more you travel, the cheaper the tickets will become. Discounts increase stepwise based on the number of ticket purchases in the last 30 days, capped at 40%."

| Ticket number | Discount |
|:--------------|:---------|
| 1–4           | 0%       |
| 5–9           | 5%       |
| 10–14         | 10%      |
| 15–19         | 15%      |
| 20–24         | 20%      |
| 25–29         | 25%      |
| 30–34         | 30%      |
| 35–39         | 35%      |
| 40+           | 40%      |

There are two aspects to implement:

1. **Calculating the discount percentage** given the number of previous purchases
2. **Counting eligible purchases** within the last 30 days

## Calculating the Discount

### Starting with a Test

We begin with a table expressing the simplest case — a single base scenario:

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
```java
@TableTest("""
    Purchases last 30 days | Discount?
    0                      | 0%
    """)
void next_purchase_discount(int purchasesLast30Days,
                            PercentageDiscount expectedDiscount) {
    // TODO
}
```
{{< /tab >}}
{{< tab >}}
```kotlin
@TableTest("""
    Purchases last 30 days | Discount?
    0                      | 0%
    """)
fun next_purchase_discount(purchasesLast30Days: Int,
                           expectedDiscount: PercentageDiscount) {
    // TODO
}
```
{{< /tab >}}
{{< /tabs >}}

Running this before writing any production code gives a conversion error — TableTest doesn't know how to convert `"0%"` to a `PercentageDiscount`. We add a `@TypeConverter` method:

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
{{< codefile file="examples/src/test/java/guide/RealisticExampleDiscountTest.java" id="discount-converter" >}}
{{< /tab >}}
{{< tab >}}
{{< codefile file="examples/src/test/kotlin/guide/RealisticExampleDiscountKtTest.kt" id="discount-converter" >}}
{{< /tab >}}
{{< /tabs >}}

Now we fill in the test body and implement just enough to make it pass:

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
```java
@TableTest("""
    Purchases last 30 days | Discount?
    0                      | 0%
    """)
void next_purchase_discount(int purchasesLast30Days,
                            PercentageDiscount expectedDiscount) {
    assertEquals(
        expectedDiscount.percentage(),
        LevelledDiscount.calculateDiscountPercentage(purchasesLast30Days)
    );
}
```
{{< /tab >}}
{{< tab >}}
```kotlin
@TableTest("""
    Purchases last 30 days | Discount?
    0                      | 0%
    """)
fun next_purchase_discount(purchasesLast30Days: Int,
                           expectedDiscount: PercentageDiscount) {
    assertEquals(
        expectedDiscount.percentage,
        LevelledDiscount.calculateDiscountPercentage(purchasesLast30Days)
    )
}
```
{{< /tab >}}
{{< /tabs >}}

### Extending Coverage with Value Sets

Once the base case passes, we add more scenarios and extend the implementation to handle them. Value sets group inputs that share the same expected result:

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
{{< codefile file="examples/src/test/java/guide/RealisticExampleDiscountTest.java" id="discount-test" >}}
{{< /tab >}}
{{< tab >}}
{{< codefile file="examples/src/test/kotlin/guide/RealisticExampleDiscountKtTest.kt" id="discount-test" >}}
{{< /tab >}}
{{< /tabs >}}

This single table generates **41 test executions** covering all tiers thoroughly. Notice how it mirrors the discount scheme description — the table reads as documentation of the business rules.

## Counting Eligible Purchases

### Starting Simple

The second aspect is counting purchases within the last 30 days. We start with the simplest scenario:

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
```java
@TableTest("""
    Scenario     | Purchases | Count?
    No purchases | []        | 0
    """)
void count_purchases_last_30_days(List<Purchase> purchases,
                                  int expectedCount) {
    // TODO
}
```
{{< /tab >}}
{{< tab >}}
```kotlin
@TableTest("""
    Scenario     | Purchases | Count?
    No purchases | []        | 0
    """)
fun count_purchases_last_30_days(purchases: List<Purchase>,
                                 expectedCount: Int) {
    // TODO
}
```
{{< /tab >}}
{{< /tabs >}}

This passes — with an empty list, no conversion is needed. Next we add a scenario with a purchase timestamp. Running it gives a conversion error, so we provide a `@TypeConverter` that accepts `LocalDateTime` — TableTest uses built-in conversion to transform the timestamp string to `LocalDateTime`, then passes it to our converter:

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
```java
@TypeConverter
public static Purchase createPurchase(LocalDateTime purchaseTimestamp) {
    return new Purchase(purchaseTimestamp, /* other fields use constants */);
}
```
{{< /tab >}}
{{< tab >}}
```kotlin
companion object {
    @JvmStatic
    @TypeConverter
    fun createPurchase(purchaseTimestamp: LocalDateTime): Purchase =
        Purchase(purchaseTimestamp, /* other fields use constants */)
}
```
{{< /tab >}}
{{< /tabs >}}

### Absolute Timestamps

We add scenarios to test the 30-day window boundary. Timestamps inside lists need quoting because the colon in `T00:00:00` would collide with map syntax:

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
```java
@TableTest("""
    Scenario              | Time now            | Purchases                                      | Count?
    No purchases          | 2025-09-30T23:59:59 | []                                             | 0
    Purchase too old      | 2025-09-30T23:59:59 | ["2025-08-01T00:00:00"]                        | 0
    Purchase just outside | 2025-09-30T23:59:59 | ["2025-08-31T23:59:59"]                        | 0
    Purchase just inside  | 2025-09-30T23:59:59 | ["2025-09-01T00:00:00"]                        | 1
    All purchases inside  | 2025-09-30T23:59:59 | ["2025-09-02T00:00:00", "2025-09-03T00:00:00"] | 2
    One inside, one not   | 2025-09-30T23:59:59 | ["2025-08-02T00:00:00", "2025-09-03T00:00:00"] | 1
    """)
void count_purchases_last_30_days(LocalDateTime timeNow,
                                  List<Purchase> purchases,
                                  int expectedCount) {
    assertEquals(expectedCount,
        LevelledDiscount.countPurchasesLast30Days(timeNow, purchases));
}
```
{{< /tab >}}
{{< tab >}}
```kotlin
@TableTest("""
    Scenario              | Time now            | Purchases                                      | Count?
    No purchases          | 2025-09-30T23:59:59 | []                                             | 0
    Purchase too old      | 2025-09-30T23:59:59 | ["2025-08-01T00:00:00"]                        | 0
    Purchase just outside | 2025-09-30T23:59:59 | ["2025-08-31T23:59:59"]                        | 0
    Purchase just inside  | 2025-09-30T23:59:59 | ["2025-09-01T00:00:00"]                        | 1
    All purchases inside  | 2025-09-30T23:59:59 | ["2025-09-02T00:00:00", "2025-09-03T00:00:00"] | 2
    One inside, one not   | 2025-09-30T23:59:59 | ["2025-08-02T00:00:00", "2025-09-03T00:00:00"] | 1
    """)
fun count_purchases_last_30_days(timeNow: LocalDateTime,
                                 purchases: List<Purchase>,
                                 expectedCount: Int) {
    assertEquals(expectedCount,
        LevelledDiscount.countPurchasesLast30Days(timeNow, purchases))
}
```
{{< /tab >}}
{{< /tabs >}}

The tests pass, but the table has a readability problem. Is `"2025-08-31T23:59:59"` inside or outside the 30-day window from `2025-09-30T23:59:59`? You have to do mental arithmetic to tell.

### Making the Table Readable

A relative timestamp notation solves this. Instead of absolute timestamps, we express times relative to "now":

- `T-60d` — 60 days ago
- `T-30d` — exactly 30 days ago
- `T-29d23h59m59s` — 30 days minus one second ago

We add a `@TypeConverter` that overrides the built-in `LocalDateTime` conversion:

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
{{< codefile file="examples/src/test/java/guide/RealisticExampleCountingTest.java" id="relative-timestamp-converter" >}}
{{< /tab >}}
{{< tab >}}
{{< codefile file="examples/src/test/kotlin/guide/RealisticExampleCountingKtTest.kt" id="relative-timestamp-converter" >}}
{{< /tab >}}
{{< /tabs >}}

Now we rewrite the table:

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
{{< codefile file="examples/src/test/java/guide/RealisticExampleCountingTest.java" id="counting-test" >}}
{{< /tab >}}
{{< tab >}}
{{< codefile file="examples/src/test/kotlin/guide/RealisticExampleCountingKtTest.kt" id="counting-test" >}}
{{< /tab >}}
{{< /tabs >}}

The improvement is significant. The boundary conditions — `T-29d23h59m59s` (just inside) versus `T-30d` (just outside) — are immediately obvious. The "Time now" column has become unnecessary because all timestamps are relative. And because the relative notation doesn't contain colons, the values no longer need quoting inside lists.

The small investment in `parseRelativeTimestamp` transformed the table from hard-to-read arithmetic into clear intent.

## Design Lessons

**Tables as documentation.** The discount table mirrors the business rules so closely that stakeholders can review it directly. Strive for tables that communicate well — they become living documentation.

**Invest in readable notation.** Custom `@TypeConverter` methods are not just about type conversion — they shape how your tables communicate. The relative timestamp converter changed the entire character of the counting test.

**Test-first with tables.** Writing the table before the implementation forces you to think about behaviour from the outside. Conversion errors guide you to create the converters you need.

**Value sets for thorough coverage.** The discount table generates 41 executions from 9 rows. Value sets are ideal when multiple inputs share an expected outcome.

**Start simple, extend incrementally.** Begin with a base case, make it pass, then expand the table. Each row you add is a new behaviour to implement or verify.

## Next Steps

For the complete specification of all features, see the [User Guide on GitHub](https://github.com/nchaugen/tabletest/blob/main/USERGUIDE.md).

For more examples, check the [TableTest repository](https://github.com/nchaugen/tabletest/tree/main/examples).
