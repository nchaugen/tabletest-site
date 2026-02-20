---
title: "IntelliJ Plugin"
weight: 2
draft: true
---

# TableTest IntelliJ Plugin

The TableTest IntelliJ plugin provides IDE support for writing and maintaining TableTests, making development faster and more reliable.

## Overview

The plugin enhances the IntelliJ IDEA development experience with automatic table formatting, syntax highlighting, and real-time validation for TableTest tables.

## Installation

### From JetBrains Marketplace

1. Open IntelliJ IDEA
2. Go to **Settings/Preferences** → **Plugins**
3. Click the **Marketplace** tab
4. Search for "TableTest"
5. Click **Install**
6. Restart IntelliJ IDEA

### Manual Installation

Download from [JetBrains Marketplace](https://plugins.jetbrains.com/plugin/27334-tabletest) and install via **Settings** → **Plugins** → **⚙️** → **Install Plugin from Disk**.

## Features

### Automatic Table Formatting

The plugin automatically aligns table columns as you type, maintaining clean, readable formatting without manual adjustment.

**Before:**
```java
@TableTest("""
    Scenario | Input | Expected
    Test case 1 | 42 | true
    Another longer test | 0 | false
    """)
```

**After formatting:**
```java
@TableTest("""
    Scenario            | Input | Expected
    Test case 1         | 42    | true
    Another longer test | 0     | false
    """)
```

Columns automatically align to maintain consistency as you add or modify rows.

### Syntax Highlighting

The plugin provides visual distinction for different table elements:

- **Headers** - Column names stand out
- **Scenario column** - First column highlighted differently
- **Data cells** - Easy to distinguish from structure
- **Delimiters** - Pipe characters clearly marked

Syntax highlighting makes tables more scannable and helps catch structural errors quickly.

### Real-Time Validation

The plugin validates table syntax as you type and provides immediate visual feedback:

- **Column count mismatches** - Highlights rows with wrong number of columns
- **Invalid delimiters** - Catches missing or extra pipes
- **Malformed structures** - Identifies syntax errors in nested data

Errors appear with red underlines and helpful tooltips explaining the issue.

### Code Assistance

Smart code completion and assistance features:

- **Column name suggestions** - Auto-complete when referencing existing columns
- **Quick fixes** - One-click corrections for common problems
- **Intention actions** - Context-aware suggestions for improvements

## Keyboard Shortcuts

The plugin integrates with standard IntelliJ shortcuts:

- **⌘⌥L / Ctrl+Alt+L** - Format table (uses standard "Reformat Code" shortcut)
- **⌥↩ / Alt+Enter** - Show intention actions and quick fixes

## Configuration

Plugin settings are available at **Settings** → **Editor** → **Code Style** → **TableTest**:

- **Column alignment** - Configure alignment preferences
- **Spacing** - Adjust padding around pipes and cells
- **Auto-format on save** - Enable/disable automatic formatting

## Compatibility

- **IntelliJ IDEA:** 2023.1 and above
- **Languages:** Java and Kotlin
- **Platforms:** All IntelliJ-based IDEs (IntelliJ IDEA, Android Studio with limitations)

## Benefits

### Saves Time

Manual table alignment is tedious and error-prone. The plugin handles formatting automatically, letting you focus on test logic.

### Reduces Errors

Real-time validation catches syntax errors immediately, before you run tests. This tight feedback loop prevents wasted time debugging malformed tables.

### Improves Readability

Well-formatted, highlighted tables are easier to scan and understand. The plugin maintains consistency across your entire codebase.

### Lowers Barrier to Entry

New team members can write TableTests confidently with IDE guidance. Auto-completion and validation reduce the learning curve.

## Tips

### Let the Plugin Format

Don't manually align columns - type your data and let the plugin handle spacing. This is faster and more consistent.

### Use Quick Fixes

When you see a red underline, press **Alt+Enter** to see suggested fixes. Often the plugin can correct the problem automatically.

### Review Validation Errors

If tests fail unexpectedly, check for plugin-highlighted syntax errors in your tables. They're easy to miss in plain text.

## Known Limitations

- External table files (`resource` attribute) aren't validated by the plugin
- Very large tables (100+ rows) may see formatting delays
- Some custom formatting rules may not apply in all contexts

## Roadmap

Planned features for future releases:

- Navigation to external table files
- Refactoring support (rename columns, extract rows)
- Test generation from table structure
- Enhanced Kotlin support

## Resources

- **Marketplace:** [plugins.jetbrains.com/plugin/27334-tabletest](https://plugins.jetbrains.com/plugin/27334-tabletest)
- **GitHub:** [github.com/nchaugen/tabletest-intellij](https://github.com/nchaugen/tabletest-intellij)
- **Issue Tracker:** [Report bugs or request features](https://github.com/nchaugen/tabletest-intellij/issues)

## License

Apache License 2.0

---

**Next:** Generate documentation with the [Reporter →](/docs/tools/reporter/)
