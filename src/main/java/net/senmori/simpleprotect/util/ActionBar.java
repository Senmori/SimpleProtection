package net.senmori.simpleprotect.util;


import net.minecraft.server.v1_11_R1.ChatComponentText;
import net.minecraft.server.v1_11_R1.IChatBaseComponent;
import net.minecraft.server.v1_11_R1.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class ActionBar {

    public static void sendMessage(Player player, String message) {
        IChatBaseComponent cbc = new ChatComponentText(message);
        PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte)2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(ppoc);
    }
}
