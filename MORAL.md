# MORAL

Status: draft for the owner's review, 2026-11-16. Nothing in this document is final until the owner accepts it.

## 1. The owner's statement

In the owner's words, paraphrased:
- Good people continue on Earth.
- Conduct leads to flat earths, which are the primitive and the start.
- This is good for the ages to come.
- Conduct is subject to review.

## 2. How this document reads it

These readings are the drafter's. The owner can correct any of them.

- **"Good people continue on Earth."** The software exists to serve people who carry on after it, so it must leave them able to understand it, change it and refuse it.
- **"Flat earth."** This is read as a figure of speech: a level starting ground, the plain base case from which everything else is built. In geometry, flat space is the simplest space and comes before any curvature; here, a flat earth is the first, level plane of a design. It is not a claim about the shape of the planet.
- **Level for everyone.** On a flat start no one begins higher than anyone else. Every party meets the same rules: an actor and a reason for every act that changes what others do, and the same reason codes for every refusal.
- **"Primitive."** In the sense of the primitives of a system: the few things everything else is built from. Section 4 lists them.
- **"Good to such ages."** Each later version of the software should keep the level start rather than tilt it toward whoever controls the system.
- **"Conduct unto such reviews."** All conduct, by people and by programs, is open to review. A review is itself conduct and follows the same rules.

## 3. Moral statements

Each statement is short so it can be lifted out and examined on its own.

1. **People decide.** Software may propose. An accountable person approves anything that changes the whole system.
2. **Everyone starts level.** The same rules apply to every party. No role is exempt from giving an actor and a reason.
3. **Nothing is hidden.** Every act is recorded before it takes effect, and the record is never edited.
4. **Every refusal is explained.** A thing that is turned away is told why.
5. **Consent is mutual.** An agreement holds only when both parties have acted.
6. **Claims are honest.** A guarantee states what is checked and how. An aim is called an aim.
7. **Endings keep their record.** When a part of a design is retired, who retired it, why and what replaced it stay readable.
8. **People are not scored by who they are.** Conduct records can be read as evidence of what someone did. They are never used to rank or exclude a person by age, sex, origin, health, belief or any other personal trait.
9. **The start stays level over time.** Each new version is reviewed against these statements before it replaces the old one.

## 4. The primitives (the flat earth of APML)

The design rests on these primitives. Each one is a rule that code already enforces.

| Primitive | Where it is enforced | Moral statements |
|---|---|---|
| An act names an actor and a reason | `sleela-source/modules/ApmlBus.sleela`; `conduct/ConductDemo` | 1, 2 |
| Write the record first, never edit it | `modules/ApmlMessageLog.sleela`, `conduct/ConductLedger.sleela` | 3 |
| Every refusal has a reason code | `circuit/ReasonCatalog.sleela`, `circuit/CircuitReport.sleela` | 4 |
| Agreement needs both parties | `modules/ApmlExchange.sleela` | 5 |
| Every design states what is still open | `DesignKnit.sleela` | 6 |
| Nothing enters or leaves uncounted | the conservation checks in `medium/` and `professional/` | 3, 6 |
| Human approvals have a fixed budget | `circuit/ApprovalGate.sleela` | 1 |
| Demise keeps its record | `conduct/DesignTrait.sleela` | 7 |
| Evidence fields are allowlisted | `conduct/HireabilityView.sleela` | 8 |

The moral-medium alphabet in `conduct/MoralAlphabet.sleela` holds the same themes. A theme belongs there only if a check guarantees it.

## 5. Conduct unto review

How a review of this software is conducted:

1. **What is reviewed.** The design proposal, the run results, the Tranchion study guide and the conduct ledger. Opinion about a person is not reviewed.
2. **Who reviews.** A named person, who records their name and reason like any other actor.
3. **What the reviewer checks.** That every program passes, that each moral statement in section 3 still has its primitive in section 4, and that nothing new tilts the start toward one party.
4. **What a review produces.** One of three findings: accepted; accepted with open items listed; or refused with a reason code. The finding is recorded and never edited.
5. **Reviewing the review.** A later reviewer may challenge an earlier finding by recording a new one. The earlier finding stays in the record.

## 6. Open items for the owner

- Is the reading of "flat earth" as a level starting ground the intended one?
- Should "good people continue on Earth" become a design goal with a check, for example that the system can always be exported and run without its original authors?
- Who are the named reviewers, and how often is a review held?
- Should statement 9 apply to the website under `source/` as well as to `sleela-source/`?
