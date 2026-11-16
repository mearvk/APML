# Engineering and Principle: The Six-Strand Discipline of APML

Status: first full draft for the owner's review.

How this draft is built. Every statement about APML, Bloq, Bodi, Sprung,
Runyn and Falthruu comes from the owner's own site text or from the
owner's descriptions given in chat. Where the owner has not defined a term
in a way an engineer can use, this document says so and does not invent a
formula. Statements marked "Proposal" are the drafter's own suggestions
and are open to rejection. References to the wider field (for example
Brooks, Parnas, Lehman) are to well known published work and are named so
that a reader can look them up.

The detailed page-by-page study is in `analysis/APML-ANALYSIS.md`. The
short doctrine is in `docs/DOCTRINE.md`. This document is the long form.

---

## Part I. Purpose, audience and how to read

### 1. What this document is for

This document sets out one overall condition for how software is
codified, and the engineering principles that follow from it, from the
architect's drawing board to the developer's front desk and the manager's
schedule. It treats the six APML technologies as six strands of one
discipline, numbered 1 to 6, so that a team can say where in the strand a
problem sits and what to do about it.

The word "codification" is used in its plain sense: turning what people
mean into a written form that can be checked, shared, built from and kept.
The question the whole document asks is simple. When a team has decided
what a system is, where is that decision written, and what keeps it true
as the work goes on?

### 2. Who it is written for

There are three readers in mind.

The first is the working developer, who needs to know what to do on a
Tuesday. The second is the manager or architect, who needs to know what to
ask for, what to measure and where the risk sits. The third is the
careful thinker from outside the trade, such as a student of theology,
philosophy or law, who wants to take the principles and restate them in
the terms of their own field. For that third reader each principle below
is stated in a short sentence first, then explained at length, and then
tied to the principle that comes next. The aim is that a principle can be
lifted out, examined, and translated without losing the chain of reasoning
that holds it in place.

### 3. How each strand is presented

Each of the six strands is treated in the same order so that they can be
compared:

1. The question the strand answers.
2. The principle, in one sentence.
3. The reasoning behind it.
4. What it means for the developer.
5. What it means for management.
6. The failure it is meant to prevent.
7. The hand-off to the next strand.
8. What is still open.

---

## Part II. The overall condition

### 4. The condition in which most software is made

The owner's site describes the usual way software is made. A requirements
document arrives. A design is opened against it. The team patches together
the best version it can before the deadline. The site calls this fine for
some purposes and lackluster for others.

That account matches long-standing observation in the field. Fred Brooks,
in "No Silver Bullet" (1986), separated the essential difficulty of
software, which is deciding what the thing is, from the accidental
difficulty, which is the labor of writing it down in a particular
language. Most tools attack the accidental part. The condition APML
addresses is that the essential decisions are made, but they are kept in
places that do not stay connected to the code: in meeting notes, in
diagrams that nobody updates, in the memory of a senior person. Over time
the code and the decisions drift apart. M. M. Lehman's laws of software
evolution describe the same pattern: a system in use must keep changing,
and as it changes its structure tends to decay unless work is done to
stop that.

### 5. The overall condition, stated as a principle

**Principle of Single Description.** For any system, there should be one
written description of what it is, precise enough to build from, readable
by both the people who decide and the people who build, and kept true for
the life of the system.

Everything else in this document follows from that sentence. Strand 1
supplies the language for the description. Strand 2 builds from it.
Strand 3 lets the running system keep its parts addressable and
changeable. Strand 4 records what happens in time. Strand 5 folds the
real code back into the description. Strand 6 carries a late change
through all of it.

The reason for stating it as a principle and not as a tool is that the
tools may change but the condition does not. A team that holds the
principle can judge any tool, including these six, by one test: does it
keep the single description true?

---

## Part III. The six strands

The strands are numbered in the order on the site and in the owner's own
numbering of the pages. They read as a progression: from deciding what a
system is, to making it, to running it, to recording it, to checking it,
to changing it.

### Strand 1. APML: the description

**Question.** What is this system, written so that anyone can check it?

**Principle.** Structure comes first, and function follows from it.

**Reasoning.** APML, Application Markup Language, is a subset of XML. The
owner's site gives this as the reason it can be trusted: it inherits what
XML already provides. In practice that means mature parsers, schemas that
can reject a malformed description, and ordinary tools for comparing two
versions. Putting structure first reverses the usual order, where code is
written and structure is inferred later. David Parnas's work on modular
design (1972) argued that a system should be divided according to the
decisions most likely to change, and that each module should hide such a
decision. A written structure is the natural place to record those
divisions before the code exists.

**For the developer.** Write the structure down before writing the
behavior. Treat the description as a file in version control, reviewed
like code. When the description and the code disagree, that is a defect
and one of them is wrong.

