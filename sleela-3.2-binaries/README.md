# sleela-3.2-binaries

Fresh builds of the SLeeLa toolchain, compiled from source on 2026-11-16 in the Kiro sandbox.

| Binary | Reports |
|--------|---------|
| `sleela` | SLeeLa Language/Runtime 1.3.2-pro, source syntax 1.0 to 3.1 |
| `sleelvac` | Sleelvac 1.3.2-pro, SLeeLa compiler, source syntax 1.0 to 3.1 |
| `nordshrift` | Nordshrift 2.7.1-dev, NS-SST-0001 spec 2.2, source syntax up to 3.1 |

The folder name says 3.2 at the owner's request. The binaries themselves report
runtime version 1.3.2-pro and source syntax up to 3.1. They do not report a
syntax 3.2.

## Provenance
- Source: https://github.com/mearvk/SLeeLa, branch master, commit `8ccb9cf1`.
- Build: `cd impl && make -j8 build/sleela build/sleelvac build/nordshrift` (g++ -std=c++17 -O2, Linux x86-64, glibc, dynamically linked).
- Integrity gate: the repository's `security/sha256-manifest.json` did not match 19 of its source files at this commit (recent edits to impl/core and impl/frontend sources, three new math files, and some lib files). The build and the run-time check were done against a manifest freshly generated from the same checkout, kept outside the repository. The repository's own manifest was not changed.
- Checksums: see `SHA256SUMS`.

## Checked
- `version` on all three.
- `sleela run` and `sleelvac run` on impl/examples hello, fizzbuzz and factorial.
- `sleelvac compile -o` produced a runnable artifact.
- Not run: the full test suite.

## Caveats
- Linux x86-64 only. Rebuild from source for any other target.
- Because these were built against a regenerated manifest, they will refuse to run in a stock SLeeLa checkout until its manifest is regenerated (`make regen-manifest` in impl) or SLEELA_SHA256_MANIFEST points to a matching manifest.
- The sibling folder `sleela-3.0-binaries` holds the prebuilt copies from SLeeLa/working-binaries, unchanged (sleela and nordshrift only; there is no sleelvac there).

## Additional modules (added after the core three)

Built from the same SLeeLa checkout by running `make -C <module> all` for each module that has a Makefile. All are Linux x86-64 ELF. Each was checked for missing shared libraries (none). Beyond that they were not exercised, except as noted.

| Folder | Contents | Source module |
|--------|----------|---------------|
| `antivirus/` | `sleelavirin-scan` | `antivirus/` (the anti-virus the owner calls Sleelvarin; the source spells it Sleelavirin). Starts and prints usage. Not run against a signature database. |
| `xlt/` | `xltc` | `impl/xlt` |
| `registry/` | `sleela-registry`, `sleela-registry3` | `registry/` |
| `debugger/` | `sleela-debugger` | `debugger/` |
| `servers/` | `h21_server`, `http3_server`, `http4_server_bin`, `http5_server`, `http6_server`, `http7_server`, `http8_server`, `http-server-1`, `http-server-3`, `sleela-web-server`, `sleelas` | `http-2.0` to `http-8.0`, `http-servers/1,3`, `web-server-technology/native`, `server-edition` |
| `terminal/` | `slsh` | `sleela-terminal` (shell only; the GTK4 terminal app did not build) |
| `editors/` | `sleela-editor-theme` | `linux-editors` |
| `idea/` | `idea-system`, `idea-canvas-bench`, `sln-demo` | `idea/` |
| `transport/` | `transport`, `bulkfetch`, `libadvantagetransport.so.1.0.0` | `advantage/transport` |
| `regex/` | `regex-c`, `regex-cpp`, `regex-natural-c`, `regex-natural-cpp` | `regex/` |
| `java28/` | `sleela_java28_demo`, `libsleela_java28.so` | `java28/` |
| `selftests/` | self-test and smoke programs from antivirus, config, debugger, http-3.0, http-4.0, lib/compiler, logger, runtime, sleela-terminal | various |

## Not included, and why
- `http-1.0`: does not compile. `http1_server.c` defines `http1_download_should_resume` twice (source defect).
- `http-servers/2`: needs the nghttp2 development headers (`nghttp2/nghttp2.h`), not installed here.
- `ballum`, and the GTK4 `SleelaTerminal` app: need system development packages (GTK4, VTE, and the ballum dev files).
- Converter (`lib/java/converter`): a SLeeLa library, not a native binary. Compiling `JConvMain.sleela` with `sleelvac` fails with a syntax error at line 99 (an inline `<` comment), so no artifact was produced.
- `lib/decompiler`, `lib/vm`, `sleela-scripting`, `codecs`, `churn`, `compression`, `crypto`, `http-1.0`, `ui`, `user-interface`: ran without producing a Linux executable (libraries, data, or Java build output only).
- Not attempted: Windows and macOS targets, and modules whose Makefile has no plain `all` target.

## Second sweep (every other module with a Makefile)

A second pass ran each remaining Makefile's default target (118 directories, skipping `install`, `jetbrains`, `build/macos`, `build/windows` and `lib/ballum`, whose default targets install to system paths or are not Linux builds). Every Linux ELF executable and shared library it produced was copied here, grouped in a folder named after its module, with test and smoke programs under `selftests/`. Only compiled binaries are in this folder; no source files.

Notable additions: `sleelac` (`compiler/`), `sleela-decompiler` and the `slecompiler-api-*` tools (`decompiler/`), `javaconv` (`sleelactl/`, the Java converter), `slvm-01` and `slvm-02` (`sleela-virtual-machine/`), `sleela-sql`, `sleela-resolver`, `ledger_tool`, `analyze` and `trading_demo` (`advantage/`), `bodi-xml-runner` (`api-sleela-2.1-bodi/`), and the `philosophy`, `bash` (model-3D, readline) and `signal-processing` examples.

Several modules built without producing a binary, and some failed (for example `connector`, most `lib/connector/*`, `lib/gui`, `sommeliers`, `website-generator`, `autocad`). Their logs were not reviewed in this pass.

## CMake projects
Built out of tree with cmake (Release): `os-level-support` (loader, runtime, trust), `lib/os-level-support`, `lib/native/loader` and `utf-4088`. The tool `utf4088-inspect` is in `utf-4088/`; the test programs and loader fixtures are under `selftests/`. `lib/native/loader` produced no executable.

## Why other modules did not produce binaries
- Missing system development packages: `lib/connector/*` (mysql, curl, odbc and others), `connector` (download blocked), `http-servers/2` (nghttp2), `http-3.0/kernel` (Linux kernel headers), `ballum` and the GTK4 terminal.
- Defects in the repo sources: `http-1.0` (duplicate function definition), `sommeliers` (stray backslash in `feedback.c`), `server-edition/http` (a source file it needs, `api/http-multiplexing/sleela_http_api.c`, is missing), `sleela-virtual-machine/008/build/linux` (include path to `sleela_core.h`).
- SLeeLa programs rather than native builds: `autocad` and `website-generator` fail `sleelvac` checks (unknown types `Model` and `Design`), and `lib/gui` and the `lib/*/example/science` targets run SLeeLa checks.
These can be revisited when the headers are installed or the sources are fixed.
