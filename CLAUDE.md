# TableTest Website

Marketing and documentation site for [TableTest](https://github.com/nchaugen/tabletest) — a JUnit extension for table-driven testing on the JVM.

## Tech Stack

- **Hugo** static site generator with the [Hextra](https://github.com/imfing/hextra) theme (imported as a Hugo module)
- Raw HTML enabled in markdown (`unsafe: true` in hugo.yaml)
- Hextra shortcodes: `hextra/hero-headline`, `hextra/hero-subtitle`, `hextra/hero-button`, `hextra/feature-grid`, `hextra/feature-card`, `tabs`, `tab`, `steps`
- Hugo param interpolation: `{{< param currentTableTestVersion >}}` renders version strings from `hugo.yaml`

## Local Development

```shell
hugo server --logLevel debug --disableFastRender -p 1313
hugo mod get -u && hugo mod tidy   # update theme
```

## Content Structure

- `content/_index.md` — Landing page (hextra-home layout)
- `content/docs/getting-started/` — Introduction, installation, first test, next steps
- `content/docs/guide/` — Basic usage, common mistakes (+ draft pages for value formats, type conversion, advanced features, realistic example)
- `content/docs/reference/` — All draft
- `content/docs/tools/` — All draft
- Page resource images live alongside `content/_index.md` (e.g. `content/leap-year-table.png`)

Draft pages have `draft: true` in front matter. They exist but are not published until reviewed.

## Related Repositories

All repos are cloned locally as sibling directories — read source files from disk rather than fetching from GitHub.

| Repo | Local path | Key docs |
|------|-----------|----------|
| [tabletest](https://github.com/nchaugen/tabletest) | `../tabletest/` | `README.md`, `USERGUIDE.md` |
| [tabletest-formatter](https://github.com/nchaugen/tabletest-formatter) | `../tabletest-formatter/` | `README.md`, `FEATURES.md` |
| [tabletest-reporter](https://github.com/nchaugen/tabletest-reporter) | `../tabletest-reporter/` | `README.md`, `FEATURES.md` |
| [tabletest-intellij](https://github.com/nchaugen/tabletest-intellij) | `../tabletest-intellij/` | `README.md`, `FEATURES.md` |

- Core library: group ID `org.tabletest`, artifact `tabletest-junit`
- IntelliJ plugin: [JetBrains Marketplace](https://plugins.jetbrains.com/plugin/27334-tabletest)

## Git Commit Messages

- Omit `Co-Authored-By` attribution footer

## Content Guidelines

- Code examples must be verified against the actual tool behaviour — the USERGUIDE.md in the main repo is the source of truth
- Published pages must not link to draft or deleted pages; link to GitHub repos instead
- Versions are parameterised in `hugo.yaml` (`currentTableTestVersion`, `currentJUnitVersion`) — use `{{< param ... >}}` in content
