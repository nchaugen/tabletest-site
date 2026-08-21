---
title: Changelog
toc: false
---

Changes across the TableTest ecosystem, sorted newest first.

## 2026-08-20 — TableTest Reporter 1.4.0


### Added
- An HTML template of your own can add to the built-in stylesheet through a new `extra_stylesheet`
  block, without replacing it. A report carries its stylesheet inside the file, so until now a role
  declared with `@ColumnRole` had nowhere to be styled from: the only way to reach the CSS was to
  rewrite the whole sheet. The block is left by `table.html.peb`, `index.html.peb` and
  `single.html.peb` alike.
- `@Tree` marks a column whose cells hold a tree, written as a nested collection. The built-in HTML
  report then opens each level below its parent rather than beside it, with a guide line down the
  level and a connector on each entry. The default map rendering puts a key beside its value, which
  walks a deep tree sideways across the page. The cell value is unchanged, so a reader still meets
  the notation they would write.
- A cell whose set expands its row is now marked `value-set` in the published report. A published
  table shows no parameters, so `{a, b}` reads the same whether it expands the row into one run per
  value or is a `Set` the test receives whole. The reporter tells them apart the way the runtime
  does — a set value against a parameter that is not a set expands — and the built-in HTML
  stylesheet labels the cell "any of". Markdown carries no roles, so the two stay alike there.
- `@Lines` marks a column whose cells hold the lines of one block of text. The parameter receives
  the lines joined by newlines (or the lines themselves, for a `List` parameter), and the HTML
  report renders the cell as a stacked monospace block rather than a bulleted list, so text whose
  alignment is the point reads as it was written. AsciiDoc publishes the role and keeps its bulleted
  list; Markdown is unchanged.
- A space run at the end of a line now carries a `trailing` class alongside `sp`, so a stylesheet can
  tell the one run a whitespace-preserving layout cannot show from the ones it can. The built-in HTML
  stylesheet uses it to drop the markers from alignment padding inside a `lines` column while keeping
  a trailing run marked, and to leave a blank line in such a column unmarked — it is already visible
  as a line of the block. Only the class is new — the marked runs and the characters in them are
  unchanged.
- A test parameter can now declare a role for its column, and the reporter publishes it on every
  cell of that column. Annotate an annotation of your own with `@ColumnRole` and put it on the
  parameter; the role is published as the annotation's simple name in kebab case, or as the token
  `@ColumnRole("...")` names. Published roles reach the HTML report as CSS classes and the AsciiDoc
  report as element roles, so a stylesheet of yours can style a column the reporter knows nothing
  about. `scenario`, `expectation`, `passed` and `failed` are still derived by the reporter itself;
  a declared role is published alongside them without being treated as one.
- A table wide enough to scroll sideways now says so: the scroll box keeps a visible slim
  scrollbar, and a shaded edge appears on whichever side has more table beyond it. Previously the
  box scrolled silently — on a platform with overlay scrollbars a reader had no way to tell the
  last column on screen was not the last column.
- A feature in the `tabletest-reporter.yaml` `features:` tree can carry a `description`, rendered
  under the feature's title on its own index page the way a test class's `@Description` is. An
  intermediate index page could previously carry only a title, so anything true of a whole group
  of features had to be repeated on every rule beneath it.

### Fixed
- A rule page now shows the description of the page it sits under, above its own. The class or
  feature description is where the notation a rule's columns use is explained, and it rendered only
  on the index page — but the sidebar links to rule pages and search returns rule pages, so a reader
  met the columns without the explanation. All three formats show it.
- A description with more than one paragraph now renders as more than one paragraph in HTML. HTML
  collapses a blank line, so every paragraph of a `@Description` ran together into one block. The
  Markdown and AsciiDoc reports were already correct. Line breaks inside a paragraph are still
  dropped, so the text flows to the width of the page rather than to the width of the text block it
  was written in.
- Declaring a custom format with no name is now refused with `Format name cannot be missing`
  instead of a message reading only `name`. The blank-name and leading-dot refusals are
  unchanged.

