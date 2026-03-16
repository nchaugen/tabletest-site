---
title: Changelog
toc: false
---

Changes across the TableTest ecosystem, sorted newest first.

## 2026-03-15 — TableTest VS Code v0.0.7

### Added

- Formatter and diagnostics support for quoted map keys in map literals (both `"key"` and `'key'`), while preserving key quoting style and normalising map spacing.
- Diagnostics for invalid unquoted map keys in TableTest cells.
- Formatting and diagnostics support for fully-qualified Java `@org.tabletest.junit.TableTest(...)` annotations.
- Standalone `.table` range formatting support so `Format Selection` works as documented.
- Distinct syntax highlighting for question-mark header cells, with stronger header emphasis across `.table`, Java, and Kotlin.

### Changed

- Table parsing and formatting now follow the canonical TableTest parser more closely for quoted-map-key cases and other edge-case bare values.
- Comment indentation now normalises consistently to the table left edge in standalone files and Java/Kotlin host strings.
- Local test entrypoints now mirror CI with `npm run test:unit`, `npm run test:integration:strict`, and `npm run test:full`.
- Syntax highlighting now uses clearer, more consistent theme families for headers, separators, strings, and map keys across `.table`, Java, and Kotlin contexts.
- Standalone `.table` files now suppress bracket-pair colour rotation for table content.

### Fixed

- Bare unquoted scalar values containing commas or colons no longer trigger diagnostics.
- Header highlighting now correctly skips leading table comments before the real header row.
- Quoted map keys no longer miscolour bracket, brace, or parenthesis characters as structural punctuation.
- Java string-array escaping no longer breaks column alignment or leaks highlighting into surrounding annotation code.
- Java text blocks with comments before the implicit value no longer pick up extra indentation during formatting.


