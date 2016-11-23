package net.senmori.simpleprotect.protection.access;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.senmori.simpleprotect.protection.access.level.IAccessable;
import net.senmori.simpleprotect.protection.access.level.LimitedAccess;
import net.senmori.simpleprotect.protection.access.level.PrivateAccess;
import net.senmori.simpleprotect.protection.access.level.PublicAccess;
import net.senmori.simpleprotect.protection.access.level.TeamAccess;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;

public class AccessManager {
    private final static String INVALID_ACCESS_KEY = "[?]";

    private final static BiMap<AccessLevel, IAccessable> ACCESS_MAP = HashBiMap.create();

    public static void registerAccessIdentifier(AccessLevel level, IAccessable accessable) {
        if(ACCESS_MAP.containsKey(level) || ACCESS_MAP.containsValue(accessable)) {
            throw new IllegalArgumentException("Cannot register the same AccessIdentifier twice!");
        }
        ACCESS_MAP.put(level, accessable);
    }

    public static IAccessable getAccessIdentifier(AccessLevel level) {
        return ACCESS_MAP.get(level);
    }

    public static boolean isValidAccessIdentifier(Sign sign) {
        String stripped = ChatColor.stripColor(sign.getLine(0).substring(1, sign.getLine(0).length() -1)); // strip brackets
        String[] top = stripped.split(":"); // get the identifier
        if(AccessLevel.getByIdentifier(top[0]) == null) {
            sign.setLine(0, INVALID_ACCESS_KEY);
            return false;
        }
        return true;
    }

    public static void init() {
        registerAccessIdentifier(AccessLevel.PRIVATE, new PrivateAccess());
        registerAccessIdentifier(AccessLevel.LIMITED, new LimitedAccess());
        registerAccessIdentifier(AccessLevel.PUBLIC, new PublicAccess());
        registerAccessIdentifier(AccessLevel.TEAM, new TeamAccess());
    }
}
