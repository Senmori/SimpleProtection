package net.senmori.simpleprotect.protection.access.level;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public interface IAccessable {

    /** Returns if the player can interact with this protected object */
    boolean canInteract(Sign protectionSign, Player accessor);

    /** Returns if the player can destroy this protected object */
    boolean canDestroy(Sign protectionSign, Player destroyer);
}
