package org.apml.advantage;

/** A Sprung term. The definition is the owner's paraphrased description, or null when none is approved for publication. */
public final class Term {
    private final String name;
    private final String ownerDescription;
    private final String status;

    public Term(String name, String ownerDescription, String status) {
        this.name = name;
        this.ownerDescription = ownerDescription;
        this.status = status;
    }

    public String getName() { return name; }
    public String getOwnerDescription() { return ownerDescription; }
    public String getStatus() { return status; }
}
