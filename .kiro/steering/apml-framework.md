# APML Framework (important, always on)

APML is a structure-first software framework by the site owner (Wix site
https://mearvk.wixsite.com/bloq, source of truth for page text). One formal
XML-subset description drives design, build, operation and review.

Technologies, in reading order (left to right, a bridge from understanding to production):
1. APML: Application Markup Language, a subset of XML. The shared structural description.
2. Bloq: reads APML and generates Java source. Architects and developers share one artifact.
3. Bodi: remote server technology. Every server, system, object, listener and function has its own URL over TCP/IP; patching and hooking by XML; external URLs map to internal contexts via Sprung.
4. Sprung: envoy to messaging. Messages are classified as "voltage on time" and carry their own name as time. A special scripting language with ingress and egress for Java and/or SLeeLa, as a sub-mechanism of the framework. Can call Create, Alter or Destroy on time as a mechanism and on the symptom of a function; the owner calls it a creative time-force equation (equation not yet written out).
5. Runyn: refolds sprawling code during development back into clean APML and Java source for architectural review (reverse of Bloq).
6. Falthruu: propagates a late-discovered change requirement across the system and prior commits, programmatically.

Conventions:
- Java and JSP sources live under `source/` (not `src/`); see `pom.xml`.
- Use these exact product names and spellings.
- Do not invent facts about these technologies. Where something is undefined, say so and ask the owner.
- Full analysis: `analysis/APML-ANALYSIS.md`. Doctrine draft: `docs/DOCTRINE.md`.
