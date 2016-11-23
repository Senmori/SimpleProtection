package net.senmori.simpleprotect.protection.access.level;

import net.senmori.simpleprotect.protection.ProtectionManager;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class PublicAccess implements IAccessable {

    @Override
    public boolean canInteract(Block block, Player accessor) {
        return true;
    }

    @Override
    public boolean canDestroy(Block block, Player destroyer) {
        return destroyer.getName().equalsIgnoreCase(ProtectionManager.getOwnerName(block));
    }
}
