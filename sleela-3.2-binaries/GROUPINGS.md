# Groupings

The 141 binaries sit in 12 subfolders, each chosen by what the program is (its object) and how it is used (its orientation). Every file has exactly one home. Per-binary descriptions are in `README.md`. A flat, parseable list is in `binaries-index.tsv`.

| Folder | Object / orientation | Count | Listens on a port? |
|--------|----------------------|------:|--------------------|
| `toolchain/` | The three main tools: `sleela`, `sleelvac`, `nordshrift` | 3 | No |
| `compiler/` | Other compilers, decompiler, converters, Slecompiler API tools and their tests | 12 | No |
| `vm/` | SLVM generations 1 to 11 | 11 | No |
| `antivirus/` | Sleelavirin scanner and its tests | 5 | No |
| `runtime/` | Registry, resolver, debugger, loader, OS-level support, GC and security tests | 22 | No |
| `native-bridges/` | Opcode, governance, document and audio bridges, Idea System | 10 | `sleela_java28_demo` needs a host on 127.0.0.1 |
| `network/` | HTTP servers 1 to 8, TLS, key distribution, web server, Skya, launchers, self-tests | 23 | Yes, for most servers |
| `data/` | Transport and fetch, ledger, SQL, advantage and trading demos | 8 | No |
| `signal-recognition/` | CDMA, OFDMA, signal processing, character recognition, neural net, UTF-4088 | 11 | No |
| `regex/` | Regex engines and natural-language variants | 6 | No |
| `philosophy/` | Conversation mask, event tree, partition, help desk and speculation examples and tests | 19 | No |
| `terminal-gui/` | Terminal, editor themes, GUI bridge tests, City 3D, Readline examples | 11 | No |

## Placement rules

1. A program goes where its object lives, not where it is tested. Tests sit next to the thing they test (for example `http4_selftest` is in `network/`, `test_scan` is in `antivirus/`).
2. Each HTTP generation and its self-tests stay together in `network/`.
3. Folder names are lowercase and do not carry version numbers, so later releases can reuse them.

## Caveat on `kind` in `binaries-index.tsv`

The `kind` column (`program` or `test`) is a name-based guess (names containing `test` or `smoke`). It has not been checked against each program's behavior.

## Verifying

```
cd sleela-3.2-binaries && sha256sum -c SHA256SUMS
```

`SHA256SUMS` uses paths relative to this folder, such as `vm/slvm-01`. The hashes are unchanged from before the move, because moving a file does not alter its bytes.
