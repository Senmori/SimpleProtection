package net.senmori.simpleprotect.listeners;

import java.util.Iterator;
import net.senmori.simpleprotect.ProtectionConfig;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerListener implements Listener {
    private ProtectionConfig config;

    public PlayerListener(ProtectionConfig config) {
        this.config = config;
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent e) {
        Iterator<Block> iter = e.blockList().iterator();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Block b = e.getClickedBlock();
    }
}
