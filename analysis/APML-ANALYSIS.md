# APML Site Analysis

Status: partial. Based on text supplied by the site owner: the APML and Bloq
material, the Bodi "Technological Overview", and three headed sections
("Design Goals", "ROI Considerations", "Progress Inventory"), the Sprung
page (page 4) and the Runyn page (page 5, in the owner's numbering). The
URL list had six entries; no separate text has been received for a sixth
page. Nothing here is inferred from pages not supplied.

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
| 4 | Sprung (page text describes Falthruu) | Producial, over time | Propagates a late design change across the system and prior commits. See 3.4 for the naming question. |
| 5 | Runyn | Producial, review stage | Folds a sprawling code base back into clean APML and Java source for architectural review. Closes the loop to APML. |

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

### 3.4 Sprung page (text describes Falthruu)

Cordial (my inference): Adaptability over Time as Design.

The page is titled Sprung technology, but its opening paragraph describes
a product called Falthruu and never describes Sprung. The Bodi page calls
Sprung a scripting language for mapping external URLs into Bodi contexts.
The two descriptions do not match, so either Falthruu is the product name
of the Sprung page (and Sprung the language inside it), or the page text
belongs to a different technology. The owner should say which.

Claims about Falthruu, in the owner's text:
- It connects "what should have been" across the system without returning to design or implementation.
- One change requirement can be propagated across the system and into prior commits, programmatically.
- A developer can work at their own pace and style, then run Falthruu when a redesign that should have been caught earlier turns up.
- "It's safe to fall thru. No more traps in software."

Assessment:
- The problem is real and well chosen: late-discovered design changes are expensive, and most tools handle them by manual search and edit.
- Propagating a change "into prior commits" is the most ambitious claim and the riskiest. Rewriting history changes commit hashes for everyone who has pulled them. The page should say whether Falthruu rewrites commits, or adds new commits that apply the change to old branches. It should also say how it behaves on shared and published branches.
- "Safe" and "no more traps" need a stated guarantee: a preview of the change, a test run, and a way to undo it.
- The link to APML and Bloq is not stated. It is natural for Falthruu to work from the APML description and regenerate through Bloq, which would make this the third step of one chain. The page should say so if that is the intent.
- Below the Falthruu paragraph the page repeats the Bodi text (the second and third paragraphs, then the three headed sections), the same duplication described in section 0. The Falthruu content appears only once, in the opening paragraph.

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
4. Confirm whether Sprung is a scripting language, Falthruu is its product name, or both are separate technologies.
4a. Replace the duplicated Bodi text under Design Goals, ROI Considerations and Progress Inventory with content specific to each heading.
5. Fix small text issues: "software herself", "very usually", "notion of excellent software in Day" (unclear, possibly a typo), "its its".
6. Pick the one agency name for each product (APML, Bloq, Bodi, Sprung, Runyn) and use it consistently in folder names under `agencies/`.

## 5a. URL slugs

Assuming the owner's numbering follows the order of the URL list, the
slugs map as follows:

| # | Slug | Content received |
|---|------|------------------|
| 1 | `copy-of-bodi-technology` | APML |
| 2 | `bloq-technology` | Bloq |
| 3 | `bodi-technology` | Bodi |
| 4 | `sprung-technology` | Sprung page (Falthruu text) |
| 5 | `copy-of-bodi-technology-1` | Runyn |
| 6 | `copy-of-runyn-technology` | No text received; likely a copy of the Runyn page |

Slugs 1 and 5 begin with `copy-of-bodi`, but their content is not Bodi.
This fits why every page ends with the same Bodi text: pages were made by
duplicating Bodi and only the opening paragraph was rewritten. The slugs
should be replaced with names that match the content when the site moves
to JSP (`apml`, `bloq`, `bodi`, `sprung`, `runyn`).

## 6. Link to the doctrine

This analysis supports a refinement of `docs/DOCTRINE.md`: the doctrine
should be "structure first, one description from design through
operation", with each technology owning one stage. The agencies table in
that file can now be started with APML, Bloq, Bodi, Sprung (Falthruu) and
Runyn, with the naming question in 3.4 still open.
