package net.senmori.simpleprotect.listeners;

import net.senmori.simpleprotect.protection.ProtectionManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class DebugListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if(doActivate(e.getPlayer(), e.getAction(), Action.LEFT_CLICK_BLOCK)) {
            e.setCancelled(true);
            Block b = e.getClickedBlock();
            p.sendMessage(ChatColor.GREEN + "==============");
            p.sendMessage(ChatColor.GOLD + "Block: " + ChatColor.RESET + b.getType().toString() + ChatColor.GOLD + " : " + ChatColor.RESET + b.getState().getData().getData());
            p.sendMessage(ChatColor.GOLD + "Lockable: " + ChatColor.RESET + formatBool(ProtectionManager.canProtect(b)));
            p.sendMessage(ChatColor.GOLD + "Locked: " + ChatColor.RESET + formatBool(ProtectionManager.isProtected(b)));
            p.sendMessage(ChatColor.GOLD + "Owner: " + ChatColor.RESET + ProtectionManager.getOwnerName(b));
            p.sendMessage(ChatColor.GOLD + "CanDestroy: " + ChatColor.RESET + formatBool(ProtectionManager.canDestroy(p, b)));
            p.sendMessage(ChatColor.GOLD + "CanInteract: " + ChatColor.RESET + formatBool(ProtectionManager.canInteract(p, b)));
            if(b.getType() == Material.WALL_SIGN) {
                Sign signBlock = (Sign)b.getState();
                org.bukkit.material.Sign mat = (org.bukkit.material.Sign)signBlock.getData();
                p.sendMessage(ChatColor.GOLD + "Attached Face: " + ChatColor.RESET + mat.getAttachedFace() + ChatColor.GOLD + " : " + ChatColor.RESET + b.getRelative(mat.getAttachedFace()).getType());
            }
            p.sendMessage(ChatColor.GREEN + "==============");
        }
    }

    private String formatBool(boolean value) {
        return value ? ChatColor.GREEN + "true" : ChatColor.RED + "false";
    }

    private boolean doActivate(Player player, Action action, Action required) {
        return player.isSneaking() && action == required && player.getInventory().getItemInMainHand().getType() == Material.DIAMOND_SWORD;
    }
}
