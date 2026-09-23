#!/usr/bin/env bash
#
# End-to-end test for a built simplelocalize-cli binary: uploads translations to a
# real project, downloads them back and verifies the content survived the round trip.
#
# This is what the offline smoke test cannot cover - that the binary built for this
# platform can actually talk to the API: TLS/HTTP stack, JSON parsing, file I/O and
# path handling all have to work in the native image on that OS and architecture.
#
# Usage:
#   SIMPLELOCALIZE_TEST_API_KEY=... ./scripts/e2e-test.sh <command> [args...]
#
# Examples:
#   SIMPLELOCALIZE_TEST_API_KEY=... ./scripts/e2e-test.sh ./target/simplelocalize-cli
#   SIMPLELOCALIZE_TEST_API_KEY=... ./scripts/e2e-test.sh java -jar ./target/simplelocalize-cli-2.12.0.jar
#
# Every run works inside its own namespace (E2E_NAMESPACE, default 'ci-local'), so
# jobs running in parallel on different platforms never see each other's data. CI
# passes the matrix label, e.g. E2E_NAMESPACE=ci-linux-arm64.

set -uo pipefail

if [ "$#" -lt 1 ]; then
  echo "Usage: $0 <command> [args...]"
  exit 1
fi

if [ -z "${SIMPLELOCALIZE_TEST_API_KEY:-}" ]; then
  echo "SIMPLELOCALIZE_TEST_API_KEY is not set - nothing to test against, skipping"
  exit 0
fi

CMD=()
for arg in "$@"; do
  if [ -e "$arg" ]; then
    CMD+=("$(cd "$(dirname "$arg")" && pwd)/$(basename "$arg")")
  else
    CMD+=("$arg")
  fi
done

NAMESPACE="${E2E_NAMESPACE:-ci-local}"
# Optional: point the test at a staging server instead of the production API.
BASE_URL_ARGS=()
if [ -n "${E2E_BASE_URL:-}" ]; then
  BASE_URL_ARGS=(--baseUrl "$E2E_BASE_URL")
fi
WORK_DIR="$(mktemp -d)"
UPLOAD_FILE="$WORK_DIR/translations_en.json"
DOWNLOAD_PATTERN="$WORK_DIR/downloaded_{lang}.json"
DOWNLOADED_FILE="$WORK_DIR/downloaded_en.json"

# Changes on every run, so a stale file from an earlier run can never make this pass.
STAMP="$(date +%Y%m%d%H%M%S)-$$"

cleanup()
{
  rm -rf "$WORK_DIR"
}
trap cleanup EXIT

echo "Testing command: ${CMD[*]}"
echo "Namespace: $NAMESPACE"
echo "Stamp: $STAMP"

fail()
{
  echo "::error::E2E test FAILED - $1"
  exit 1
}

cat >"$UPLOAD_FILE" <<JSON
{
  "e2e.hello": "Hello $STAMP",
  "e2e.multiline": "First line\nSecond line",
  "e2e.unicode": "Zażółć gęślą jaźń $STAMP"
}
JSON

echo "::group::Upload"
"${CMD[@]}" upload \
  --apiKey "$SIMPLELOCALIZE_TEST_API_KEY" \
  --uploadFormat single-language-json \
  --languageKey en \
  --uploadNamespace "$NAMESPACE" \
  --uploadPath "$UPLOAD_FILE" \
  --overwrite "${BASE_URL_ARGS[@]}" || fail "upload command returned a non-zero exit code"
echo "::endgroup::"

echo "::group::Download"
"${CMD[@]}" download \
  --apiKey "$SIMPLELOCALIZE_TEST_API_KEY" \
  --downloadFormat single-language-json \
  --downloadNamespace "$NAMESPACE" \
  --languageKey en \
  --downloadPath "$DOWNLOAD_PATTERN" "${BASE_URL_ARGS[@]}" || fail "download command returned a non-zero exit code"
echo "::endgroup::"

[ -f "$DOWNLOADED_FILE" ] || fail "expected downloaded file at $DOWNLOADED_FILE, got: $(ls "$WORK_DIR")"

echo "Downloaded file:"
cat "$DOWNLOADED_FILE"
echo

# The uploaded values must come back unchanged, including the stamp of THIS run.
grep -q "Hello $STAMP" "$DOWNLOADED_FILE" || fail "'e2e.hello' of this run is missing in the downloaded file"
grep -q "Zażółć gęślą jaźń $STAMP" "$DOWNLOADED_FILE" || fail "non-ASCII translation did not survive the round trip"
grep -q "e2e.multiline" "$DOWNLOADED_FILE" || fail "'e2e.multiline' key is missing in the downloaded file"

echo "E2E test PASSED - translations uploaded and downloaded back unchanged."
