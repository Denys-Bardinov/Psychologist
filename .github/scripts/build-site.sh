#!/usr/bin/env bash
# Builds the site into ./dist.
# - When the Astro app exists (package.json in the repo root): install and build it.
# - Until then: publish the "coming soon" page from ./placeholder.
set -euo pipefail

rm -rf dist

if [ -f package.json ]; then
  echo "package.json found: building the app"
  npm run build
  if [ ! -d dist ]; then
    echo "Build finished but ./dist was not created" >&2
    exit 1
  fi
else
  echo "No package.json yet: publishing the placeholder page"
  cp -R placeholder dist
fi

test -f dist/index.html || { echo "dist/index.html is missing" >&2; exit 1; }
echo "Site built into ./dist"
