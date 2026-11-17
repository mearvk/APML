# tests: regression tests for sleela-source

`run-regression.sh` compiles and runs all nine programs in `sleela-source` and compares their output with the files in `expected/`.

```
bash sleela-source/tests/run-regression.sh             # run the tests
bash sleela-source/tests/run-regression.sh --update    # rewrite expected/ after an intended change
```

## What is checked

Every program must:
1. compile;
2. exit with code 0 within its time limit;
3. print no `FAIL` line;
4. end with its "all N checks passed" line.

Then the output is compared with the expected file:

| Program | Compared with | Why |
|---|---|---|
| knit, modules, medium, professional, conduct, user, weighty, db | `expected/<name>.txt`, line for line | These programs are deterministic: the same code gives the same output. Simulated times count as output. |
| native-weighty | `expected/native-weighty.pass.txt`, the PASS lines and the summary only | It runs over real TCP sockets in real time, so its counts and timings change from run to run. |

The comparison catches changes that a program's own checks would not. For example, changing Bloq's fan-out from 3 to 4, or one profit price, leaves every built-in check passing. The diff against the expected file fails. Both cases were tried and caught.

If a change to the output is intended, run `--update`, read the diff with `git diff sleela-source/tests/expected/`, and commit the new expected files together with the change.

## The execution gate

The `sleela` toolchain will not run until its SHA-256 execution gate passes. That needs `tools/verify-before-execution.py` from SLeeLa and a matching manifest.

When `SLEELA_HOME` and `SLEELA_SHA256_MANIFEST` are not set, `run-regression.sh` calls `setup-gate.sh`, which:
1. fetches only that one file from SLeeLa, at the pinned commit `89b3b02a`;
2. checks it against the pinned SHA-256 `5ab3ec80…becc2f` and stops if the hash differs;
3. writes a manifest for it under `tests/.gate/`.

Build outputs go to `tests/.build/`. Both `.gate/` and `.build/` are git-ignored.

## CI

`.github/workflows/sleela-regression.yml` runs the tests on Ubuntu 24.04. It runs on every push to `main` and on pull requests that touch `sleela-source/` or the toolchain, and it can also be started by hand. It:
1. installs the shared libraries the binary links against;
2. checks `toolchain/sleela` against `sleela-3.2-binaries/SHA256SUMS`;
3. runs `run-regression.sh`;
4. uploads `tests/.build/` if anything fails.

The native test opens ports 48000 to 48999 on 127.0.0.1.

## Result (2026-11-17)

```
ok   knit (knit: all checks passed)
ok   modules (modules: all 41 checks passed)
ok   medium (medium: all 7 checks passed)
ok   professional (professional: all 11 checks passed)
ok   conduct (conduct: all 29 checks passed)
ok   user (user: all 19 checks passed)
ok   weighty (weighty: all 30 checks passed)
ok   native-weighty (native weighty: all 25 checks passed)
ok   db (db port: all 47 checks passed)
regression: all 9 passed
```
