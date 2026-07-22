#!/usr/bin/env bash
#
# Smoke-tests a built simplelocalize-cli binary (native executable or JAR).
# Confirms the app starts and every command runs without crashing (segfault,
# missing native-image reflection config, etc). This does NOT verify business
# logic correctness - only that the process doesn't die from a signal.
#
# Usage:
#   ./scripts/smoke-test.sh ./target/simplelocalize-cli-linux
#   ./scripts/smoke-test.sh java -jar ./target/simplelocalize-cli-2.10.0.jar
#
# Optional: set SIMPLELOCALIZE_TEST_API_KEY to also exercise network-calling
# commands (status/download/upload/pull/auto-translate/publish) against a
# real test project, in addition to the argument-less invocation that always
# runs (which uses whatever apiKey is in the repo's tracked simplelocalize.yml
# and is expected to fail with an auth error - that's fine, we only care that
# it doesn't crash).
#
# 'purge' deletes translations, so it is never invoked for real here.

set -uo pipefail

if [ "$#" -lt 1 ]; then
  echo "Usage: $0 <command> [args...]"
  echo "Example: $0 ./target/simplelocalize-cli-linux"
  echo "Example: $0 java -jar ./target/simplelocalize-cli-2.10.0.jar"
  exit 1
fi

# Resolve any argument that is an existing file to an absolute path, since
# some checks below run with a different working directory (e.g. 'init').
CMD=()
for arg in "$@"; do
  if [ -e "$arg" ]; then
    CMD+=("$(cd "$(dirname "$arg")" && pwd)/$(basename "$arg")")
  else
    CMD+=("$arg")
  fi
done

FAILED=0
LOG_DIR="$(mktemp -d)"
WORK_DIR="$(mktemp -d)"
REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"

echo "Testing command: ${CMD[*]}"
echo "Work dir: $WORK_DIR"
echo "Log dir: $LOG_DIR"

# run_check <description> [args...]
# Runs CMD with the given args, captures output, and flags SIGSEGV/SIGABRT/etc.
# A non-zero "normal" exit code (usage errors, auth failures) is NOT a failure -
# only a signal-terminated process (exit code >= 128) is treated as a crash.
# Stdin is always /dev/null so a command can never block waiting for input.
run_check()
{
  local description="$1"
  shift
  local slug
  slug=$(echo "$description" | tr -c 'a-zA-Z0-9' '_')
  local logfile="$LOG_DIR/${slug}.log"

  echo "::group::${description}"
  echo "+ ${CMD[*]} $*"

  "${CMD[@]}" "$@" >"$logfile" 2>&1 </dev/null
  local exit_code=$?

  cat "$logfile"
  echo "Exit code: $exit_code"

  if [ "$exit_code" -ge 128 ]; then
    local signal=$((exit_code - 128))
    echo "::error::CRASH DETECTED in '${description}' - killed by signal ${signal} (exit code ${exit_code})"
    FAILED=1
  fi

  echo "::endgroup::"
}

# --- Basic startup checks ---------------------------------------------------

run_check "Version" --version
run_check "Help" --help
run_check "No arguments"

# --- Real, offline command runs (no network / credentials required) --------

pushd "$WORK_DIR" >/dev/null || exit 1
run_check "Init (real)" init
popd >/dev/null || exit 1

run_check "Extract i18next (real)" extract \
  --projectType "i18next/i18next" \
  --searchDir "$REPO_ROOT/src/test/resources/i18next" \
  --output "$WORK_DIR/extraction-i18next.json"

run_check "Extract data-i18n-key (real)" extract \
  --projectType "simplelocalize/data-i18n-key" \
  --searchDir "$REPO_ROOT/src/test/resources/extract-data-i18n" \
  --output "$WORK_DIR/extraction-data-i18n.json"

# --- Argument-less invocation of every remaining command --------------------
# These run for real (no --help mixin on subcommands, so we can't rely on
# that). Without SIMPLELOCALIZE_TEST_API_KEY they'll use whatever apiKey is in
# the repo's tracked simplelocalize.yml (a placeholder) and are expected to
# fail with a handled auth/validation error - we only check they don't crash.
# 'purge' is intentionally excluded: it can delete real translations.

run_check "Upload (no args)" upload
run_check "Download (no args)" download
run_check "Pull (no args)" pull
run_check "Status (no args)" status
run_check "Publish (no args)" publish
run_check "Auto-translate (no args)" auto-translate

# --- Real, network-calling commands against a real test project ------------

if [ -n "${SIMPLELOCALIZE_TEST_API_KEY:-}" ]; then
  echo "SIMPLELOCALIZE_TEST_API_KEY is set, running network-calling commands for real"

  run_check "Status (real)" status --apiKey "$SIMPLELOCALIZE_TEST_API_KEY"
  run_check "Download (real)" download \
    --apiKey "$SIMPLELOCALIZE_TEST_API_KEY" \
    --downloadFormat yaml \
    --downloadPath "$WORK_DIR/translations_{lang}.yml"
  run_check "Pull (real)" pull \
    --apiKey "$SIMPLELOCALIZE_TEST_API_KEY" \
    --environment _latest \
    --pullPath "$WORK_DIR/hosting/"

  echo "en:" >"$WORK_DIR/translations_en.yml"
  echo "  HELLO: Hello" >>"$WORK_DIR/translations_en.yml"
  run_check "Upload (real)" upload \
    --apiKey "$SIMPLELOCALIZE_TEST_API_KEY" \
    --uploadFormat yaml \
    --languageKey en \
    --uploadPath "$WORK_DIR/translations_en.yml" \
    --dryRun

  run_check "Auto-translate (real)" auto-translate --apiKey "$SIMPLELOCALIZE_TEST_API_KEY"
  run_check "Publish (real)" publish --apiKey "$SIMPLELOCALIZE_TEST_API_KEY" --environment _latest
else
  echo "SIMPLELOCALIZE_TEST_API_KEY not set - skipping authenticated network commands (argument-less variants above already covered the code path)"
fi

# --- Summary -----------------------------------------------------------------

rm -rf "$WORK_DIR"

if [ "$FAILED" -ne 0 ]; then
  echo "::error::Smoke test FAILED - one or more commands crashed. Logs kept at: $LOG_DIR"
  exit 1
fi

echo "Smoke test PASSED - no crashes detected across all commands."
rm -rf "$LOG_DIR"
exit 0
