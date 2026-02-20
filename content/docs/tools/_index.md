---
title: "Tools"
weight: 4
bookCollapseSection: false
draft: true
---

# TableTest Ecosystem

TableTest provides a complete suite of tools to enhance your data-driven testing workflow. Each tool serves a specific purpose and can be used independently or together.

## The Tools

### [Core Library](/docs/tools/library/)

The foundation - a JUnit 5 extension that enables table-driven testing with the `@TableTest` annotation.

**Use when:** Writing or running tests
**Available on:** [Maven Central](https://central.sonatype.com/artifact/io.github.nchaugen/tabletest-junit)

---

### [IntelliJ Plugin](/docs/tools/intellij-plugin/)

IDE support providing automatic table formatting, syntax highlighting, and real-time validation.

**Use when:** Developing tests in IntelliJ IDEA
**Available on:** [JetBrains Marketplace](https://plugins.jetbrains.com/plugin/27334-tabletest)

---

### [Reporter](/docs/tools/reporter/)

Generate web-based documentation from your TableTests, making test specifications accessible to non-technical stakeholders.

**Use when:** Publishing test documentation
**Available on:** [Maven Central](https://central.sonatype.com/search?q=tabletest-reporter) & [Gradle Plugin Portal](https://plugins.gradle.org/plugin/io.github.nchaugen.tabletest-reporter)

---

### [Formatter](/docs/tools/formatter/)

Command-line tool and build plugin for consistent table formatting across your codebase.

**Use when:** Enforcing formatting standards in CI/CD
**Available on:** [Maven Central](https://central.sonatype.com/search?q=tabletest-formatter)

---

## How They Work Together

```
┌─────────────────────────────────────────────────────┐
│  Development                                        │
│  ┌──────────────┐         ┌──────────────┐        │
│  │   IntelliJ   │ ◄─────► │   Library    │        │
│  │   Plugin     │         │   (Tests)    │        │
│  └──────────────┘         └──────────────┘        │
│         │                        │                  │
│         │                        │                  │
│         ▼                        ▼                  │
│  ┌──────────────┐         ┌──────────────┐        │
│  │  Formatter   │         │   Reporter   │        │
│  │  (CI/CD)     │         │   (Docs)     │        │
│  └──────────────┘         └──────────────┘        │
└─────────────────────────────────────────────────────┘
```

**Typical Workflow:**

1. **Write tests** using the core library with IDE plugin support
2. **Format tables** automatically with the plugin or manually via formatter
3. **Run tests** as part of your standard JUnit execution
4. **Generate documentation** with the reporter for stakeholder review
5. **Enforce standards** using the formatter in CI/CD pipelines

## Compatibility

All tools support:
- **Java:** 21 or above
- **Build Tools:** Maven and Gradle
- **Frameworks:** Spring Boot 3.4.0+, Quarkus 3.21.2+

## Migration Notice

> [!WARNING]
> **Future Group ID Change**
>
> All TableTest tools are being republished under the new group ID `org.tabletest`. Current artifacts under `io.github.nchaugen` will continue to work, but new versions will use the new group ID.
>
> **Timeline:** To be announced

## Source Code

All tools are open source under Apache License 2.0:

- [tabletest](https://github.com/nchaugen/tabletest) - Core library
- [tabletest-intellij](https://github.com/nchaugen/tabletest-intellij) - IntelliJ plugin
- [tabletest-reporter](https://github.com/nchaugen/tabletest-reporter) - Documentation generator
- [tabletest-formatter](https://github.com/nchaugen/tabletest-formatter) - Formatting tool

---

Explore each tool to see how it can improve your testing workflow.
