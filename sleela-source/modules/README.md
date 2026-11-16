# modules: messaging with the six strands

These classes let any module poll, query, interrupt or exchange with the six strands, and with other modules. Micro and macro messaging share one bus and one log.

## Parties

| Id | Party |
|---:|-------|
| 1 to 6 | APML, Bloq, Bodi, Sprung, Runyn, Falthruu |
| 7, 8 | module-A, module-B (example modules) |

## Message kinds

Kinds are plain ints, catalogued in `ApmlMessageKind`.

| Code | Kind | Scope | Needs actor and reason |
|-----:|------|-------|------------------------|
| 1 | POLL | micro | no; poll reads a mailbox and sends nothing |
| 2 | QUERY | micro | no |
| 3 | INTERRUPT | micro | yes |
| 4 | OFFER | macro (exchange) | yes |
| 5 | COUNTER | macro (exchange) | yes |
| 6 | ACK | macro (exchange) | yes |
| 7 | DECLINE | macro (exchange) | yes |
| 8 | REPLY | micro | no |
| 9 | NOTICE | micro, or macro when broadcast | broadcast only |

**Micro** messaging is one envelope from one sender to one receiver. A query and its reply share a correlation id.

**Macro** messaging is one correlation id that spans many envelopes. It takes two forms: a broadcast to all six strands, or a multi-step co-exchange.

## Classes

| File | Role |
|------|------|
| `ApmlMessageKind.sleela` | Kind, scope and party names; which kinds need an accountable actor |
| `ApmlMessageLog.sleela` | Append-only log with sequence numbers and Lamport logical time. It has no method that edits or removes an entry. A full log refuses new entries. |
| `ApmlMailbox.sleela` | Bounded mailbox per party (16 per lane). An interrupt lane is drained first, then a normal lane in arrival order. A full lane refuses and counts the refusal. |
| `ApmlBus.sleela` | One delivery path: check the guard, write the log, then deliver. Provides `notice`, `query`, `reply`, `interrupt`, `poll`, `receive`, `broadcast` and `step`. |
| `ApmlStrandDesk.sleela` | Answers queries for each strand from that strand's design proposal. Topics are `name`, `question`, `principle`, `handoff`, `pair`, `guards` and `open`. Unknown topics are answered `unknown topic`. The desk counts interrupts but never acts on one by itself. |
| `ApmlExchange.sleela` | Co-exchange state machine. The states are idle, offered, countered, committed, declined and expired. |
| `ModulesDemo.sleela` | Runs every pattern and checks it (41 checks) |

## Co-exchange

```
idle --offer(initiator)--> offered --counter(responder)--> countered --ack(initiator)--> committed
offered or countered --decline(either party)--> declined
offered or countered, no progress for 5 ticks --> expired
```

An exchange commits only after both parties have taken a logged, attributed step. A step from a non-party is refused. A committed, declined or expired exchange cannot move again.

## Design choices

| Choice | Reason | Alternative not taken |
|--------|--------|-----------------------|
| Log before deliver | Nothing happens that cannot be reconstructed, which is Sprung's Time Is Identity | Deliver first and log asynchronously: faster, but a crash loses history |
| Lamport time, not wall clock | Ordering is exact and testable without a clock | Wall clock: readable, but ties and skew. The Sprung proposal leaves this open |
| Bounded mailboxes with refusal | A slow party pushes back and nothing is dropped silently | Unbounded queues: simpler, but memory grows without limit |
| Interrupt lane | Urgent work such as a security patch is seen first | Priority numbers on a single queue: more general, but harder to reason about |
| Mailboxes hold sequence numbers | One copy of each message, in the log | Copy envelopes into mailboxes |
| Polling and pull delivery | Deterministic and single-threaded, suitable for a proposal | Callbacks or threads, for a later implementation |

## Limits of this proposal

- It runs in a single process. There is no network transport, no persistence and no threads.
- Log capacity is 256 entries and there is no rotation.
- Actor names are not authenticated. A real Bodi would require signed identities (Principle of Guarded Reach).
- The desk answers from fixed design text. It does not run any strand.
