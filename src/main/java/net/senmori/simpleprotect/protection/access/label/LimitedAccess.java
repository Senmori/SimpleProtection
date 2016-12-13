package net.senmori.simpleprotect.protection.access.label;

import net.senmori.simpleprotect.SimpleProtection;
import net.senmori.simpleprotect.protection.ProtectionManager;
import net.senmori.simpleprotect.protection.access.AccessLevel;
import net.senmori.simpleprotect.protection.access.AccessManager;
import net.senmori.simpleprotect.util.Reference;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public class LimitedAccess implements IAccessable {

    @Override
    public boolean validateSign(Sign sign) {
        for(int i = 0; i < sign.getLines().length; i++) {
            String line = sign.getLine(i);
            if(!line.startsWith(AccessManager.START_CHAR) && !line.endsWith(AccessManager.END_CHAR)) continue;
            if(getAccessLevel().getIdentifier().equalsIgnoreCase(line) || getAccessLevel().getAlias().equalsIgnoreCase(line)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canInteract(Sign sign, Player player) {
        if(SimpleProtection.instance.config.allowAdminBypass && player.hasPermission(Reference.Permissions.ADMIN_BYPASS)) {
            return true;
        }
        return isUser(sign, player);
    }

    @Override
    public boolean canDestroy(Sign sign, Player player) {
        if(SimpleProtection.instance.config.allowAdminBypass && player.hasPermission(Reference.Permissions.ADMIN_BREAK)) {
            return true;
        }
        return ChatColor.stripColor(sign.getLine(1)).equalsIgnoreCase(player.getName());
    }

    @Override
    public AccessLevel getAccessLevel() {
        return AccessLevel.LIMITED;
    }

    private boolean isUser(Sign sign, Player player) {
        org.bukkit.material.Sign mat = (org.bukkit.material.Sign)sign.getData();
        Block attachedTo = sign.getBlock().getRelative(mat.getAttachedFace());
        if(attachedTo == null || attachedTo.getType() == Material.AIR) return true;
        if(ProtectionManager.isBlackListed(attachedTo.getType())) return true; // invalid protection; allow interaction
        if(!ProtectionManager.canProtect(attachedTo)) return true; // invalid protection

        for(int i = 0; i < sign.getLines().length; i++) {
            String line = sign.getLine(i);
            if(line.startsWith(AccessManager.START_CHAR)) continue;
            if(org.bukkit.ChatColor.stripColor(line).equalsIgnoreCase(player.getName())) {
                return true;
            }
        }
        return false;
    }
}
