---
title: "Formatter"
weight: 4
draft: true
---

# TableTest Formatter

The TableTest Formatter is a command-line tool and build plugin that ensures consistent table formatting across your codebase, perfect for enforcing standards in CI/CD pipelines.

## Overview

Formatter automatically aligns and formats TableTest tables according to consistent rules. While the [IntelliJ plugin](/docs/tools/intellij-plugin/) provides real-time formatting in your IDE, Formatter enables batch processing and CI/CD integration for entire projects.

## Why Use Formatter?

### Consistent Style

Maintain uniform table formatting across your entire codebase, regardless of which developer wrote the test or which IDE they use.

### CI/CD Integration

Enforce formatting standards automatically during builds. Fail builds that contain poorly formatted tables, ensuring code quality.

### Batch Processing

Format hundreds of test files in seconds. Useful when adopting TableTest in an existing project or applying new formatting rules.

### IDE-Agnostic

Works with any editor or IDE. Developers using VS Code, Vim, Emacs, or other editors benefit from consistent formatting.

## Installation

Formatter is available as a **command-line tool**, **Gradle plugin**, and **Maven plugin**.

### Command-Line Tool

Download the standalone JAR from [Maven Central](https://central.sonatype.com/search?q=tabletest-formatter):

```bash
wget https://repo1.maven.org/maven2/io/github/nchaugen/tabletest-formatter/{{< param currentVersion >}}/tabletest-formatter-{{< param currentVersion >}}.jar
```

Or install via package manager (if available).

### Gradle Plugin

Add to your `build.gradle`:

```groovy
plugins {
    id 'io.github.nchaugen.tabletest-formatter' version '{{< param currentVersion >}}'
}

tabletestFormatter {
    sourceDir = file("src/test/java")
    failOnUnformatted = false  // Set true for CI
    columnPadding = 1
}
```

### Maven Plugin

Add to your `pom.xml`:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>io.github.nchaugen</groupId>
            <artifactId>tabletest-formatter-maven-plugin</artifactId>
            <version>{{< param currentVersion >}}</version>
            <configuration>
                <sourceDirectory>src/test/java</sourceDirectory>
                <failOnUnformatted>false</failOnUnformatted>
                <columnPadding>1</columnPadding>
            </configuration>
        </plugin>
    </plugins>
</build>
```

## Usage

### Command Line

Format a single file:

```bash
java -jar tabletest-formatter.jar format MyTest.java
```

Format a directory recursively:

```bash
java -jar tabletest-formatter.jar format --recursive src/test/java
```

Check formatting without modifying files:

```bash
java -jar tabletest-formatter.jar check src/test/java
```

The `check` command exits with a non-zero status if any files are unformatted, useful for CI validation.

### Gradle

Format all test files:

```bash
./gradlew formatTableTests
```

Check formatting:

```bash
./gradlew checkTableTestFormat
```

The check task fails the build if tables aren't formatted when `failOnUnformatted = true`.

### Maven

Format tables:

```bash
mvn tabletest-formatter:format
```

Check formatting:

```bash
mvn tabletest-formatter:check
```

## Configuration

### Column Padding

Space around cell content:

```groovy
columnPadding = 1  // " value " (default)
columnPadding = 0  // "value"
columnPadding = 2  // "  value  "
```

### Alignment

Column alignment style:

```groovy
alignment = "LEFT"   // Default, align left
alignment = "RIGHT"  // Align right
alignment = "CENTER" // Center align
```

### Maximum Line Length

Wrap long tables:

```groovy
maxLineLength = 120  // Wrap at 120 characters
```

### Fail on Unformatted

For CI/CD enforcement:

```groovy
failOnUnformatted = true  // Fail build if formatting needed
```

### Include/Exclude Patterns

Control which files are formatted:

```groovy
includes = ["**/*Test.java", "**/*IT.java"]
excludes = ["**/generated/**"]
```

## CI/CD Integration

### GitHub Actions

Check formatting during PR builds:

```yaml
name: Check Formatting

on: [pull_request]

jobs:
  format-check:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          java-version: '21'

      - name: Check TableTest formatting
        run: ./gradlew checkTableTestFormat
```

### GitLab CI

```yaml
format-check:
  stage: test
  script:
    - ./gradlew checkTableTestFormat
  only:
    - merge_requests
```

### Pre-commit Hook

Format tables automatically before each commit:

```bash
#!/bin/sh
# .git/hooks/pre-commit

./gradlew formatTableTests --quiet

# Stage any formatting changes
git add -u
```

## Formatting Rules

Formatter applies these rules:

1. **Column Alignment** - All cells in a column align to the same width
2. **Consistent Spacing** - Equal padding around cell content
3. **Pipe Alignment** - Pipe delimiters line up vertically
4. **Trailing Spaces** - Removed from row ends
5. **Empty Rows** - Preserved for readability
6. **Comments** - Preserved and aligned with tables

### Example Transformation

**Before:**
```java
@TableTest("""
Scenario | Input|Expected
Test case 1|42|true
Another longer test case|0|false
Short|1|true
""")
```

**After:**
```java
@TableTest("""
    Scenario                  | Input | Expected
    Test case 1               | 42    | true
    Another longer test case  | 0     | false
    Short                     | 1     | true
    """)
```

## Best Practices

### Run in CI/CD

Always check formatting in CI to catch inconsistencies before they're merged.

### Use with Pre-commit Hooks

Automatic formatting before commits prevents formatting issues from reaching the repository.

### Configure Once

Set formatting rules in your build configuration and share across the team. Everyone uses the same settings.

### Combine with IntelliJ Plugin

Use the plugin for real-time formatting during development, and Formatter for batch operations and CI validation.

## Troubleshooting

### Formatter Changes Valid Code

Formatter only modifies whitespace within `@TableTest` annotations. If it breaks tests, report it as a bug.

### Performance with Large Codebases

For projects with thousands of tests, use `includes`/`excludes` patterns to limit scope:

```groovy
includes = ["**/important/**"]
```

### Conflicting Styles

If team members use different IDEs with different formatting, Formatter enforces a single standard. Configure it once and let tooling handle consistency.

## Resources

- **GitHub:** [github.com/nchaugen/tabletest-formatter](https://github.com/nchaugen/tabletest-formatter)
- **Maven Central:** [tabletest-formatter](https://central.sonatype.com/search?q=tabletest-formatter)
- **Issue Tracker:** [Report bugs or request features](https://github.com/nchaugen/tabletest-formatter/issues)

## License

Apache License 2.0

---

**Tools complete!** Explore the [User Guide →](/docs/guide/) for in-depth TableTest usage.
