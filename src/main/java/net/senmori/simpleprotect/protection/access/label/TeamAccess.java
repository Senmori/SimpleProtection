package net.senmori.simpleprotect.protection.access.label;

import net.md_5.bungee.api.ChatColor;
import net.senmori.simpleprotect.SimpleProtection;
import net.senmori.simpleprotect.protection.access.AccessLevel;
import net.senmori.simpleprotect.protection.access.AccessManager;
import net.senmori.simpleprotect.util.Reference;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public class TeamAccess implements IAccessable {

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
        String team = player.getScoreboard().getEntryTeam(player.getName()).getName();
        if(team == null || team.isEmpty()) {
            return true;
        }
        return ChatColor.stripColor(sign.getLine(1)).equalsIgnoreCase(team);
    }

    @Override
    public boolean canDestroy(Sign sign, Player player) {
        if(SimpleProtection.instance.config.allowAdminBypass && player.hasPermission(Reference.Permissions.ADMIN_BREAK)) {
            return true;
        }
        String team = player.getScoreboard().getEntryTeam(player.getName()).getName();
        if(team == null || team.isEmpty()) {
            return true;
        }
        return ChatColor.stripColor(sign.getLine(1)).equalsIgnoreCase(team);
    }

    @Override
    public AccessLevel getAccessLevel() {
        return AccessLevel.TEAM;
    }
}