[GitHub Release](https://github.com/nchaugen/tabletest-reporter/releases/tag/tabletest-reporter-1.4.0)

---

## 2026-08-18 — TableTest Claude Code Plugin v1.9.0


The three skills now share one table-design core — the same rules, in the same words, illustrated in each skill's own notation. **Every entry below applies to all three skills unless it names one.**

### Added

- Tables that sit together are read together. Across one set, a concept takes one column name, a kind of value one notation, a failure one spelling, and the tables share their helpers instead of each carrying a copy
- A compound cell value is judged by whether a reader can name each part, not by which punctuation it uses. `2 x 5 mg tablet` explains itself; `G3/HEAT/zone-2` needs a key that lives outside the table
- A field no surface makes a claim about can be left out of a cell and supplied by a fixture — what keeps a cell readable when the object has twelve properties and the rule reads two
- A seam you cannot add is named rather than passed over. Where a table fuses two rules because the intermediate value is not observable, one sentence says so, and it says whether the gap belongs in the code or in the test
- **tabletest**: A shape for composite cell values, chosen by what a reader can name — a map where the parts need their keys, a domain notation where the values name themselves, the value alone for a single part, and a list of any of those for several objects
- **tabletest**: The bundled `scripts/format-table.sh` is part of the workflow — run it to align columns before finishing. It doubles as a parse check: `--check` exits 1 when the table parses and 0 when it does not
- **tabletest**: A list of what the notation cannot express, so the limits are read rather than met as a failing build — a value set cannot vary an expectation, its members split on commas, one converter serves a target type per class (matched on the erased type), and a collection cell cannot hold a null element
- **tabletest**: A reference for two shapes a table cannot express directly — work handed to another thread that the assertions depend on, and positional output fields whose occupant depends on configuration
- **tabletest**: Where the code under test has no type to pin a converter to, an object's parts may go in separate columns and be assembled in the method — named as the last resort it is, and declared in `@Description`

### Changed

- Table design is stated once and shared — one rule per table, the signs a table should be decomposed, value sets for inputs a rule ignores, covering every tier and both sides of every boundary, what the published surfaces carry, scenario and column naming, rejection as an expected column, blank meaning absent, black-box columns. Each skill previously stated an overlapping subset in its own wording, and the wordings had drifted, so the same table could be judged well designed by one skill and badly designed by another. `spec-by-example` and `table-driven-testing` gain the rules they never carried
- **tabletest**: *Collapse Sparse Columns into a Map* is folded into *Putting a Composite Value in a Cell*, which stated the same rule; two checklist lines the shared rules had already replaced are gone
- **spec-by-example**: Worked examples no longer illustrate with loan approval, discounts, order-status transitions, shipping zones, or subscription trial and loyalty columns

### Fixed

**Which rows a table owes**

- The obligation list is derived from the inputs before any rows are counted. Take each input the rule reads and ask whether it is counted or read in bands, whether it charges per unit or once for having any, and whether its effect depends on another input. Each question names rows nothing else will
- *Give each obligation exactly one row* reads as a floor as well as a ceiling. It listed only ways to spot a redundant row, so used as a checklist it could only ever remove
- A rule that reads as one behaviour can still be several. "X holds only if C1 and C2 and C3" names cleanly in a single breath and is three independent claims, each owing its own table
- A tier ladder shows every tier, and a formula behind it reduces neither the tiers nor the boundaries — nine tiers stay nine rows. A value set spanning a tier carries that tier's boundaries only when its first and last members are the tier's own edges
- A boundary pair is written in the finest unit the rule distinguishes. Rows of 30 and 31 days straddle nothing where the rule turns on hours, so the column is `Hours Ago`
- Where one ladder repeats across classes that share its boundary positions, the straddling pairs are written in one class and every other class carries one row per tier. The obligation previously read as boundaries × classes, and a four-class ladder had no stated way to be discharged
- Two rows sharing an answer are one row when swapping one's differing input for the other's would not change an expectation cell *in this table*. The old test asked whether the values behaved alike everywhere, which blocked the collapse whenever any neighbouring rule told them apart
- Collapsing means putting every collapsed value in the surviving cell, never deleting rows — a value set discharges every obligation its members carried, because it expands into one case per value
- A row kept because it "reaches the answer by a different route" has to name a route this table's rule cares about. Three rows for three kinds of coupon, in a table whose rule reads only whether the code was valid, was the common false positive

**Which values become columns**

- A constant the outcome depends on is a column wherever it can be one, and a threshold or limit the rule turns on always can be. The title and description carry only what a column cannot — where the data came from, what the fixture fixes
- A column blank for most of its rows is a column decision before it is a table decision. Sparse columns feeding one expectation column are a family and collapse into one column; columns feeding different expectation columns are different concerns and split the table
- Where another rule derives an input, that rule keeps its own table and this one takes the derived value as an input column. Making a value visible is not a reason to absorb the rule that produces it
- Inputs that are contributions to one combined answer stay in one table with a column each. Splitting them gives one table per contribution, each holding the others at nothing
- The `?` suffix marks outputs only. An input column never takes it, however yes/no it looks — `Repeat Donor`, not `Repeat Donor?`
- A reference point in its own column leaves the values measured from it readable as offsets, instead of absolutes restated in every row
- The rule for an input a table's rule ignores is named for the property rather than the notation — *Show That an Input Does Not Change the Outcome* — and it names the two ways of losing that conclusion. Leaving the column out states nothing: it reads exactly like having forgotten the input. And where the operation does not take the input at all, that is the thing to fix before the table — a value a caller hands over with the request is an input, so it stays in the signature and the table varies it, even where the code will not read it
- Value sets gained the sign that you want one — a column that could carry every one of its values on every row without changing anything is the rule saying, in data, that it does not read that column — and the limit that goes with it: a value set cannot vary an expectation
- Collapsing a family of rules means one table, not necessarily one column. Where the family's members are separate inputs the system reads independently, each takes its own column and stays blank on the rows where it plays no part
- A held constant declared as "and it makes no difference" is a claim, not apparatus. Vary the value across what it ignores rather than writing the sentence, because no row can contradict the sentence
- **tabletest**: A policy constant the API does not expose still gets a column. The column binds to the *test method's* parameter list, not to the arguments of the code under test, so a hardcoded cutoff date or tier threshold takes a parameter like any other column and the body simply does not pass it on
- **tabletest**: A parameter that is one object or one collection keeps one column even where the table moves only part of it — one map column in every table of the class, rather than a field-per-column spread blank in most cells

**What a cell holds**

- A compound expectation stays a native collection down to its keys. Nest one level per part, or give the key a type with a name; a key pasted together from several fields leaves the row one token whose parts the reader separates by eye
- A shorthand cell keeps a slot for every field the description makes a claim about. Packing only the fields the rule reads pins the rest for every row with no column saying so
- Cell values prefer what the system really produces — a sentinel, enum constant or error string that is part of the observable contract, rather than a tidier test-only label. Where a value is too long to scan, shorten the value and never the vocabulary
- Identity and status varying together in one output position is named as the exception it is. `Primary OK` against `Secondary ERROR` is one domain value where both parts vary, and splitting it into two columns makes the reader join the halves back up
- A value the rule ignores that sits inside a composite cell has two routes out, and the cheaper one is stated first: where the cell holds a list, vary the ignored value across its elements — one row, nothing reshaped
- **tabletest**: An object with two collection-shaped parts is not the last-resort case for spreading a value across columns. A converter takes one cell, so an object holding a map and a set cannot be built from two columns — the table varies the one part the converter builds from, and the parts no row varies are fixed in the method and declared in `@Description`
- **tabletest**: A custom `@TypeConverter` is reached for collection elements, at any depth, exactly as a built-in converter is. The skill said this of built-in conversion only, which left a list of domain objects looking unsupported

**Rejected cases**

- Whether accepted and rejected rows belong in one table is decidable rather than a matter of taste. Strike the rejected rows: if what remains still states a rule, the rejection was a separate concern and takes its own table
- **table-driven-testing**: An accept/reject boundary is one rule and stays in one test, so the last accepted value sits beside the first rejected one. Five references to a section that had stopped existing now resolve
- **tabletest**: The exception column in the rejection example holds `java.lang.IllegalArgumentException`. The bare name it carried cannot be converted to a `Class<?>` and fails every row with `ClassNotFoundException`

**Titles and descriptions**

- A value already shown as a column is fully declared and no other surface owes it anything. A column that never varies is declared as well as one that does, so being constant is not on its own a reason to add a row varying it
- **tabletest**: `@Description` no longer offers "fixed values shared by all rows" as a reason to write one. A value that can be a column belongs in a column; the description carries what cannot
- **tabletest** and **spec-by-example**: Column and scenario names are written in domain terms in the first draft, not deferred to a refinement pass after the tests go green. The table someone reads is the one you hand over

**tabletest notation and tooling**

- The table formatter installs and runs. It asked Maven Central for a `shaded` classifier that has never been published, so the download always failed and the script exited without formatting anything — including the bundled auto-formatting hook
- The formatter is referenced by a path that resolves: `${CLAUDE_PLUGIN_ROOT}/skills/tabletest/scripts/format-table.sh`. It was written as if relative to your working directory, where it has never existed
- The quoting rules were wrong about colons and are now decidable without running anything. A colon in a whole cell needs no quotes — `Alert: condensation risk` is a plain value — while a colon inside `[…]` or `{…}` does, because the parser reads brackets before it knows the parameter type
- The one-converter-per-target-type rule says what it actually is: per *class*, matched on the *erased* type, so `Optional<String>` and `Optional<Boolean>` collide. That is also what forces several tables in one class onto one cell format
- The from-existing-code workflow no longer asks for a mockup to be confirmed before implementing, which contradicted the ambiguity policy two branches above — choose the reasonable reading, record it, deliver
- "A lone error case belongs in the table rather than a separate `@Test`" no longer reads as licence to merge two tables. It is about `@Test` methods, and never overrides the test that decides whether accepted and rejected rows share a table

**spec-by-example**

- A blank cell means one thing: the value is genuinely absent and the system under test decides what missing means. `0` says the value is present and is zero, and a test method must never convert a blank to a default on the way in
- A `@Test` method is for a sequential path, not for re-running the rules end to end. A combination that behaves in a way neither rule shows alone earns a table of its own

**table-driven-testing**

- Inputs a rule ignores vary together in one case list instead of being stacked into a cartesian product. Two stacked `parametrize` decorators turn one claim into four visible cases, and a third input turns it into eight

**Worked examples and wording**

- Eighteen defects in the illustrations themselves, found by reading every example against the rules beside it — `[EMPTY]` where the empty map is `[:]`, boundary examples using comfortable values where the boundary value belongs, a hand-rolled separator inside the rule that forbids one, and a TDD example demonstrating the `ERROR+1` encoding the guidance names as the thing not to write. Illustrations also moved off borrowed business domains — the legend now reads `2 x 5 mg tablet` against `G3/HEAT/zone-2`
- **spec-by-example** and **table-driven-testing**: Two sentences in *Assume the Table Is Published* rendered with a duplicated article — "A the note beneath the table sentence naming a value" — because the shared source was built around a bare noun for a surface both skills name with an article

### Removed

- **tabletest**: Five of the eight references — `column-design.md`, `common-patterns.md`, `table-design-advanced.md`, `pair-programming.md` and `testing-reveals-bugs.md`, about 1,300 of 1,746 lines. A reference is for a corner case whose trigger you can see in your own task before opening the file; "torn between maps and separate columns" is a judgement, not a trigger, and the table design behind it is in the skill file, which is always read. The patterns that did have a trigger moved into `type-converters.md` and a new short file
- **tabletest**: The "iterative column evolution" and "progressive refinement" phases went with them. They described starting from parameter names and fixing them after the tests go green, which contradicts naming columns in domain terms in the first draft



[GitHub Release](https://github.com/nchaugen/tabletest-claude-plugin/releases/tag/v1.9.0)

---

## 2026-07-31 — TableTest Claude Code Plugin v1.8.0


### Fixed
- **tabletest**: Corrected the rule for multiple type converters. The skill claimed two `@TypeConverter` methods returning the same wrapper type are selected by matching the parameter name — they are not, and the published example fails every row with `TableTestException: Multiple type converters found`. Selection is by return type alone, and the match is on the *erased* type, so `Optional<String>` and `Optional<Boolean>` collide with each other. Several parameters of one wrapper type share a single converter
- **tabletest**: "Irrelevant input" meant two opposite things in two places. An input **another** rule owns is held at one obviously-valid value; an input **this** rule claims not to affect the outcome has to vary across the values it ignores, or the claim cannot be contradicted by any row. The distinction is now stated once, as a question to ask of your own table, and both misuses are corrected. Same fix applied to the table-driven-testing skill, which carried the identical conflict in its own checklist
- **tabletest**: A converter is no longer described as being "for formatting only". Any domain object built from a table value belongs in one, whatever the construction idiom. What a regex inside a converter signals is a *cell* carrying two values — the repair is a column, never moving construction back into the test body
- **tabletest**: A column blank for most of its rows now collapses into a map column before any table is split. Splitting first produced several tables fixing the same setup and reporting the same output column, which the skill elsewhere calls an over-split
- **tabletest**: The worked example for annotation order no longer breaks three rules while demonstrating a fourth — its description published the whole fee algorithm, it claimed an input did not matter while never varying it, and it fused a classification with the arithmetic that follows it
- **tabletest**: The comments-and-grouping example tested no system — it asserted `output == input * input`, recomputing its own expectation. It now calls a policy object, and its two comment groups each straddle a real band boundary instead of labelling rows "basic" and "edge"
- **tabletest**: The last worked examples naming a row after its own answer are fixed (`Alice succeeds` beside `Result?` `SUCCESS`, `Primary master, both ok` beside `Primary OK`). Two of them could not be repaired by renaming: the row's real input was named only in the scenario text and appeared in no column, so the tables now carry that input and the names describe it

### Removed
- **tabletest**: The async-and-performance reference. Almost all of it was general advice on testing asynchronous code — latches, thread-safe collections for recording call order, timing assertions — rather than anything about expressing those tests as a table. The parts that were TableTest-specific already lived elsewhere: the `<50` upper-bound cell convention and its converter, waiting for off-thread work before asserting, and map columns for composite request data. One distinction was kept on the way out: a range assertion still beats an upper bound when the rule is "the duration the system reported matches the real one"

### Changed
- **tabletest**: Guidance that encoded two values into one cell (`ERROR+1`, `TIMEOUT+3`, `OK in 10ms`) is removed. A flattened cell has to be parsed back in the method body, which tests the format rather than the rule; a pair that is really one value is a domain type, and a pair that is two values is two columns
- **tabletest**: Patterns that kept a table short by moving its meaning into the test body are replaced. An unshowable expected value (an ANSI escape, Base64) gets a type whose constants carry it, so the table names the constant and built-in enum conversion does the rest — not a lookup map resolved in the body. Composite keys use short real values (`acme:search:v2`), not single-letter placeholders needing a legend the table does not contain
- **tabletest**: Worked examples no longer put `if`, `switch` or a ternary in a `@TableTest` method body, no longer leave an expectation column holding one value in every row, and are named for the action the code performs rather than `test…`



[GitHub Release](https://github.com/nchaugen/tabletest-claude-plugin/releases/tag/v1.8.0)

---

## 2026-07-30 — TableTest Claude Code Plugin v1.7.0


### Fixed
- **tabletest**: Removed a second, contradictory statement about blank cells and converters. "Blank cells for irrelevant inputs" still told readers to handle null-to-default conversion *in a `@TypeConverter`* — the very thing a blank cell makes impossible, and the claim corrected elsewhere in the same file. Blank now explicitly means *absent*, with the empty value (`[:]`, `[]`, `{}`, `''`) as the way to reach a converter for defaults
- **tabletest**: An input that exists but does not affect the outcome is a value set, not a blank cell. The guidance said both, in adjacent paragraphs
- **tabletest**: Worked examples no longer name a row after its own answer. The tier-ladder table named each row for the standing it expects (`Freshman` beside `Standing?` `Freshman`), and the priority-resolution tables named theirs for the source they resolve to (`Configured wins` beside `Source?` `CONFIGURED`) — both contradicting the rule that scenario names describe the condition, not the outcome. The advice to name decision rows "X wins over Y" went with them: a priority table almost always publishes the winner as an expectation column
- **tabletest**: Corrected the documented behaviour of blank cells and `@TypeConverter`. A blank cell becomes `null` *before* conversion is attempted, so the converter is never called for that row — the skill previously stated the opposite, and its map-column example used a blank "all defaults" row whose null guard could not fire. The all-defaults row is now `[:]`, and dead `value == null` guards are gone from the converter examples

### Changed
- **tabletest**: Scenario-name guidance now opens with the check instead of ending with it. The decidable test — read each name beside its own expectation cells, and cut whatever repeats one — is the first line of the section, followed by a single transform table covering every shape: the compound name, the bare outcome, the verdict-led prefix, and the name that says nothing changed. Two explanatory paragraphs and a second table became rows in it
- **tabletest**: The rule about inputs a rule ignores is now written as an instruction with the trigger first — *if a title or description says an input doesn't affect the result, vary that input in the rows* — with the value-set syntax to copy and the prohibition (not in a converter, a field, or the test body) stated plainly rather than argued for
- **tabletest**: Table Design guidance is now grouped under the three questions that decide almost every table — what is this table's axis, what does a reader see, and what is left in the method body. The sections themselves are unchanged; they were previously in an order that interleaved all three, so advice that answers one question arrived in three separate places
- **tabletest**: Collapsing an optional-field parameter object into a map column is now decided from the method signature rather than from how blank the drafted columns look, and the converter returns the domain object — a `Map<String, String>` parameter plus a private construction helper leaves construction in the test, which the map column exists to remove
- **tabletest**: Compound expectations stay native collections — a result that is several items, or items grouped under a key, is a list, set, or map column (`[W1: [camera, lens]]`), not a quoted string assembled by a stringifying helper; use a set where order is not part of the rule
- **tabletest**: A map column is a column decision, not a table decision. Choosing a map for one parameter does not make that parameter's fields the concern boundary — another input that drives the same rule to the same output column is another column in the same table, not a table of its own
- **tabletest**: Separating rules from arithmetic now names the symptom: if reading a row means classifying first and then computing, the table has fused two rules and states neither. The classification gets its own table whose expectation columns *are* the classification, and the calculation table takes those as input columns
- **tabletest**: The rule for when a second expectation column belongs elsewhere is now about axes, not response shape. A response carrying two fields usually keeps both columns; what matters is whether each column moves for its own reason along the axis the table varies. A column constant down every row, or changing only as a side effect of another, is not being tested — give it rows that vary it, or move it to the table whose axis does. The previous wording ("a second rule's output does not belong") would have split any two-field response, causing the over-decomposition the skill warns against two sections earlier
- **tabletest**: Decomposition guidance now states the opposite failure as well — several tables that fix the same setup, each varying one sub-rule and reporting the same output column, are one concern scattered across methods, and belong in one table with a column for the varying input. That shape is now given as the *symptom*: the cause is a family of rules you did not name. If you can name what several tables have in common in one term, that term is the table and its members are a column — and members of a family computing differently (a flat reduction, a percentage, a weight-based recalculation) is not a reason to split them. Guarded against collapsing on a bag rather than a family: the family name must work as a column header with the members as its values
- **tabletest**: Tier ladders get one row per tier — all of them, none twice. Do not sample the ladder and trust the reader to interpolate, and do not split a tier into a "tier begins" row beside a "tier holds" row: a value set spanning the tier already carries its boundaries
- **tabletest**: Null, empty and blank variants of an input are one row per distinct *outcome*, not one per representation. Where all three produce the same rejection that is a single row, or `{'', '   '}` as a value set with a blank-cell row only where the null case must be visible on its own
- **tabletest**: Whether a value set is right is decided by the rule's own granularity, not by how different the inputs look. Where the rule answers differently for each — distinct reason codes — those are separate outcomes and separate rows. Where it returns one undifferentiated rejection however the input fails, that is one obligation: a representative case or two, and a value set for the rest. Enumerating every way an input can be malformed is coverage of the *format*, not of the rule. This replaces guidance that said the opposite by example, asserting that malformed-input variants "each test a different structural rule" — which licensed the enumeration the rest of the section forbids

### Added
- **tabletest**: What the assertion tolerates is part of the rule. A comparison that sorts either side before comparing, accepts a subset, matches "contains" rather than equals, or normalises case or whitespace changes which behaviours the test would accept — and none of it reaches a reader of the table. Ordering is the usual case and a helper is where it hides, written once and invisible at every call site. Either name the criterion in the `@Description`, or remove the need for it: a `Set` expectation column says order does not matter in the table itself. A conventional numeric epsilon, and constructing the objects the columns name, are exempt
- **tabletest**: Any domain object built from a table value belongs in a `@TypeConverter`, whatever the construction idiom — constructor, builder, static factory, or a chain of `with…` calls are all construction, and construction in the test body puts the arrangement between the reader and the rule. Previously this was stated only for objects with several optional fields, as part of the map-column guidance, so a composed object assembled by a factory or wither chain fell outside it
- **tabletest**: How to write a table where some rows throw and some do not — the shape any boundary straddling a validation limit produces. Leave `Throws?` blank where nothing is thrown and compare the thrown type against it, so the method keeps one assertion; branching between `assertThrows` and `assertDoesNotThrow` puts the rule back in the body where the table cannot show it
- **tabletest**: One value can carry two obligations in two different tables. A value that is a boundary for one rule is often the subject of another — showing it once, in whichever table you reached first, feels like coverage and is not. Count obligations per rule, never per value
- **tabletest**: A new frame — assume the table is published. Only three surfaces reach a reader who never sees the test body, and they divide the work: the title carries the rule, the description carries the apparatus that cannot be a column, the table carries the variations. Whatever the table holds constant is silently promoted into the rule, so a constant the outcome depends on is either a column or declared in the title or description — never left in the method body, a field, a `@TypeConverter`, or a `//` comment, which reaches no published surface at all. An input the rule is claimed to ignore must still be shown varying, as a value set; pinning it makes the independence unfalsifiable
- **tabletest**: Guidance on titles. `@DisplayName` — or the method name when there is none — is the line a reader scans in a report index, so titles are judged as a set. Each states an action the code performs rather than labelling a topic (`Sets the deferral interval from donation type`, not `Deferral interval by donation type`), no three share an uninformative opener, and one grammatical shape runs across the family. That action voice belongs to the title alone: scenario names stay condition phrases
- **tabletest**: `@Description` must not publish the algorithm. Restating an internal formula turns a black-box table into a white-box one; the test is whether a reader could recompute the expectation cells from the description alone. A threshold the rule *is about* stays welcome — better still as a column
- **tabletest**: Before deleting a combining table, salvage the obligations only its rows discharge into the table that owns their rule — then delete. It is a salvage step, not a reprieve: a single `@Test` re-proving one already-proven total is the same combining table with fewer rows
- **tabletest**: Scenario-name guidance now covers the mistake people actually make. The published good/bad pairs only showed bare outcomes (`Returns error`, `Cannot rent`); the common failure is a name that states the condition *and then adds the outcome* — `Contractor gets no bonus` beside a bonus column of `0.0`. Such a name looks right, because a condition really is in it. Added the repair as a transform (point at the expectation cell the name restates, cut that clause, keep the rest), the verdict-led prefix (`Deferred: …`) as the same mistake, and the case where a name says nothing changed, which publishes the answer just as surely
- **tabletest**: Custom converters claim expectation columns too — conversion is by parameter type, not by column role, so `true` arrives as `false` in a class registering a `Yes/No` boolean converter
- **tabletest**: A collection value cannot hold a null element — `[a, , c]` is a parse error, not a list containing null
- **tabletest**: Guidance on how many rows a table needs. List the concern's obligations — the distinct behaviours the rule must demonstrate — then write the smallest set of rows covering all of them. Where two rows share an expectation, the difference between them must be the thing the rule is about; three shapes of redundant row are named: a value further past a boundary an earlier row already crossed, a larger n in the same direction, and an input the rule is indifferent to (one row with a value set)
- **tabletest**: Guidance on combining tables. A table exercising several rules together earns its place only where the combination behaves in a way neither rule shows alone — a precedence, an ordering, an interaction whose result neither single-rule table produces. A final table that runs the whole feature end to end re-proves what those tables established; if the description you would write for it is "end-to-end scenarios combining the rules above", it has no rule of its own
- **tabletest**: Four Quality Checks — one row per obligation, every tier exactly once, combining tables prove an interaction, and expectation columns are all outputs of the same rule



[GitHub Release](https://github.com/nchaugen/tabletest-claude-plugin/releases/tag/v1.7.0)

---

## 2026-07-23 — TableTest Reporter 1.3.0

> [!IMPORTANT]
> **Slug generation changed, and some published page names change with it.** A test or class
> name containing a letter with no ASCII form (`ß æ ø ł þ ð œ đ`), a compatibility character
> (`ﬁ`, fullwidth letters, `x²`, `Ⅻ`, `™`, `½`), or a non-Latin script now produces a different
> filename and URL than earlier versions did — those characters used to be dropped, so `Grüße`
> published as `grue` and now publishes as `grusse`. If you already publish your documentation,
> the affected pages move and existing links to them break; regenerate the whole report rather
> than an incremental subset, and expect to update any links you control. Names made only of
> ASCII, and accented letters that already folded to a base letter (`ü ö ä é å ñ`), are
> unaffected — their slugs are byte-for-byte what they were.

### Fixed
- A test named wholly in a non-Latin script no longer produces an empty filename: `Москва` now
  publishes as `москва` rather than as nothing at all, and likewise for Greek, CJK, Devanagari
  and every other script. Such a name keeps its own characters, which browsers percent-encode
  and GitHub Pages serves as UTF-8; a name whose ASCII form is only a number it contained
  (`Москва основана в 1147`) is treated the same way rather than published as `1147`. A name
  with no letters or digits anywhere gets `unnamed-` plus a stable hash, so two of them still
  get two files. Names that already produced a working slug are unaffected.

### Changed
- Compatibility characters now reduce to the characters they stand for instead of being dropped
  from filenames and URLs: `ﬁle ﬂow` becomes `file-flow`, fullwidth `Ｆｕｌｌｗｉｄｔｈ` becomes
  `fullwidth`, `x² area` becomes `x2-area`, `Chapter Ⅻ` becomes `chapter-xii`. A precomposed
  accented ligature (`Ǽgir`) no longer loses its letter either. Names written in a script of
  their own are composed the same way, so halfwidth and fullwidth katakana spellings of one
  name (`ﾃｽﾄ`, `テスト`) give one slug rather than two.
- Latin letters with no ASCII form now appear in filenames and URLs instead of vanishing from
  them: `Grüße` becomes `grusse` where it used to become `grue`, and `ÆØÅ` becomes `aeoa` where
  it used to become `a`. One rule decides the spelling — ligatures expand to their component
  letters (`ß`→`ss`, `æ`→`ae`, `œ`→`oe`), stroked letters fold to their base letter (`ø`→`o`,
  `ł`→`l`, `đ`→`d`, `ð`→`d`), and `þ`→`th` because thorn has no Latin base letter. Letters that
  already folded (`ü ö ä é å ñ`) are untouched, so a name built only from those keeps the exact
  slug it had; a name containing one of the newly spelled-out letters gets a new one.
- The JUnit extension no longer depends on the Slugify library: filename slug generation is
  now built in. Slugify required Java 21, which forced every project documenting its tests to
  run them on a 21+ runtime; the extension now targets Java 17, so a Java 17 project can use
  it on its own test runtime. Slug output is unchanged — the replacement is pinned by the same
  characterisation table, extended with non-ASCII cases, and reproduces the library exactly.
  This also removes Slugify and its SLF4J transitive from the test classpath, so they can no
  longer conflict with versions a project uses itself. The build still requires Java 21+.

### Added
- Multi-module reports: several directories of TableTest output now merge into a single
  spec, so the modules of a multi-module build publish one set of documentation. Maven gains
  a `tabletest-reporter:aggregate` goal that walks the reactor and finds each module's output
  by itself, plus `<inputDirectories>` on the `report` goal for naming them explicitly;
  Gradle gains `inputDirs`, and the CLI accepts a repeated `-i`/`--input`. The report tree
  comes from the test class names, so modules land in one package hierarchy; where two
  modules published the same class the most recent output wins. A listed directory that does
  not exist is skipped with a warning, so a partial build still publishes what it has.
- Report-time publish selection: a `publish` section in `tabletest-reporter.yaml` decides
  which pages the report holds, with `exclude` paths holding a page (and its subtree) back
  and `include` paths re-admitting one below an excluded page, so a single rule table still
  publishes from an otherwise internal class. Paths name pages as the report's URLs do
  (`converting/convert-with`), with `*` for any part of a page name and `**` for any number
  of levels. Selection happens when the report is generated, so what publishes is no longer
  tied to how the suite was tagged or run, and re-curating a spec needs no new test run. A
  feature page left with nothing published under it drops with its pages; a path matching no
  page is logged and skipped. Without the section every table publishes, as before.
- Spec-level metadata via an optional `tabletest-reporter.yaml` in the project directory:
  give the whole spec a real title and intro paragraph on its root index (instead of the
  leaked lowercase package segment like "junit" or "example"), retitle intermediate index
  pages, and set an explicit feature reading order for the top-level sections and their
  children — declared features lead, undeclared siblings follow alphabetically. The file is
  read at report time and applied on top of the generated tree, so a project without it is
  unaffected. Point elsewhere with Maven `<configFile>` / `-Dtabletest.report.configFile`,
  Gradle `configFile`, or the CLI `--config` / `-c` option.
- Every HTML page footer states when the report was generated ("Generated by
  tabletest-reporter · 20 Jul 2026 14:32 UTC"), so a reader can tell whether published
  documentation still tracks the code it came from. The timestamp is stated in UTC and
  carries a machine-readable `<time datetime>` attribute; every page of a run shares the
  one timestamp.

[GitHub Release](https://github.com/nchaugen/tabletest-reporter/releases/tag/tabletest-reporter-1.3.0)

---

## 2026-07-18 — TableTest IntelliJ Plugin v0.4.2

### Changed

- Removed support for escaped quotes (`\"`, `\'`) and backslashes (`\\`) in quoted strings and map keys, reversing the 0.4.1 feature. The TableTest format has no escape sequences: quote characters cannot be escaped inside quoted values, and backslashes are ordinary characters. The plugin now matches the core parser.

### Fixed

- An unclosed quote no longer swallows the rest of the row: the lenient literal now stops at the next pipe, and cells split the same way as the core parser. Inside lists, sets, and maps an unclosed quote is now highlighted as an error, matching the core parser's rejection.
- Fixed a crash in the "Unused declaration" suppressor when the Kotlin plugin is disabled: Kotlin-aware suppression now loads only when the Kotlin plugin is available.

[GitHub Release](https://github.com/nchaugen/tabletest-intellij/releases/tag/v0.4.2)

---

## 2026-07-18 — TableTest VS Code v0.1.1

### Fixed

- `@TableTest` annotations inside line comments, block comments, or string literals are no longer treated as real tables. Previously, formatting a file with a commented-out annotation rewrote the comment into multi-line code (breaking compilation), and diagnostics and header highlighting fired inside comments and Javadoc.
- Formatting now preserves CRLF line endings. Previously, formatted text blocks and string arrays in CRLF documents were rewritten with LF, leaving mixed line endings, and an already-aligned CRLF table always produced an edit on the first format.
- Block comments (`/* … */`) inside annotation arguments no longer prevent table detection; tables like `@TableTest(/* rows */ """…""")` are now formatted, with the comments kept in place.
- Formatting no longer deletes comments around string-array tables: comments between the parenthesis and the array brace are preserved, and arrays with comments between entries keep diagnostics and highlighting but are left unformatted rather than having their comments silently removed.
- Kotlin `@TableTest` with a positional table and named arguments (e.g. `@TableTest("""…""", encoding = "UTF-16")`) is now recognised for formatting, diagnostics, and highlighting. The unsupported Kotlin array forms of `value` are now documented as a limitation.
- Formatting a string array whose opening brace sits on its own line now produces the canonical layout in a single pass; previously the first pass indented rows relative to the old brace position and only a second format settled the result.


[GitHub Release](https://github.com/nchaugen/tabletest-vscode/releases/tag/v0.1.1)

---

## 2026-07-18 — TableTest Formatter 1.1.2

### Fixed
- String arrays: comments between entries are now preserved — commented-out rows were previously reinserted as live table rows and descriptive comments deleted
- String arrays: empty-string entries are now kept as blank rows instead of being dropped
- CLI: crash (NullPointerException) when a file was given as a bare relative path like `tabletest-format Foo.java`
- CLI: a file that cannot be read (missing, unreadable, non-UTF-8) is now reported and skipped instead of aborting the whole run; directory walk errors report a friendly message instead of a stack trace
- CLI: file permissions (e.g. executable bit) are preserved when a file is rewritten
- CLI: `--version` now reports the actual build version instead of a stale hardcoded 0.1.0-SNAPSHOT
- String arrays: closing-quote alignment now accounts for wide characters (CJK, emoji), consistent with cell alignment
- Blank lines inside indented tables no longer get trailing whitespace, so the formatter stops fighting whitespace-trimming editors and hooks
- Kotlin raw strings whose table content starts immediately after the opening `\"""` with quote characters no longer confuse the extractor

[GitHub Release](https://github.com/nchaugen/tabletest-formatter/releases/tag/tabletest-formatter-1.1.2)

---

## 2026-07-18 — TableTest Reporter 1.2.0

### Added
- HTML format marks whitespace-significant literals with IDE-style per-character markers:
  values with leading/trailing whitespace (on any line), tabs, runs of spaces, or pipes
  (e.g. indent expectations, whitespace-only cells, formatted-row values) render in
  monospace with a CSS-drawn dot per significant space and an arrow per tab, so space
  counts and tab-vs-space composition are readable at a glance. Single spaces between
  words stay unmarked, and the value text itself stays unaltered for copy/paste and
  search.
- Built-in `html` output format: self-contained, single-file-per-page living documentation
  (inline CSS/JS, no external references) with autowidth tables, sticky header/first column,
  nested-collection rendering, pass/fail badges and status colouring, collapsible failure
  details, per-page row filter and "failing only" toggle, roles legend, light/dark toggle,
  and a print stylesheet. Relative links throughout make the output tree GitHub Pages-ready.
- HTML index pages roll pass/fail status up the tree: each nav item shows a status dot and
  every index summarises its scenario pass rate ("N of M scenarios broken"/"All passing").
- Every HTML page shows a breadcrumb trail of its ancestor pages (root package → class →
  table), with relative links so the trail works from any subpath.
- Every HTML page has a menu button opening a navigation drawer with the whole-report tree
  (status dots included), the current page highlighted and all links relative to that page.
  The drawer slides in over the content, so tables always get the full page width.
- Whole-report search: a search box in the navigation drawer searches across every page's
  title, description, headers and cell values, listing matching pages (with status dots) to
  jump to. Backed by a single shared `tabletest-search-index.js` written once to the output
  root and linked from every page by a relative prefix, so search works offline (`file://`)
  and from any subpath without external requests.
- Single-file HTML mode (`--single-file` / `-s` on the CLI): assembles the whole report into
  one self-contained `.html` — every table inlined as an anchored section, sidebar navigation
  and search targeting in-page anchors, search index inlined, no sibling assets. Ideal for
  sharing as a release asset, email or ticket attachment. Multi-file stays the default.
### Fixed
- The Gradle `reportTableTests` task now tracks the TableTest YAML files as task inputs even
  when no explicit `inputDir` is configured (default `build/junit-jupiter`, the JUnit output
  dir override, and the `junit-platform.properties` location). Previously the task could stay
  `UP-TO-DATE` — or restore a stale report from the build cache — after new test runs. The
  task is also ordered to run after `Test` tasks when both are requested.
- A table test whose display-name slug equals its class slug (e.g. the same `@DisplayName`
  on both) no longer silently loses one of the two published YAML files: the table file now
  gets a numeric suffix, keeping the class and table files distinct.
- A row whose scenario value is a prefix of another row's scenario (e.g. "Add" and
  "Add negative") no longer absorbs the other row's pass/fail results; and rows with
  duplicated scenario values now get no pass/fail roles (as documented) instead of the
  OR-ed result of all duplicates.
- When the input directory accumulates YAML from several test runs (e.g. a
  `junit.platform.reporting.output.dir` with `{uniqueNumber}`), the report now reflects the
  most recently modified files instead of whichever run's files happened to sort first.
- On Windows, index-page links and single-file anchors used backslashes (the platform file
  separator) and were broken in browsers and Markdown/AsciiDoc renderers; generated links now
  use `/` on every platform.

[GitHub Release](https://github.com/nchaugen/tabletest-reporter/releases/tag/tabletest-reporter-1.2.0)

---

## 2026-07-18 — TableTest 1.2.2

### Changed
- When both a `@TypeConverter`-annotated method and non-annotated candidates match a target type, the annotated method is now selected instead of failing with "multiple type converters found"
- Collection parameters must be declared using the interface types `List`, `Set`, or `Map`; concrete types like `TreeSet` or `ArrayList` now fail with a clear error instead of expanding value sets into scalars or failing obscurely at test invocation (custom converters can still produce concrete collection types)
- Duplicate map keys (e.g. `[a: 1, a: 2]`) now fail with a parse error naming the key, instead of silently keeping the last value; quoted and unquoted spellings of the same key count as duplicates
### Fixed
- An empty value set (`{}`) for an expandable parameter now fails with an error naming the column, instead of silently generating zero test invocations for the row
- Very long cell values and comment lines (roughly 15k characters and up) no longer crash parsing with `StackOverflowError`
- A blank header cell now fails with a parse error naming the column, instead of a `NullPointerException`
- A table input that is empty or holds only blank lines and comments now fails with `TableTestParseException` instead of `IllegalArgumentException`
- Table rows with missing or extra cells now fail with an error naming the offending row, instead of silently shifting values to the wrong parameters or misreading the first cell as a scenario name
- Type converter cycles (e.g. a public static method in the test class taking and returning the same type) now fail with a clear error instead of crashing the test with `StackOverflowError`
- The `@TypeConverter` deprecation warning is only emitted for methods actually selected as converters, once per method, instead of for every public static single-parameter method (including `main`) on every conversion
- Cyclic meta-annotations on a parameter no longer crash `@ConvertWith` detection with `StackOverflowError`
- Conversion failure messages now separate the searched converter locations with commas; also fixed a typo in the parameter-count error message

[GitHub Release](https://github.com/nchaugen/tabletest/releases/tag/tabletest-junit-1.2.2)

---

## 2026-07-07 — TableTest Claude Code Plugin v1.6.0


### Added
- **table-driven-testing**: New skill for writing table-driven tests outside the JVM — pytest `parametrize` (Python), Swift Testing `@Test(arguments:)`, Jest/Vitest `test.each`, Go table-driven subtests, and xUnit `[Theory]` (C#)
  - Framework mechanics per ecosystem: named cases everywhere (`pytest.param(id=...)`, Go subtest names, Jest interpolated titles), per-framework "regardless of" combination patterns, and the Swift Testing cartesian-product footgun (multiple collections passed to `arguments:` combine, they don't pair)
  - The same table-design principles as the tabletest skill, applied language-agnostically: decompose concerns into separate parameterised tests, make thresholds visible in the rows, cover every tier and both sides of every boundary, name scenarios by condition (never with the outcome appended), keep expected values literal (no named constants), and give expected-error cases their own test
  - Deliver-don't-ask ambiguity policy: choose the most reasonable interpretation, state it, and deliver complete tests
- **Plugin**: description and keywords updated to cover the third skill

### Changed
- **Routing**: requests for "table-driven tests" on Java/Kotlin projects now route reliably to the tabletest skill — the new skill's description explicitly defers to it



[GitHub Release](https://github.com/nchaugen/tabletest-claude-plugin/releases/tag/v1.6.0)

---

## 2026-07-07 — TableTest Claude Code Plugin v1.5.0


### Added
- **tabletest**: Ambiguity policy for writing tests from a feature description — proceed with the most reasonable interpretation and record assumptions and open questions in `@Description`, rather than stopping to ask clarifying questions
- **tabletest**: Worked example for collapsing an optional-field parameter object (constructor, setters, or builder) into a single map column with a `@TypeConverter` supplying defaults
- **tabletest**: "Let tables drive the API decomposition" — if a row needs a helper fabricating raw data to reach a derived input value, target a narrower function; cheap rows signal a well-placed table
- **tabletest**: Conversion workflow now finishes the migration — replace the old framework's matchers, remove its imports and build-file dependencies (with matching quality check)
- **tabletest**: Value-set guidance covers both axes — grouping same-outcome values within a row and collapsing duplicate rows for interchangeable inputs; every tier expressed as one row including both boundary values

### Changed
- **tabletest**: Quality checks tightened — no `if`/`switch`/ternary in test methods including null-guards (defaulting belongs in a `@TypeConverter` or helper); a lone error/null/empty case belongs as a table row with a `Throws?` column, not a separate `@Test`; date cutoffs prefer descriptive relative values or a cutoff column



[GitHub Release](https://github.com/nchaugen/tabletest-claude-plugin/releases/tag/v1.5.0)

---

## 2026-04-07 — TableTest Reporter 1.1.0

### Changed
- Compatible with tabletest-junit 1.2.1 (array parameter support, quoted map keys)
### Fixed
- Gradle `listFormats` task now supports build caching

[GitHub Release](https://github.com/nchaugen/tabletest-reporter/releases/tag/tabletest-reporter-1.1.0)

---

## 2026-04-06 — TableTest VS Code v0.1.0

### Added

- Web extension support for browser-hosted VS Code, including `.table` formatting, diagnostics, semantic tokens, and command support.
- Browser smoke-test coverage using `@vscode/test-web`.

### Changed

- Extension runtime is now split into shared, desktop, and web entrypoints so the browser host stays free of Node-only APIs.
- Build configuration updated for TypeScript 6 compatibility.

### Fixed

- Browser-hosted Java formatting now falls back cleanly when `java.format.settings.url` cannot be read instead of depending on Node file-system APIs.


[GitHub Release](https://github.com/nchaugen/tabletest-vscode/releases/tag/v0.1.0)

---

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

