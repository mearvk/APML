# circuit: the six-terminal circuit (shared classes)

The medium and professional circuits share these classes. They differ only in their profile and in how many lanes they run. Each strand is treated as a terminal in a circuit that carries files.

## Circuit picture

| Circuit term | Meaning here |
|--------------|--------------|
| Charge | One file |
| Current | Files per second |
| Terminal | One strand: APML, Bloq, Bodi, Sprung, Runyn, Falthruu |
| Capacitor | The terminal's queue (`FileQueue`). It absorbs bursts. |
| Current limit | The terminal's capacity in files per 10 ms tick |
| Input terminal | `Router` |
| Ground | Every refused file. Each refusal carries a reason code. |
| Sink | Sprung's log |
| Gain | Bloq fan-out: one description in, three generated files out |
| Gate | `ApprovalGate`. People approve Falthruu changes at a fixed rate. |

Two laws are checked at the end of every run:
- **Kirchhoff's current law, per terminal:** offered = refused + taken + waiting.
- **Conservation, per lane:** sourced + produced = sunk + consumed + grounded + waiting.

## Flow

```
stream -> Router --apml-------> 1 APML -> 2 Bloq =x3=> 3 Bodi -> 4 Sprung -> log
                 --generated-------------------------> 3 Bodi
                 --java, test--> 5 Runyn -----------------------> 4 Sprung
                                   | soft limit broken
                                   v
                 --change------> 6 Falthruu --approved--> 1 APML
                 --build, doc, config, binary, duplicate--> ground (with reason)
```

**Backpressure:** a strand takes a file only when the next strand has room for everything it will emit. A derived file is therefore never lost to a full queue. Only the router entry can refuse with "buffer full", and every run checks this.

## Software as developers meet it

`FileAnatomy` produces an ordinary Java code base, deterministically from each file's id. "Integration by parts" is read here as integrating the normal kinds of files developers find or create: descriptions, hand-written source, tests, generated code, build files, documents, configuration, change requests and stray binaries. The stream also contains the usual defects: files that do not parse, missing headers, oversized or minified files, generated code without a generated mark, and copied files.

The mix per 1000 files is: APML 80, Java 380, test 140, generated 60, build 30, document 120, configuration 100, change request 20, binary 70. These are assumptions, not measurements. Change them in `FileAnatomy.streamKind`.

**The square of a file** is its bounding rectangle: lines times the width of its widest line, in characters. A file's anatomy is:
- that square;
- its byte count and how much of the square holds bytes (`fillPercent`);
- its import, type and member counts;
- whether it is well-formed;
- whether it has a header, metadata and a generated mark;
- its content key, used for duplicate detection.

## Input contracts: what each strand takes, why, and what comes before and after

Each strand has a `StageContract` subclass that states these in code.

| Strand | Takes | Why | Before consideration (else refused) | After consideration (guaranteed) |
|--------|-------|-----|-------------------------------------|----------------------------------|
| 1 APML | APML descriptions, from the stream or from Falthruu | Everything is built from the description | Well-formed, has a header, within hard limits; metadata if the profile requires it | A validated description goes to Bloq |
| 2 Bloq | Descriptions validated by APML | Generating from an invalid description spreads its errors | Kind is APML and it came through APML | The description is consumed; generated, marked files go to Bodi |
| 3 Bodi | Generated Java | An address promises that the code behind it is known | Well-formed, within hard limits; a generated mark if required | An address event goes to Sprung |
| 4 Sprung | Events from Bodi and Runyn | The log is the evidence for everything later | It came from Bodi or Runyn | Appended in order and never edited |
| 5 Runyn | Hand-written Java and tests | Drift happens in hand-written code | Well-formed, within hard limits; a header if required | A report goes to Sprung; a broken soft limit raises a change request |
| 6 Falthruu | Change requests | A system-wide change must be one approved act | Well-formed, and a human approval is available | The change is consumed; an updated description goes to APML |

**Hard limits** apply before consideration: a file that breaks one is refused.

**Soft limits** apply after consideration: the file is processed, and the limit it broke becomes a finding. A finding raises a change request, or is attached to an open one when the profile coalesces findings.

## Classes

- **Anatomy and reasons:** `FileAnatomy`, `ReasonCatalog`.
- **Plumbing:** `FileQueue`, `CircuitNode`, `Router`, `Circuit`, `CircuitReport`.
- **Policy:** `CircuitProfile`, `ApprovalGate`, `DedupeSet` (exact, so no false duplicates), `SerialSource`.
- **Contracts:** `StageContract` and its six subclasses, one per strand.

## Object design

- **Polymorphism for policy:** each strand's rules are a `StageContract` subclass. A `CircuitNode` holds one contract and calls `admit` and `warn` without knowing which strand it is.
- **Composition for scale:** the professional circuit is eight `Circuit` objects behind a shard router. No circuit logic is duplicated.
- **Plain data for grades:** a profile holds every number that separates medium from professional, so a new grade is a new profile class and nothing else.
- **Injected shared state:** the gate, the duplicate set and the serial source are passed in. One lane can own them, or many lanes can share them.

## Limits of the model

- It is a discrete-time simulation of 500 ticks of 10 ms each, so 5 simulated seconds. Real files are not read.
- Capacities and the file mix are assumptions.
- Build files, documents and configuration have no strand yet, so they are grounded as out of scope. This is an open item: Bloq is the likely owner of build files.
