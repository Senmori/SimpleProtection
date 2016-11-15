package net.senmori.simpleprotect.protection.types;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import net.senmori.simpleprotect.ProtectionConfig;
import net.senmori.simpleprotect.protection.Protection;
import net.senmori.simpleprotect.protection.ProtectionManager;
import net.senmori.simpleprotect.util.BlockCache;
import net.senmori.simpleprotect.util.LogHandler;
import net.senmori.simpleprotect.util.Reference;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

/**
 * Represents a protection for any container
 * <br>
 * A container is anything that can hold items(including enchantment tables)
 */
public class ContainerProtection extends Protection {
    private ProtectionConfig config;
    private ImmutableList<Material> validMaterials = ImmutableList.<Material>builder()
        .add(Material.BEACON).add(Material.BREWING_STAND)
        .add(Material.DISPENSER).add(Material.DROPPER)
        .add(Material.HOPPER).add(Material.ENCHANTMENT_TABLE)
        .add(Material.FURNACE).add(Material.BURNING_FURNACE)
        .add(Material.CHEST).add(Material.TRAPPED_CHEST)
        .add(Material.NOTE_BLOCK).add(Material.JUKEBOX)
        .add(Material.ANVIL)
        .build();
    private final ImmutableList<BlockFace> validFaces = ImmutableList.<BlockFace>builder()
        .add(BlockFace.NORTH).add(BlockFace.EAST)
        .add(BlockFace.SOUTH).add(BlockFace.WEST)
        .build();
    
    public ContainerProtection(ProtectionConfig config) {
        this.config = config;
    }
    
    /**
     * Gets an immutable list of valid materials that can be protected.
     *
     * @return immutable list of materials
     */
    public List<Material> getProtectedMaterials() {
        return validMaterials;
    }
    
    public boolean isProtected(Block block) {
        if(BlockCache.isLocked(block)) {
            if(getOwnerSign(block) == null) {
                BlockCache.resetCache(block);
                return false;
            }
            return true;
        }
        return getOwnerSign(block) != null;
    }
    
    public boolean canProtect(Block block) {
        if(block.getType() == Material.WALL_SIGN) {
            // check what it's attached to
            org.bukkit.material.Sign mat = (org.bukkit.material.Sign)block.getState().getData();
            Block attachedTo = block.getRelative(mat.getAttachedFace());
            if(ProtectionManager.blacklistedMaterials.contains(attachedTo.getType())) {
                return false;
            }
            return canProtect(attachedTo);
        }
        return getProtectedMaterials().contains(block.getType());
    }
    
    public boolean canDestroy(Player player, Block block) {
        if(config.allowAdminBreak ) {
            if(config.useLockettePerms) {
                if(player.hasPermission(Reference.Lockette.ADMIN_BREAK)) {
                    return true;
                }
            }
            if(player.hasPermission(Reference.Permissions.ADMIN_BREAK)) {
                return true;
            }
        }
        return isOwner(player, block);
    }
    
    public boolean canBuild(Player player, Block block) {
        if(config.allowAdminBreak ) {
            if(config.useLockettePerms) {
                if(player.hasPermission(Reference.Lockette.ADMIN_BYPASS)) {
                    return true;
                }
            }
            if(player.hasPermission(Reference.Permissions.ADMIN_BYPASS)) {
                return true;
            }
        }
        return isOwner(player, block);
    }
    
    
    public boolean canInteract(Player player, Block block) {
        if(config.allowAdminBypass ) {
            if(config.useLockettePerms) {
                if(player.hasPermission(Reference.Lockette.ADMIN_SNOOP)) {
                    return true;
                }
            }
            if(player.hasPermission(Reference.Permissions.ADMIN_SNOOP)) {
                return true;
            }
        }
        return isUser(player, block);
    }
    
    public String getOwner(Block block) {
        Sign sign = getOwnerSign(block);
        if(sign != null) {
            return ChatColor.stripColor(sign.getLine(1));
        }
        return null;
    }
    
    private boolean isOwner(Player player, Block block) {
        Sign sign = getOwnerSign(block);
        return sign != null && ChatColor.stripColor(sign.getLine(1)).equals(player.getName());
    }
    
