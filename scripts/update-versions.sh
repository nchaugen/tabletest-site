#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
HUGO_CONFIG="$SCRIPT_DIR/../hugo.yaml"

# shellcheck source=scripts/releases.sh
source "$SCRIPT_DIR/releases.sh"

echo "Reading the newest release of each product..."

changed=false

for product in "${PRODUCTS[@]}"; do
    IFS='|' read -r repo prefix param <<< "$product"

    version=$(latest_release "$repo" "$prefix" | cut -f1)
    if [ -z "$version" ]; then
        echo "No release of $repo carries the tag prefix $prefix" >&2
        exit 1
    fi

    if ! grep -q "^  $param:" "$HUGO_CONFIG"; then
        echo "hugo.yaml declares no $param parameter" >&2
        exit 1
    fi

    current=$(sed -n "s|^  $param: \"\(.*\)\"|\1|p" "$HUGO_CONFIG")
    if [ "$version" != "$current" ]; then
        sed -i.bak "s|^  $param: .*|  $param: \"$version\"|" "$HUGO_CONFIG"
        echo "  $param: $current → $version"
        changed=true
    fi
done

rm -f "$HUGO_CONFIG.bak"

if [ "$changed" = false ]; then
    echo "Done. Every version parameter already names the newest release."
else
    echo "Done. Versions written to $HUGO_CONFIG"
fi
