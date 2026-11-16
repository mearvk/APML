# sleela-3.2-binaries

Compiled Linux x86-64 executables (ELF, glibc, dynamically linked) built from https://github.com/mearvk/SLeeLa, branch master. Executables only: no source folders, libraries or fixtures. Binaries are sorted into one subfolder per group (see `GROUPINGS.md`). Checksums are in `SHA256SUMS`, with paths relative to this folder (`sha256sum -c SHA256SUMS`). Further documents: `SENIOR-VIEW.md`, `ASSERTION-SERIES.md`, `binaries-index.tsv`.

## Binaries

Every file in this folder is listed below, grouped by purpose. Descriptions come from each module's own README and from the program's own usage text. "Not run here" means it was not started (to avoid opening ports); "not verified" means the program printed no usage. Test programs are included.

### Core toolchain

Folder: `toolchain/`

The three main tools, built from `impl/`.

| Binary | Description |
|--------|-------------|
| `sleela` | SLeeLa Language/Runtime 1.3.2-pro. Checks, runs and compiles `.sleela` programs on the native C/C++ Core VM. Subcommands include `check`, `run`, `compile -o`, `validate-artifact`, `nordshrift` and `version`. Source syntax 1.0 to 3.1. |
| `sleelvac` | Sleelvac 1.3.2-pro. The named SLeeLa compiler, built as an independent binary from the same front end. Compiles to runnable Core artifacts, accepts `.xclass` and JVM-family input, and runs the SHA-256 execution gate. |
| `nordshrift` | Nordshrift 2.7.1-dev. Transpiler driver for `.sst` sources (spec NS-SST-0001 2.2): `check`, `build`, `objects`, `relevance`, `version`. |

### Compilers, decompiler and converters

Folder: `compiler/`

| Binary | Description |
|--------|-------------|
| `sleelac` | SLeeLa Native Compiler 0.1.0, the C++20 prototype from `compiler/`. `sleelac [--check|--run|--emit-ir|--emit-bytecode FILE|--emit-c FILE|--emit-cpp FILE] SOURCE.sleela`. |
| `sleela_compiler_tests` | Unit tests for `sleelac`. |
| `langc_selftest` | Self-test for the `lib/compiler` language compiler. |
| `xltc` | XLT model compiler (`impl/xlt`). Takes an XLT model and prints a congruence report listing issues. |
| `javaconv` | Java converter from `sleelactl/convert`. Usage: `javaconv --jdk-version N` or `javaconv --jdk DIR --out DIR [--jdk-version N]`. Converts a JDK's Java sources to SLeeLa and expects the `sleelactl/jdk/N` layout. |
| `sleela-decompiler` | Slecompiler native-binary analysis tool: `sleela-decompiler <inspect|disassemble|cfg|functions> <file> [length] [--thumb|--arm]`. |
| `sleela-decompiler-test` | Tests for the decompiler. |
| `slecompiler-api-cfg` | Slecompiler API tool that works on a compiled artifact (control-flow view, going by its name). Takes an artifact path. |
| `slecompiler-api-graph` | Slecompiler API tool that works on a compiled artifact (graph view, going by its name). Takes an artifact path. |
| `slecompiler-api-inspect` | Slecompiler API tool that inspects a compiled artifact. Takes an artifact path. |
| `slecompiler-api-library` | Slecompiler API tool for library inspection of a compiled artifact. Takes an artifact path. |
| `slecompiler-api-slir-vm` | Runs a built-in SLIR VM sample and prints the result (`result: 42`, `steps: 4`). |

### Premium SLeeLa Virtual Machines (SLVM 1 to 11)

Folder: `vm/`

These are the eleven premium VM generations from `sleela-virtual-machine/001` to `011`, the set the repository's `make premium-vms` target builds. Each `slvm-NN` is a standalone executable that links the native Core engine (`libslcore.a`) and runs a compiled `.sleela` artifact. Each generation builds on the one before. They are about 670 KB each.

**Run a program:**

```sh
sleelvac compile -o hello.art hello.sleela     # make an artifact
./vm/slvm-04 hello.art                          # run it on the Supervisor VM
./vm/slvm-04 --selfcheck                        # run the VM's own self-check
```

`sleelvac` needs `SLEELA_HOME` set to a SLeeLa checkout when run outside the repository, and the integrity-manifest note under "Provenance and caveats" applies to it. The `slvm-NN` binaries themselves take an artifact path. They print `not a compiled .sleela artifact` for anything else, including `--help`. The repository's `sleela vm run premium <n> <program>` command is different: it rebuilds the VM from source in a checkout and does not use these files.

