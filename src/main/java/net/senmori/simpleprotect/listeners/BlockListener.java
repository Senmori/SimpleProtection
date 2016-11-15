package net.senmori.simpleprotect.listeners;

import net.senmori.simpleprotect.ProtectionConfig;
import net.senmori.simpleprotect.protection.ProtectionManager;
import net.senmori.simpleprotect.util.ActionBar;
import org.bukkit.Material;
import org.bukkit.block.Block;
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
        
        if(!ProtectionManager.canProtect(targetBlock)) {
            return;
        }
        
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
        Block targetBlock = e.getBlock();
        Player player = e.getPlayer();
        if(e.getBlockReplacedState().getType() == Material.AIR) {
            return;
        }
        
        if(!ProtectionManager.canBuild(player, targetBlock)) {
            e.setBuild(false);
            e.setCancelled(true);
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
}
