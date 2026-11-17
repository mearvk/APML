#!/usr/bin/env bash
# Regression tests for sleela-source.
#
# Compiles and runs every program, then checks:
#   - the program exits 0 and prints its "all N checks passed" line, with no FAIL;
#   - deterministic programs print exactly the output in tests/expected/<name>.txt;
#   - native weighty, whose numbers depend on real time, prints exactly the
#     PASS lines in tests/expected/native-weighty.pass.txt.
#
# Usage:
#   bash sleela-source/tests/run-regression.sh            run all tests
#   bash sleela-source/tests/run-regression.sh --update   rewrite the expected files
# Needs SLEELA_HOME and SLEELA_SHA256_MANIFEST; if they are unset, setup-gate.sh
# is run first. SLEELA overrides the path to the sleela binary.
set -uo pipefail

here="$(cd "$(dirname "$0")" && pwd)"
src="$(cd "$here/.." && pwd)"
sleela="${SLEELA:-$src/../sleela-3.2-binaries/toolchain/sleela}"
update=0
[ "${1:-}" = "--update" ] && update=1

if [ -z "${SLEELA_HOME:-}" ] || [ -z "${SLEELA_SHA256_MANIFEST:-}" ]; then
  eval "$(bash "$here/setup-gate.sh")" || { echo "regression: gate setup failed" >&2; exit 2; }
fi

out="$here/.build"
rm -rf "$out"
mkdir -p "$out"
cd "$src"

# name | sources | mode (exact or pass) | timeout seconds
tests=(
  "knit|Strand*.sleela DesignKnit.sleela|exact|60"
  "modules|Strand*.sleela modules/*.sleela|exact|60"
  "medium|circuit/*.sleela medium/*.sleela|exact|60"
  "professional|circuit/*.sleela medium/MediumProfile.sleela professional/*.sleela|exact|120"
  "conduct|circuit/*.sleela medium/MediumProfile.sleela professional/ProfessionalProfile.sleela professional/ProCircuit.sleela conduct/*.sleela|exact|120"
  "user|conduct/ConductLedger.sleela user/*.sleela|exact|60"
  "weighty|sprung/WeightyProfile.sleela sprung/NewtonCertificateAuthority.sleela sprung/SocketServerNewton.sleela sprung/SprungScaler.sleela sprung/WeightyDemo.sleela|exact|60"
  "native-weighty|sprung/WeightyProfile.sleela sprung/NewtonCertificateAuthority.sleela sprung/Native*.sleela|pass|120"
)

# Drop toolchain chatter that is not program output.
clean() { grep -v -E '^\[memory-manager\]|^\[security\]|^\[defender\]|^verification:|^sleelvac: .* -> .*runnable Sleela Core artifact'; }

failed=0
for t in "${tests[@]}"; do
  IFS='|' read -r name sources mode limit <<< "$t"
  # shellcheck disable=SC2086
  if ! "$sleela" compile $sources -o "$out/$name.sleela" > "$out/$name.compile.log" 2>&1; then
    echo "FAIL $name: compile failed"; clean < "$out/$name.compile.log" | tail -5; failed=$((failed + 1)); continue
  fi
  timeout "$limit" "$sleela" run "$out/$name.sleela" > "$out/$name.raw" 2>&1
  rc=$?
  clean < "$out/$name.raw" > "$out/$name.txt"
  if [ $rc -ne 0 ]; then
    echo "FAIL $name: exit code $rc"; tail -5 "$out/$name.txt"; failed=$((failed + 1)); continue
  fi
  if grep -q '^FAIL ' "$out/$name.txt" || ! grep -q -E 'all( [0-9]+)? checks passed$' "$out/$name.txt"; then
    echo "FAIL $name: program reported failures"; grep '^FAIL ' "$out/$name.txt" | head -5; failed=$((failed + 1)); continue
  fi
  if [ "$mode" = "exact" ]; then
    expected="$here/expected/$name.txt"; actual="$out/$name.txt"
  else
    grep -E '^PASS |checks passed$' "$out/$name.txt" > "$out/$name.pass.txt"
    expected="$here/expected/$name.pass.txt"; actual="$out/$name.pass.txt"
  fi
  if [ $update -eq 1 ]; then
    cp "$actual" "$expected"; echo "UPDATED $name"; continue
  fi
  if [ ! -f "$expected" ]; then
    echo "FAIL $name: no expected file $(basename "$expected")"; failed=$((failed + 1)); continue
  fi
  if diff -u "$expected" "$actual" > "$out/$name.diff"; then
    echo "ok   $name ($(tail -1 "$actual"))"
  else
    echo "FAIL $name: output differs from $(basename "$expected")"; head -20 "$out/$name.diff"; failed=$((failed + 1))
  fi
done

if [ $failed -ne 0 ]; then
  echo "regression: $failed of ${#tests[@]} failed"
  exit 1
fi
echo "regression: all ${#tests[@]} passed"
