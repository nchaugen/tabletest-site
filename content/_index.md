---
title: TableTest
layout: hextra-home
---

<div class="hx:mt-6 hx:mb-6">
{{< hextra/hero-headline >}}
  Tests That Document Your Application
{{< /hextra/hero-headline >}}
</div>

<div class="hx:mb-12">
{{< hextra/hero-subtitle >}}
  Express system behaviour through readable tables.&nbsp;<br class="sm:hx-block hx-hidden" />
  One method, multiple scenarios.
{{< /hextra/hero-subtitle >}}
</div>

<div class="hx:mb-6">
{{< hextra/hero-button text="Get Started" link="docs/getting-started/introduction" >}}
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

<div style="margin: 4rem 0; width: 100%;">

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

<p style="text-align: center; margin-top: 1.5rem; font-size: 0.875rem;" class="hx-text-gray-500 dark:hx-text-gray-400">Same coverage. Less code. Add test cases by adding rows.</p>

</div>

<div class="hx:w-full hx:max-w-screen-xl">
{{< hextra/feature-grid >}}
  {{< hextra/feature-card
    title="Add Test Cases, Not Methods"
    subtitle="Each row is a test case. Add scenarios by adding rows — no more copying test methods and changing values."
  >}}
  {{< hextra/feature-card
    title="See Coverage at a Glance"
    subtitle="Scannable tables reveal which cases you've covered and which you've missed. Spot gaps instantly."
    image="leap-year-coverage-table.png"
    imageClass="hx:absolute hx:max-w-none hx:top-[40%] hx:left-[24px] hx:w-[180%] hx:sm:w-[110%] hx:dark:opacity-80"
    style="background: radial-gradient(ellipse at 50% 80%,rgba(194,97,254,0.15),hsla(0,0%,100%,0));"
    class="hx:aspect-auto hx:md:aspect-[1.1/1] hx:max-md:min-h-[340px]"
  >}}
  {{< hextra/feature-card
    title="Rich IDE Support"
    subtitle="The IntelliJ plugin formats tables, highlights syntax, and catches errors as you type."
    image="intellij-plugin-light.png"
    imageClass="hx:absolute hx:max-w-none hx:top-[40%] hx:left-[24px] hx:w-[180%] hx:sm:w-[110%] hx:dark:opacity-80"
    style="background: radial-gradient(ellipse at 50% 80%,rgba(142,218,252,0.15),hsla(0,0%,100%,0));"
    class="hx:aspect-auto hx:md:aspect-[1.1/1] hx:max-md:min-h-[340px]"
  >}}
  {{< hextra/feature-card
    title="Complete Ecosystem"
    subtitle="Formatter, test reporter, Spring Boot and Quarkus support. Works with Java 21+ and Kotlin."
  >}}
{{< /hextra/feature-grid >}}
</div>
