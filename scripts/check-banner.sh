#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
HUGO_CONFIG="$SCRIPT_DIR/../hugo.yaml"

# shellcheck source=scripts/releases.sh
source "$SCRIPT_DIR/releases.sh"

# The banner block of hugo.yaml, without the lines of the sections around it.
banner_block() {
    awk '/^  banner:/ {inside = 1; next} /^  [a-zA-Z]/ {inside = 0} inside' "$HUGO_CONFIG"
}

banner_key() {
    banner_block | sed -n "s/^    key: *['\"]\(.*\)['\"] *$/\1/p"
}

banner_version() {
    banner_block | grep -oE '[0-9]+\.[0-9]+\.[0-9]+' | head -1
}

newest_release() {
    for product in "${PRODUCTS[@]}"; do
        IFS='|' read -r repo prefix _ <<< "$product"
        latest_release "$repo" "$prefix" | awk -v repo="$repo" -F'\t' '{print $2 "\t" $1 "\t" repo}'
    done | sort | tail -1
}

key=$(banner_key)
announced=$(banner_version)

if [ -z "$key" ]; then
    echo "The banner declares no key. Hextra needs one to store the dismissal." >&2
    exit 1
fi

if [ -z "$announced" ]; then
    echo "The banner message names no version, so neither check below can run." >&2
    exit 1
fi

failed=false

# Hextra stores the dismissal under the key. A message that changes under an
# unchanged key never reaches a reader who dismissed the message before it.
case "$key" in
    *"$announced"*) ;;
    *)
        echo "The banner announces $announced under the key '$key'." >&2
        echo "Every reader who dismissed the earlier banner never sees this one." >&2
        echo "Put the version in the key, as in 'announcement-reporter-$announced'." >&2
        failed=true
        ;;
esac

read -r _ newest repo <<< "$(newest_release)"

if [ "$announced" != "$newest" ]; then
    echo "The banner announces $announced, but $repo $newest is the newest release." >&2
    echo "Rewrite the banner message and key in hugo.yaml, or accept the older news deliberately." >&2
    failed=true
fi

if [ "$failed" = true ]; then
    exit 1
fi

echo "The banner announces $announced, the newest release, under the key '$key'."
