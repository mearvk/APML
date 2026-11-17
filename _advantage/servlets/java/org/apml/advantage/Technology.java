package org.apml.advantage;

import java.util.List;

/** One of the six APML strands. Immutable; getters are bean-style so JSP EL can read them. */
public final class Technology {
    private final int order;
    private final String slug;
    private final String name;
    private final String role;
    private final String question;
    private final String principle;
    private final List<String> ownerClaims;
    private final List<String> openItems;
    private final String handsOffTo;
    private final String pairedWith;

    public Technology(int order, String slug, String name, String role, String question, String principle,
                      List<String> ownerClaims, List<String> openItems, String handsOffTo, String pairedWith) {
        this.order = order;
        this.slug = slug;
        this.name = name;
        this.role = role;
        this.question = question;
        this.principle = principle;
        this.ownerClaims = List.copyOf(ownerClaims);
        this.openItems = List.copyOf(openItems);
        this.handsOffTo = handsOffTo;
        this.pairedWith = pairedWith;
    }

    public int getOrder() { return order; }
    public String getSlug() { return slug; }
    public String getName() { return name; }
    public String getRole() { return role; }
    public String getQuestion() { return question; }
    public String getPrinciple() { return principle; }
    public List<String> getOwnerClaims() { return ownerClaims; }
    public List<String> getOpenItems() { return openItems; }
    public String getHandsOffTo() { return handsOffTo; }
    public String getPairedWith() { return pairedWith; }
}
