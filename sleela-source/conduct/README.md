# conduct: conduct as a lawful, testable design layer

## The claim, and how far it goes

The request was to treat conduct as its own physics: a solution matrix in which acts produce permutable designs and results. This folder does that inside a model, and nothing more. The rules are written as laws. Each law is tested against what the code actually does, and the demo fails if a prediction is wrong.

That is the only sense in which conduct is "guaranteed" here. A law holds for this model and these assumptions. It is not a law of nature or of people.

## Alphabets

Every act is written as a word over three finite alphabets. Code 0 is never a symbol, so an unknown code is always caught.

| Alphabet | Symbols | What it holds |
|----------|--------:|---------------|
| Moral-medium (`MoralAlphabet`) | 8 | Guaranteed themes: Attribution, Consent, Recording, Reasoned refusal, Human approval, Honest claims, Conservation, Recorded demise. A theme is admitted only if `guaranteedBy` names an existing check that enforces it. A theme with no check is an aim, not a guarantee. |
| Prosaic (`ProsaicAlphabet`) | 20 | Leading causes: the 10 circuit refusal reasons and the 4 Runyn findings. Carry-cases: header, metadata, generated mark, actor, reason and correlation id, which travel with an act or file. |
| Medium wants (`WantsAlphabet`) | 12 | Subactions, from trivia to demise. Each has an effect class: none, set, add or retire. |

## Matrices

**Commutation matrix** (`CommutationMatrix`). It predicts whether two acts give the same result in either order, from their effect classes alone:
- Acts on different targets always commute.
- On the same target, only these pairs commute: none with anything, add with add, and retire with retire.

`PermutationProbe` runs all 288 pairs of wants, on the same target and on different targets, in both orders. The prediction was right in all 288 cases. The probe then runs four acts in all 24 orders:
- Two adds, an alter on another trait, and trivia gave 1 result, so that design is permutable.
- Add, alter and demise on one trait, plus a query, gave 5 different results, so that design is not.

**Solubility matrix** (`SolubilityMatrix`). Columns are acts: query, notice, alter, ack, demise. Rows are what acts produce:

```
records    1  1  1  1  1    every act writes one entry
approvals  0  0  1  1  1    acts a person must approve
value      1  0  5  8 -2    assumed units; demise costs
accounted  0  0  1  1  1    acts carrying actor and reason
```

A target is soluble when whole, non-negative counts of each act produce it exactly. The test has two steps. First, a rank test (Gaussian elimination) decides whether any real solution exists. Then a bounded search looks for a whole, non-negative one.

Results:
- **records 10, approvals 4, value 24, accounted 4:** soluble, with query 1, notice 5, alter 3, ack 1.
- **approvals 4 with accounted 3:** insoluble. The rank of A is 3 because those two rows are equal, so an approved act is always an accounted act.
- **4 approvals from 3 records:** insoluble. No act has an effect without a record.

## Demise as a property of a trait

A `DesignTrait` is a reusable capability of a design artifact. Its demise is a property of the trait, made of four values: the tick it ended, the actor, the reason and the successor.
- Demise without a reason is refused.
- After demise, every write is refused, the last value is kept and the whole record stays readable.
- Refused attempts are written to `ConductLedger` too, because an attempt is conduct. The ledger has no edit or remove method.

Demise applies to design traits only, never to people.

## Hireability

`HireabilityView` reports what the conduct record shows about a contributor, for a person who is making a hiring or promotion decision.
- It reads only the ledger. Any field outside its allowlist returns -1, and there is no input for age, sex, origin, health, belief or any other personal trait.
- It gives evidence counts and an advisory grade (Grade 3 or Grade 4 evidence). It does not rank people against each other and does not decide.

In the demo:
- **A. Rivera** shows Grade 4 evidence: 4 accounted acts, one consented exchange, one recorded demise and no recent refusals.
- **B. Chen** is not yet at Grade 3, because of recent refusals for a missing reason.

## Profit

`ProfitModel` prices a circuit run. The prices are assumed units, printed with every result:

| Item | Price |
|------|------:|
| Logged event | +1 |
| Generated file | +2 |
| Change carried through | +5 |
| Refused file | -1 |
| Approval used | -20 |

Medium earns 1089 units per second and professional earns 4800.

## Ample and support parts

A design has **ample parts**, which carry the main current, and **support parts**, which carry little current but hold up a path. `AmplitudeAnalyzer` reads each part's current as a signal, sampled once per tick. It recognises a part's role and trait from that signal, the way character recognition reads a letter from its strokes.

| Role | Rule |
|------|------|
| Ample | Utilization of at least 50% |
| Support (stanchion) | Low current, but an ample part is reachable from it along the circuit edges, so removing it cuts that path |
| Idle | Low current and nothing ample depends on it; a candidate for demise |

| Trait | Rule |
|-------|------|
| Saturated | At capacity in at least 20% of ticks |
| Bursty | Peak at least 3 times the mean |
| Idle | Mean under 5% of capacity |
| Steady | None of the above |

Medium result:

| Part | Utilization | At capacity | Role, trait |
|------|------------:|------------:|-------------|
| APML | 40% | 24% | support, saturated |
| Bloq | 40% | 24% | support, saturated |
| Bodi | 69% | 54% | ample, saturated |
| Sprung | 57% | 0% | ample, steady |
| Runyn | 58% | 1% | ample, steady |
| Falthruu | 10% | 4% | support, bursty |

**The loop.** Designs lead to ample parts, and ample parts lead to the next design. `DesignFeedback` proposes one more unit of capacity for each ample, saturated part, and never enlarges a support part. Each proposal is a trade: its gain is the change in profit, less an assumed 400 units per added unit of capacity. It becomes a real action only if the gain is positive.

In the run, Bodi went from 4 to 5. Its average wait fell from 27 ms to 16 ms, but profit did not change: 5448 before and after. Every generated file was already being processed, and the bursts come from Bloq's fan-out. The gain was -400, so the trade was refused.

**The lesson:** when an ample part is saturated, the limit may sit in the support parts that feed it. APML and Bloq are also saturated 24% of the time.

## Tranchion

`Tranchion` writes a short study guide from the run. It has one line per part: its role, its trait, the trait restated as a goal, and the wisdom that applies. It ends with the trade, its gain and the verdict. It is meant to be read in a minute.

## Build

```
sleela compile circuit/*.sleela medium/MediumProfile.sleela professional/ProfessionalProfile.sleela professional/ProCircuit.sleela conduct/*.sleela -o build/conduct.sleela
sleela run build/conduct.sleela
```

Result on 2026-11-16: `conduct: all 29 checks passed`.

## SLeeLa gotchas found here

- `role(...)` is a built-in conducted method, so a method named `role` fails with "role name must be a string literal".
- A variable or parameter named `next` misbehaves at runtime: string values came out wrong, and field access failed. Use another name.
- A field reached through a method's return value, as in `a.part(n).name`, fails at runtime. Bind the result to a local variable first.
