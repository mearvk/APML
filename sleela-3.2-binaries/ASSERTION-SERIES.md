# Assertion Series

Purpose: a record of what is steady about this folder. Each assertion is something an engineer or an AI product can rely on without re-deriving it, and each comes with a check that anyone can repeat. The assertions are ordered so that later ones build on earlier ones: integrity, then provenance, then structure, then behavior.

Use as an internal memorandum or inventory record. It states facts about files. It is not tax, legal or accounting advice, and it makes no claim about value.

## Series A: Integrity (what the files are)

| ID | Assertion | Check |
|----|-----------|-------|
| A1 | The folder holds 141 binaries, plus `README.md` and `SHA256SUMS` before this series was added. | `awk 'NR>1' binaries-index.tsv \| wc -l` prints 141 |
| A2 | Every binary matches its recorded SHA-256. | `sha256sum -c SHA256SUMS` reports no failures |
| A3 | Moving files into subfolders did not change any binary's bytes. | A2 passes with the same hashes as before the move |

## Series B: Provenance (where the files came from)

| ID | Assertion | Evidence |
|----|-----------|----------|
| B1 | The binaries are Linux x86-64 ELF executables, dynamically linked against glibc. | README, first paragraph |
| B2 | They were built from SLeeLa `master` at commit `8ccb9cf1` with g++ -std=c++17 -O2, in the Kiro sandbox. | README, Provenance and caveats |
| B3 | Source `security/sha256-manifest.json` did not match 19 source files at that commit. The core toolchain was built against a manifest generated from the same checkout. | README, Provenance and caveats |

## Series C: Structure (how the files are organized)

| ID | Assertion | Check |
|----|-----------|-------|
| C1 | There are 12 group folders and each binary is in exactly one. | `ls -d */ \| wc -l` prints 12. A1 count equals the sum of folder counts in `GROUPINGS.md` |
| C2 | Group counts: toolchain 3, compiler 12, vm 11, antivirus 5, runtime 22, native-bridges 10, network 23, data 8, signal-recognition 11, regex 6, philosophy 19, terminal-gui 11. | `cut -f1 binaries-index.tsv \| sort \| uniq -c` |

## Series D: Behavior (what was and was not established)

| ID | Assertion | Limit |
|----|-----------|-------|
| D1 | Descriptions in README come from module READMEs and from each program's own usage text. | Programs marked "not verified" printed no usage |
| D2 | Programs marked "Not run here" were not started. | Done to avoid opening ports |
| D3 | The full test suites were not run. | A passing smoke test is not a passing suite |
| D4 | Several components were not built (see README). | Missing headers or source defects |

## How the series cascades

A2 is the base: if hashes fail, nothing below it can be relied on. B depends on A, because provenance claims are only useful for files whose identity is confirmed. C depends on A1 and A2, because the structure is only meaningful if the file set is complete. D depends on all of them and carries the stated limits. Anything not listed here is not asserted.

## Adding to the series

Add an assertion only with a repeatable check or a cited source. If a value is unknown, write "unknown" and do not estimate.
