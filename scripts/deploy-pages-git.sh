#!/usr/bin/env bash
# Deploy docs dashboard (+ optional Allure) to origin gh-pages via git credentials.
# Avoids gh auth token. Enable Pages once: Settings → Pages → gh-pages / (root).
#
# Skips single-file Allure packs larger than MAX_ALLURE_MB (default 40) — those hang
# git push. Prefer multi-file target/allure-report or dashboard-only.
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"

REPO_URL="$(git remote get-url origin)"
PAGES_BASE="${ALLURE_PAGES_BASE:-https://rajcharchil.github.io/L2B-Vendor-Appium-Automation}"
MAX_ALLURE_MB="${MAX_ALLURE_MB:-40}"
LIVE="${PAGES_LIVE_DIR:-/tmp/l2b-pages-live-$$}"
ALLURE_BIN="$ROOT/.allure/allure-2.32.2/bin/allure"
RESULTS="$ROOT/target/allure-results"
REPORT="$ROOT/target/allure-report"

rm -rf "$LIVE"
mkdir -p "$LIVE/allure" "$LIVE/assets" "$LIVE/rental-flow"

python3 "$ROOT/scripts/build-landing.py" 2>/dev/null || true

cp "$ROOT/docs/index.html" "$LIVE/index.html"
cp "$ROOT/docs/bugs.html" "$LIVE/bugs.html"
cp "$ROOT/docs/rental-flow.html" "$LIVE/rental-flow.html" 2>/dev/null || true
cp "$ROOT/docs/material-flow.html" "$LIVE/material-flow.html" 2>/dev/null || true
cp "$ROOT/docs/REPORTS.md" "$LIVE/REPORTS.md" 2>/dev/null || true
cp -R "$ROOT/docs/assets/." "$LIVE/assets/" 2>/dev/null || true
if [ -d "$ROOT/docs/rental-flow" ]; then
  cp -R "$ROOT/docs/rental-flow/." "$LIVE/rental-flow/"
fi
if [ -f "$ROOT/BUGS_FOUND.docx" ]; then
  cp "$ROOT/BUGS_FOUND.docx" "$LIVE/BUGS_FOUND.docx"
fi
touch "$LIVE/.nojekyll"

copy_allure_if_small() {
  local src="$1"
  local size_mb
  size_mb=$(du -sm "$src" | awk '{print $1}')
  if [ "$size_mb" -gt "$MAX_ALLURE_MB" ]; then
    echo "SKIP Allure from $src (${size_mb}MB > ${MAX_ALLURE_MB}MB) — dashboard only"
    return 1
  fi
  cp -R "$src/." "$LIVE/allure/"
  return 0
}

ALLURE_OK=0
if [ -d "$REPORT" ] && [ -f "$REPORT/index.html" ]; then
  if copy_allure_if_small "$REPORT"; then ALLURE_OK=1; fi
fi
if [ "$ALLURE_OK" = "0" ] && [ -d "$RESULTS" ] && ls "$RESULTS"/*-result.json >/dev/null 2>&1; then
  if [ ! -x "$ALLURE_BIN" ]; then
    mvn -q allure:report || true
  fi
  if [ -x "$ALLURE_BIN" ]; then
    "$ALLURE_BIN" generate "$RESULTS" --clean -o "$LIVE/allure"
    ALLURE_OK=1
  fi
fi
if [ "$ALLURE_OK" = "0" ] && [ -d "$ROOT/reports/company-allure/latest" ]; then
  copy_allure_if_small "$ROOT/reports/company-allure/latest" || true
fi

if [ ! -f "$LIVE/allure/index.html" ]; then
  cat > "$LIVE/allure/index.html" <<EOF
<!doctype html><meta charset=utf-8>
<title>Allure not published</title>
<p>Dashboard is live. Full Allure was skipped (pack too large for git or missing results).</p>
<p>Generate locally: <code>mvn test && ./report.sh</code> then re-run this script with a multi-file <code>target/allure-report</code>.</p>
<p><a href="../">Back to dashboard</a></p>
EOF
fi

export GIT_AUTHOR_NAME="${GIT_AUTHOR_NAME:-L2B Vendor Allure}"
export GIT_AUTHOR_EMAIL="${GIT_AUTHOR_EMAIL:-noreply@users.noreply.github.com}"
export GIT_COMMITTER_NAME="$GIT_AUTHOR_NAME"
export GIT_COMMITTER_EMAIL="$GIT_AUTHOR_EMAIL"

cd "$LIVE"
git init -b gh-pages
git add -A
git commit -m "Pages dashboard $(date -u +%Y-%m-%dT%H:%M:%SZ)"
git remote add origin "$REPO_URL"
GIT_TERMINAL_PROMPT=0 git push -f origin gh-pages

echo
echo "Pushed gh-pages."
echo "  Landing: $PAGES_BASE/"
echo "  Allure:  $PAGES_BASE/allure/"
echo "If 404: GitHub → Settings → Pages → Deploy from branch → gh-pages / (root)"
echo "(gh CLI token may be expired — Pages enable is a one-time UI click.)"