[GitHub Release](https://github.com/nchaugen/tabletest-vscode/releases/tag/v0.0.7)

---

## 2026-03-15 — TableTest IntelliJ Plugin v0.4.1

### Added

- Support for leading comments (comments at the very beginning of a table).
- Support for escaped quotes (`\"`, `\'`) and backslashes (`\\`) in quoted strings and map keys.
- Methods annotated with `@TableTest` are now recognized as entry points, suppressing "Unused declaration" inspections.

### Changed

- Upgraded `tabletest-parser` to 1.2.0.
- Upgraded IntelliJ Platform Gradle Plugin to 2.13.0.

### Fixed

- Table formatting is now disabled for files with syntax errors to prevent accidental code corruption.
- Map keys only require quotes if they contain spaces or commas.
- Improved parsing of rows at the end of a file without a trailing newline.
- Resolved a compatibility warning for IntelliJ 2026.1.

[GitHub Release](https://github.com/nchaugen/tabletest-intellij/releases/tag/v0.4.1)

---

## 2026-03-12 — TableTest Formatter 1.1.1

### Added
- Support quoted map keys, both single- and double-quoted

[GitHub Release](https://github.com/nchaugen/tabletest-formatter/releases/tag/tabletest-formatter-1.1.1)

---

## 2026-03-12 — TableTest 1.2.1

### Added
- Quoted map keys: map keys can now be single or double quoted, enabling keys containing whitespace, colons, commas, brackets, and other special characters (e.g. `["key with spaces": value]`)

[GitHub Release](https://github.com/nchaugen/tabletest/releases/tag/tabletest-junit-1.2.1)

---

## 2026-03-12 — TableTest IntelliJ Plugin v0.4.0

### Added

- Support for single and double quoted map keys in TableTest maps.
- TableTest Code Style settings page under `Settings > Editor > Code Style > TableTest` with value spacing controls for commas, colons, and bracket/brace interiors.

[GitHub Release](https://github.com/nchaugen/tabletest-intellij/releases/tag/v0.4.0)

---

## 2026-03-10 — TableTest 1.2.0

### Added
- Array parameter support: list syntax `[a, b, c]` now converts to array types, e.g. `String[]`, `int[]`, `Map<K,V>[]`, nested arrays. Thanks to @AlexeyKuznetsov-DD for the contribution!

[GitHub Release](https://github.com/nchaugen/tabletest/releases/tag/tabletest-junit-1.2.0)

---

## 2026-03-09 — TableTest Formatter 1.1.0

### Added
- Support for Java string array syntax `@TableTest({"header | col", "val | val"})` alongside existing text block syntax
### Changed
- Lowered Java requirement from 21 to 17
### Removed
- `tabletest-formatter-spotless` module — TableTest formatting is now supported natively by [Spotless](https://github.com/diffplug/spotless)

[GitHub Release](https://github.com/nchaugen/tabletest-formatter/releases/tag/tabletest-formatter-1.1.0)

---

## 2026-03-09 — TableTest IntelliJ Plugin v0.3.1

### Fixed

- Kotlin plugin mode compatibility declaration is now loaded from the main plugin descriptor, removing K2 compatibility warnings in plugin verification.
- Expected header styling now inherits from language defaults (`Static method`) by default.

[GitHub Release](https://github.com/nchaugen/tabletest-intellij/releases/tag/v0.3.1)

---

## 2026-03-08 — TableTest IntelliJ Plugin v0.3.0

### Added

- Support Java `@TableTest` static string-array values for TableTest injection and table formatting.
- Align closing quotes in Java `@TableTest` string arrays by normalising row lengths after formatting.

### Changed

- Expected header default fallback style is now `Classes -> Static method`.

### Fixed

- Fix Kotlin mode compatibility by aligning minimum build; keep Java implicit-usage when Kotlin plugin is disabled.
- Prevent zero-length `COMMENT` token matches in the lexer (avoids potential non-termination warnings).


[GitHub Release](https://github.com/nchaugen/tabletest-intellij/releases/tag/v0.3.0)

---

## 2026-03-08 — TableTest 1.1.0

### Changed
- Minimum Java version lowered from 21 to 8
- `@TableTest` value parameter accepts a string array to support Java versions without text blocks
### Added
- Java 8 compatibility tests in CI
### Upgrading from 1.0.0
This release is **source compatible** but **binary incompatible** with 1.0.0.

- **A clean rebuild is required**: run `mvn clean test` or `gradle clean test` after upgrading
- Upgrading without a clean rebuild (e.g. `mvn test` alone) will fail with `AnnotationTypeMismatchException`
- Recompiling without cleaning (e.g. `mvn compile test`) is also insufficient — Maven's incremental compiler does not detect annotation return type changes
- **Kotlin projects are not affected**: the Kotlin incremental compiler handles the annotation change automatically

[GitHub Release](https://github.com/nchaugen/tabletest/releases/tag/tabletest-junit-1.1.0)

---

## 2026-03-08 — TableTest VS Code v0.0.6

**Full Changelog**: https://github.com/nchaugen/tabletest-vscode/compare/v0.0.5...v0.0.6

[GitHub Release](https://github.com/nchaugen/tabletest-vscode/releases/tag/v0.0.6)

---

## 2026-03-06 — TableTest Claude Code Plugin v1.3.0

## Changes

### Changed
- **tabletest**: Simplified pre-check — dependency and shape checks rewritten as readable prose rather than a prescriptive checklist
- **tabletest**: Improved skill trigger description so the skill activates on value-set, type-converter, and column-design questions even when the user doesn't say "TableTest" explicitly
- **tabletest**: Pair programming guidance extracted to `references/pair-programming.md`; SKILL.md retains the key habit (show a mockup first) with a pointer to the full cadence
- **spec-by-example**: Improved skill trigger description — now activates on vague requirements and mid-implementation edge cases, not just upfront spec work
- **spec-by-example**: Expanded value-set guidance with a dedicated state/status example (`{PENDING, CONFIRMED}`) and an explicit callout that blank and value-set mean different things and must not be conflated
- **spec-by-example**: Clearer handoff section linking to `/tabletest` with column-translation notes

### Added
- **tabletest**: Date format limitation warning — built-in `LocalDate`/`LocalDateTime` conversion handles ISO 8601 only; non-standard formats require a `@TypeConverter`
- **Plugin**: Updated description to cover both skills; keywords updated (`spec-by-example`, `example mapping` added; `fit`, `acceptance testing` removed)

**Full changelog**: https://github.com/nchaugen/tabletest-claude-plugin/blob/main/CHANGELOG.md

[GitHub Release](https://github.com/nchaugen/tabletest-claude-plugin/releases/tag/v1.3.0)

---

## 2026-02-28 — TableTest Claude Code Plugin v1.2.0

### Added
- Spec-by-example skill (`/spec-by-example`) for clarifying behaviour with multiple cases or rules through concrete example tables
  - Elicitation workflow: naming the concern, finding the first example, identifying columns, probing for edge cases and irrelevant inputs
  - Example table design principles: one concern per table, business language throughout, concrete domain values, traceable outputs, thresholds visible as columns, conditions as scenario names
  - Multiple-table guidance: when to split, how to let additional tables emerge naturally
  - Bridge from example table to `@TableTest`: direct column mapping, value set carry-over, handoff to `/tabletest` skill
  - Quality checklist for example tables

[GitHub Release](https://github.com/nchaugen/tabletest-claude-plugin/releases/tag/v1.2.0)

---

## 2026-02-25 — TableTest Claude Code Plugin v1.1.0

### Added
- Non-obvious built-in type conversions reference table (enums, hex/octal integers, `Class<?>` variants, `Duration`, `Period`, `Currency`, `Locale`)
- Minimal quoting strategy: start without quotes, add only where needed
- Guidance on quoting inside collection elements rather than wrapping the whole collection
- Newline handling in table values (`\\n` + manual replace in test method)
- Set `{}` vs List `[]` common mistake callout
- Single-scenario `@TableTest` exception in pre-check
- Guidance on when NOT to use TableTest (trivial implementations, complex setup, already covered by integration tests)
- New advanced design pattern: separate tables when column sets diverge
- New common pattern: static constants for readable expected values (e.g. ANSI codes)

[GitHub Release](https://github.com/nchaugen/tabletest-claude-plugin/releases/tag/v1.1.0)

---

## 2026-02-25 — TableTest Claude Code Plugin v1.0.0

### Added
- TableTest skill for writing and converting JUnit tests to TableTest format
- Reference guides: dependency setup, value sets, type converters, column design, common patterns, large tables, example patterns, async and performance, provided parameters, advanced table design, incremental development, consolidating tests, testing reveals bugs

[GitHub Release](https://github.com/nchaugen/tabletest-claude-plugin/releases/tag/v1.0.0)

---

## 2026-02-23 — TableTest Formatter 1.0.1

### Changed
- Merged config module into core so integrations only need a single Maven coordinate (`org.tabletest:tabletest-formatter-core`)


[GitHub Release](https://github.com/nchaugen/tabletest-formatter/releases/tag/tabletest-formatter-1.0.1)

---

## 2026-02-23 — TableTest Reporter 1.0.1

### Fixed
- `FileSystemException: File name too long` when test methods have long fully qualified parameter type signatures (e.g. overloaded methods with complex types)


[GitHub Release](https://github.com/nchaugen/tabletest-reporter/releases/tag/tabletest-reporter-1.0.1)

---

## 2026-02-23 — TableTest VS Code v0.0.5

### Added
- Theme-aware comment highlighting for `//` table comments in `.table`, Java, and Kotlin table contexts.
- Dedicated map key highlighting scope for map literals (`support.type.property-name.tabletest`).
- Recursive syntax highlighting for nested list/set/map values.
- Warnings for additional malformed collection patterns:
  - trailing-comma empty elements (for example `[a, b,]`)
  - map entries without values (for example `[key:]`)
  - map values with extra top-level colons (for example `[a: b:c:d]`)

### Changed
- Extension activation now includes `onLanguage:java` and `onLanguage:kotlin`, so diagnostics appear without running formatting first.
- Extension metadata keywords were expanded for discovery in Marketplace search.
- Release packaging now includes repository metadata and an explicit `.vscodeignore` to avoid shipping development-only files.

### Fixed
- Nested collection values that were previously tokenized as plain unquoted text now receive correct collection scopes.

[GitHub Release](https://github.com/nchaugen/tabletest-vscode/releases/tag/v0.0.5)

---

## 2026-02-22 — TableTest VS Code v0.0.4

### Added
- Header-specific grammar scope for table header cells (`entity.name.column.tabletest`).
- Wider Unicode coverage for alignment tests (emoji sequences, keycaps, flags, and mixed scripts).
- Tab-width regression tests for width calculation and formatter output.

### Changed
- Formatter width calculation is now grapheme-aware for Unicode and emoji.
- Tab expansion is now aligned to tab stops using actual column start offsets.
- Formatter now resolves table tab size from document editor settings before runtime fallback.

### Fixed
- Comment indentation drift when reformatting tables with mixed row indentation.
- Closing triple-quote alignment after formatting Java/Kotlin annotation tables.
- Java table indentation consistency when one file reports different runtime tab-size options.
- Header token colouring now differs reliably from data rows across themes.

[GitHub Release](https://github.com/nchaugen/tabletest-vscode/releases/tag/v0.0.4)

---

## 2026-02-22 — TableTest VS Code v0.0.3

### Added
- Publishing setup for VS Code Marketplace under `tabletest` publisher.
- Configurable extra table indentation via `tabletest.format.extraIndentLevel`.
- Diagnostics for malformed collection cells in tables.
- CI workflows for tests, integration checks, and tagged releases.

### Changed
- Java/Kotlin `@TableTest` value parsing to better match real annotation usage.
- Kotlin default table indentation behaviour to align with common triple-quote style.
- README and extension metadata for marketplace use (icon, install guidance, docs links).

### Fixed
- Java/Kotlin injection grammar edge cases around triple-quoted content.
- Release workflow validation and token-gated publishing steps.

[GitHub Release](https://github.com/nchaugen/tabletest-vscode/releases/tag/v0.0.3)

---

## 2026-02-21 — TableTest IntelliJ Plugin v0.2.2

### Changed

- Upgraded dependencies intellij-platform, grammarkit and kotlin

### Fixed

- Added Kotlin plugin compatibility declaration

[GitHub Release](https://github.com/nchaugen/tabletest-intellij/releases/tag/v0.2.2)

---

## 2026-02-17 — TableTest Formatter 1.0.0

### Added
- Support for reading indent style and size from .editorconfig
### Removed
- BREAKING: Removed config parameters for indent style and size, instead use .editorconfig to override defaults
### Changed
- Moved to new org.tabletest coordinates, please update your dependencies accordingly
### Fixed
- Corrected emoji display width calculation

[GitHub Release](https://github.com/nchaugen/tabletest-formatter/releases/tag/tabletest-formatter-1.0.0)

---

## 2026-02-16 — TableTest Reporter 1.0.0

### Changed
- Migrated to org.tabletest coordinates, please update
- Support `io.nchaugen.tabletest.junit.*` annotations for backwards compatibility


[GitHub Release](https://github.com/nchaugen/tabletest-reporter/releases/tag/tabletest-reporter-1.0.0)

---

## 2026-02-15 — TableTest Reporter 0.4.0

### Changed
- Index files now show all levels of nested items by default. Set indexDepth = 1 to restore previous behaviour.
### Added
- Configurable index depth to control how many levels of nested items appear in each index file
- Simplified setup for Gradle: plugin automatically adds `tabletest-reporter-junit` dependency and configures JUnit extension autodetection (Maven continues to require manual setup)
- Support `org.tabletest.junit.*` annotations


[GitHub Release](https://github.com/nchaugen/tabletest-reporter/releases/tag/tabletest-reporter-0.4.0)

---

## 2026-02-15 — TableTest 1.0.0

### Changed
- BREAKING CHANGE: Artefacts now published to Maven Central with group id `org.tabletest`
- BREAKING CHANGE: TableTest annotations moved to package `org.tabletest.junit`. Please update imports.
- BREAKING CHANGE: `@FactorySources` annotation replaced with `@TypeConverterSources` in new package
- Old annotations in package `io.github.nchaugen.tabletest.junit` now deprecated (still works but will be removed in a future release)
### Added
- `@TypeConverter` annotation for tagging custom converter methods (formerly refered to as "factory methods")


[GitHub Release](https://github.com/nchaugen/tabletest/releases/tag/tabletest-junit-1.0.0)

---

## 2026-02-05 — TableTest IntelliJ Plugin v0.2.1

### Added

- Support `org.tabletest.junit.TableTest` annotations
- Treat `@TypeConverter` methods as used in both Java and Kotlin by the unused declaration/symbol inspections

[GitHub Release](https://github.com/nchaugen/tabletest-intellij/releases/tag/v0.2.1)

---

## 2026-02-02 — TableTest Reporter 0.3.2

### Added
- Auto-detection of JUnit output directory from Maven Surefire and Gradle test task configurations
### Changed
- Upgraded Pebble template engine to 4.1.0 (security fix)
- Published YAML files now include additional metadata — YAML files from 0.3.x must be regenerated by re-running tests with the updated JUnit extension
- Report output structure now derived from class/package names in YAML metadata instead of input directory layout
- CLI, Maven plugin, and Gradle plugin now display file count on successful generation
- Empty input directories now show informational message instead of silent success
### Fixed
- AsciiDoc description list nesting now cycles colon delimiters to stay within AsciiDoctor's 4-colon limit (issue #11)
- YAML parsing errors now include file path for easier debugging

[GitHub Release](https://github.com/nchaugen/tabletest-reporter/releases/tag/tabletest-reporter-0.3.2)

---

## 2026-01-21 — TableTest Reporter 0.3.1

### Changed
- `.passed`/`.failed` roles now only applied to tables with a scenario column as row and test results correlation is otherwise not possible

### Fixed
- AsciiDoc index-to-index links now generate as proper file paths instead of anchor references in HTML output
- Empty index files no longer generated for test classes without TableTest methods
- Scenario names containing parentheses now match correctly (previously truncated at first opening parenthesis)
- Error messages in failed rows now properly separated from closing delimiter with newline (affects both AsciiDoc and Markdown)


[GitHub Release](https://github.com/nchaugen/tabletest-reporter/releases/tag/tabletest-reporter-0.3.1)

---

## 2026-01-17 — TableTest IntelliJ Plugin v0.2.0

### Changed

- Expected header columns are now bold by default
- Data rows now align with header row position in Kotlin tables, allowing manual control of table indentation

### Fixed

- Quoted strings containing delimiters (commas, brackets, braces) inside compound structures were incorrectly parsed
- Comments now align with header and data rows in Kotlin raw strings (previously comments lost their indentation)
- Data rows following comments now re-align correctly in Java text blocks (previously rows with extra indentation stayed misaligned)
- Lines with varying input indentation now normalize to a consistent alignment position

[GitHub Release](https://github.com/nchaugen/tabletest-intellij/releases/tag/v0.2.0)

---

## 2026-01-16 — TableTest IntelliJ Plugin v0.1.0

### Added
- Move row up/down with keyboard shortcuts (Cmd+Shift+Up/Down on Mac, Alt+Shift+Up/Down on Windows/Linux)
### Fixed
- First-line comments were incorrectly parsed as headers
- Unpaired quotes in unquoted strings were incorrectly shown as illegal


[GitHub Release](https://github.com/nchaugen/tabletest-intellij/releases/tag/v0.1.0)

---

## 2026-01-03 — TableTest Formatter 0.1.0

### Added
- **TableTest table formatting** for Java and Kotlin files (with `@TableTest` annotations), and standalone `.table` files
- **Column alignment** with proper spacing, Unicode/emoji width handling, and collection literal formatting
- **Command-line tool** for formatting files and directories with check mode for CI integration
- **Spotless integration** (Gradle) with configurable indentation and formatting options
- **Graceful error handling** that never breaks builds when encountering malformed tables
- **Indentation alignment**: Aligns tables relative to `@TableTest` annotation, preserving source indentation style (tabs/spaces) with configurable extra indentation

[GitHub Release](https://github.com/nchaugen/tabletest-formatter/releases/tag/tabletest-formatter-0.1.0)

---

## 2025-12-21 — TableTest Reporter 0.3.0

### Added
- Custom output format support – define formats like HTML, XML, JSON via templates
- Support for both extension (child templates) and complete template replacement of built-in templates
- Template extension blocks (frontMatter, title, description, table/contents, footer) for customisation
- New runner options to specify custom template directory and to list all available output formats


[GitHub Release](https://github.com/nchaugen/tabletest-reporter/releases/tag/tabletest-reporter-0.3.0)

---

## 2025-12-19 — TableTest Reporter 0.2.1

### Added
- Configurable expectation column pattern via `tabletest.reporter.expectation.pattern` configuration parameter
### Fixed
- Parameter types no longer included in the test title generated from the method name
- Passed/failed roles now added correctly when scenario name is null or empty string 

[GitHub Release](https://github.com/nchaugen/tabletest-reporter/releases/tag/tabletest-reporter-0.2.1)

---

## 2025-12-15 — TableTest Reporter 0.2.0

## [0.2.0] - 2025-12-15
### Added
- JUnit extension to collect TableTest report data during test runs (tabletest-reporter-junit)
- Multiple roles supported per cell in published YAML
- Roles added to signal if a row passed or failed
- CamelCase and snake_case aware slugified YAML file name generation
- Human-readable titles for test classes and methods without `@DisplayName` annotation
- Test class and package index pages rendered with proper title of child pages 
### Changed
- YAML files prefixed with `TABLETEST-` to avoid conflicts with other YAML files
- Output file name for TableTest methods either explicit `@DisplayName` or method name (without parameters)

[GitHub Release](https://github.com/nchaugen/tabletest-reporter/releases/tag/tabletest-reporter-0.2.0)

---

## 2025-12-15 — TableTest 0.5.8

### Fixed
- Sets retain order through conversion
- Restored compatibility for JUnit 5.11-5.12
### Removed
- Reporting functionality moved to [TableTest-Reporter](https://github.com/nchaugen/tabletest-reporter)


[GitHub Release](https://github.com/nchaugen/tabletest/releases/tag/tabletest-0.5.8)

---

## 2025-12-09 — TableTest 0.5.7

### Fixed
- Reverted accidental usage of JUnit MediaType moved in JUnit 5.14 to restore compatibility with JUnit 5.12 upwards 

[GitHub Release](https://github.com/nchaugen/tabletest/releases/tag/tabletest-0.5.7)

---

## 2025-12-09 — TableTest Reporter 0.1.1

### Added
- Core [TableTest](https://github.com/nchaugen/tabletest) reporting functionality
- Support for AsciiDoc and Markdown output formats
- Template-based rendering using Pebble template engine
- Slugified output directories and filenames

[GitHub Release](https://github.com/nchaugen/tabletest-reporter/releases/tag/tabletest-reporter-0.1.1)

---

## 2025-12-08 — TableTest 0.5.6

### Added
- Publishing to YAML format by default
- Role `scenario` added to cells in report scenario column
- Role `expectation` added to cells in report columns where header name ends in `?`
- Using `@DisplayName` as test and table title in reports
- Added `@Description` annotation for test and table descriptions in reports
### Fixed
- Preventing table values from being misinterpreted as markup when rendered to AsciiDoc
- Including explicit whitespace in reports

[GitHub Release](https://github.com/nchaugen/tabletest/releases/tag/tabletest-0.5.6)

---

## 2025-11-11 — TableTest 0.5.5

### Added
- Configurable styling of lists and sets in AsciiDoc format
### Fixed
- Detects `@ConvertWith` parameter annotation when used in custom composed annotations
- Published AsciiDoc files now uses `.adoc` extension instead of `.asciidoc`
- Corrected AsciiDoc rendering of collections containing an empty collection

[GitHub Release](https://github.com/nchaugen/tabletest/releases/tag/tabletest-0.5.5)

---

## 2025-11-03 — TableTest 0.5.4

### Added
- Configurable publishing of tables to TableTest, Markdown and AsciiDoc formats
### Changed
- Set and maps conserve insertion order
- Improved error message for table parse failures


[GitHub Release](https://github.com/nchaugen/tabletest/releases/tag/tabletest-0.5.4)

---

## 2025-10-26 — TableTest 0.5.3

### Added
- Unsuccessful parsing of table fails the test with TableTestParseException pointing to the problematic section


[GitHub Release](https://github.com/nchaugen/tabletest/releases/tag/tabletest-0.5.3)

---

## 2025-09-14 — TableTest 0.5.2

### Changed
- Empty quoted values no longer convert to `null` for non-string types

### Fixed
- Support for factory methods returning primitive type


[GitHub Release](https://github.com/nchaugen/tabletest/releases/tag/tabletest-0.5.2)

---

## 2025-08-20 — TableTest 0.5.1

### Added
- Inherited factory methods are now found and used (for Java-based tests, inheritance of static methods not supported in Kotlin) 
- Compatibility tests for build systems (Maven, Gradle) and frameworks (JUnit, Quarkus, SpringBoot)
### Changed
- Easier install as dependent JUnit modules are no longer packaged with TableTest distribution
- TableTestException now of type ParameterResolutionException
- Improved user guide on value conversion topic

[GitHub Release](https://github.com/nchaugen/tabletest/releases/tag/tabletest-0.5.1)

---

## 2025-06-21 — TableTest 0.5.0

## Added

- Upped JUnit dependency to 5.13.1
- Descriptive error messages
- Allow test method parameters provided by parameter resolvers (`TestInfo`, `TestReporter`, etc.)
- Unique test invocation display names when using value sets
- Explicit scenario name column with `@Scenario` annotated parameter can be in any position

## Changed

- More concise README.md, moved details to USERGUIDE.md


[GitHub Release](https://github.com/nchaugen/tabletest/releases/tag/tabletest-0.5.0)

---

## 2025-06-09 — TableTest 0.4.0

## Added
- TableTest will search classes listed in new annotation `@FactorySources` for factory methods
- For `@Nested` test classes, TableTest will search enclosing test classes for factory methods
- Blank cell converts to null also for String types

## Fixed
- Explicit leading and trailing whitespace in a quoted string is no longer removed
- More robust analysis of parameterized target types



[GitHub Release](https://github.com/nchaugen/tabletest/releases/tag/tabletest-0.4.0)

---

## 2025-06-02 — TableTest 0.3.1

See [changelog](https://github.com/nchaugen/tabletest/blob/main/CHANGELOG.md)

[GitHub Release](https://github.com/nchaugen/tabletest/releases/tag/tabletest-0.3.1)

---

## 2025-06-01 — TableTest 0.3.0

See [changelog](https://github.com/nchaugen/tabletest/blob/main/CHANGELOG.md)

[GitHub Release](https://github.com/nchaugen/tabletest/releases/tag/tabletest-0.3.0)

---

## 2025-05-25 — TableTest 0.2.1

See changelogs for [tabletest-junit](https://github.com/nchaugen/tabletest/blob/main/tabletest-junit/CHANGELOG.md) and [tabletest-parser](https://github.com/nchaugen/tabletest/blob/main/tabletest-parser/CHANGELOG.md)

[GitHub Release](https://github.com/nchaugen/tabletest/releases/tag/tabletest-0.2.1)

---

## 2025-05-22 — TableTest 0.2.0

See changelogs for [tabletest-junit](https://github.com/nchaugen/tabletest/blob/main/tabletest-junit/CHANGELOG.md) and [tabletest-parser](https://github.com/nchaugen/tabletest/blob/main/tabletest-parser/CHANGELOG.md)

[GitHub Release](https://github.com/nchaugen/tabletest/releases/tag/tabletest-0.2.0)

---

## 2025-05-11 — TableTest 0.1.0

See changelogs for [tabletest-junit](https://github.com/nchaugen/tabletest/blob/main/tabletest-junit/CHANGELOG.md) and [tabletest-parser](https://github.com/nchaugen/tabletest/blob/main/tabletest-parser/CHANGELOG.md)

[GitHub Release](https://github.com/nchaugen/tabletest/releases/tag/tabletest-0.1.0)

---

