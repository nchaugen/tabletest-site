# TableTest Website Evolution Plan

## Context

The TableTest website needs to effectively market the tool to potential users and provide guidance on using it well. The Getting Started section and landing page are shipped. Draft content exists but needs review before publication.

**Goal:** Ship a minimal, high-quality v1 that catches interest and helps people get started, then incrementally add reviewed content.

---

## Stage 1: Housekeeping — Fix Broken State ✅

Clean up configuration issues before any content work.

### Changes

- **hugo.yaml:** Remove dead "About" and "Tools" menu entries. Update versions to 1.0.0 / 6.0.3. Menu: Documentation, Search, GitHub.
- **Group ID:** Update from `io.github.nchaugen` to `org.tabletest` in installation.md and first-test.md.
- **Draft unvetted content:** Add `draft: true` to value-formats, type-conversion, advanced-features, realistic-example, all reference/*, all tools/*.
- **Fix broken internal links:** next-steps.md, installation.md, basic-usage.md, guide/_index.md — point to published pages or GitHub repos.
- **Fix typos:** "intialize" → "initialise" in basic-usage.md; duplicate "Fix:" in common-mistakes.md.

---

## Stage 2: Landing Page Redesign ✅

Replace the abstract landing page with a concrete before/after code comparison.

### Structure

1. Hero headline + subtitle
2. CTA button → Getting Started
3. Before/After code comparison (two-column grid, stacks on mobile)
4. Four feature cards: Add Test Cases Not Methods, See Coverage at a Glance, Rich IDE Support, Complete Ecosystem

---

## Stage 3: Polish Published Content Path ✅

Ensure the Getting Started + Guide basics journey is internally consistent with no dead ends.

- **next-steps.md:** Links only to published pages; draft topics point to GitHub USERGUIDE.md; tools link to GitHub repos.
- **guide/_index.md:** Lists only published pages (basic-usage, common-mistakes) with GitHub link for more.
- **docs/_index.md:** Created as navigation hub for the documentation section.

---

## Stage 4: Create Feature and Tools Overview Pages

Give evaluators enough information to decide whether to adopt, without relying on unvetted detailed docs.

### New: Features overview

Add a "Features" section to the guide index or create `content/docs/features.md` summarising key capabilities with brief code examples:
- Table format basics (link to getting-started)
- Type conversion (built-in + custom converters) — brief example
- Value sets and Cartesian products — brief example
- Collections (lists, sets, maps) — brief example
- External table files — mention
- Scenario names and display names — mention

Each feature gets 2–3 sentences + a small code snippet, sourced from the authoritative README.md/USERGUIDE.md. Link to GitHub for full details.

### New: Tools overview page

Replace the current draft `content/docs/tools/_index.md` with a verified overview:
- One paragraph per tool (IntelliJ plugin, formatter, reporter)
- "How They Work Together" description
- Compatibility matrix
- Links to each tool's GitHub repo for full documentation

Re-add "Tools" to the `hugo.yaml` menu.

### Verification
- Verify all code examples against actual tool READMEs
- Run `hugo server` and check rendering

---

## Stage 5: Incremental Content Review and Publication

Review draft content one page at a time, fix errors, and publish:

1. **value-formats.md** — essential for going beyond basics
2. **type-conversion.md** — needed for custom domain types
3. **advanced-features.md** — value sets, external files, parameter resolvers
4. **realistic-example.md** — the discount calculation walkthrough
5. **Reference pages** — annotation, value-syntax, conversion-rules
6. **Tool detail pages** — library, intellij-plugin, reporter, formatter

For each: remove `draft: true`, verify code examples, fix links, update section index.

---

## Stage 6: Pull-In Documentation from Tool Repos

**Goal:** Keep detailed documentation near the code in tool repos, publish on website automatically.

**Recommended approach:** A CI build script that:
1. Fetches specific markdown files from tagged releases of each repo
2. Adds Hugo front matter
3. Places them in the content tree
4. Hugo builds normally

The feature/tools overview pages (Stage 4) remain hand-written for marketing control; the detailed docs are pulled from repos.
