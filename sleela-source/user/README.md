# user: User Class II

`UserClassII` is a person who uses an APML system. It is written so that the nine moral statements in [`MORAL.md`](../../MORAL.md) hold for that person. "Primitives" here means the base rules of the system as they apply to a human user. They are not biological or medical primitives.

**No health claim.** This class does not treat, diagnose or cure any illness, cancer included, and nothing in it claims to. It holds no health data and refuses any attempt to store some.

| Statement | Method | What is enforced |
|---|---|---|
| 1 People decide | `decide` | Only the user accepts or refuses what is proposed to them, and a refusal needs no reason |
| 2 Everyone starts level | (no rank fields) | The class has no rank, privilege or role flag. Every actor must give a reason. |
| 3 Nothing is hidden | `entriesAbout` | The user can count and read every entry about them |
| 4 Every refusal is explained | `refuseUser` | Turning the user away needs a reason code from `ReasonCatalog` |
| 5 Consent is mutual | `consent`, `withdraw`, `actOn` | No one acts on the user without consent, an actor and a reason. Withdrawing consent stops further acts. |
| 6 Claims are honest | `claim` | A claim is a count from the record. Anything else returns "no claim: not in the record". |
| 7 Endings keep their record | `closeAccount` | The record and the pseudonym stay. The display name and preferences are dropped. |
| 8 Not scored by who they are | `setAttribute` | Only language, time zone and notification preferences are stored. Age, health and other traits are refused. |
| 9 The start stays level over time | `exportAll` | The user can take everything held about them |
| Growth | `grant` | A capability is granted only with an actor and a reason, and is recorded |

```
sleela compile conduct/ConductLedger.sleela user/*.sleela -o build/user.sleela
sleela run build/user.sleela
```

Result on 2026-11-16: `user: all 19 checks passed`.

Open item: closing an account keeps a pseudonymous record, which follows statement 7. Whether a user may also have that record erased, as some data-protection laws require, is a decision for the owner and for counsel.

## Session record, 2026-11-16

This section records the working session that produced User Class II and the work before it. It is kept here for now, at the owner's request, and may move to its own file later. Every commit was pushed straight to `main`.

### What was asked and what was done, in order

1. **Organize `sleela-3.2-binaries`.**
   - The 141 binaries were moved into 12 group folders: `toolchain`, `compiler`, `vm`, `antivirus`, `runtime`, `native-bridges`, `network`, `data`, `signal-recognition`, `regex`, `philosophy`, `terminal-gui`.
   - `SHA256SUMS` was rewritten for the new paths, and every binary still matches its checksum.
   - Added `GROUPINGS.md`, `SENIOR-VIEW.md` (an orientation for Grade 3 and 4 engineers), `ASSERTION-SERIES.md` (numbered, checkable assertions in four series: integrity, provenance, structure, behavior) and `binaries-index.tsv`.
   - The assertion series states facts only and makes no tax, legal or value claim.
2. **Initial design proposal in SLeeLa (`sleela-source/`).**
   - Six strand classes, one for each of APML, Bloq, Bodi, Sprung, Runyn and Falthruu. Each records the strand's question, principle, owner claims, goals, procedures, signals, agency guards, implementation items and open items.
   - `DesignKnit` checks two properties: the hand-off loop closes, and folding the strand in the middle pairs 1 with 6, 2 with 5 and 3 with 4.
   - The six owner terms that still have no formula are listed and not invented.
3. **`modules/`: micro and macro messaging.** Modules can poll, query, interrupt and co-exchange with the strands through one bus. Every message is logged before delivery, and mailboxes are bounded. Interrupts, broadcasts and exchange steps need an actor and a reason. The co-exchange has five outcomes: offer, counter and acknowledge to commit, or decline, or expire.
4. **`circuit/`, `medium/`, `professional/`: the six strands as a circuit carrying files.**
   - Medium runs at 600 files per second in one lane. Professional runs at 6,000 files per second in 8 lanes, with stricter input contracts and duplicate detection.
   - One approval gate is shared across all lanes, and Runyn groups its findings into fewer change requests.
   - Each strand's contract states what it takes, why, and what must hold before and after it considers a file. File shape is measured by the "square": lines times the widest line.
   - Every run checks that each terminal accounts for its files and that the whole circuit balances.
   - Finding: the human approval rate limits the whole circuit. At 10x load, grouping findings is required, or 4,836 files are refused in five seconds.
5. **`conduct/`: conduct as a lawful, testable layer.**
   - Three alphabets: moral-medium themes, prosaic causes and carry-cases, and medium wants (subactions).
   - A commutation matrix, confirmed on 288 of 288 pairs, and a solubility matrix with a rank test and a whole-number search.
   - Demise as a recorded property of a design trait.
   - Hireability as conduct evidence only, advisory, with a person deciding.
   - Profit priced in assumed units.
   - Ample and support parts read from each strand's signal, with a design feedback loop that weighs each change as a trade.
   - The Tranchion short study guide.
   - The word "guaranteed" is limited to laws that the model tests.
6. **Review.**
   - What is missing: time across runs, real input data, the six undefined owner terms, a strand for build, doc and config files, building outside the sandbox, and housekeeping (a duplicate `ENGINEERING-PRINCIPLES.md`, and a top-level README that does not mention `sleela-source/`).
   - Recommended next: a `time/` support series and a real-input reader. AI, if added, comes later and only proposes.
7. **`MORAL.md`.**
   - Records the owner statement and the drafter's readings of it. "Flat earth" is read as a level starting ground, not a claim about the planet.
   - Nine moral statements, with each primitive mapped to the code that enforces it.
   - How a review is conducted, and open questions for the owner.
8. **User Class II.** This folder. The nine statements are enforced for one user, with 19 checks. The class makes no medical claim, and the request to cure cancer was declined as something software cannot do.

### State at the end of the session

| Program | Checks |
|---|---|
| knit | all passed |
| modules | 41 passed |
| medium | 7 passed |
| professional | 11 passed |
| conduct | 29 passed |
| user | 19 passed |

### Open items carried forward

- The owner's definitions for Voltage on Time, the Attention Creation Principle, Economic and mode, Two, Public speaker is born, and the creative time-force equation.
- Whether "flat earth" in `MORAL.md` is read as intended.
- Named reviewers and how often reviews are held.
- Whether a closed user's pseudonymous record may be erased on request.
- Whether to build the `time/` series and the real-input reader next.

### Toolchain notes learned in this session

- **Execution gate.** `sleela` 1.3.2-pro will not run until its SHA-256 gate passes. That needs `SLEELA_HOME` pointing to a SLeeLa checkout and a manifest that matches it.
- **Compile together.** Programs that span files must be compiled together with `sleela compile a.sleela b.sleela -o out.sleela`.
- **`#sleela 3.1`** is needed for array initializers.
- **Reserved names.** `role` is a built-in, and a variable named `next` misbehaves at runtime.
- **Chained calls.** A field reached through a method's return value must first be bound to a local variable.
- **Comments.** A `<` comment cannot span lines. Use `<<` and `>>`.
