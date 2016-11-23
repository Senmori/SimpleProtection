package net.senmori.simpleprotect.protection.access.level;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public class PublicAccess implements IAccessable {

    @Override
    public boolean canInteract(Sign protectionSign, Player accessor) {
        return true;
    }

    @Override
    public boolean canDestroy(Sign protectionSign, Player destroyer) {
        return destroyer.getName().equalsIgnoreCase(ChatColor.stripColor(protectionSign.getLine(0)));
    }
}
