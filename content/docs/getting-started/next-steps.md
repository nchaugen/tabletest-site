---
title: "Next Steps"
weight: 4
---

Congratulations! You've written your first TableTest. Here's where to go from here based on what you want to learn.

## Continue Learning

### Learn the Fundamentals

Work through the User Guide to understand TableTest deeply:

1. **[Basic Usage](/docs/guide/basic-usage/)** — Table syntax, column mapping, scenario names
2. **[Common Mistakes](/docs/guide/common-mistakes/)** — Pitfalls to avoid and how to fix them

For topics beyond the basics — value formats, type conversion, value sets, and external table files — see the [User Guide on GitHub](https://github.com/nchaugen/tabletest/blob/main/USERGUIDE.md).

### Explore the Ecosystem

TableTest provides [tools](/docs/tools/) that enhance your development workflow — an IntelliJ plugin for auto-formatting and syntax highlighting, a formatter for CI/CD enforcement, and a reporter for generating documentation from your tests.

## Quick Tips

### Start Simple

Begin with basic value types (strings, numbers, booleans) before exploring collections and custom types. Get comfortable with the fundamentals first.

### Use Descriptive Scenarios

The scenario column is your documentation. Make descriptions clear:

```java
// Good
"Valid email format"
"Email missing @ symbol"

// Less helpful
"Test 1"
"Test 2"
```

### Install the IDE Plugin

The [IntelliJ plugin](https://plugins.jetbrains.com/plugin/27334-tabletest) saves significant time by automatically formatting tables, highlighting syntax, and catching errors as you type.

## Common Questions

**Q: Can I use TableTest with Kotlin?**
A: Yes! TableTest fully supports Kotlin. See the [User Guide](/docs/guide/basic-usage/) for examples.

**Q: How do I test exceptions?**
A: Use standard JUnit assertions like `assertThrows`:

```java
@TableTest("""
    Input
    -1
    """)
void testNegativeInput(int input) {
    assertThrows(IllegalArgumentException.class,
        () -> someMethod(input));
}
```

**Q: Where do I put large tables?**
A: Use external files with the `resource` attribute:

```java
@TableTest(resource = "/test-data/large-dataset.txt")
```

See the [User Guide on GitHub](https://github.com/nchaugen/tabletest/blob/main/USERGUIDE.md) for details.

## Get Help

- **GitHub Issues:** [Report bugs or request features](https://github.com/nchaugen/tabletest/issues)
- **Documentation:** Browse the [User Guide](/docs/guide/) for published guides, or the [full User Guide on GitHub](https://github.com/nchaugen/tabletest/blob/main/USERGUIDE.md)
- **Examples:** Check the repository for [example projects](https://github.com/nchaugen/tabletest/tree/main/examples)

## Contributing

TableTest is open source (Apache 2.0). Contributions are welcome!

- Star the project on [GitHub](https://github.com/nchaugen/tabletest)

---

Ready to learn more? Start with [Basic Usage →](/docs/guide/basic-usage/)