**Checked:** all eleven were rebuilt from source with the `premium-vms` flow and came out byte-identical to the files here. All eleven `--selfcheck` runs pass. A hello-world artifact ran correctly on `slvm-01`, `slvm-04`, `slvm-08` and `slvm-11`. The other seven were self-checked but not given a program. Every generation also reports the `intXXX`/`uintXXX` fixed-width number series (`int32` to `int16777216`, `uint32` to `uint16777216`).

| Binary | Codename | What the generation adds (from its self-check) |
|--------|----------|-----------------------------------------------|
| `slvm-01` | Foundation | Dedicated VM foundation: broker, capabilities, security supervisor and OS boundary. |
| `slvm-02` | Operator | Cryptographic and VM-link evolution over the Foundation. |
| `slvm-03` | Specialist | Verified execution: isolation, deterministic policy and supply-chain integrity. |
| `slvm-04` | Supervisor | Authenticated and distributed execution. |
| `slvm-05` | Manager | Reproducible, policy-verifiable, fault-tolerant execution. |
| `slvm-06` | Director | Continuously verified, migratable execution: lineage, attestation and leases. |
| `slvm-07` | Administrator | Hardened management and recovery: logging, health, checkpoints and resources. |
| `slvm-08` | Executive | Supervised execution: admission, leases, transactions and audit. |
| `slvm-09` | Authority | Portable, capability-controlled filesystem substrate with OS adapters. |
| `slvm-10` | Principal | Verified storage execution and an execution-assurance layer. |
| `slvm-11` | Sovereign | Filesystem-module host above the SLVM/9 and SLVM/10 storage contracts. |

Source documentation for each generation is in its `sleela-virtual-machine/0NN/README.md`. The catalogue of generations is `sleela-virtual-machine/SLVmPremiumRegistry.sleela`.

### Anti-virus (Sleelvarin)

Folder: `antivirus/`

The module's own name is Sleelavirin.

| Binary | Description |
|--------|-------------|
| `sleelavirin-scan` | Anti-virus scanner: `sleelavirin-scan [--database FILE] [--content-db FILE] <file> ...`. Whole-file SHA-256 index and byte-pattern content signatures. Not run against a signature database. |
| `test_scan` | Test for the scan core. |
| `test_ac` | Test for the Aho-Corasick content matcher. |
| `test_bridge` | Test for the scan bridge. |
| `test_cpp_scan` | Test for the C++ scan wrapper. |

### Runtime services, registry, configuration, debugging

Folder: `runtime/`

| Binary | Description |
|--------|-------------|
| `sleela-registry` | Registry core: `sleela-registry <seed|review> <file.sleelareg>`. A hierarchical key registry for settings and feature flags. |
| `sleela-registry3` | Registry version 3 tool: `sleela-registry3 init FILE REGISTRY_ID` and further subcommands. |
| `sleela-resolver` | Shared resolver for DNS, IP and filesystem paths: `--self-test` or `--resolve HOSTNAME_OR_IP_OR_PATH`. |
| `sleela-debugger` | SLeeLa debugger: `--self-test` or `--report FILE`. |
| `debug-conformance-test` | Debugger conformance test. |
| `debug-diagnostics-test` | Debugger diagnostics test. |
| `debug-engine-test` | Debugger engine test. |
| `config-location-smoke` | Smoke test for configuration-file location. |
| `vm-config-smoke` | Smoke test for VM configuration. |
| `test_gc_security` | Runtime test for garbage collection and security. |
| `test_java_bridge_injection` | Runtime test for injection handling in the Java bridge. |
| `logger-smoke` | Smoke test for the SLeeLa Logger (packet storage for server editions). |
| `loader_test_c` | Native-loader test, C. |
| `loader_test_cpp` | Native-loader test, C++. |
| `loader_test_reject` | Native-loader test that rejects bad modules. |
| `loader_test_table` | Native-loader test for the module table. |
| `loader_test_typed` | Native-loader test for typed entries. |
| `osls_test` | OS-level-support core test. |
| `osls_runtime_test` | OS-level-support runtime test. |
| `osls_vm_bridge_test` | OS-level-support VM bridge test. |
| `grt_claim_test` | OS-level-support trust-claim test. |
| `sleela_os_support_test` | OS support library test. |

### Native bridges and the Idea System

Folder: `native-bridges/`

