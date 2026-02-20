---
title: "Reporter"
weight: 3
draft: true
---

# TableTest Reporter

The TableTest Reporter generates web-based documentation from your TableTests, making test specifications accessible and reviewable by non-technical stakeholders.

## Overview

Reporter transforms your executable TableTests into professional HTML documentation that can be shared with product managers, business analysts, and other stakeholders who need to understand system behaviour without reading code.

## Why Use Reporter?

### Living Documentation

Unlike static documentation that becomes outdated, Reporter generates docs directly from executable tests. Your documentation is always synchronized with actual system behaviour.

### Stakeholder Communication

Business stakeholders can review test scenarios, suggest edge cases, and validate business logic without needing to understand Java or test code.

### Compliance and Auditing

Generated documentation provides an audit trail showing what the system is tested to do, useful for compliance requirements and regulatory reviews.

## Installation

Reporter is available as both a **Gradle plugin** and **Maven plugin**.

### Gradle Plugin

Add to your `build.gradle`:

```groovy
plugins {
    id 'io.github.nchaugen.tabletest-reporter' version '{{< param currentVersion >}}'
}

tabletestReporter {
    outputDir = file("$buildDir/tabletest-reports")
    title = "My Project Test Specification"
    includeTimestamp = true
}
```

Or with Kotlin DSL (`build.gradle.kts`):

```kotlin
plugins {
    id("io.github.nchaugen.tabletest-reporter") version "{{< param currentVersion >}}"
}

tabletestReporter {
    outputDir.set(file("$buildDir/tabletest-reports"))
    title.set("My Project Test Specification")
    includeTimestamp.set(true)
}
```

### Maven Plugin

Add to your `pom.xml`:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>io.github.nchaugen</groupId>
            <artifactId>tabletest-reporter-maven-plugin</artifactId>
            <version>{{< param currentVersion >}}</version>
            <configuration>
                <outputDirectory>${project.build.directory}/tabletest-reports</outputDirectory>
                <title>My Project Test Specification</title>
                <includeTimestamp>true</includeTimestamp>
            </configuration>
            <executions>
                <execution>
                    <goals>
                        <goal>generate</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

## Usage

### Generate Reports

**Gradle:**
```bash
./gradlew generateTabletestReport
```

**Maven:**
```bash
mvn tabletest-reporter:generate
```

Reporter scans your test source code, extracts all `@TableTest` annotations, and generates an HTML report in the configured output directory.

### View Reports

Open the generated `index.html` file in any web browser. The report includes:

- **Test class hierarchy** - Organized by package and class
- **Scenario tables** - All test tables rendered with syntax highlighting
- **Test metadata** - Method names, scenario descriptions, parameter types
- **Navigation** - Easy browsing with table of contents and search
- **Timestamps** - When the report was generated (optional)

## Configuration Options

### Output Directory

Where to generate the report:

```groovy
outputDir = file("docs/test-specification")
```

### Report Title

Custom title for the generated documentation:

```groovy
title = "E-Commerce Platform Test Specification"
```

### Include Timestamp

Show when the report was generated:

```groovy
includeTimestamp = true  // or false
```

### Source Directories

Specify custom test source directories:

```groovy
sourceDirs = files("src/test/java", "src/integrationTest/java")
```

### Filters

Include or exclude specific tests:

```groovy
includeClasses = ["**/integration/**"]
excludeClasses = ["**/internal/**"]
```

### Styling

Customize appearance:

```groovy
theme = "light"  // or "dark"
highlightStyle = "monokai"
```

## Integration with CI/CD

### Publish to GitHub Pages

Generate reports during CI and publish to GitHub Pages:

```yaml
# .github/workflows/test-docs.yml
name: Generate Test Documentation

on:
  push:
    branches: [main]

jobs:
  generate-docs:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          java-version: '21'

      - name: Generate report
        run: ./gradlew generateTabletestReport

      - name: Deploy to GitHub Pages
        uses: peaceiris/actions-gh-pages@v3
        with:
          github_token: ${{ secrets.GITHUB_TOKEN }}
          publish_dir: ./build/tabletest-reports
```

### Archive as Build Artifact

Keep reports from each build:

```yaml
- name: Upload report
  uses: actions/upload-artifact@v3
  with:
    name: test-specification
    path: build/tabletest-reports
```

## Use Cases

### Acceptance Criteria Review

Generate reports before sprint reviews. Product owners can review test scenarios to confirm acceptance criteria are correctly understood and implemented.

### Onboarding Documentation

New team members can read generated docs to understand system behaviour and business rules without diving into code immediately.

### Regulatory Compliance

Industries with compliance requirements can use generated reports as evidence of testing coverage and business rule implementation.

### Stakeholder Demos

Present test specifications during stakeholder meetings. Tables are more accessible than code walkthroughs.

## Example Output

Given this test:

```java
@TableTest("""
    Scenario            | Amount | Discount
    No discount         | 50     | 0%
    Small order         | 150    | 10%
    Medium order        | 500    | 20%
    Large order         | 1500   | 30%
    """)
void testOrderDiscount(int amount, String discount) {
    // ...
}
```

Reporter generates an HTML page showing:

- Class name: `OrderTest`
- Method name: `testOrderDiscount`
- Parameters: `int amount`, `String discount`
- Table with all scenarios rendered with formatting and highlighting

## Resources

- **GitHub:** [github.com/nchaugen/tabletest-reporter](https://github.com/nchaugen/tabletest-reporter)
- **Gradle Plugin Portal:** [tabletest-reporter](https://plugins.gradle.org/plugin/io.github.nchaugen.tabletest-reporter)
- **Maven Central:** [tabletest-reporter-maven-plugin](https://central.sonatype.com/search?q=tabletest-reporter)

## License

Apache License 2.0

---

**Next:** Maintain consistent formatting with the [Formatter →](/docs/tools/formatter/)
