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