| Binary | Description |
|--------|-------------|
| `sleela-opcode-native` | Opcode bridge self-test (103 opcodes, fetch and dispatch). |
| `sleela-gov-native` | Opcode-governance bridge self-test (before, during, after). |
| `sleela-sldocument-native` | `.sldocument` bridge self-test (compile, invoke, kind). |
| `sleela-audio-native` | Audio mixer: `--output OUTPUT --sample-rate RATE --input PATH START_SECONDS GAIN_DB ...` (from `audio/native`). |
| `sleela-audio-native-from-native-dir` | Audio mixer with a simpler form: `OUTPUT INPUT.wav [INPUT.wav...]` (from `native/`). |
| `alphabet-runtime-tests` | Tests for the alphabet native runtime. |
| `sleela_java28_demo` | Demo of the Java 28 bridge. Needs a Java 28 memory host listening on 127.0.0.1. |
| `idea-system` | Idea System 1.0.0 native core demo. |
| `idea-canvas-bench` | Idea canvas benchmark. Reported `create failed` when run here (no display or GPU). |
| `sln-demo` | Demo of the Idea System native layer (`idea/sleela-native`). |

### Servers and networking

Folder: `network/`

Several of these listen on network ports. Start them deliberately and read their `--help` first.

| Binary | Description |
|--------|-------------|
| `h21_server` | HTTP 2.0 reference server: `--port N`, `--self-test`, `--help`. |
| `http3_server` | HTTP 3.0 reference server: `--port N`, `--self-test`. |
| `http4_server_bin` | HTTP 4.0 server: `--bind-address IPv4`, `--port N`, `--self-test`. Binds 127.0.0.1 by default. |
| `http5_server` | HTTP 5.0 server: `--port N`, `--self-test`. |
| `http6_server` | HTTP 6.0 server: `--port N`, `--self-test`. |
| `http7_server` | HTTP 7.0 server: `--port N`, `--bind HOST`, `--allow-fallback`, `--self-test`. |
| `http8_server` | HTTP 8.0 server: `--port N`, `--self-test`. |
| `http-server-1` | HTTP server 1: `--port N --threads N --root DIR --log FILE [--once]`. |
| `http-server-3` | HTTP server 3 (TLS): `--addr ADDR --port PORT --key PRIVATE_KEY --cert CERTIFICATE [--backend PATH]`. Default port 8082. |
| `http-server-3-http3` | HTTP/3 variant of server 3, with the same options. |
| `key_distribution_server` | Key distribution server for HTTP 3.0. Stops with `TLS initialization failed` without TLS material. |
| `http3_pipeline_demo` | Prints the HTTP 3.0 data flow: request, envelope, pipeline, response. |
| `crypto_selftest` | HTTP 3.0 cryptography self-test. |
| `http4_selftest` | HTTP 4.0 self-test. |
| `http4_session_selftest` | HTTP 4.0 session self-test. |
| `http4_resume_selftest` | HTTP 4.0 resume self-test. |
| `http4_segmentation_selftest` | HTTP 4.0 segmentation self-test. |
| `sleela-web-server` | SQL-backed HTTP/1.1 web server (`web-server-technology`). Not run here. |
| `sleelas-api-2.1` | `sleelas`, the SLeeLa Server Launcher for the API 2.1 reference program. |
| `sleelas-server-edition` | `sleelas` built from `server-edition/`. Not run here. |
| `skya` | Skya telephony server. Starts a listener on 0.0.0.0:8443 (HTTP/3, room `lobby`) as soon as it runs. |
| `sleela-skya` | Skya telephony native program (`telephony-skya/native`). Not run here. |
| `bodi-xml-runner` | Runs a Bodi XML project (API 2.1). Needs the project path. |

### Data transport, ledger, SQL and the advantage module

Folder: `data/`

| Binary | Description |
|--------|-------------|
| `transport` | Secure, idempotent acquisition: `transport review <url>`, `fetch <url> [--store DIR] [--sha256 HEX]`, `keystore`, `kex <x25519|p256|ffdhe2048|ffdhe3072>`. |
| `bulkfetch` | Bulk fetcher. Takes a list file of URLs. |
| `ledger_tool` | Ledger tool: `ledger_tool build|verify ...`. |
| `sleela-sql` | SQL tool from `lib/sleela-sql`. Printed nothing for `--help`; not verified further. |
| `advantage` | Demo of the advantage calculation (reward, baseline, weight). |
| `analyze` | Analysis tool from the advantage module. Rejects `--help`; its options are not documented here. |
| `trade_examples` | Examples that consume the advantage model for trades. |
| `trading_demo` | Prints strategic trade-offs across venue types (crypto, and others). |

### Telecom, signal processing and recognition

Folder: `signal-recognition/`