**For management.** Ask to see the description, not a slide about it. A
description that can be validated gives a plain measure of progress: it
either validates or it does not. It also gives managers, architects and
developers a common document to discuss, which the site names as a goal.

**Failure prevented.** Structure that exists only in people's heads.

**Hand-off.** A description that nobody builds from is only paperwork.
Strand 2 turns it into source code.

**Open.** The site does not yet show what APML looks like. A small sample
file is the single most useful addition.

### Strand 2. Bloq: the making

**Question.** How does the description become working source code without
hand-building the structure each time?

**Principle.** What can be generated from the description should be
generated, and people should spend their effort on what cannot.

**Reasoning.** Bloq reads APML and produces Java source. The owner
describes this as an implementation layer above hand-written production
code, covering everything from Java Swing programs to large systems. The
benefit named is that architects and developers share one technology and
the program structure no longer has to be set up by hand. This is the
established idea of model-driven engineering: a model of the system is the
main artifact and code is derived from it.

**For the developer.** Do not edit generated structure by hand. Put
hand-written logic where the generator leaves room for it. If the
generated structure is wrong, change the description and generate again.

**For management.** Generation turns structural work from a skilled,
variable task into a repeatable one. It also makes estimates steadier,
because structure is no longer where schedules slip.

**Failure prevented.** Inconsistent hand-built structure, and the slow
leak of time into setup work.

**Hand-off.** A generated program must be run, reached and maintained.
Strand 3 deals with the running system.

**Open.** The word "correct" is used on the site for the generated code.
The guarantee needs to be stated. For example: the code compiles, it
matches the structure in the description, and regeneration does not
destroy hand-written parts.

### Strand 3. Bodi: the reach

**Question.** How does every part of a running system stay addressable and
changeable, even years later?

**Principle.** Every part has a name that can be reached, and a name can
be pointed somewhere new.

**Reasoning.** On the owner's site, each server, system, object, listener
and function has its own URL, reachable over TCP/IP. Software patches,
network access and function hooking can be done through XML documents.
The example given is a protocol stack of four functions. If a security
patch is needed years later, Bodi is told that a function now lives at a
new URL on a patch server. The same applies to the server, the system
object and the whole stack. This is late binding: deciding what a name
means as late as possible, so that a decision made in the past does not
lock the future. Parnas's idea of hiding changeable decisions behind a
stable interface is the same idea applied across a network.

**For the developer.** Give every part that may need to change a stable
address, and keep the address separate from the implementation behind it.
Never assume that the code at an address is the code that was first put
there.

**For management.** This is a maintenance and security argument. A patch
that is a change of mapping, and not a redeployment, is cheaper to apply
and quicker to roll back. It is also where the largest risk sits, and
that risk must be managed openly (see Principle 7 in Part IV).

**Failure prevented.** Software that cannot be extended or patched
because its inside is closed to the people who now depend on it.

**Hand-off.** A system of reachable parts produces a continuous stream of
messages between them. Strand 4 gives that stream a form.

**Open.** The trust model for remapping an address is not described on the
site, and it needs to be.

### Strand 4. Sprung: the message and the time

**Question.** What passes between the parts, and how is it named, ordered
and acted on?

**Principle.** A message carries its own name as time, and what happens
over time can create, change or end things.

**Reasoning.** This strand rests on the owner's own descriptions, which
are given here in plain restatement and are not yet formal.

- Sprung is an envoy to messaging. Messages are classified as "voltage on time", and each carries its own name as time.
- Voltage on Time produces an economic force over a distribution of causes, with gifters counted as gains.
- As time cycles, patterns emerge. The Attention Creation Principle asks whether a group can be trusted as a single sane individual with respect to taxes, taxation and consumption.
- Economic and mode: a person consumes "two", meaning twice the gauge of a standard reticle of a standard product, usually the gauge for a citizen or a mechanic. Economy travels and mode heals.
- Sprung is to be a special scripting language with ingress and egress for Java and for SLeeLa, as a sub-mechanism of the framework.
- Sprung can call Create, Alter or Destroy on time as a mechanism, and on the symptom of a function. The owner calls the whole a creative time-force equation, which equals county government as proceeds, and pays specially for announcements and for certain favors.

The part an engineer can build now is the following. Messages are
identified by time, so the record of messages is an ordered log. Three
verbs, Create, Alter and Destroy, act on things in response to that log. A
scripting language lets Java and SLeeLa code call these verbs and be
called by them. An ordered, time-named log from which state is derived is
a known and well-tested pattern, usually called event sourcing, and it
gives replay, audit and the ability to find recurring patterns by
comparing windows of time.

The remaining terms, namely the economic force, the group treated as an
individual, and the gauge, are the owner's design vocabulary. They are
recorded faithfully here and should be read as the intended meaning of
the system's scoring and accounting layer. They need formulas and a worked
numeric example before an implementer can use them.

