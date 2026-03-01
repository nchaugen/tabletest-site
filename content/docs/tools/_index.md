---
title: "Tools"
weight: 4
bookCollapseSection: false
---

TableTest provides a suite of tools to enhance your data-driven testing workflow. Each tool serves a specific purpose and can be used independently or together.

## Core Library

The foundation — a JUnit extension that enables table-driven testing with the `@TableTest` annotation.

- **Java and Kotlin** — works with both languages out of the box
- **Behaviour-focused** — tables document expected behaviour as readable examples
- **Standard JUnit** — runs alongside regular `@Test` methods with no special setup
- **Extensible** — custom type converters, external table files, and value sets for advanced scenarios

**Available on:** [Maven Central](https://central.sonatype.com/artifact/org.tabletest/tabletest-junit) | **Source:** [GitHub](https://github.com/nchaugen/tabletest)

## IntelliJ Plugin

IDE support for working with TableTest tables inside IntelliJ IDEA. The plugin provides syntax highlighting, automatic column alignment, comment toggling, and row reordering — all with familiar keyboard shortcuts.

- **Auto-formatting** — press `Cmd+Option+L` (macOS) or `Ctrl+Alt+L` to align columns
- **Syntax highlighting** — distinct colours for headers, delimiters, values, and comments
- **Language injection** — automatic TableTest language support in `@TableTest` annotations

**Available on:** [JetBrains Marketplace](https://plugins.jetbrains.com/plugin/27334-tabletest) | **Source:** [GitHub](https://github.com/nchaugen/tabletest-intellij)

## VS Code Extension

IDE support for working with TableTest tables inside Visual Studio Code. The extension provides syntax highlighting, automatic column alignment, and cell value normalisation for lists, sets, and maps.

- **Auto-formatting** — format tables via `Format Document` or the `TableTest: Format All Tables in Document` command
- **Syntax highlighting** — distinct colours for headers, delimiters, values, and comments
- **Language injection** — automatic TableTest language support in `@TableTest` annotations in Java and Kotlin

**Available on:** [VS Code Marketplace](https://marketplace.visualstudio.com/items?itemName=tabletest.tabletest) | **Source:** [GitHub](https://github.com/nchaugen/tabletest-vscode)

## Formatter

A formatting tool for consistent table layout across your codebase. Formats tables in Java files, Kotlin files, and standalone `.table` files.

- **Spotless plugin** for Gradle — automatic formatting in your build
- **CLI tool** for standalone formatting or CI integration
- **Maven integration** via exec-maven-plugin
- **EditorConfig support** — reads indent style and size from `.editorconfig`

The formatter is safe by default: it returns input unchanged on parse errors, so it never breaks your build.

**Available on:** [Maven Central](https://central.sonatype.com/search?q=org.tabletest+tabletest-formatter) | **Source:** [GitHub](https://github.com/nchaugen/tabletest-formatter)

## Reporter

Generates documentation from your TableTest tests. Run your tests, then run the reporter to turn test tables into readable AsciiDoc or Markdown documentation that you can publish alongside your project docs.

- **Gradle plugin** and **Maven plugin** — integrates into your build
- **Custom templates** — extend or replace built-in templates using Pebble
- **Pass/fail indicators** — rows are marked with test results in the generated output
- **Custom output formats** — define HTML, XML, or any format via templates

**Available on:** [Maven Central](https://central.sonatype.com/search?q=org.tabletest+tabletest-reporter) | **Source:** [GitHub](https://github.com/nchaugen/tabletest-reporter)

## Claude Code Plugin

Two guided skills for writing TableTest-style tests from inside [Claude Code](https://claude.ai/claude-code), the AI coding assistant from Anthropic.

- **`/spec-by-example`** — clarify behaviour by working through concrete examples as a table, using business language throughout; the resulting table maps directly to a `@TableTest`
- **`/tabletest`** — create `@TableTest` methods with guided table design, syntax reference, and a quality checklist; handles both new tests and converting similar `@Test` methods
- **Auto-formatting** — tables are automatically aligned after every file edit via a PostToolUse hook

Install from the Claude Code CLI:

```shell
/plugin marketplace add https://tabletest.org/marketplace.json
/plugin install tabletest@tabletest.org
```

**Available on:** [Claude Code Marketplace](https://tabletest.org/marketplace.json) | **Source:** [GitHub](https://github.com/nchaugen/tabletest-claude-plugin)

## Compatibility

All tools support:
- **Java:** 21 or above
- **Build Tools:** Maven and Gradle
- **Frameworks:** Spring Boot 3.4.0+, Quarkus 3.21.2+

## Source Code

All tools are open source under Apache Licence 2.0:

- [tabletest](https://github.com/nchaugen/tabletest) — Core library
- [tabletest-intellij](https://github.com/nchaugen/tabletest-intellij) — IntelliJ plugin
- [tabletest-vscode](https://github.com/nchaugen/tabletest-vscode) — VS Code extension
- [tabletest-formatter](https://github.com/nchaugen/tabletest-formatter) — Formatting tool
- [tabletest-reporter](https://github.com/nchaugen/tabletest-reporter) — Documentation generator
- [tabletest-claude-plugin](https://github.com/nchaugen/tabletest-claude-plugin) — Claude Code plugin
