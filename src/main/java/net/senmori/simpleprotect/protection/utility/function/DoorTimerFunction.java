package net.senmori.simpleprotect.protection.utility.function;

import net.senmori.simpleprotect.protection.utility.SignFunctionLabel;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class DoorTimerFunction extends SignFunction {

    public DoorTimerFunction() {
        super(SignFunctionLabel.TIMER);
    }

    @Override
    void onInteract(Player player, Block block) {

    }

    @Override
    void onDestroy(Player player, Block block) {

    }

    @Override
    void onItemTransfer(Inventory source, Inventory destination) {

    }

    @Override
    boolean requiresProtection() {
        return true;
    }
}
