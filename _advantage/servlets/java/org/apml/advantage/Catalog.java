package org.apml.advantage;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The single source of truth that the API, the JSP pages and the static HTML pages all read.
 * Text comes from analysis/APML-ANALYSIS.md, docs/ENGINEERING-PRINCIPLES.md and sleela-source/Strand*.sleela.
 * Nothing here is invented: where the owner has not defined something, it is listed as an open item.
 */
public final class Catalog {

    public static final String API_VERSION = "v1";

    /**
     * Assumed legacy Wix slugs (analysis/APML-ANALYSIS.md section 5a). Assumption: the owner's page numbering
     * follows the order of the original URL list. Unconfirmed; the owner should correct any that are wrong.
     */
    private static final Map<String, String> LEGACY_SLUGS = Map.of(
        "apml", "copy-of-bodi-technology",
        "bloq", "bloq-technology",
        "bodi", "bodi-technology",
        "sprung", "sprung-technology",
        "runyn", "copy-of-bodi-technology-1",
        "falthruu", "copy-of-runyn-technology");

    private final List<Technology> technologies;
    private final Map<String, Technology> bySlug;
    private final List<Term> terms;

    public Catalog() {
        List<Technology> t = List.of(
            new Technology(1, "apml", "APML", "The description",
                "What is this system, written so that anyone can check it?",
                "Structure comes first, and function follows from it.",
                List.of("APML is Application Markup Language, a subset of XML.",
                        "Structure guides function and compiles into source code.",
                        "Software systems can be compiled, extended, interacted with and published in minutes."),
                List.of("No APML sample is shown yet, so a reader cannot judge the language.",
                        "The in-minutes claim has no worked example."),
                "bloq", "falthruu"),
            new Technology(2, "bloq", "Bloq", "The making",
                "How does the description become working source code without hand-building the structure each time?",
                "What can be generated from the description should be generated, and people should spend their effort on what cannot.",
                List.of("Bloq reads APML and produces Java source for further development.",
                        "Architects and developers share one artifact.",
                        "Scope runs from Java Swing to large systems."),
                List.of("What 'correct' guarantees is not stated.",
                        "How regeneration treats hand edits is not stated."),
                "bodi", "runyn"),
            new Technology(3, "bodi", "Bodi", "The reach",
                "How does every part of a running system stay addressable and changeable, even years later?",
                "Every part has a name that can be reached, and a name can be pointed somewhere new.",
                List.of("Every server, system, object, listener and function has its own URL over TCP/IP.",
                        "Bodi-compliant software can be patched and hooked through XML documents.",
                        "External URLs map to internal contexts, expressed through Sprung."),
                List.of("The trust model for remapping a URL (authentication, signing, audit) is not documented.",
                        "Which calls are local and which are remote is not stated."),
                "sprung", "sprung"),
            new Technology(4, "sprung", "Sprung", "The message and the time",
                "What passes between the parts, and how is it named, ordered and acted on?",
                "A message carries its own name as time, and what happens over time can create, change or end things.",
                List.of("Sprung is an envoy to messaging; messages are classed as voltage on time.",
                        "It is a scripting language with ingress and egress for Java and/or SLeeLa.",
                        "It can call Create, Alter or Destroy on time, and on the symptom of a function."),
                List.of("Message format, and how a script binds to a Bodi URL, are undefined.",
                        "Whether time names are wall-clock, logical or both is undefined.",
                        "The creative time-force equation is not yet written out."),
                "runyn", "bodi"),
            new Technology(5, "runyn", "Runyn", "The refolding",
                "After the work has grown, is the system still what the description says it is?",
                "The description must be recoverable from the code, so the two can always be compared.",
                List.of("Runyn refolds sprawling code during development into clean APML and Java source.",
                        "The result is ready for architectural review."),
                List.of("'Clean', 'correct' and 'right direction' need definitions."),
                "falthruu", "bloq"),
            new Technology(6, "falthruu", "Falthruu", "The carrying through",
                "When a change turns out to be needed, how does it reach everywhere it should have been?",
                "A late change should be applied to the whole system and its history as one act, and that act must be safe to attempt.",
                List.of("One change requirement can be propagated across the system and prior commits, programmatically.",
                        "A developer works at their own pace, then runs Falthruu when a late redesign is found."),
                List.of("Whether it rewrites commits or adds new ones is not stated.",
                        "The guarantee behind 'safe' (preview, test run, undo) is not stated."),
                "apml", "apml"));
        this.technologies = t;
        Map<String, Technology> m = new LinkedHashMap<>();
        for (Technology x : t) m.put(x.getSlug(), x);
        this.bySlug = Collections.unmodifiableMap(m);

        String pending = "Owner description recorded; no formula or worked example yet.";
        this.terms = List.of(
            new Term("Voltage on Time",
                "An economic force over a distribution of causes, plus gifters counted as gains.", pending),
            new Term("Attention Creation Principle",
                "Whether a group can be trusted as a sane individual over taxes, taxation and consumption.", pending),
            new Term("Economic and mode",
                "A person consumes 2 as a static membership, for benefit and not for cause. Economy travels, mode heals.", pending),
            new Term("Two",
                "Twice the gauge of a standard reticle of a standard product, usually per citizen or mechanic.", pending),
            new Term("Public speaker is born",
                "An economic circle settles at a home near a man and becomes known to its kind and to listeners.", pending),
            new Term("Creative time-force equation", null,
                "Not yet written out. The owner has not approved wording for publication."));
    }

    public List<Technology> getTechnologies() { return technologies; }
    public Map<String, Technology> getBySlug() { return bySlug; }
    public List<Term> getTerms() { return terms; }
    public String legacySlug(String slug) { return LEGACY_SLUGS.get(slug); }
    public Technology find(String slug) { return bySlug.get(slug); }
}