**For the developer.** Treat time as part of every message's identity.
Keep the log append-only. Make Create, Alter and Destroy explicit,
recorded actions and not silent edits.

**For management.** A time-named log is an audit trail. It answers who
did what and when, which is what review, compliance and incident analysis
all need.

**Failure prevented.** Behavior that nobody can reconstruct after the fact.

**Hand-off.** A log of what happened lets two later checks work: the
check of whether the code still matches its description (Strand 5), and
the carrying of a change through the system (Strand 6).

**Open.** The message format, how a script binds to a Bodi address, the
formulas for the scoring terms, what "two" counts in a base unit, and what
Destroy guarantees. The site's Sprung page also still carries the
Falthruu paragraph and must be rewritten.

### Strand 5. Runyn: the refolding

**Question.** After the work has grown, is the system still what the
description says it is?

**Principle.** The description must be recoverable from the code, so that
the two can always be compared.

**Reasoning.** The owner names software sprawl as the problem that
remains even with Bloq, APML and Bodi. A team is sure at the start that
the structure is right. Later a redesign becomes a major issue and costs
time and money. Runyn refolds the software during development into clean
APML and Java source, giving a clear indication of whether the work is
heading the right way and leaving it ready for architectural review. It
runs in the opposite direction from Bloq. Together they make a round
trip: description to code, and code back to description. A system that
can make the round trip can be checked at any time for drift. Lehman's
laws predict that structure decays as a system changes, and this is the
tool for measuring it before it becomes expensive.

**For the developer.** Run the refold often, as you would run tests. Read
the differences it reports as early warnings.

**For management.** This is an early-warning measure for redesign cost.
The owner's own words give the case: the embarrassment of redesign costs,
in time and money. A regular refold report turns an invisible risk into a
visible one.

**Failure prevented.** Late, expensive discovery that the structure was
wrong.

**Hand-off.** Finding drift is not enough. When it is found, the fix must
be carried through the system. Strand 6 does that.

**Open.** What "clean", "correct" and "right direction" mean in numbers:
for example, the refolded description regenerates code that behaves the
same, and the report lists new dependencies, cycles and oversized parts.

### Strand 6. Falthruu: the carrying through

**Question.** When a change turns out to be needed, how does it reach
everywhere it should have been?

**Principle.** A late change should be applied to the whole system and its
history as one act, and that act must be safe to attempt.

**Reasoning.** On the owner's site, Falthruu connects what should have
been across the system without going back to design or implementation. One
change requirement can be propagated across the system and into prior
commits, programmatically. A developer works at their own pace and style
and, on finding a redesign that should have been caught earlier, runs
Falthruu. The site's phrase is that it is safe to fall through, with no
more traps in software. This is the sixth strand because it depends on the
five before it: a single description to change, generation to rebuild
from, addressable parts to retarget, a log to say what changed and when,
and a refold to check the result.

**For the developer.** Preview the change, run the checks, and keep a way
to undo it. Prefer new commits that apply the change to older lines of
work over rewriting history that others have already pulled.

**For management.** This is the answer to the cost of changing one's mind.
It makes late correction an ordinary operation and not a crisis.

**Failure prevented.** The trap in which a known-needed change is too
costly to make, so the flaw stays.

**Hand-off.** The end of the strand is also its start. A change that has
been carried through is recorded in the description, and Strand 1 is true
again.

**Open.** Whether history is rewritten or extended, and what "safe"
guarantees in practice.

---

## Part IV. The strand as a whole

### 6. Why a strand, and why it closes

A strand is a sequence in which each element depends on the one before and
makes the next possible. The six technologies have that shape. A
description (1) is built (2), reached (3), recorded (4), checked (5) and
changed (6), and the change is written into the description (1) again.
The sequence is a loop, and a loop is the right picture for a discipline,
because discipline is something repeated.

### 7. Proposal: the pairing of the strand

This is the drafter's proposal and the owner may reject it. If the
strand is folded in the middle, so that 1 faces 6, 2 faces 5 and 3 faces
4, each pair becomes a matched pair of opposite and complementary acts.

| Pair | Forward act | Return act | What the pair guarantees |
|------|-------------|------------|--------------------------|
| 1 and 6, APML and Falthruu | Write the description | Carry a change through the description and the system | The description stays true after change |
| 2 and 5, Bloq and Runyn | Generate code from the description | Refold code back into the description | The code and the description can always be compared |
| 3 and 4, Bodi and Sprung | Give each part an address | Record each message by time | The system can be reached and its history reconstructed |

The pairing is useful because each pair can be tested as a unit. For the
middle pair the test is a round trip: generate, refold, and compare. For
the inner pair the test is that every address reached appears in the log.
For the outer pair the test is that after a change the description still
validates.

