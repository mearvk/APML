# Senior View

A short orientation for engineers at Grade 3 or 4, and for AI tools that need the shape of this folder quickly. The aim is that a reader who already knows Product 2 can start domain interviews without first reading all 141 descriptions.

## Read in this order

1. `toolchain/`: `sleela` (check, run, compile), `sleelvac` (compiler with SHA-256 execution gate), `nordshrift` (`.sst` transpiler). Everything else depends on these.
2. `vm/`: eleven SLVM generations. Each builds on the one before, so read `slvm-01`, then pick the generation you care about.
3. `runtime/`: registry, loader, debugger and OS-level support. This is where most tests live.
4. `network/`: servers. Read `--help` before starting any of them.
5. The remaining folders are domain modules. Open them only when an interview touches that domain.

## Risk at a glance

| Folder | Handle with care because |
|--------|--------------------------|
| `network/` | Many servers open ports. `skya` listens on 0.0.0.0:8443 as soon as it starts. |
| `toolchain/` | `sleela` and `sleelvac` run the SHA-256 execution gate against the checkout they run in. The 2026-11-20 rebuilds pass it in a stock checkout of SLeeLa `ffc820696`; a checkout whose manifest does not match its sources is refused (see README, Provenance and caveats). |
| `native-bridges/` | `idea-canvas-bench` needs a display or GPU. `sleela_java28_demo` needs a Java 28 memory host. |
| all others | Local, no listener known. |

## Questions worth asking in a domain interview

- Which SLVM generation does the product actually target, and why that one?
- Which HTTP generation is in use, and is TLS material available for it?
- Which checks are the team's acceptance gates (the `*_test`, `*-smoke` and `*selftest` programs)?
- Which of the "not built" components listed in README are on the critical path?

## What this view does not cover

The full test suites were not run. Usage was not verified for the programs the README marks "not verified" or "not run here".
