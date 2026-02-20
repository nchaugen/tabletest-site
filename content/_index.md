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
  Make business rules explicit, not buried in code.&nbsp;<br class="sm:hx-block hx-hidden" />
  One method, multiple scenarios.
{{< /hextra/hero-subtitle >}}
</div>

<div class="hx:mb-6">
{{< hextra/hero-button text="Get Started" link="docs/getting-started/introduction" >}}
{{< hextra/hero-button text="How it works" link="docs/guide" style="background: transparent !important; border: 1px solid currentColor; color: inherit;" >}}
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
    title="Tests as Documentation"
    subtitle="The table format makes test scenarios readable by anyone on the team. Business rules are explicit, not buried in code."
  >}}
  {{< hextra/feature-card
    title="Effortless Coverage"
    subtitle="Scannable tables reveal coverage gaps at a glance. Adding a scenario is just adding a row."
  >}}
  {{< hextra/feature-card
    title="Rich Tool Support"
    subtitle="IntelliJ plugin for formatting and syntax highlighting, plus a CLI formatter and test reporter."
  >}}
  {{< hextra/feature-card
    title="Built on JUnit"
    subtitle="A JUnit extension, not a separate framework. Use the same assertions, mocking frameworks, and test utilities you already know."
  >}}
  {{< hextra/feature-card
    title="Java and Kotlin"
    subtitle="Write table-driven tests in either language. Same annotation, same table format."
  >}}
  {{< hextra/feature-card
    title="Ready for Production"
    subtitle="Works with your existing test runner and build tool. Supports Spring Boot and Quarkus."
  >}}
{{< /hextra/feature-grid >}}
</div>
