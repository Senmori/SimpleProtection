package net.senmori.simpleprotect.protection.access.level;

import net.senmori.simpleprotect.protection.ProtectionManager;
import net.senmori.simpleprotect.protection.access.AccessLevel;
import net.senmori.simpleprotect.protection.access.AccessManager;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public class TeamAccess implements IAccessable {

    @Override
    public boolean canInteract(Block block, Player accessor) {
        if(!validate(block)) {
            return true;
        }
        return accessor.getScoreboard().getEntryTeam(accessor.getName()).getName().equals(getTeam(ProtectionManager.getOwnerSign(block)));
    }

    @Override
    public boolean canDestroy(Block block, Player destroyer) {
        if(!validate(block)) {
            return true;
        }
        return destroyer.getScoreboard().getEntryTeam(destroyer.getName()).getName().equals(getTeam(ProtectionManager.getOwnerSign(block)));
    }

    private String getTeam(Sign sign) {
        String stripped = sign.getLine(0).substring(1, sign.getLine(0).length() - 1);
        String[] top = stripped.split(":");
        if(AccessLevel.getByIdentifier(top[0]) == AccessLevel.TEAM) {
            return top[1];
        }
        return null;
    }

    private boolean validate(Block block) {
        if(!AccessManager.isValidAccessIdentifier(ProtectionManager.getOwnerSign(block))) {
            return false; // invalid sign format
        }
        return getTeam(ProtectionManager.getOwnerSign(block)) != null;
    }
}
