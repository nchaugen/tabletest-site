#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CHANGELOG_FILE="$SCRIPT_DIR/../content/changelog.md"

REPOS=(
    "nchaugen/tabletest"
    "nchaugen/tabletest-formatter"
    "nchaugen/tabletest-reporter"
    "nchaugen/tabletest-intellij"
)

REPO_NAMES=(
    "TableTest"
    "TableTest Formatter"
    "TableTest Reporter"
    "TableTest IntelliJ Plugin"
)

echo "Fetching GitHub releases..."

ALL_RELEASES='[]'

for i in "${!REPOS[@]}"; do
    repo="${REPOS[$i]}"
    name="${REPO_NAMES[$i]}"
    echo "  → $repo"

    releases=$(gh api "repos/$repo/releases?per_page=100" \
        --jq '[.[] | {tagName: .tag_name, name: .name, body: (.body // ""), publishedAt: .published_at}]' \
        2>/dev/null || echo '[]')

    with_repo=$(echo "$releases" | jq \
        --arg repo "$repo" \
        --arg repoName "$name" \
        '[.[] | . + {repo: $repo, repoName: $repoName}]')

    ALL_RELEASES=$(jq -n --argjson a "$ALL_RELEASES" --argjson b "$with_repo" '$a + $b')
done

SORTED=$(echo "$ALL_RELEASES" | jq 'sort_by(.publishedAt) | reverse')
COUNT=$(echo "$SORTED" | jq 'length')
echo "Found $COUNT releases total."

{
    cat <<'FRONTMATTER'
---
title: Changelog
---

Changes across the TableTest ecosystem, sorted newest first.

FRONTMATTER

    echo "$SORTED" | jq -r '.[] |
        "## " + .publishedAt[0:10] + " \u2014 " + .repoName + " " + .tagName + "\n\n" +
        (if (.body | length) > 0 then .body + "\n\n" else "" end) +
        "[GitHub Release](https://github.com/" + .repo + "/releases/tag/" + .tagName + ")\n\n---\n"
    '
} > "$CHANGELOG_FILE"

echo "Done. Changelog written to $CHANGELOG_FILE"
