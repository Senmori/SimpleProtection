package net.senmori.simpleprotect.listeners;

import net.md_5.bungee.api.ChatColor;
import net.senmori.simpleprotect.ProtectionConfig;
import net.senmori.simpleprotect.protection.ProtectionManager;
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
        String topLine = ChatColor.stripColor(e.getLine(0));

        if(ProtectionManager.canCreateProtection(e.getPlayer(), block)) {
            // trying to create a [Private] sign
            if(topLine.equalsIgnoreCase(ProtectionManager.PRIVATE_KEY)) {
                if(ProtectionManager.isProtected(block)) {
                    e.setLine(0, ProtectionManager.ERROR_KEY);
                    e.getPlayer().sendMessage(ChatColor.RED + "Cannot add another [Private] sign to a locked container!");
                    e.setCancelled(true);
                    return;
                }
            }
            // trying to create a [More Users] sign
            if(topLine.equalsIgnoreCase(ProtectionManager.MORE_USERS_KEY)) {
                if(!ProtectionManager.isProtected(block)) {
                    e.setLine(0, ProtectionManager.ERROR_KEY);
                    e.getPlayer().sendMessage(ChatColor.RED + "Cannot add users to a container that is not protected!");
                    e.setCancelled(true);
                    return;
                }
            }
        }
    }
}
