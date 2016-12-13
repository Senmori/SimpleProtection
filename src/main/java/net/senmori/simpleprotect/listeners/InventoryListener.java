package net.senmori.simpleprotect.listeners;

import net.senmori.simpleprotect.ProtectionConfig;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;

public class InventoryListener implements Listener {
    private ProtectionConfig config;

    public InventoryListener(ProtectionConfig config) {
        this.config = config;
    }

    @EventHandler
    public void onMove(InventoryMoveItemEvent e) {
        boolean sourceLocked = isInventoryLocked(e.getSource());
        boolean destLocked = isInventoryLocked(e.getDestination());
    }

    private boolean isInventoryLocked(Inventory inventory) {
        return false;
    }

    private Block getBlock(Inventory inv) {
        return null;
    }
}
