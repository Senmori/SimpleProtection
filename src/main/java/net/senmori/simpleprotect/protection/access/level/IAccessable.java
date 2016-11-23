package net.senmori.simpleprotect.protection.access.level;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public interface IAccessable {

    /** Returns if the player can interact with this protected object */
    boolean canInteract(Block block, Player accessor);

    /** Returns if the player can destroy this protected object */
    boolean canDestroy(Block block, Player destroyer);
}
