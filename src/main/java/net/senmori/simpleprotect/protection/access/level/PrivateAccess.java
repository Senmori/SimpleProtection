package net.senmori.simpleprotect.protection.access.level;

import net.senmori.simpleprotect.protection.ProtectionManager;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public class PrivateAccess implements IAccessable {

    @Override
    public boolean canInteract(Sign protectionSign, Player accessor) {
        return ProtectionManager.getOwnerName(protectionSign.getBlock()).equals(accessor.getName());
    }

    @Override
    public boolean canDestroy(Sign protectionSign, Player destroyer) {
        return ProtectionManager.getOwnerName(protectionSign.getBlock()).equals(destroyer.getName());
    }
}
