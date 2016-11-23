package net.senmori.simpleprotect.protection.access.level;

import net.senmori.simpleprotect.protection.ProtectionManager;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public class PrivateAccess implements IAccessable {

    @Override
    public boolean canInteract(Block block, Player accessor) {
        return ProtectionManager.getOwnerName(block).equals(accessor.getName());
    }

    @Override
    public boolean canDestroy(Block block, Player destroyer) {
        return ProtectionManager.getOwnerName(block).equals(destroyer.getName());
    }
}
