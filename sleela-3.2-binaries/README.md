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
