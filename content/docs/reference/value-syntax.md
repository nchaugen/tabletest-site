---
title: "Value Syntax"
weight: 2
draft: true
---

# Value Syntax Reference

Formal specification of table value syntax.

## Table Structure

```
Header1 | Header2 | Header3
value1  | value2  | value3
value4  | value5  | value6
```

### Rules

1. **First row** - Column headers (required)
2. **Subsequent rows** - Data rows (one or more)
3. **Delimiter** - Pipe character (`|`) separates columns
4. **Optional leading/trailing pipes** - Both `a | b` and `| a | b |` are valid

## Column Headers

### Syntax

```
ColumnName1 | ColumnName2 | ColumnName3
```

### Rules

- **Case-insensitive** - `Input`, `input`, `INPUT` all match parameter `input`
- **Whitespace-tolerant** - `  Input  ` matches `input`
- **Spaces allowed** - `First Name` matches parameter `firstName`
- **Conversion** - Spaces removed, converted to camelCase for matching

### Matching Algorithm

1. Remove leading/trailing whitespace from column name
2. Remove internal spaces and convert to camelCase
3. Case-insensitive match against parameter name

Examples:
- `First Name` → `firstname` → matches `firstName`
- `Expected Sum` → `expectedsum` → matches `expectedSum`

## Simple Values

### Unquoted Values

Most values don't require quotes:

```
hello
42
true
2024-01-15
```

### Quoted Values

Quote values containing special characters:

```
"hello, world"      // Contains comma
"value | other"     // Contains pipe
"key: value"        // Contains colon
"[bracketed]"       // Contains brackets
```

### Quote Types

Both single and double quotes work:

```
'hello'
"hello"
```

### Escaping Quotes

**Java (text blocks):**
```java
"say \"hello\""     // Escaped quotes
```

**Kotlin (raw strings):**
```kotlin
"""say "hello""""   // Doubled quotes
```

## Lists

### Syntax

```
[element1, element2, element3]
```

### Examples

```
[1, 2, 3]                    // List of integers
[hello, world]               // List of strings
[]                           // Empty list
[1.5, 2.5, 3.5]             // List of doubles
[[1, 2], [3, 4]]            // Nested lists
```

### Rules

- **Square brackets** - Enclose elements
- **Comma-separated** - Elements separated by commas
- **Whitespace-tolerant** - Spaces around commas optional
- **Empty list** - `[]` represents empty list
- **Type inference** - Element type determined by parameter type

## Sets

### Syntax

```
{element1, element2, element3}
```

### Examples

```
{1, 2, 3}              // Set of integers
{apple, banana}        // Set of strings
{}                     // Empty set
{1, 2, 2, 3}          // Duplicates removed → {1, 2, 3}
```

### Rules

- **Curly braces** - Enclose elements
- **Comma-separated** - Elements separated by commas
- **Duplicate removal** - Duplicates automatically removed
- **Unordered** - No guaranteed iteration order
- **Empty set** - `{}` represents empty set

## Maps

### Syntax

```
[key1: value1, key2: value2]
```

### Examples

```
[apple: 1.50, banana: 0.75]                    // String keys, double values
[1: first, 2: second]                          // Integer keys, string values
[a: [1, 2], b: [3, 4]]                        // String keys, list values
[]                                             // Empty map
[name: Alice, age: 30, city: Oslo]            // Multi-entry map
```

### Rules

- **Square brackets** - Enclose key-value pairs
- **Colon separator** - `key: value` format
- **Comma-separated** - Pairs separated by commas
- **Type inference** - Key and value types from parameter type
- **Empty map** - `[]` (context determines list vs map)

## Nested Structures

### List of Lists

```
[[1, 2], [3, 4], [5, 6]]
```

### List of Maps

```
[[name: Alice, age: 30], [name: Bob, age: 25]]
```

### Map of Lists

```
[grades: [95, 87, 92], scores: [78, 85, 90]]
```

### Map of Maps

```
[person1: [name: Alice, age: 30], person2: [name: Bob, age: 25]]
```

