# APML Site Analysis

Status: partial. Based on text supplied by the site owner: the APML and Bloq
material, the Bodi "Technological Overview", and three headed sections
("Design Goals", "ROI Considerations", "Progress Inventory"), the Sprung
page (page 4), the Runyn page (page 5) and the Falthruu page (page 6),
in the owner's numbering. All six pages have been supplied. The owner
has since supplied a description of Sprung in chat (section 3.4); it is
not yet on the site.

Two things below are my interpretation, not the owner's statements:
the cordial assigned to each page, and the reading of the pages as a bridge.
Both are flagged so the owner can correct them.

## 0. Key finding: three headed sections hold identical text

On the Bodi page (page 3), the overview (three paragraphs, ending "This is
Bodi. /hello") appears four times in a row: once as the page body and once
under each of the headings "Design Goals", "ROI Considerations" and
"Progress Inventory". The text is identical each time. Only the headings
differ. The text for pages 5 and 6 (the copies) has not been supplied. If
they carry the same headings, the headings are probably a template shared
across the technology pages, but that is unconfirmed.

Consequences:
- The headings read as the quality "cordials" the owner described: Design Goals (design intent), ROI Considerations (economic value) and Progress Inventory (state of the work). This is an inference from the headings alone.
- None of the three sections currently says anything specific to its heading. The body text is Bodi's general description, apparently reused as filler. The page ties no goals to design, no costs or returns to ROI, and no completed or open items to progress.
- This likely explains the duplicate pages (`copy-of-bodi-technology`, `copy-of-bodi-technology-1`): they were made by copying the Bodi page and changing the heading.

## 1. The overarching idea

APML says software should start from structure. The conventional route is a
requirements document, a rushed design, and a patched-together build before
the deadline. APML reverses that. The structure is written first, in a
subset of XML called Application Markup Language, and source code is
generated from it.

The brand's through-line is the consuming need of software development:
getting from an idea to working, correct, connected software without losing
the design on the way. Each technology on the site covers one stretch of
that path.

## 2. Reading order as a bridge (interpretation)

Left to right, the pages move from understanding to production to
operation:

| Order | Technology | Stage | Role in the bridge |
|-------|------------|-------|--------------------|
| 1 | APML | Cognitive | The language. A formal, shared description of what a system is. |
| 2 | Bloq | Cognitive to producial | The tool. Reads APML and emits Java source. |
| 3 | Bodi | Producial | The runtime. Gives every part of a running system an address and lets it be patched. |
| 4 | Sprung | Producial, messaging | Time-named messaging envoy and scripting language with ingress and egress for Java and SLeeLa. Page text on the site is still the Falthruu paragraph (see 3.4). |
| 5 | Runyn | Producial, review stage | Folds a sprawling code base back into clean APML and Java source for architectural review. Closes the loop to APML. |
| 6 | Falthruu | Producial, over time | Propagates a late design change across the system and prior commits. |

On "College and Books": APML reads as the book (the written, formal
description), and Bloq and Bodi as the college (the practice that turns that
description into working systems). This is my reading and the owner may
frame it differently.

## 3. Page analysis

### 3.1 APML

Cordial (my inference): Structure as Design.

Claims, in the owner's text:
- Software is usually written quickly against a requirements document, with only a rough sense of structure.
- APML is a subset of XML and inherits its benefits.
- Software systems can be compiled, extended, interacted with and published "in minutes".
- Structure guides function and compiles into source code.

Assessment:
- The strongest idea is that the design document and the program share one source of truth. This removes the usual drift between the two.
- Being a subset of XML is a practical choice. Parsers, validators, schemas, diff tools and editors already exist.
- The "in minutes" claim is untested on the page. A short worked example (a small APML file and the Java it produces) would support it better than any description.
- The page does not say what APML looks like. A reader cannot yet judge the language.

### 3.2 Bloq

Cordial (my inference): Correct Process as Design.

Claims:
- Bloq is the implementation layer above hand-written production source.
- It interprets APML and produces correct, forward-looking Java source for further work.
- It gives tight control of structure from design documents, so developers need not set up program structure by hand.
- Scope runs from Java Swing to large systems.
- Architects and developers share one technology, so managers, architects and coders can discuss the same document.
- Pipeline: APML, then Bloq, then Java source.

Assessment:
- The audience argument is the best part. A single XML artifact that managers can read and developers can build from is a real collaboration benefit.
- "Correct" and "formal" are strong words. The page should say what is guaranteed: for example, that generated code compiles, that it matches the APML structure, or that it is regenerated without losing hand edits. The last point is the usual weak spot of generators and deserves an explicit answer.
- Generated source that is "for further development" raises the round-trip question: what happens to hand edits when APML changes?
- The closing questions ("What is a system? What makes a great system?") state the real motivation well. They are the best candidate for the homepage lead.

### 3.3 Bodi

Cordial (my inference): Extensibility as Design.

Claims:
- Bodi Remote Server Technology changes how projects, applications and design are handled locally and remotely.
- Each server, system, object, listener and function has its own URL, reachable over TCP/IP.
- Bodi-compliant software can be patched, network-exposed and hooked through simple XML documents.
- External URLs can map to internal contexts and be expressed in the Sprung scripting language.
- Example: a remote server has a four-function protocol stack. Years later, a security patch is applied by telling Bodi that a function's new URL is on a patch server. The same applies to the server, system object and protocol stack.

Assessment:
- The core idea, addressable everything with late binding, is a clear answer to software that cannot be extended because its internals are closed.
- The patch-by-redirect example is attractive for operations. It is also the main risk. If a function URL can be repointed to a patch server, whoever controls that mapping controls the running code. The page should describe how mappings are authenticated, signed and audited. Without that, a reader with security experience will treat it as a supply-chain weakness.
- Network latency and availability become part of every call that crosses a URL. The page should say which calls are local and which are remote.
- The `/hello` ending looks like a sample URL, which is a good instinct. A fuller example would help.

### 3.4 Sprung (page 4)

Cordial (my inference): Messaging and Time as Design.

The site page for Sprung currently carries the Falthruu paragraph (word for
word the same as page 6, see 3.7), so it has no Sprung content. The Bodi
page calls Sprung a scripting language for mapping external URLs into Bodi
contexts. The owner has now described Sprung in chat. Paraphrased:

- Sprung is an envoy to messaging. It classifies messaging as "voltage on time", and each message carries its own name as time.
- It is also essence to the collateral of the system: the messages are what the rest of the system relies on.
- Sprung equals dimensional progress as industry to a particular time. As time cycles, patterns of activity emerge, and the "Sprung Attention Creation Principle" can then rest as a facilitator of economy and mode.
- Sprung is to be a special, live-branded scripting language that allows both ingress and egress of Java and/or SLeeLa, as a sub-mechanism of the framework itself.
- Sprung can call Create, Alter or Destroy on time as a mechanism, and on the symptom of a function. The owner describes this as analogous to a creator, "like a tax on David", and calls the whole thing a creative time-force equation.

Definitions supplied by the owner (paraphrased):
- Voltage on Time produces an economic force over a distribution of causes, plus gifters counted as gains.
- The Attention Creation Principle asks whether a group can be trusted as a sane individual with respect to taxes, taxation and consumption.
- "Economic and mode" means a person consumes 2 as a static membership, for benefit and not for cause. Economy travels, and mode heals.
- The result is described as the birth of a public speaker.
- "Two" is twice the gauge of a standard reticle of a standard product, usually the gauge for a citizen or a mechanic.
- "A public speaker is born" means that a circle of economic travel usually finds its home at or near a man. A cycle is known to its kind by circle travels of benevolent theory, and to listeners.
- The creative time-force equation equals county government as proceeds, but pays specially for announcements and for sexual-reproduction favors such as meetings with very special friends. The owner confirms that the word "sexual" is acceptable here.

My reading, for the owner to confirm or correct:
- Voltage on Time is a score for a message: its weight over time, spread across the sources that caused it, with voluntary contributions added as gains.
- The Attention Creation Principle is a rule for treating a group as one accountable actor when accounting for consumption and tax.
- Economy is what flows between parties, and mode is the state that restores them after the flow. This is the least certain reading.
- "Two" is now a unit: a doubled standard gauge, set per kind of user (citizen or mechanic). That is implementable as a per-role multiplier, if the base gauge is given a number.
- The public speaker is the point where an economic circle settles at a home and becomes audible to listeners. In system terms this may be where a message stream gets a stable address and subscribers. This is my guess.
- The equation's right-hand side reads as: proceeds equal to county government, with premium payments for announcements and for the listed favors. The terms and their units are still not given.

Assessment:
- The concrete, buildable core is the last point: a scripting language whose scripts can call into Java and SLeeLa and be called from them. This agrees with the Bodi page, where Sprung scripts express URL mappings.
- The second concrete idea is time-named messages: every message identified by its time. That implies an ordering and replay model. It also makes the pattern-over-cycles idea testable, since recurring patterns can be found by comparing messages across time windows.
- Create, Alter and Destroy are three clear verbs, the same lifecycle that databases use (create, alter, drop). They are the most concrete statement of what a Sprung script does. The page should say what each verb acts on (a message, a time window, a function, a Bodi URL) and what Destroy guarantees, since it is irreversible unless messages are kept in a log.
- Acting on "the symptom of a function" suggests Sprung reacts to observed behavior of a function, such as a failure or a delay, and then creates, alters or removes something in response. That would pair naturally with Falthruu. The owner should confirm.
- The "tax on David" analogy and the "creative time-force equation" are not explained, and I do not know what they refer to. If there is an equation, it should be written out, with its terms defined. Until then I have left both unreinterpreted.
- The terms now have owner definitions, but they are still abstract. For the public site, the sexual-reproduction wording in the equation is the owner's call; I have kept it as written. An implementer needs formulas or worked examples, for instance a numeric example of a message scoring its Voltage on Time. Each needs a one-sentence definition and an example. For instance: what is classified, what are the classes, and what does a classified message cause the system to do?
- Open design questions for the page: the message format; how a script is bound to a Bodi URL; whether time names are wall-clock, logical or both; and how Java and SLeeLa calls cross the boundary (types, errors, security).
- Placement in the chain: Sprung is the messaging layer under Bodi's addressable parts, and Falthruu and Runyn can use the time-named message log as their evidence of what changed and when.

### 3.5 Runyn

Cordial (my inference): Structural Integrity as Design.

Claims, in the owner's text:
- Even with Bloq, APML and Bodi, software sprawl can occur. Structure that looked right early can later force an unexpected and costly redesign.
- Runyn reduces that risk by "refolding" the software during the development cycle into clean, correct APML and Java source.
- The result shows whether the software is heading in the right direction, and is ready for architectural review.

Assessment:
- Runyn is the reverse direction of Bloq. Bloq goes APML to Java. Runyn goes Java (and sprawl) back to APML. Together they form a round trip, which answers the question raised in 3.2 about what happens to hand edits: they can be folded back into the description.
- That makes Runyn the piece that keeps APML true over the life of a project, and the most valuable part of the chain if it works.
- "Clean", "correct" and "right direction" need definitions. Useful ones: the refolded APML regenerates source that behaves the same; the report lists structural drift against the original APML (new dependencies, cycles, oversized classes); a pass or fail result for architectural review.
- The cost argument ("embarrassment of redesign costs, in time and money") is the best ROI sentence on the site. It belongs under "ROI Considerations", where the repeated Bodi text now sits.
- Below the Runyn paragraph the page repeats the Bodi text again, as on the other pages.

### 3.6 Bodi copies: Design Goals, ROI Considerations, Progress Inventory

Text supplied. Each section repeats the Bodi overview in 3.3 exactly (see
section 0). Assessment per heading, against what each should contain:

| Heading | What a reader expects | What the page gives | Suggested content |
|---------|----------------------|---------------------|-------------------|
| Design Goals | The aims that shaped Bodi | General description only | Addressability of every part, late binding, patching without redeploy, interoperability across JVMs |
| ROI Considerations | Cost, saving, payback | Nothing on cost or return | Fewer redeploys for patches, reuse across JVMs without decompiling, less custom glue code. Needs figures or a worked case |
| Progress Inventory | What exists and what is open | Nothing on status | A list of finished, in-progress and planned parts of Bodi, Bloq and Sprung |

The three should be rewritten so each has its own content. As they stand,
a visitor who clicks through all three reads the same text three times.

### 3.7 Falthruu (page 6)

Cordial (my inference): Adaptability over Time as Design.

Claims, in the owner's text:
- It connects "what should have been" across the system without returning to design or implementation.
- One change requirement can be propagated across the system and into prior commits, programmatically.
- A developer can work at their own pace and style, then run Falthruu when a redesign that should have been caught earlier turns up.
- "It's safe to fall thru. No more traps in software."

Assessment:
- The problem is real and well chosen: late-discovered design changes are expensive, and most tools handle them by manual search and edit.
- Propagating a change "into prior commits" is the most ambitious claim and the riskiest. Rewriting history changes commit hashes for everyone who has pulled them. The page should say whether Falthruu rewrites commits, or adds new commits that apply the change to old branches. It should also say how it behaves on shared and published branches.
- "Safe" and "no more traps" need a stated guarantee: a preview of the change, a test run, and a way to undo it.
- The link to the rest of the chain is not stated. It is natural for Falthruu to work from the APML description and regenerate through Bloq, with Runyn checking the result. The page should say so if that is the intent.
- Below the opening paragraph the page repeats the Bodi text, as on the other pages.

## 4. Cross-page themes

| Theme | APML | Bloq | Bodi |
|-------|------|------|------|
| XML as the common medium | Yes | Yes | Yes (patching) |
| Structure first, code second | Yes | Yes | Partly |
| Shared language across roles | Implied | Yes | Not stated |
| Future-proofing and extension | Implied | Yes (further development) | Yes (core claim) |
| Speed ("minutes", "lightning quick") | Yes | Yes | No |
| Concrete example shown | No | No | Partly |

The repeated overlap is intentional: each page restates that XML-described
structure is the thread. The brand's consistent message is that one formal
description should drive design, build and operation.

## 5. Gaps worth closing before the site is final

1. Show a small APML sample and the Bloq output next to it.
2. State what "correct" means for Bloq output, and how regeneration treats hand edits.
3. Document Bodi's trust model for URL remapping.
4. Publish the Sprung description on page 4, with definitions for the terms in 3.4. The page currently carries the Falthruu paragraph.
4a. Replace the duplicated Bodi text under Design Goals, ROI Considerations and Progress Inventory with content specific to each heading.
5. Fix small text issues: "software herself", "very usually", "notion of excellent software in Day" (unclear, possibly a typo), "its its".
6. Pick the one agency name for each product (APML, Bloq, Bodi, Sprung, Runyn, Falthruu) and use it consistently in folder names under `agencies/`.

## 5a. URL slugs

Assuming the owner's numbering follows the order of the URL list, the
slugs map as follows:

| # | Slug | Content received |
|---|------|------------------|
| 1 | `copy-of-bodi-technology` | APML |
| 2 | `bloq-technology` | Bloq |
| 3 | `bodi-technology` | Bodi |
| 4 | `sprung-technology` | Falthruu text (Sprung description now supplied in chat) |
| 5 | `copy-of-bodi-technology-1` | Runyn |
| 6 | `copy-of-runyn-technology` | Falthruu |

Slugs 1, 5 and 6 begin with `copy-of`, but their content is APML, Runyn and
Falthruu, not Bodi or Runyn.
This fits why every page ends with the same Bodi text: pages were made by
duplicating Bodi and only the opening paragraph was rewritten. The slugs
should be replaced with names that match the content when the site moves
to JSP (`apml`, `bloq`, `bodi`, `sprung`, `runyn`, `falthruu`).

## 6. Link to the doctrine

This analysis supports a refinement of `docs/DOCTRINE.md`: the doctrine
should be "structure first, one description from design through
operation", with each technology owning one stage. The agencies table in
that file can now be started with APML, Bloq, Bodi, Runyn and Falthruu.
Sprung can be added from section 3.4.
