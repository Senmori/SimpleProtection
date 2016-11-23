package net.senmori.simpleprotect.protection.access.level;

import net.senmori.simpleprotect.protection.access.AccessLevel;
import net.senmori.simpleprotect.protection.access.AccessManager;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public class TeamAccess implements IAccessable {

    @Override
    public boolean canInteract(Sign protectionSign, Player accessor) {
        if(!validate(protectionSign)) {
            return true;
        }
        return accessor.getScoreboard().getEntryTeam(accessor.getName()).getName().equalsIgnoreCase(getTeam(protectionSign));
    }

    @Override
    public boolean canDestroy(Sign protectionSign, Player destroyer) {
        if(!validate(protectionSign)) {
            return true;
        }
        return destroyer.getScoreboard().getEntryTeam(destroyer.getName()).getName().equalsIgnoreCase(getTeam(protectionSign));
    }

    private String getTeam(Sign sign) {
        String stripped = sign.getLine(0).substring(1, sign.getLine(0).length() - 1);
        String[] top = stripped.split(":");
        if(AccessLevel.getByIdentifier(top[0]) == AccessLevel.TEAM) {
            return top[1];
        }
        return null;
    }

    private boolean validate(Sign sign) {
        if(!AccessManager.isValidAccessIdentifier(sign)) {
            return false; // invalid sign format
        }
        return getTeam(sign) != null;
    }
}