    private boolean isUser(Player player, Block block) {
        if(isOwner(player, block)) return true;
        
        List<Sign> users = getUserSigns(block);
        if(users != null && users.size() > 0) {
            for(Sign sign : users) {
                for(String line : sign.getLines()) {
                    String str = ChatColor.stripColor(line);
                    LogHandler.debug("Looking for user: " + str);
                    if(str.equalsIgnoreCase(ProtectionManager.EVERYONE_KEY)) {
                        return true;
                    }
                    if(str.equalsIgnoreCase(player.getName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    /**
     * Gets the main protection sign for this block.(including double chests)
     *
     * @param block the block to check
     * @return the sign, or null if not found/protected
     */
    private Sign getOwnerSign(Block block) {
        List<Sign> signs = getAttachedSigns(block);
        
        for(Sign sign : signs) {
            if(ProtectionManager.isProtectionSign(sign)) {
                return sign;
            }
        }
        return null;
    }
    
    /**
     * Gets a list of all [More Users] signs for this block
     *
     * @param block the block to checks
     * @return a list of signs, or null if none were found(or the block is not protected)
     */
    private List<Sign> getUserSigns(Block block) {
        List<Sign> signs = getAttachedSigns(block);
        if(!isProtected(block)) {
            signs.clear();
            return signs;
        }
        
        List<Sign> users = new ArrayList<>();
        for(Sign sign : signs) {
            if(ProtectionManager.isExtraUserSign(sign)) {
                users.add(sign);
            }
        }
        return users;
    }
    
    /*
        Will get all signs relevant to this protection.
        For Chests/Trapped Chests, it will look for another chest of the same type(double chest)
        and look at that block for signs as well.
        Will also look directly above and below the block for valid blocks that can contain signs as well
     */
    private List<Sign> getAttachedSigns(Block block) {
        List<Sign> signs = new ArrayList<>();
        // block is a wall sign, look at what it's attached to
        if(block.getType() == Material.WALL_SIGN) {
            signs.clear();
            Sign sign = (Sign)block.getState();
            org.bukkit.material.Sign mat = (org.bukkit.material.Sign)sign.getData();
            if(ProtectionManager.blacklistedMaterials.contains(block.getRelative(mat.getAttachedFace()).getType())) {
                BlockCache.resetCache(block); // reset cache to be safe
                return signs; // ignore signs attached to blacklisted materials
            }
            // check the block it's attached to for signs
            return getAttachedSigns(block.getRelative(mat.getAttachedFace()));
        }
        
        if(isChest(block)) {
            signs.clear();
            // find all signs attached to this chest
            signs.addAll(scanForSigns(block));
            
            Block attachedTo = null;
            for(BlockFace face : validFaces) {
                if(block.getRelative(face).getType() == block.getType()) {
                    attachedTo = block.getRelative(face); // chest is a double chest
                    break;
                }
            }
            if(attachedTo != null) {
                signs.addAll(scanForSigns(attachedTo));
            }
            return signs;
        }
        signs.clear();
        // check the actual block for signs
        signs.addAll(scanForSigns(block));
        return signs;
    }
    
    private List<Sign> scanForSigns(Block block) {
        List<Sign> signs = new ArrayList<>();
        if(block.getType() == Material.WALL_SIGN) {
            Sign sign = (Sign)block.getState();
            org.bukkit.material.Sign mat = (org.bukkit.material.Sign)sign.getData();
            if(ProtectionManager.isProtectionSign(sign) || ProtectionManager.isExtraUserSign(sign)) {
                BlockCache.setCache(sign.getBlock());
                signs.add(sign);
            }
            // check the block the sign is attached to for signs
            block = block.getRelative(mat.getAttachedFace());
            if(ProtectionManager.blacklistedMaterials.contains(block.getType())) {
                signs.clear(); // clear signs -> not allowed to be placed on this material
                BlockCache.resetCache(block);
                return signs;
            }
            
        }
        for(BlockFace face : validFaces) {
            if(block.getRelative(face).getType() == Material.WALL_SIGN) {
                Sign sign = (Sign)block.getRelative(face).getState();
                if(ProtectionManager.isProtectionSign(sign) || ProtectionManager.isExtraUserSign(sign)) {
                    BlockCache.setCache(sign.getBlock());
                    signs.add(sign);
                }
            }
        }
        if(signs.size() > 0) {
            BlockCache.setCache(block); // if it found protection signs, set the cache for this block
        } else {
            BlockCache.resetCache(block);
        }
        return signs;
    }
    
    private boolean isChest(Block block) {
        switch(block.getType()) {
            case CHEST:
            case TRAPPED_CHEST:
                return true;
            default:
                return false;
        }
    }
}
