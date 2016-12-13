package net.senmori.simpleprotect.protection.access;

import java.util.ArrayList;
import java.util.List;
import net.senmori.simpleprotect.ProtectionConfig;
import net.senmori.simpleprotect.SimpleProtection;
import net.senmori.simpleprotect.protection.ProtectionManager;
import net.senmori.simpleprotect.protection.access.label.IAccessable;
import net.senmori.simpleprotect.protection.access.label.LimitedAccess;
import net.senmori.simpleprotect.protection.access.label.PrivateAccess;
import net.senmori.simpleprotect.protection.access.label.TeamAccess;
import net.senmori.simpleprotect.protection.utility.SignFunctionLabel;
import net.senmori.simpleprotect.protection.utility.SignFunctionManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public final class AccessManager {
    public static final Material REQUIRED_SIGN_TYPE = Material.WALL_SIGN;
    public static final String START_CHAR = "[";
    public static final String END_CHAR = "]";
    private static ProtectionConfig config = SimpleProtection.instance.config;

    private static final List<IAccessable> accessLabels;

    static {
        accessLabels = new ArrayList<>();
        accessLabels.add(new PrivateAccess());
        accessLabels.add(new LimitedAccess());
        accessLabels.add(new TeamAccess());
    }

    public static boolean validateProtectionSign(Sign sign) {
        if(sign.getType() != REQUIRED_SIGN_TYPE) return false;
        if(sign.getLines().length < 2) return false;

        Block attachedTo = getAttachedBlock(sign);

        if(attachedTo == null || attachedTo.getType() == Material.AIR) return false;
        if(ProtectionManager.isBlackListed(attachedTo.getType())) return false;
        if(!ProtectionManager.canProtect(attachedTo)) return false;
        AccessLevel level = AccessLevel.UNKNOWN;
        for(IAccessable access : accessLabels) {
            if(access.validateSign(sign)) {
                level = access.getAccessLevel();
            }
        }
        return level.canProtect();
    }

    public static boolean canAccess(Sign owningSign, Player player) {
        AccessLevel level = getHighestAccessLevel(owningSign);
        switch(level) {
            case LIMITED:
            case GROUP:
            case TEAM:
            case PRIVATE:
                return isUser(getAttachedBlock(owningSign), player);
            case UNKNOWN:
            default:
                return true;
        }
    }

    public static boolean canDestroy(Sign owningSign, Player player) {
        for(IAccessable access : accessLabels) {
            if(access.canDestroy(owningSign, player)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isUser(Block block, Player player) {
        Sign sign = ProtectionManager.getOwningSign(block);
        if(sign == null) return true;
        for(Sign attachedSign : ProtectionManager.getAttachedSigns(block)) {
            if(SignFunctionManager.hasUtility(attachedSign) && SignFunctionManager.getLabel(attachedSign, SignFunctionLabel.EVERYONE) != null) {
                return true;
            }
        }
        AccessLevel level = getHighestAccessLevel(sign);
        String owner = ChatColor.stripColor(sign.getLine(1));
        for(IAccessable access : accessLabels) {
            if(access.canInteract(sign, player)) {
                return true;
            }
        }
        return false;
    }


    public static AccessLevel getHighestAccessLevel(Sign sign) {
        if(sign == null) return AccessLevel.UNKNOWN;
        AccessLevel level = AccessLevel.UNKNOWN;
        for(int i = 0; i < sign.getLines().length; i++) {
            String line = sign.getLine(i);
            if(!line.startsWith(START_CHAR) && !line.endsWith(END_CHAR)) continue;
            String top = ChatColor.stripColor(line.substring(1, line.length() - 1));
            AccessLevel newLevel = AccessLevel.getByIdentifier(top);
            if(newLevel == null || newLevel == AccessLevel.UNKNOWN) {
                newLevel = AccessLevel.getByAlias(top);
                if(newLevel == null || newLevel == AccessLevel.UNKNOWN) {
                    return AccessLevel.UNKNOWN;
                }
            }
            if(!newLevel.canProtect() || (level.canProtect() && newLevel.canProtect())) {
                // conflicting access levels; use old one
                continue;
            }
            if(newLevel.getAllowedLines().contains(i)) {
                // the newLevel is valid for this line
                level = newLevel;
            }
        }
        return level;
    }

    private static Block getAttachedBlock(Sign sign) {
        org.bukkit.material.Sign mat = (org.bukkit.material.Sign)sign.getData();
        return sign.getBlock().getRelative(mat.getAttachedFace());
    }
}
