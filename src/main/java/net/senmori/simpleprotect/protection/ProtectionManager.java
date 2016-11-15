package net.senmori.simpleprotect.protection;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import net.md_5.bungee.api.ChatColor;
import net.senmori.simpleprotect.ProtectionConfig;
import net.senmori.simpleprotect.SimpleProtection;
import net.senmori.simpleprotect.protection.types.ContainerProtection;
import net.senmori.simpleprotect.protection.types.DoorProtection;
import net.senmori.simpleprotect.util.Reference;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

/**
 * Helper class to hold global protection related variables
 */
public final class ProtectionManager {
    public static final String PRIVATE_KEY = "[Private]";
    public static final String MORE_USERS_KEY = "[More Users]";
    public static final String EVERYONE_KEY = "[Everyone]";
    public static final String TIMER_KEY = "[Timer:%d]";
    public static final String ERROR_KEY = "[?]";
    
    private static final List<Protection> protections = new ArrayList<>();
    
    
    public static final ImmutableList<Material> blacklistedMaterials = ImmutableList.<Material>builder()
            .add(Material.LEAVES_2).add(Material.LEAVES).add(Material.TNT)
            .add(Material.SAND).add(Material.GRAVEL)
            .add(Material.SIGN_POST).add(Material.WALL_SIGN)
            .build();
    private static ProtectionConfig config = SimpleProtection.instance.config;
    
    public static void init(ProtectionConfig config) {
        ProtectionManager.config = config;
        protections.add(new DoorProtection(config));
        protections.add(new ContainerProtection(config));
    }
    
    public static boolean isProtected(Block block) {
        if(block == null || block.getType() == Material.AIR) return false;
        for(Protection prot : protections) {
            if(prot.isProtected(block)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean canProtect(Block block) {
        if(block == null || block.getType() == Material.AIR) return false;
        for(Protection prot : protections) {
            if(prot.canProtect(block)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean canDestroy(Player player, Block block) {
        if(block == null || block.getType() == Material.AIR) return true;
        for(Protection prot : protections) {
            if(prot.canDestroy(player, block)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean canBuild(Player player, Block block) {
        if(block == null || block.getType() == Material.AIR) return true;
        for(Protection prot : protections) {
            if(prot.canBuild(player, block)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean canInteract(Player player, Block block) {
        if(block == null || block.getType() == Material.AIR) return true;
        for(Protection prot : protections) {
            if(prot.canInteract(player, block)) {
                return true;
            }
        }
        return false;
    }
    
    public static String getOwnerName(Block block) {
        String owner = null;
        if(block == null || block.getType() == Material.AIR) return owner;
        for(Protection prot : protections) {
            if( (owner = prot.getOwner(block)) != null) {
                return owner;
            }
        }
        return owner;
    }
    
    public static boolean canCreateProtection(Player player, Block block) {
        if(block == null || block.getType() == Material.AIR) return false;
        if(!canProtect(block)) return false;
        if(config.useLockettePerms) {
            return player.hasPermission(Reference.Lockette.ADMIN_BYPASS) ||
                   player.hasPermission(Reference.Lockette.USER_CREATE) ||
                   player.hasPermission(Reference.Lockette.ADMIN_CREATE);
        }
        return player.hasPermission(Reference.Permissions.USER_CREATE) ||
               player.hasPermission(Reference.Permissions.ADMIN_CREATE) ||
               player.hasPermission(Reference.Permissions.ADMIN_BYPASS);
    }
    
    public static boolean isProtectionSign(Sign sign) {
        if(sign.getType() != Material.WALL_SIGN) return false;
        
        return (ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase(PRIVATE_KEY));
    }
    
    public static boolean isExtraUserSign(Sign sign) {
        if(sign.getType() != Material.WALL_SIGN) return false;
        // 0 & 1 are reserved for [Private] and owner's name
        if(sign.getLine(2).equalsIgnoreCase(EVERYONE_KEY) || sign.getLine(3).equalsIgnoreCase(EVERYONE_KEY)) {
            return true;
        }
        return ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase(MORE_USERS_KEY);
    }
}