#!/usr/bin/env bash
# PostToolUse hook: after an edit to a message file, check that the Ukrainian
# and Russian files have the same keys. Exit 2 sends the message back to Claude.
set -u

input=$(cat)
# Only react when the edited file itself is one of the message files, not when
# some other edit just mentions their names in its text.
edited=$(printf '%s' "$input" | grep -o '"file_path"[[:space:]]*:[[:space:]]*"[^"]*"' | head -n 1)
case "$edited" in
  *messages_uk.properties\"|*messages_ru.properties\") ;;
  *) exit 0 ;;
esac

root="${CLAUDE_PROJECT_DIR:-$(git rev-parse --show-toplevel 2>/dev/null || pwd)}"
dir="$root/src/main/resources"
uk_file="$dir/messages_uk.properties"
ru_file="$dir/messages_ru.properties"
[ -f "$uk_file" ] && [ -f "$ru_file" ] || exit 0

# Keys are the text before the first "=" or ":" on each non-comment line.
keys() {
  grep -v '^[[:space:]]*[#!]' "$1" | grep '[=:]' \
    | sed -E 's/^[[:space:]]*//; s/[[:space:]]*[=:].*$//' | grep -v '^$' | sort -u
}

uk_keys=$(keys "$uk_file")
ru_keys=$(keys "$ru_file")
only_uk=$(comm -23 <(printf '%s\n' "$uk_keys") <(printf '%s\n' "$ru_keys"))
only_ru=$(comm -13 <(printf '%s\n' "$uk_keys") <(printf '%s\n' "$ru_keys"))

if [ -n "$only_uk$only_ru" ]; then
  {
    echo "Message keys do not match between messages_uk.properties and messages_ru.properties."
    [ -n "$only_uk" ] && echo "Only in Ukrainian (add to messages_ru.properties):" && echo "$only_uk" | sed 's/^/  /'
    [ -n "$only_ru" ] && echo "Only in Russian (add to messages_uk.properties):" && echo "$only_ru" | sed 's/^/  /'
    echo "Add the missing keys to the other file in the same change."
    echo "(If you are in the middle of adding a key to both files, finish the second file now; this message is only a reminder.)"
  } >&2
  exit 2
fi
exit 0
