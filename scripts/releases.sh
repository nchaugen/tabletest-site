#!/usr/bin/env bash
# Shared release lookup for the TableTest products.
#
# Every product tags its releases differently, and the core repo tags two products
# in one repo. Each entry therefore names the tag prefix that selects the product,
# and the hugo.yaml parameter that carries its version.
#
# repo | tag prefix | hugo.yaml parameter
PRODUCTS=(
    "nchaugen/tabletest|tabletest-junit-|currentTableTestVersion"
    "nchaugen/tabletest-formatter|tabletest-formatter-|currentFormatterVersion"
    "nchaugen/tabletest-reporter|tabletest-reporter-|currentReporterVersion"
    "nchaugen/tabletest-intellij|v|currentIntelliJVersion"
    "nchaugen/tabletest-vscode|v|currentVSCodeVersion"
    "nchaugen/tabletest-claude-plugin|v|currentClaudePluginVersion"
)

# Writes "<version><TAB><published_at>" for the newest release of one product.
# Drafts and prereleases do not count.
latest_release() {
    local repo="$1" prefix="$2"
    gh api "repos/$repo/releases?per_page=100" --jq "
        [ .[]
          | select(.draft | not)
          | select(.prerelease | not)
          | select(.tag_name | startswith(\"$prefix\"))
        ]
        | sort_by(.published_at)
        | last
        | select(. != null)
        | (.tag_name | ltrimstr(\"$prefix\")) + \"\t\" + .published_at
    "
}
