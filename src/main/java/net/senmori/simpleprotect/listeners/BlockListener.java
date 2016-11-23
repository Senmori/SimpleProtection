package net.senmori.simpleprotect.listeners;

import net.senmori.simpleprotect.ProtectionConfig;
import net.senmori.simpleprotect.protection.ProtectionManager;
import net.senmori.simpleprotect.util.ActionBar;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.material.Door;
import org.bukkit.material.MaterialData;

public class BlockListener implements Listener {
    private ProtectionConfig config;

    public BlockListener(ProtectionConfig config) {
        this.config = config;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Block targetBlock = e.getBlock();
        Player player = e.getPlayer();
        // If it can't be protected, or isn't protected return
        if(!ProtectionManager.canProtect(targetBlock) || !ProtectionManager.isProtected(targetBlock)) {
            return;
        }
        // if they can't destroy this block; cancel it.
        // Reset the data to be safe
        if(!ProtectionManager.canDestroy(player, targetBlock)) {
            BlockState state = e.getBlock().getState();
            MaterialData data = state.getData();
            state.setData(data);
            state.update();
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        // check for adjacent chests
        if(isChest(e.getBlockPlaced())) {
            for(BlockFace face : ProtectionManager.validFaces) {
                if(e.getBlockPlaced().getRelative(face).getType() == e.getBlockPlaced().getType()) {
                    if(ProtectionManager.isProtected(e.getBlockPlaced().getRelative(face))) {
                        if(!ProtectionManager.canInteract(e.getPlayer(), e.getBlock())) {
                            e.setBuild(false);
                            e.setCancelled(true);
                            return;
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPistonExtend(BlockPistonExtendEvent e) {
        for(Block b : e.getBlocks()) {
            if(ProtectionManager.isProtected(b)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPistonRetract(BlockPistonRetractEvent e) {
        for(Block b : e.getBlocks()) {
            if(ProtectionManager.isProtected(b)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onRedstone(BlockRedstoneEvent e) {
        if(ProtectionManager.isProtected(e.getBlock())) {
            e.setNewCurrent(e.getOldCurrent());
        }
    }

    @EventHandler
    public void onStructureGrow(StructureGrowEvent e) {
        for(BlockState b : e.getBlocks()) {
            if(ProtectionManager.isProtected(b.getBlock())) {
                e.setCancelled(true);
            }
        }
    }

    private boolean isChest(Block block) {
        switch(block.getType()) {
            case CHEST:
            case TRAPPED_CHEST:
                return true;
            default:
                return false;
        }
    }

    private boolean isDoor(Material type) {
        switch(type) {
            case WOODEN_DOOR:
            case IRON_DOOR_BLOCK:
            case BIRCH_DOOR:
            case SPRUCE_DOOR:
            case DARK_OAK_DOOR:
            case JUNGLE_DOOR:
            case ACACIA_DOOR:
                return true;
            default:
                return false;
        }
    }
}
