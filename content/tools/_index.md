---
title: "Tools"
description: >-
  The TableTest toolchain: the JUnit extension, the table formatter, the HTML reporter,
  and the IntelliJ and VS Code extensions.
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

**Latest version:** {{< param currentTableTestVersion >}} | **Available on:** [Maven Central](https://central.sonatype.com/artifact/org.tabletest/tabletest-junit) | **Source:** [GitHub](https://github.com/nchaugen/tabletest)

## IntelliJ Plugin

IDE support for working with TableTest tables inside IntelliJ IDEA. The plugin provides syntax highlighting, automatic column alignment, comment toggling, and row reordering — all with familiar keyboard shortcuts.

- **Auto-formatting** — press `Cmd+Option+L` (macOS) or `Ctrl+Alt+L` to align columns
- **Syntax highlighting** — distinct colours for headers, delimiters, values, and comments
- **Language injection** — automatic TableTest language support in `@TableTest` annotations

**Latest version:** {{< param currentIntelliJVersion >}} | **Available on:** [JetBrains Marketplace](https://plugins.jetbrains.com/plugin/27334-tabletest) | **Source:** [GitHub](https://github.com/nchaugen/tabletest-intellij)

## VS Code Extension

IDE support for working with TableTest tables inside Visual Studio Code. The extension provides syntax highlighting, automatic column alignment, and cell value normalisation for lists, sets, and maps.

- **Auto-formatting** — format tables via `Format Document` or the `TableTest: Format All Tables in Document` command
- **Syntax highlighting** — distinct colours for headers, delimiters, values, and comments
- **Language injection** — automatic TableTest language support in `@TableTest` annotations in Java and Kotlin

**Latest version:** {{< param currentVSCodeVersion >}} | **Available on:** [VS Code Marketplace](https://marketplace.visualstudio.com/items?itemName=tabletest.tabletest) | **Source:** [GitHub](https://github.com/nchaugen/tabletest-vscode)

## Formatter

A formatting tool for consistent table layout across your codebase. Formats tables in Java files, Kotlin files, and standalone `.table` files.

- **Spotless plugin** for Gradle and Maven — natively supported in Spotless Gradle 8.3.0+ and Maven 3.3.0+ (Java 21+), or Gradle 8.4.0+ and Maven 3.4.0+ (Java 17+ and `.table` file support)
- **CLI tool** for standalone formatting or CI integration
- **EditorConfig support** — reads indent style and size from `.editorconfig`

The formatter is safe by default: it returns input unchanged on parse errors, so it never breaks your build.

**Latest version:** {{< param currentFormatterVersion >}} | **Available on:** [Maven Central](https://central.sonatype.com/search?q=org.tabletest+tabletest-formatter) | **Source:** [GitHub](https://github.com/nchaugen/tabletest-formatter)

## Reporter

Turns your TableTest tests into a specification anyone can read. Run your tests, then run the reporter to publish the tables as living documentation beside your project docs.

- **Self-contained HTML** — a static site with no external reference: a navigation tree that folds, search across the whole report, keyboard control, and a light/dark theme
- **Pass/fail throughout** — a status dot on every entry, per-row and per-cell colouring, and a scenario pass rate on each page
- **Column roles** — mark a column as lines of text, a tree, or a role of your own, and the report styles it
- **AsciiDoc and Markdown** — for a site generator you already run, with front matter you configure
- **Gradle plugin**, **Maven plugin** and **CLI** — integrates into your build
- **Custom templates** — extend or replace the built-in templates with Pebble, or define a format of your own

Every page under [Specifications](/spec/) is a reporter HTML report, generated from the test suite of the tool it describes.

**Latest version:** {{< param currentReporterVersion >}} | **Available on:** [Maven Central](https://central.sonatype.com/search?q=org.tabletest+tabletest-reporter) | **Source:** [GitHub](https://github.com/nchaugen/tabletest-reporter)

## Claude Code Plugin

Three guided skills for writing table-driven tests from inside [Claude Code](https://claude.ai/claude-code), the AI coding assistant from Anthropic.

- **`/spec-by-example`** — clarify behaviour by working through concrete examples as a table, using business language throughout; the resulting table maps directly to a `@TableTest`
- **`/tabletest`** — create `@TableTest` methods with guided table design, syntax reference, and a quality checklist; handles both new tests and converting similar `@Test` methods
- **Table-driven testing** — the same table design rules outside the JVM: pytest `parametrize`, Swift Testing `@Test(arguments:)`, Jest and Vitest `test.each`, Go table-driven subtests, and xUnit `[Theory]`. Claude reaches for this skill on its own when the project is not Java or Kotlin
- **Auto-formatting** — tables are automatically aligned after every file edit via a PostToolUse hook

Install from the Claude Code CLI:

```shell
/plugin marketplace add https://tabletest.org/marketplace.json
/plugin install tabletest@tabletest.org
```

**Latest version:** {{< param currentClaudePluginVersion >}} | **Available on:** [Claude Code Marketplace](https://tabletest.org/marketplace.json) | **Source:** [GitHub](https://github.com/nchaugen/tabletest-claude-plugin)

## Source Code

All tools are open source under Apache Licence 2.0:

- [tabletest](https://github.com/nchaugen/tabletest) — Core library
- [tabletest-intellij](https://github.com/nchaugen/tabletest-intellij) — IntelliJ plugin
- [tabletest-vscode](https://github.com/nchaugen/tabletest-vscode) — VS Code extension
- [tabletest-formatter](https://github.com/nchaugen/tabletest-formatter) — Formatting tool
- [tabletest-reporter](https://github.com/nchaugen/tabletest-reporter) — Documentation generator
- [tabletest-claude-plugin](https://github.com/nchaugen/tabletest-claude-plugin) — Claude Code plugin
