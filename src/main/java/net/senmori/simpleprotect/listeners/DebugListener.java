package net.senmori.simpleprotect.listeners;

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
import org.bukkit.material.Door;

public class DebugListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if(doActivate(e.getPlayer(), e.getAction(), Action.LEFT_CLICK_BLOCK)) {
            e.setCancelled(true);
            Block block = e.getClickedBlock();
            p.sendMessage(ChatColor.GREEN + "==============");
            //p.sendMessage(ChatColor.GOLD + "Block: " + ChatColor.RESET + block.getType().toString() + ChatColor.GOLD);
            //p.sendMessage(ChatColor.GOLD + "Lockable: " + ChatColor.RESET + formatBool(ProtectionManager.canProtect(block)));
            //p.sendMessage(ChatColor.GOLD + "Locked: " + ChatColor.RESET + formatBool(ProtectionManager.isProtected(block)));
            //p.sendMessage(ChatColor.GOLD + "Owner: " + ChatColor.RESET + ProtectionManager.getOwnerName(block));
            //p.sendMessage(ChatColor.GOLD + "CanDestroy: " + ChatColor.RESET + formatBool(ProtectionManager.canDestroy(p, block)));
            //p.sendMessage(ChatColor.GOLD + "CanInteract: " + ChatColor.RESET + formatBool(ProtectionManager.canInteract(p, block)));
            if(block.getType() == Material.WALL_SIGN) {
                Sign signBlock = (Sign) block.getState();
                org.bukkit.material.Sign mat = (org.bukkit.material.Sign) signBlock.getData();
                p.sendMessage(ChatColor.GOLD + "Attached Face: " + ChatColor.RESET + mat.getAttachedFace() + ChatColor.GOLD + " : " + ChatColor.RESET + block.getRelative(mat.getAttachedFace()).getType());
            }
            if(isDoor(block.getType())) {
                Door door = (Door) block.getState().getData();
                p.sendMessage(ChatColor.GOLD + "Open: " + formatBool(door.isOpen()));
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

    public void openDoors(Block door, Block otherDoor) {
        door.setData((byte) (door.getData() ^ 4));
        otherDoor.setData((byte)(otherDoor.getData() ^ 4));
    }
}