### Arbitrary Nesting

Nesting depth is unlimited:

```
[a: [[1, 2], [3, 4]], b: [[5, 6], [7, 8]]]
```

## Value Sets

### Syntax

Use sets for multiple values in a single cell:

```
{value1, value2, value3}
```

### Examples

```
{1, 2, 3}              // Test with 1, 2, and 3
{apple, banana}        // Test with apple and banana
{[1,2], [3,4]}        // Test with two lists
```

### Cartesian Product

Multiple value sets create all combinations:

```
Scenario | A      | B      | Expected
Test     | {1, 2} | {x, y} | ...
```

Generates 4 tests:
1. A=1, B=x
2. A=1, B=y
3. A=2, B=x
4. A=2, B=y

## Null Values

### Explicit Null

Use `null` keyword:

```
null
```

### Empty Cell

Empty cells become `null` for non-primitive types:

```
Scenario | Value
Present  | hello
Absent   |         // Empty cell → null
```

### Primitives

Primitive types cannot be null - use wrapper types:

```java
void test(Integer value) { }  // Can be null
void test(int value) { }      // Cannot be null
```

## Special Characters

### Delimiter Characters

These have special meaning and should be quoted if literal:

- `|` - Column delimiter
- `,` - Element separator (lists, sets, maps)
- `:` - Key-value separator (maps)
- `[` `]` - List/map delimiters
- `{` `}` - Set delimiters
- `"` `'` - Quote characters

### Example

```
"Contains | pipe"          // Quoted pipe
"key: value"              // Quoted colon
"[not a list]"            // Quoted brackets
```

## Comments and Blank Lines

### Single-line Comments

Lines starting with `//`:

```
// This is a comment
Scenario | Value
// Another comment
Test     | 42
```

### Blank Lines

Empty lines are ignored:

```
Scenario | Value

Test 1   | 1

Test 2   | 2
```

## Whitespace Rules

### Trimming

By default, leading/trailing whitespace is trimmed:

```
"  hello  "  → "hello"
```

Disable with `trimValues = false`:

```java
@TableTest(value = """...""", trimValues = false)
```

### Significant Whitespace

Preserve whitespace with quotes:

```
"  hello  "   // Whitespace preserved if trimValues = false
```

### Internal Whitespace

Whitespace within values is always preserved:

```
"hello world"  // Space between words preserved
```

## Grammar (EBNF)

```ebnf
table       ::= header rows
header      ::= column ("|" column)*
rows        ::= row+
row         ::= value ("|" value)*

value       ::= simple | list | set | map | valueset | null
simple      ::= quoted | unquoted
quoted      ::= '"' chars '"' | "'" chars "'"
unquoted    ::= [^|,:\[\]{}]+

list        ::= "[" (value ("," value)*)? "]"
set         ::= "{" (value ("," value)*)? "}"
map         ::= "[" (pair ("," pair)*)? "]"
pair        ::= value ":" value
valueset    ::= set

null        ::= "null" | ""
```

## Examples by Type

### Primitives

```
42              // int
3.14            // double
true            // boolean
A               // char
```

### Strings

```
hello           // Simple string
"hello world"   // String with space
""              // Empty string
```

### Collections

```
[1, 2, 3]                       // List<Integer>
{a, b, c}                       // Set<String>
[key: value, foo: bar]          // Map<String, String>
```

### Nested

```
[[1, 2], [3, 4]]                           // List<List<Integer>>
[a: [1, 2], b: [3, 4]]                    // Map<String, List<Integer>>
[[id: 1, name: Alice], [id: 2, name: Bob]] // List<Map<String, Object>>
```

### Value Sets

```
{1, 2, 3}                    // Three separate tests
{apple, banana}              // Two separate tests
{[1,2], [3,4]}              // Two tests with lists
```

## See Also

- [@TableTest Annotation](/docs/reference/annotation/) - Annotation attributes
- [Conversion Rules](/docs/reference/conversion-rules/) - Type conversion details
- [User Guide](/docs/guide/value-formats/) - Practical examples
