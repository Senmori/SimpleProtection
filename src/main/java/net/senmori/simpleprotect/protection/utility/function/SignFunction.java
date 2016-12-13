package net.senmori.simpleprotect.protection.utility.function;

import net.senmori.simpleprotect.protection.utility.SignFunctionLabel;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public abstract class SignFunction {

    private SignFunctionLabel label;

    public SignFunction(SignFunctionLabel label) {
        this.label = label;
    }

    abstract void onInteract(Player player, Block protectedBlock);

    abstract void onDestroy(Player player, Block protectedBlock);

    abstract void onItemTransfer(Inventory source, Inventory destination);

    abstract boolean requiresProtection();

    public SignFunctionLabel getLabel() {
        return label;
    }
}
