# sleela-source: APML initial design proposal in SLeeLa

Status: initial design proposal, version 0.1.0, 2026-11-16. Nothing here implements APML, Bloq, Bodi, Sprung, Runyn or Falthruu yet. These files record the proposal for each of them in a form that compiles and runs, so its structure can be checked by a program and not only by a reader.

Source documents: [`docs/ENGINEERING-PRINCIPLES.md`](../docs/ENGINEERING-PRINCIPLES.md) (the six strands) and [`analysis/APML-ANALYSIS.md`](../analysis/APML-ANALYSIS.md) (the page-by-page study). Statements of what a technology does are the owner's, taken from those documents. Goals, procedures, guards and implementation items are the drafter's proposals and can be rejected.

## Layout

```
sleela-source/
  Strand1Apml.sleela       strand 1, APML      the description
  Strand2Bloq.sleela       strand 2, Bloq      the making
  Strand3Bodi.sleela       strand 3, Bodi      the reach
  Strand4Sprung.sleela     strand 4, Sprung    the message and the time
  Strand5Runyn.sleela      strand 5, Runyn     the refolding
  Strand6Falthruu.sleela   strand 6, Falthruu  the carrying through
  DesignKnit.sleela        checks that the six proposals fit together
  modules/                 how other modules poll, query, interrupt and exchange with the strands
  build.sh                 compiles and runs both programs
```

## What each strand file holds

Every strand class answers the same questions, in the same order as the engineering-principles document:

| Method | Content |
|--------|---------|
| `question()`, `principle()` | The question the strand answers and its one-sentence principle |
| `ownerClaim(i)` | What the owner says the technology does |
| `goal(i)` | What the design must achieve |
| `procedure(i)` | What a developer does day to day |
| `signalIn(i)`, `signalOut(i)` | What the strand receives and from whom, and what it hands on |
| `agencyGuard(i)` | Rules that keep decisions with accountable people |
| `implementation(i)` | The first concrete things to build |
| `openItem(i)` | What is still undefined; never empty |
| `handOffTo()`, `pairedWith()` | The next strand, and the strand it faces in the middle fold |

Each class also carries the full SLeeLa Metadata Class record (eleven attributes with get and set accessors). Trademark, corporation and origin are `"unknown"` and the schedule is `"none"` because no real values have been supplied. The science rating is 1 because nothing has been built.

## The knit

The engineering-principles document uses "strand" for each technology. The proposal is knitted when two properties hold, and `DesignKnit` checks both:

1. **The loop.** Each strand hands off to the next, and Falthruu hands back to APML, so following hand-offs from any strand visits all six and returns.
2. **The fold.** Folding the loop in the middle pairs 1 with 6, 2 with 5 and 3 with 4. Each pair is a forward act and its return: write and carry a change through, generate and refold, address and record.

`DesignKnit` also requires every strand to carry at least one agency guard and one open item. It lists the six owner terms that still need a definition (Voltage on Time, Attention Creation Principle, Economic and mode, Two, Public speaker is born, Creative time-force equation). Their owner descriptions are recorded, but no formula has been invented for any of them.

## Agency

"Protect agency" is read here as a design rule: software may propose, but an accountable person decides. In code this becomes:

- Bodi and Falthruu changes need a named human approver.
- The Sprung log is append-only. Destroy ends a thing but not its record.
- Runyn reports are advisory.
- Generated files say they are generated.
- No scoring term may rank or exclude a person without a stated, reviewable formula.

In `modules/`, the bus refuses interrupts, broadcasts and exchange steps that do not name an actor and a reason.

## Mapping to the requested themes

| Theme | Where it lives |
|-------|----------------|
| Design proposals | the six strand classes |
| Implementations | `implementation(i)` in each strand |
| Signals | `signalIn(i)`, `signalOut(i)`, and the envelopes in `modules/` |
| Attentions | Sprung's Attention Creation Principle, recorded as an owed term |
| Attenuation | bounded mailboxes in `modules/` that refuse and count overflow instead of dropping messages |
| Games | the co-exchange in `modules/ApmlExchange.sleela`, a two-party offer, counter and acknowledge protocol |
| Goals, procedures | `goal(i)`, `procedure(i)` |
| Intelligences | not implemented; no AI component is part of this proposal |

## Build and run

Requires the toolchain in [`../sleela-3.2-binaries/toolchain/`](../sleela-3.2-binaries/toolchain/) (Linux x86-64). The toolchain refuses to run until its SHA-256 execution gate passes. That gate needs `tools/verify-before-execution.py` from a SLeeLa checkout, plus a manifest that matches that checkout:

```
export SLEELA_HOME=/path/to/SLeeLa
export SLEELA_SHA256_MANIFEST=/path/to/matching/sha256-manifest.json
./build.sh
```

Programs that span files must be compiled together (`sleela compile a.sleela b.sleela -o out.sleela`). `sleela check` on a single file does not see the other classes.

Result on 2026-11-16 with `sleela` 1.3.2-pro:

```
knit: all checks passed
modules: all 41 checks passed
```

## Language notes

- Files declare `#sleela 3.1`, the first version that accepts array initializers such as `{ "a", "b" }`.
- Comments use the `<` and `<<` angle-bracket style. Message kind codes are plain ints, and no class reads another class's constants.
