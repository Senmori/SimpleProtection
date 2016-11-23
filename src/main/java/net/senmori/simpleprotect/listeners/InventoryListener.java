package net.senmori.simpleprotect.listeners;

import net.senmori.simpleprotect.ProtectionConfig;
import net.senmori.simpleprotect.protection.ProtectionManager;
import net.senmori.simpleprotect.util.LogHandler;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.minecart.HopperMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class InventoryListener implements Listener {
    private ProtectionConfig config;

    public InventoryListener(ProtectionConfig config) {
        this.config = config;
    }

    @EventHandler
    public void onMove(InventoryMoveItemEvent e) {
        boolean sourceLocked = isInventoryLocked(e.getSource());
        boolean destLocked = isInventoryLocked(e.getDestination());
        if(sourceLocked) {
            // if the destination is locked as well
            if(destLocked) {
                String sourceName = ProtectionManager.getOwnerName(getBlock(e.getSource()));
                String destName = ProtectionManager.getOwnerName(getBlock(e.getDestination()));
                // if both containers have the same owner, allow it
                if((sourceName != null && destName != null) && sourceName.equals(destName)) {
                    e.setCancelled(false);
                    return;
                }
            }
            e.setCancelled(true);
        }
    }

    private boolean isInventoryLocked(Inventory inventory) {
        InventoryHolder holder = inventory.getHolder();
        
        if(holder instanceof DoubleChest) {
            holder = ((DoubleChest)holder).getLeftSide();
        }

        if(holder instanceof BlockState) {
            Block block = ((BlockState)holder).getBlock();
            return ProtectionManager.isProtected(block);
        }
        return false;
    }

    private Block getBlock(Inventory inv) {
        InventoryHolder holder = inv.getHolder();

        if(holder instanceof DoubleChest) {
            holder = ((DoubleChest)holder).getLeftSide();
        }
        if(holder instanceof BlockState) {
            return ((BlockState)holder).getBlock();
        }
        return null;
    }
}
