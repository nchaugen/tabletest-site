---
title: TableTest
layout: hextra-home
---

<div class="hx:mt-6 hx:mb-6">
{{< hextra/hero-headline >}}
  Turn Test Methods into Rows
{{< /hextra/hero-headline >}}
</div>

<div class="hx:mb-12">
{{< hextra/hero-subtitle >}}
  A JUnit extension for table-driven tests in Java and Kotlin.&nbsp;<br class="sm:hx-block hx-hidden" />
  Same coverage. Less code.
{{< /hextra/hero-subtitle >}}
</div>

<div class="hx:mb-6">
{{< hextra/hero-button text="Learn more" link="learn/getting-started/introduction" >}}
</div>

<style>
.before-after-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: 1.5rem;
}
@media (min-width: 938px) {
  .before-after-grid {
    grid-template-columns: 1fr 1fr;
  }
}
.before-after-grid > div {
  min-width: 0;
}
</style>

<div style="margin: 3rem 0; width: 100%;">

<div class="before-after-grid">

<div>
<h3 class="hx:text-lg hx:font-semibold hx:mb-3 hx:text-gray-500 dark:hx:text-gray-400">Before — repetitive test methods</h3>

```java
@Test
void notDivisibleBy4() {
    assertFalse(Year.isLeap(2001));
}

@Test
void divisibleBy4() {
    assertTrue(Year.isLeap(2004));
}

@Test
void divisibleBy100ButNotBy400() {
    assertFalse(Year.isLeap(2100));
}

@Test
void divisibleBy400() {
    assertTrue(Year.isLeap(2000));
}
```

</div>
<div>
<h3 class="hx:text-lg hx:font-semibold hx:mb-3 hx:text-emerald-600 dark:hx:text-emerald-400">After — one method, one table</h3>

```java
@TableTest("""
    Scenario                        | Year | Leap?
    Not divisible by 4              | 2001 | false
    Divisible by 4                  | 2004 | true
    Divisible by 100 but not by 400 | 2100 | false
    Divisible by 400                | 2000 | true
    """)
void leapYear(int year, boolean leap) {
    assertEquals(leap, Year.isLeap(year));
}
```

</div>
</div>


</div>

<div class="hx:w-full hx:max-w-screen-xl">
{{< hextra/feature-grid >}}
  {{< hextra/feature-card
    title="Adopt Incrementally"
    subtitle="Convert one test class at a time. @TableTest lives alongside @Test and @ParameterizedTest in the same project."
  >}}
  {{< hextra/feature-card
    title="First-Class IDE Experience"
    subtitle="Syntax highlighting, auto-formatting, and scenario names in your test runner. IntelliJ and VS Code extensions available."
  >}}
  {{< hextra/feature-card
    title="Tests as Documentation"
    subtitle="Scenario names describe the behaviour. Columns show the inputs and expected results. The test reads like a spec."
  >}}
{{< /hextra/feature-grid >}}
</div>

