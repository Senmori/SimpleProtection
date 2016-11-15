package net.senmori.simpleprotect.util;

import net.minecraft.server.v1_10_R1.ChatComponentText;
import net.minecraft.server.v1_10_R1.IChatBaseComponent;
import net.minecraft.server.v1_10_R1.PacketPlayOutChat;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class ActionBar {
    
    public static void sendMessage(Player player, String message) {
        IChatBaseComponent cbc = new ChatComponentText(message);
        PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte)2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(ppoc);
    }
    
    public static void sendLockedMessage(Player player, Block block) {
        String message = ChatColor.RED + "This " + block.getType().toString().toLowerCase().replace("_", " ") + " is locked!";
        sendMessage(player, message);
        player.playSound(block.getLocation(), Sound.BLOCK_CHEST_LOCKED, 1.0F, 1.0F);
    }
}
