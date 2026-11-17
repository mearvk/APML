#!/usr/bin/env bash
# Compile and run the APML design proposal and its messaging modules.
# Needs a SLeeLa checkout for the SHA-256 execution gate: set SLEELA_HOME to it,
# and SLEELA_SHA256_MANIFEST to a manifest that matches that checkout.
set -euo pipefail
here="$(cd "$(dirname "$0")" && pwd)"
sleela="${SLEELA:-$here/../sleela-3.2-binaries/toolchain/sleela}"
: "${SLEELA_HOME:?set SLEELA_HOME to a SLeeLa checkout}"
: "${SLEELA_SHA256_MANIFEST:?set SLEELA_SHA256_MANIFEST to a matching manifest}"
mkdir -p "$here/build"
cd "$here"
"$sleela" compile Strand*.sleela DesignKnit.sleela -o build/knit.sleela
"$sleela" run build/knit.sleela
"$sleela" compile Strand*.sleela modules/*.sleela -o build/modules.sleela
"$sleela" run build/modules.sleela
"$sleela" compile circuit/*.sleela medium/*.sleela -o build/medium.sleela
"$sleela" run build/medium.sleela
"$sleela" compile circuit/*.sleela medium/MediumProfile.sleela professional/*.sleela -o build/professional.sleela
"$sleela" run build/professional.sleela
"$sleela" compile circuit/*.sleela medium/MediumProfile.sleela professional/ProfessionalProfile.sleela professional/ProCircuit.sleela conduct/*.sleela -o build/conduct.sleela
"$sleela" run build/conduct.sleela
