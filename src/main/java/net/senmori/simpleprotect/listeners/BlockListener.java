package net.senmori.simpleprotect.listeners;

import net.senmori.simpleprotect.ProtectionConfig;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.world.StructureGrowEvent;

public class BlockListener implements Listener {
    private ProtectionConfig config;

    public BlockListener(ProtectionConfig config) {
        this.config = config;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Block targetBlock = e.getBlock();
        Player player = e.getPlayer();
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        // check for adjacent chests
    }

    @EventHandler
    public void onPistonExtend(BlockPistonExtendEvent e) {
        for(Block b : e.getBlocks()) {

        }
    }

    @EventHandler
    public void onPistonRetract(BlockPistonRetractEvent e) {
        for(Block b : e.getBlocks()) {

        }
    }

    @EventHandler
    public void onRedstone(BlockRedstoneEvent e) {

    }

    @EventHandler
    public void onStructureGrow(StructureGrowEvent e) {
        for(BlockState b : e.getBlocks()) {

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
