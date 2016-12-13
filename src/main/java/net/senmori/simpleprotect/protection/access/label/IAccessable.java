package net.senmori.simpleprotect.protection.access.label;

import net.senmori.simpleprotect.protection.access.AccessLevel;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public interface IAccessable {

    boolean validateSign(Sign sign);

    boolean canInteract(Sign sign, Player player);

    boolean canDestroy(Sign sign, Player player);

    AccessLevel getAccessLevel();
}