| Binary | Description |
|--------|-------------|
| `cdma-sim` | CDMA simulator: `--std is95|1xrtt|evdo|wcdma`, `--sf N`, `--users N`, `--ebn0 dB`. |
| `ofdma-demo` | OFDMA reference demo (100 resource blocks). |
| `test-ofdma` | OFDMA tests. |
| `sleela_sp_c_example` | Signal-processing library example, C (prints RMS and samples). |
| `sleela_sp_cpp_example` | Signal-processing library example, C++. |
| `character-recognition-cli` | `character-recognition-cli MODEL.slcrm INPUT.pgm OUTPUT.txt REVIEW.jsonl [pixel-threshold] [minimum-similarity]`. |
| `character-recognition-smoke` | Character recognition smoke test (prints PASS). |
| `neural-nets-xor` | Small neural network trained on XOR; prints expected and predicted values. |
| `utf4088-inspect` | `utf4088-inspect --id HEX_IDENTIFIER`: inspects a UTF-4088 identifier. |
| `utf4088_tests` | UTF-4088 tests. |
| `utf4088_encoder_decoder_tests` | UTF-4088 encoder and decoder tests. |

### Regex

Folder: `regex/`

Regular-expression engines and tests from `regex/`. They printed nothing for `--help`; their usage was not verified.

| Binary | Description |
|--------|-------------|
| `regex-c` | Regex engine, C. |
| `regex-cpp` | Regex engine, C++. |
| `regex-natural-c` | Natural-language regex variant, C. |
| `regex-natural-cpp` | Natural-language regex variant, C++. |
| `natural-c` | Natural-language regex test, C. |
| `natural-cpp` | Natural-language regex test, C++. |

### Philosophy module examples and tests

Folder: `philosophy/`

Reference implementation of the conversation mask and integer reduction (non-clinical). All data is synthetic.

| Binary | Description |
|--------|-------------|
| `example-c` | Conversation-mask example, C (records visible and omitted). |
| `example-cpp` | Conversation-mask example, C++. |
| `consistency-example` | Consistency example (signal tossing and commit intervals). |
| `event-example-c` | Event tree example, C. |
| `event-example-cpp` | Event tree example, C++. |
| `partition-example` | Partition example. |
| `int-set-generate` | Writes `int_set.json` and `int_set.xml` (16 traits, 4 age bands). |
| `help-example-c` | Help and reach-out desk example, C (placeholder contacts only). |
| `help-example-cpp` | Help and reach-out desk example, C++. |
| `speculation-example` | Invitation for speculative commentary example. |
| `test-consistency` | Consistency tests. |
| `test-event-tree` | Event tree tests. |
| `test-help-desk` | Help desk tests. |
| `test-int-set` | Integer set tests. |
| `test-partition-api` | Partition API tests. |
| `test-screen-map` | Screen map tests. |
| `test-speculation-invite` | Speculation invite tests. |
| `heritage-print-test-c` | Common Rails printing test, C. |
| `heritage-print-test-cpp` | Common Rails printing test, C++. |

### Terminal, editors, GUI and 3D

Folder: `terminal-gui/`

| Binary | Description |
|--------|-------------|
| `slsh` | SleelaTerminal shell. |
| `smoke` | SleelaTerminal smoke test. |
| `sleela-editor-theme` | Editor themes: `--themes`, `--theme ID --palette`, `--theme ID --hex COLOR`. Themes: american, eurasia, russian-slavic, silk-road, east-asian. |
| `slgui_bridge_test` | GUI bridge test. |
| `slgui_theme_test` | GUI theme test. |
| `city3d` | Phraign City 3D: `city3d generate --config <file> [--out <model.city>] [--ppm <img.ppm>]`. |
| `city3d_smoke` | City 3D smoke test. |
| `render_smoke` | 3D render smoke test. |
| `fileman` | GNU Readline example: file manager. |
| `rl` | GNU Readline example: `rl [-p prompt] [-u unit] [-d default] [-n nchars]`. |
| `rltest` | GNU Readline test program. |

## Provenance and caveats
- Built from SLeeLa `master` at commit `8ccb9cf1` in the Kiro sandbox, g++ -std=c++17 -O2.
- SLeeLa's `security/sha256-manifest.json` did not match 19 of its own source files at that commit. The core toolchain was built and checked against a manifest generated from the same checkout, kept outside the repo. In a stock checkout, `sleela` and `sleelvac` will refuse to run until `make regen-manifest` is run in `impl` or `SLEELA_SHA256_MANIFEST` points to a matching manifest.
- Linux x86-64 only. Rebuild from source for other platforms.
- Not built (missing system headers or defects in the SLeeLa sources): `http-1.0`, `http-servers/2`, `ballum`, the GTK4 terminal, `lib/connector/*`, `connector`, `sommeliers`, `server-edition/http`, the alternate `sleela-virtual-machine/008/build/linux` build (`slvm-08` itself is included). The full test suites were not run.
