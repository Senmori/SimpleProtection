package net.senmori.simpleprotect.protection.access.level;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public class LimitedAccess implements IAccessable {

    @Override
    public boolean canInteract(Sign protectionSign, Player accessor) {
        return false;
    }

    @Override
    public boolean canDestroy(Sign protectionSign, Player destroyer) {
        return false;
    }
}
