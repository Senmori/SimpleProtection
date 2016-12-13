package net.senmori.simpleprotect.listeners;

import net.md_5.bungee.api.ChatColor;
import net.senmori.simpleprotect.ProtectionConfig;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignListener implements Listener {
    private ProtectionConfig config;

    public SignListener(ProtectionConfig config) {
        this.config = config;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onSignChange(SignChangeEvent e) {
        if(e.getBlock().getType() != Material.WALL_SIGN) return;
        Block block = e.getBlock();
    }
}