### 8. Cross-cutting engineering principles

These principles apply to every strand. They are stated so that each can
be taken out and examined on its own.

1. **Single Description.** One written source of truth for what the system is.
2. **Structure Before Function.** Decide the shape before the behavior, and record it.
3. **Generate the Repeatable.** Human effort goes to what cannot be generated.
4. **Stable Names.** Every part that may change has a name that outlasts its implementation.
5. **Time Is Identity.** What happened is recorded in order and never silently edited.
6. **Round Trip.** What is made from the description can be returned to it and compared.
7. **Guarded Reach.** The power to repoint a name is the power to change running code, and it must be signed, logged and reviewable.
8. **Safe Change.** Any change that spans the system can be previewed, tested and undone.
9. **Review by Evidence.** Architectural review looks at the description and the refold report, not at opinion.
10. **Honest Claims.** A claim of correctness, safety or speed states what is guaranteed and how it is checked.

The seventh principle is stated first among the risks because Strand 3
makes it essential. If an address can be repointed to a patch server, then
whoever controls the mapping controls what runs. The method that makes
patching cheap is the same method that makes an attack cheap, unless
mappings are authenticated and audited.

---

## Part V. From the front desk to management

### 9. What each role does with the strand

| Role | Daily concern | Strand most used | Evidence they should produce or read |
|------|---------------|------------------|-------------------------------------|
| Developer | Writing and changing code | 2, 5, 6 | Generated build, refold report, change preview |
| Architect | Keeping structure sound | 1, 5 | Validated description, drift report |
| Operator | Running and patching | 3, 4 | Address map, time-named log |
| Manager | Schedule and cost | 5, 6 | Redesign-risk trend, change cost |
| Reviewer or auditor | Judging soundness | 1, 4, 5 | Description, log, refold report |

### 10. The front-desk routine (Proposal)

A practical daily and weekly rhythm that follows the strand:

1. At the start of work, validate the description (1).
2. Generate and build (2).
3. After meaningful changes, run the refold and read the drift report (5).
4. When a needed change is found, preview, apply and verify it (6).
5. Before release, check that addresses map where intended and that the log is complete (3, 4).
6. At review, present the description, the refold report and the log.

### 11. What management should ask

Five plain questions follow from the principles. Where is the description
and does it validate? What did the last refold report say about drift?
What is the cost and risk of the next change we may need? Who may repoint
an address and how is that recorded? Which claims on our site and in our
reports state a guarantee, and which only state an aim?

---

## Part VI. Notes for the reader who will translate

### 12. How to carry these principles into another field

Each principle in Part IV has a short statement, a reason and a failure
it prevents. A reader from another field may take any principle and ask
three questions of it in their own terms. What in my field plays the part
of the single description? What plays the part of the log? What is my
field's version of the failure the principle prevents? The answers will
differ by discipline, and the differences are the useful result.

The strand's order may also be read as a cycle of any disciplined work:
state what is, make it, make it reachable, keep a record, check against
the statement, change it, and restate. The drafter makes no claim that
this is the only such cycle. It is the cycle these six technologies
express.

### 13. Terms still owed an exact definition

These are the owner's terms, recorded faithfully, whose exact working
definition is still needed before they can be implemented or taught
without ambiguity.

| Term | Owner's description so far | What is needed |
|------|----------------------------|----------------|
| Voltage on Time | Economic force over a distribution of causes plus gifters as gains | A formula, units, one numeric example |
| Attention Creation Principle | Whether a group can be trusted as a sane individual over taxes, taxation and consumption | The rule for treating a group as one actor |
| Economic and mode | Economy travels, mode heals; a person consumes two for benefit and not cause | What is consumed and what heals |
| Two | Twice the gauge of a standard reticle of a standard product, per citizen or mechanic | The base gauge as a number |
| Public speaker is born | An economic circle settles at a home near a man and is known to its kind and listeners | The system event this marks |
| Creative time-force equation | County government as proceeds, with special payment for announcements and certain favors | The equation written out with its terms |

---

## Part VII. What remains to be done

1. Add a small APML sample and the Java it generates (Strands 1 and 2).
2. State what "correct" means for generated code and how regeneration treats hand edits (Strand 2).
3. Write the trust model for address remapping (Strand 3).
4. Rewrite the Sprung page on the site with the owner's description, and supply formulas for the scoring terms (Strand 4).
5. Define the numbers behind "clean" and "right direction" for the refold report (Strand 5).
6. State whether Falthruu rewrites or extends history, and what it guarantees (Strand 6).
7. Replace the repeated Bodi text on every page with content specific to the page.
8. Decide whether the pairing proposed in section 7 is adopted.

When these are done, this document can move from a draft to the standing
statement of discipline for the six technologies, and the site can be
loaded from it.
