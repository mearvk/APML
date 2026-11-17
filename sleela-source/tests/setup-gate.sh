#!/usr/bin/env bash
# Prepare the SHA-256 execution gate that the sleela toolchain requires.
#
# sleela refuses to compile or run until tools/verify-before-execution.py from
# a SLeeLa checkout passes against a manifest. This script fetches only that
# file, at a pinned SLeeLa commit, checks it against a pinned SHA-256, and
# writes a manifest covering it. Nothing else from SLeeLa is downloaded.
#
# Usage: source <(bash sleela-source/tests/setup-gate.sh)
#   or:  eval "$(bash sleela-source/tests/setup-gate.sh)"
# It prints the two export lines for SLEELA_HOME and SLEELA_SHA256_MANIFEST.
set -euo pipefail

SLEELA_REPO="https://github.com/mearvk/SLeeLa.git"
SLEELA_COMMIT="89b3b02a9f3dd72647c5fd8ddb9d26867f9ad91b"
VERIFIER="tools/verify-before-execution.py"
VERIFIER_SHA256="5ab3ec8051ec308680c5c684193985066cca1df30beaa63c8b6a173fe0becc2f"

here="$(cd "$(dirname "$0")" && pwd)"
gate="${SLEELA_GATE_DIR:-$here/.gate}"
home="$gate/SLeeLa"

if [ ! -f "$home/$VERIFIER" ]; then
  rm -rf "$home"
  mkdir -p "$home"
  git -C "$home" init -q
  git -C "$home" remote add origin "$SLEELA_REPO"
  git -C "$home" config core.sparseCheckout true
  printf '/%s\n' "$VERIFIER" > "$home/.git/info/sparse-checkout"
  git -C "$home" fetch -q --depth 1 --filter=blob:none origin "$SLEELA_COMMIT" >&2
  git -C "$home" checkout -q FETCH_HEAD >&2
fi

actual="$(sha256sum "$home/$VERIFIER" | cut -d' ' -f1)"
if [ "$actual" != "$VERIFIER_SHA256" ]; then
  echo "setup-gate: $VERIFIER has SHA-256 $actual, expected $VERIFIER_SHA256" >&2
  exit 1
fi

manifest="$gate/manifest.json"
printf '{"algorithm": "sha256", "files": [{"path": "%s", "sha256": "%s"}]}\n' "$VERIFIER" "$VERIFIER_SHA256" > "$manifest"

echo "export SLEELA_HOME=\"$home\""
echo "export SLEELA_SHA256_MANIFEST=\"$manifest\""
