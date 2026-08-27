# tabletest-site

Documentation and marketing site for TableTest.

## Stack

- **Hugo** with the **Hextra** theme (imported as a Hugo module).
- Raw HTML enabled in markdown (`unsafe: true` in hugo.yaml).
- Hextra shortcodes: `hextra/hero-headline`, `hextra/hero-subtitle`, `hextra/hero-button`,
  `hextra/feature-grid`, `hextra/feature-card`, `tabs`, `tab`, `steps`.
- Version interpolation: `{{< param currentTableTestVersion >}}` reads from hugo.yaml.

## Local development

```shell
hugo server --logLevel debug --disableFastRender -p 1313
hugo mod get -u && hugo mod tidy   # update theme
```

## Content structure

- `content/_index.md` — landing page (hextra-home layout).
- `content/docs/getting-started/` — introduction, installation, first test, next steps.
- `content/docs/guide/` — basic usage, common mistakes (+ draft pages for value formats,
  type conversion, advanced features, realistic example).
- `content/docs/reference/`, `content/docs/tools/` — all draft.
- Page-resource images live alongside `content/_index.md` (e.g. `content/leap-year-table.png`).

Draft pages carry `draft: true` in front matter and are not published until reviewed.

## Related repos

Cloned as sibling directories — read source from disk rather than fetching from GitHub.

| Repo | Path | Key docs |
|---|---|---|
| tabletest | `../tabletest/` | README.md, USERGUIDE.md |
| tabletest-formatter | `../tabletest-formatter/` | README.md, FEATURES.md |
| tabletest-reporter | `../tabletest-reporter/` | README.md, FEATURES.md |
| tabletest-intellij | `../tabletest-intellij/` | README.md, FEATURES.md |
| tabletest-vscode | `../tabletest-vscode/` | README.md |
| tabletest-claude-plugin | `../tabletest-claude-plugin/` | README.md |

Core library: groupId `org.tabletest`, artifact `tabletest-junit`. IntelliJ plugin:
JetBrains Marketplace plugin 27334-tabletest.

## Link previews

Slack, LinkedIn and X read the Open Graph tags in the page head.

- Every section `_index.md` carries a `description` in its front matter. Without one, the
  preview falls back to the page summary, which on the landing page is the Java code.
- `layouts/_partials/opengraph.html` and `twitter_cards.html` override the theme and the
  Hugo internal templates. Both read `utils/social-description.html`.
- The social card is `static/images/og-card.png`, set in `hugo.yaml` as `params.images`.
  Its source is `scripts/og-card.html`, which carries the command that renders it. The
  card is a frame with the wordmark, holding the `content/leap-year-table-old.png`
  screenshot. Render at scale factor 1, so the screenshot keeps its own pixels.

## Content guidelines

- Verify code examples against actual tool behaviour — the main repo's USERGUIDE.md is the
  source of truth.
- Published pages must not link to draft or deleted pages; link to GitHub repos instead.
- Versions are parameterised in hugo.yaml — use `{{< param ... >}}` in content. Every product
  version (`currentReporterVersion`, `currentClaudePluginVersion`, and the rest) is written by
  `scripts/update-versions.sh` from the newest GitHub release each day, so do not hand-edit one:
  the job overwrites it. `currentJUnitVersion` is not ours and stays manual.
- The banner is prose, so no job can write it. `scripts/check-banner.sh` runs daily and fails when
  the banner announces something other than the newest release, or when its `key` does not carry
  the announced version. Hextra stores the dismissal under that key, so a message that changes
  under an unchanged key never reaches a reader who dismissed the one before it.
- Omit the `Co-Authored-By` attribution footer in commits.
