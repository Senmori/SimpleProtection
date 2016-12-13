package net.senmori.simpleprotect.protection.access;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the different AccessLevels available for protected blocks
 * They represent who can interact/destroy those blocks
 */
public enum AccessLevel {
    PRIVATE("Private", "P", true, Lists.newArrayList(0)),
    LIMITED("More Users", "M", true, Lists.newArrayList(0)),
    TEAM("Team", "T", true, Lists.newArrayList(0)),
    GROUP("Group", "G", true, Lists.newArrayList(0)),
    UNKNOWN("?", "?", false, Lists.newArrayList(-1)),
    ;

    private static final Map<String, AccessLevel> ACCESS_MAP;
    private static final Map<String, AccessLevel> ALIAS_MAP;
    private final String identifier;
    private final String alias;
    private final boolean canProtect;
    private final List<Integer> allowedLines;

    AccessLevel(String identifier, String alias, boolean canProtect, List<Integer> allowedLines) {
        this.identifier = identifier;
        this.alias = alias;
        this.canProtect = canProtect;
        this.allowedLines = allowedLines;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getAlias() {
        return alias;
    }

    public boolean canProtect() {
        return canProtect;
    }

    public List<Integer> getAllowedLines() {
        return allowedLines;
    }

    public boolean conflictsWith(AccessLevel level) {
        if(level == null) return true;
        return this.canProtect() && level.canProtect() || level == AccessLevel.UNKNOWN;
    }

    public static AccessLevel getByIdentifier(String identifier) {
        return ACCESS_MAP.get(identifier) != null ? ACCESS_MAP.get(identifier) : AccessLevel.UNKNOWN;
    }

    public static AccessLevel getByAlias(String alias) {
        return ALIAS_MAP.get(alias) != null ? ALIAS_MAP.get(alias) : AccessLevel.UNKNOWN;
    }

    static {
        Map<String, AccessLevel> map = new HashMap<>();
        Map<String, AccessLevel> alias = new HashMap<>();
        for(AccessLevel level : AccessLevel.values()) {
            if(level == UNKNOWN) continue; // don't track UNKNOWN
            map.put(level.getIdentifier(), level);
            alias.put(level.getAlias(), level);
        }
        ACCESS_MAP = ImmutableMap.copyOf(map);
        ALIAS_MAP = ImmutableMap.copyOf(alias);
    }
}
