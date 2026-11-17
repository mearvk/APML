#!/usr/bin/env bash
# Build and deploy APML Advantage (advantage.war) from Linux.
#
#   ./deploy.sh --build-only            render aliases, build the WAR, stop
#   ./deploy.sh --dry-run               do everything except copy the WAR; print where it would go
#   DEPLOY_TARGET=/var/lib/tomcat10/webapps ./deploy.sh
#   DEPLOY_TARGET=deploy@host:/opt/tomcat/webapps DEPLOY_URL=https://host/advantage ./deploy.sh
#
# Environment:
#   DEPLOY_TARGET  Tomcat/Jetty webapps directory. A local path, or user@host:/path for SSH.
#   DEPLOY_URL     Base URL of the deployed app (optional). If set, the script waits for
#                  <DEPLOY_URL>/api/v1/health to answer "ok" after the copy.
#   MVN            Maven command (default: mvn).
#   SSH_OPTS       Extra options for ssh and scp (optional), e.g. "-i ~/.ssh/deploy_key".
#
# The container must auto-deploy WARs dropped into its webapps directory (the Tomcat default).
# The WAR is copied under a temporary name and then renamed, so the container never sees a half-copied file.

set -euo pipefail

HERE="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
APP=advantage
WAR="$HERE/target/$APP.war"
MVN="${MVN:-mvn}"
BUILD_ONLY=0
DRY_RUN=0

die() { echo "deploy.sh: $*" >&2; exit 1; }
say() { echo "==> $*"; }

for arg in "$@"; do
  case "$arg" in
    --build-only) BUILD_ONLY=1 ;;
    --dry-run)    DRY_RUN=1 ;;
    -h|--help)    sed -n '2,19p' "${BASH_SOURCE[0]}" | sed 's/^# \{0,1\}//'; exit 0 ;;
    *)            die "unknown option: $arg (try --help)" ;;
  esac
done

# --- preflight ---------------------------------------------------------------------------------
command -v javac >/dev/null || die "javac not found; install a JDK 17 or newer"
command -v "$MVN" >/dev/null || die "Maven not found (set MVN to its path)"
command -v unzip >/dev/null || die "unzip not found (used to check the WAR contents)"
JAVA_MAJOR="$(javac -version 2>&1 | sed -E 's/^javac ([0-9]+).*/\1/')"
[[ "$JAVA_MAJOR" =~ ^[0-9]+$ ]] && (( JAVA_MAJOR >= 17 )) || die "JDK 17 or newer required (found: $(javac -version 2>&1))"

TARGET="${DEPLOY_TARGET:-}"
if (( ! BUILD_ONLY )) && [[ -z "$TARGET" ]]; then
  die "DEPLOY_TARGET is not set. Set it, or use --build-only."
fi
REMOTE=0
if [[ "$TARGET" == *:* ]]; then REMOTE=1; command -v ssh >/dev/null && command -v scp >/dev/null || die "ssh and scp are required for a remote target"; fi
if (( ! BUILD_ONLY && ! REMOTE && ! DRY_RUN )); then
  [[ -d "$TARGET" && -w "$TARGET" ]] || die "DEPLOY_TARGET is not a writable directory: $TARGET"
fi

# --- 1. render assumed-aliases from the Catalog -------------------------------------------------
say "Rendering assumed-aliases"
TMP="$(mktemp -d)"
trap 'rm -rf "$TMP"' EXIT
SRC="$HERE/servlets/java/org/apml/advantage"
javac --release 17 -d "$TMP" "$SRC/Technology.java" "$SRC/Term.java" "$SRC/Catalog.java" "$SRC/AliasRenderer.java"
java -cp "$TMP" org.apml.advantage.AliasRenderer "$HERE/assumed-aliases"

# --- 2. build the WAR ---------------------------------------------------------------------------
say "Building $APP.war"
( cd "$HERE" && "$MVN" -q -B clean package )
[[ -s "$WAR" ]] || die "build finished but $WAR is missing or empty"
say "Built $(du -h "$WAR" | cut -f1) $WAR"

# Every source type must be in the WAR, or the layout in pom.xml is wrong.
for entry in WEB-INF/web.xml html/index.html jsp/index.jsp assumed-aliases/index.html servlets/css/console.css; do
  unzip -l "$WAR" | awk '{print $4}' | grep -qx "$entry" || die "WAR is missing $entry; check the webResources in pom.xml"
done
unzip -l "$WAR" | awk '{print $4}' | grep -q '^WEB-INF/classes/org/apml/advantage/ApiServlet.class$' \
  || die "WAR is missing the compiled servlets"

if (( BUILD_ONLY )); then say "Build only: done"; exit 0; fi

# --- 3. copy ------------------------------------------------------------------------------------
if (( DRY_RUN )); then
  say "Dry run: would copy $WAR to $TARGET/$APP.war"
  exit 0
fi

if (( REMOTE )); then
  HOST="${TARGET%%:*}"
  DIR="${TARGET#*:}"
  say "Copying to $HOST:$DIR"
  # shellcheck disable=SC2086
  scp -q ${SSH_OPTS:-} "$WAR" "$HOST:$DIR/.$APP.war.tmp"
  # shellcheck disable=SC2086
  ssh -o BatchMode=yes ${SSH_OPTS:-} "$HOST" "mv -f '$DIR/.$APP.war.tmp' '$DIR/$APP.war'"
else
  say "Copying to $TARGET"
  cp "$WAR" "$TARGET/.$APP.war.tmp"
  mv -f "$TARGET/.$APP.war.tmp" "$TARGET/$APP.war"
fi

# --- 4. wait for the app ------------------------------------------------------------------------
if [[ -n "${DEPLOY_URL:-}" ]]; then
  command -v curl >/dev/null || die "curl is required for the DEPLOY_URL check"
  URL="${DEPLOY_URL%/}/api/v1/health"
  say "Waiting for $URL"
  for _ in $(seq 1 30); do
    if curl -fsS -m 5 "$URL" 2>/dev/null | grep -q '"status":"ok"'; then say "Deployed and healthy"; exit 0; fi
    sleep 2
  done
  die "copied, but $URL did not report ok within 60 s; check the container log"
fi
say "Copied. Set DEPLOY_URL to have the script check /api/v1/health next time."
