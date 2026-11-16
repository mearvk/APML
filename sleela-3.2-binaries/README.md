# sleela-3.2-binaries

Compiled Linux x86-64 executables (ELF, glibc, dynamically linked) built from https://github.com/mearvk/SLeeLa, branch master. Executables only: no source folders, libraries or fixtures. Checksums are in `SHA256SUMS`.

## Core toolchain
| Binary | Reports |
|--------|---------|
| `sleela` | SLeeLa Language/Runtime 1.3.2-pro, source syntax 1.0 to 3.1 |
| `sleelvac` | Sleelvac 1.3.2-pro, SLeeLa compiler |
| `nordshrift` | Nordshrift 2.7.1-dev, `.sst` transpiler driver |

The folder name says 3.2. The binaries report runtime 1.3.2-pro and source syntax up to 3.1.

## Other tools and programs
Everything else was built by running each module's default `make` target (and `cmake` for the CMake projects). Examples:
`sleelavirin-scan` (anti-virus), `sleelac` (compiler), `sleela-decompiler` and `slecompiler-api-*`, `javaconv` (Java converter), `xltc`, `sleela-registry`, `sleela-registry3`, `sleela-debugger`, `slsh`, `sleela-sql`, `sleela-resolver`, `sleela-web-server`, `sleelas-server-edition`, `sleelas-api-2.1`, `slvm-01`, `slvm-02`, and the `http*` servers.
Test and smoke programs (`test_*`, `*-smoke`, `*_selftest`, `debug-*-test`, and similar) are also here.

Name clashes were resolved like this: `sleelas-api-2.1` and `sleelas-server-edition` (two different `sleelas`), `heritage-print-test-c` and `heritage-print-test-cpp`, `sleela-audio-native` (from `audio/native`) and `sleela-audio-native-from-native-dir` (from `native`).

## Provenance and caveats
- Built from SLeeLa `master` at commit `8ccb9cf1` in the Kiro sandbox, g++ -std=c++17 -O2.
- SLeeLa's `security/sha256-manifest.json` did not match 19 of its own source files at that commit. The core toolchain was built and checked against a manifest generated from the same checkout, kept outside the repo. In a stock checkout, `sleela` and `sleelvac` will refuse to run until `make regen-manifest` is run in `impl` or `SLEELA_SHA256_MANIFEST` points to a matching manifest.
- Linux x86-64 only. Rebuild from source for other platforms.
- Not built (missing system headers or defects in the SLeeLa sources): `http-1.0`, `http-servers/2`, `ballum`, the GTK4 terminal, `lib/connector/*`, `connector`, `sommeliers`, `server-edition/http`, `sleela-virtual-machine/008`. The full test suites were not run.
