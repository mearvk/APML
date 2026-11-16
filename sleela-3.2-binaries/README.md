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
