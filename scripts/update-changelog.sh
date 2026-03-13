#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CHANGELOG_FILE="$SCRIPT_DIR/../content/changelog.md"

REPOS=(
    "nchaugen/tabletest"
    "nchaugen/tabletest-formatter"
    "nchaugen/tabletest-reporter"
    "nchaugen/tabletest-intellij"
    "nchaugen/tabletest-vscode"
    "nchaugen/tabletest-claude-plugin"
)

REPO_NAMES=(
    "TableTest"
    "TableTest Formatter"
    "TableTest Reporter"
    "TableTest IntelliJ Plugin"
    "TableTest VS Code"
    "TableTest Claude Code Plugin"
)

echo "Fetching GitHub releases..."

ALL_RELEASES='[]'

for i in "${!REPOS[@]}"; do
    repo="${REPOS[$i]}"
    name="${REPO_NAMES[$i]}"
    echo "  → $repo"

    releases=$(gh api "repos/$repo/releases?per_page=100" \
        --jq '[.[] | select(.tag_name | startswith("tabletest-parser") | not) | {tagName: .tag_name, name: .name, body: (.body // "")}]' \
        2>/dev/null || echo '[]')

    # Replace publishedAt with the tag's commit date for accuracy
    enriched='[]'
    while IFS= read -r release; do
        tagName=$(echo "$release" | jq -r '.tagName')
        tagDate=$(gh api "repos/$repo/commits/$tagName" --jq '.commit.committer.date' 2>/dev/null || echo "")
        release=$(echo "$release" | jq --arg d "${tagDate:-1970-01-01T00:00:00Z}" '. + {publishedAt: $d}')
        enriched=$(jq -n --argjson a "$enriched" --argjson b "$release" '$a + [$b]')
    done < <(echo "$releases" | jq -c '.[]')

    with_repo=$(echo "$enriched" | jq \
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
toc: false
---

Changes across the TableTest ecosystem, sorted newest first.

FRONTMATTER

    echo "$SORTED" | jq -r '.[] |
        "## " + .publishedAt[0:10] + " \u2014 " + .repoName + " " + (.tagName | split("-") | last) + "\n\n" +
        (if (.body | length) > 0 then .body + "\n\n" else "" end) +
        "[GitHub Release](https://github.com/" + .repo + "/releases/tag/" + .tagName + ")\n\n---\n"
    '
} > "$CHANGELOG_FILE"

echo "Done. Changelog written to $CHANGELOG_FILE"
