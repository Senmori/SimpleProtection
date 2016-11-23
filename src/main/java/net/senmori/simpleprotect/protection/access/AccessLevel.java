package net.senmori.simpleprotect.protection.access;

import java.util.ArrayList;
import java.util.List;

public enum AccessLevel {
    /** Access is limited to the owner */
    PRIVATE("Private", "P"),
    /** Access is limited to those the owner specifies */
    LIMITED("More Users", "More"),
    /** Everyone can access this protected block */
    PUBLIC("Everyone", "E"),
    /** Only team mates can access this protected block */
    TEAM("Team", "T"),
    ;
    private final static List<AccessLevel> ACCESS_LEVELS = new ArrayList<>();
    private final String identifier;
    private final String alias;

    AccessLevel(String identifier) {
        this(identifier, "");
    }

    AccessLevel(String identifier, String alias) {
        this.identifier = identifier;
        this.alias = alias.isEmpty() ? "_UNKNOWN_" : alias;
    }

    /** Returns the identifier that represents this AccessLevel */
    public String getPrimaryIdentifier() {
        return identifier;
    }

    /** Returns an immutable list of aliases that are associated with this AccessLevel */
    public String getAlias() {
        return alias;
    }

    public static AccessLevel getByIdentifier(String identifier) {
        if(identifier == null) return null;
        for(AccessLevel al : ACCESS_LEVELS) {
            if(al.identifier.equalsIgnoreCase(identifier) || al.getAlias().equalsIgnoreCase(identifier)) {
                return al;
            }
        }
        return null;
    }

    static {
        // so we don't keep creating new
        for(AccessLevel a : AccessLevel.values()) {
            ACCESS_LEVELS.add(a);
        }
    }
}
