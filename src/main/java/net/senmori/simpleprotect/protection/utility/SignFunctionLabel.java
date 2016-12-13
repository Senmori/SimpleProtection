package net.senmori.simpleprotect.protection.utility;

import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents an option that can be placed to signs to perform certain functions
 */
public enum SignFunctionLabel {
    TIMER("Timer"),
    EVERYONE("Everyone"),
    ;

    private static final Map<String, SignFunctionLabel> SIGN_MAP;
    private final String name;

    SignFunctionLabel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static SignFunctionLabel getByName(String name) {
        return SIGN_MAP.get(name.toLowerCase());
    }

    static {
        Map<String, SignFunctionLabel> map = new HashMap<>();
        for(SignFunctionLabel label : SignFunctionLabel.values()) {
            map.put(label.name.toLowerCase(), label);
        }
        SIGN_MAP = ImmutableMap.copyOf(map);
    }
}
