package net.senmori.simpleprotect.protection.types;

import static sun.audio.AudioPlayer.player;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import net.senmori.simpleprotect.ProtectionConfig;
import net.senmori.simpleprotect.protection.Protection;
import net.senmori.simpleprotect.protection.ProtectionManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.material.Door;
import org.bukkit.material.TrapDoor;

/**
 * Represents a protection for any door, trapdoor, or fence gate
 */
public class DoorProtection extends Protection {
    private ProtectionConfig config;
    private ImmutableList<Material> validMaterials = ImmutableList.<Material>builder()
        // doors
        .add(Material.WOODEN_DOOR).add(Material.IRON_DOOR_BLOCK)
        .add(Material.BIRCH_DOOR).add(Material.SPRUCE_DOOR)
        .add(Material.DARK_OAK_DOOR).add(Material.JUNGLE_DOOR)
        .add(Material.ACACIA_DOOR)
        // trapdoors
        .add(Material.TRAP_DOOR).add(Material.IRON_TRAPDOOR)
        // fence gates
        .add(Material.FENCE_GATE).add(Material.ACACIA_FENCE_GATE)
        .add(Material.BIRCH_FENCE_GATE).add(Material.DARK_OAK_FENCE_GATE)
        .add(Material.JUNGLE_FENCE_GATE).add(Material.SPRUCE_FENCE_GATE)
        .build();
    private ImmutableList<BlockFace> faces = ImmutableList.<BlockFace>builder()
        .add(BlockFace.NORTH).add(BlockFace.EAST)
        .add(BlockFace.SOUTH).add(BlockFace.WEST)
        .build();

    public DoorProtection(ProtectionConfig config) {
        this.config = config;
    }

    /**
     * Gets an immutable list of valid materials that can be protected
     *
     * @return immutable list of materials
     */
    public List<Material> getProtectedMaterials() {
        return validMaterials;
    }

    public boolean isProtected(Block block) {
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
        return getProtectedMaterials().contains(block.getType())
               || getProtectedMaterials().contains(block.getRelative(BlockFace.UP).getType())
               || getProtectedMaterials().contains(block.getRelative(BlockFace.DOWN).getType());
    }

    public boolean canDestroy(Player player, Block block) {
        if(!isProtected(block)) return true;
        if(!getProtectedMaterials().contains(block.getType())) {
            Block down = block.getRelative(BlockFace.DOWN);
            Block above = block.getRelative(BlockFace.UP);
            if(isOwner(player, above) || isOwner(player, down)) {
                return true;
            }
        }
        return isOwner(player, block) ;
    }

    public boolean canInteract(Player player, Block block) {
        if(!isProtected(block)) return true;
        if(!getProtectedMaterials().contains(block.getType())) {
            Block down = block.getRelative(BlockFace.DOWN);
            Block above = block.getRelative(BlockFace.UP);
            if(isOwner(player, above) || isOwner(player, down)) {
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
        if(!isProtected(block)) return true;
        if(isOwner(player, block)) return true;

        List<Sign> users = getUserSigns(block);

        if(users != null && users.size() > 0) {
            for(Sign sign : users) {
                for(String line : sign.getLines()) {
                    String str = ChatColor.stripColor(line);
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
     * Gets the main protection sign for this door
     *
     * @param block block to check
     * @return the main protection sign if found, or null.
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
     * Get a list of all signs that have [more users] or [everyone].
     *
     * @param block block to check
     * @return list of valid sign locations, or null.
     */
    private List<Sign> getUserSigns(Block block) {
        List<Sign> signs = getAttachedSigns(block);
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
        For Doors it will get the blocks above and below the door
     */
    private List<Sign> getAttachedSigns(Block block) {
        List<Sign> signs = new ArrayList<>();
        if(block.getType() == Material.WALL_SIGN) {
            // block is a wall sign
            Sign sign = (Sign)block.getState();
            org.bukkit.material.Sign mat = (org.bukkit.material.Sign)sign.getData();
            if(ProtectionManager.blacklistedMaterials.contains(block.getRelative(mat.getAttachedFace()).getType())) {
                return signs; // ignore signs attached to blacklisted materials
            }
            // check the block it's attached to for signs
            return getAttachedSigns(block.getRelative(mat.getAttachedFace()));
        }

        if(isTrapDoor(block.getType())) {
            // block is a trap door, check it and the block it is against for signs
            TrapDoor trap = (TrapDoor)block.getState().getData();
            if( block.getRelative(trap.getAttachedFace()) != null && block.getRelative(trap.getAttachedFace()).getType() != Material.AIR
                && !ProtectionManager.blacklistedMaterials.contains(block.getRelative(trap.getAttachedFace()).getType()) ) {
                Block against = block.getRelative(trap.getAttachedFace());
                signs.addAll(scanForSigns(against));
            }
            signs.addAll(scanForSigns(block));
            return signs;
        }

        if(isFenceGate(block.getType())) {
            // fence gate, check around it for signs
            signs.addAll(scanForSigns(block));
            return signs;
        }

        if(isDoor(block.getType())) {
            Door door = (Door)block.getState().getData();
            Block otherHalf = door.isTopHalf() ? block.getRelative(BlockFace.DOWN) : block.getRelative(BlockFace.UP);
            // now we have both halves of the door


            signs.addAll(scanForSigns(block));
            signs.addAll(scanForSigns(otherHalf));
            // check blocks above/below door
            Block aboveDoor = door.isTopHalf() ? block.getRelative(BlockFace.UP) : otherHalf.getRelative(BlockFace.UP);
            Block belowDoor = door.isTopHalf() ? otherHalf.getRelative(BlockFace.DOWN) : block.getRelative(BlockFace.DOWN);

            if(aboveDoor != null && !ProtectionManager.blacklistedMaterials.contains(aboveDoor.getType())) {
                signs.addAll(scanForSigns(aboveDoor));
            }
            if(belowDoor != null && !ProtectionManager.blacklistedMaterials.contains(belowDoor.getType())) {
                signs.addAll(scanForSigns(belowDoor));
            }
            return signs;
        }
        // block wasn't any of the valid materials, check above it
        if(getProtectedMaterials().contains(block.getRelative(BlockFace.UP).getType())) {
            return getAttachedSigns(block.getRelative(BlockFace.UP));
        }
        // block wasn't any of the valid materials, check below it
        if(getProtectedMaterials().contains(block.getRelative(BlockFace.DOWN).getType())) {
            return getAttachedSigns(block.getRelative(BlockFace.DOWN));
        }
        return signs;
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

    private boolean isTrapDoor(Material type) {
        switch(type) {
            case TRAP_DOOR:
            case IRON_TRAPDOOR:
                return true;
            default:
                return false;
        }
    }

    private boolean isFenceGate(Material type) {
        switch(type) {
            case FENCE_GATE:
            case ACACIA_FENCE_GATE:
            case BIRCH_FENCE_GATE:
            case DARK_OAK_FENCE_GATE:
            case JUNGLE_FENCE_GATE:
            case SPRUCE_FENCE_GATE:
                return true;
            default:
                return false;
        }
    }

    private List<Sign> scanForSigns(Block block) {
        List<Sign> signs = new ArrayList<>();
        if(block.getType() == Material.WALL_SIGN) {
            Sign sign = (Sign)block.getState();
            org.bukkit.material.Sign mat = (org.bukkit.material.Sign)sign.getData();
            if(ProtectionManager.isProtectionSign(sign) || ProtectionManager.isExtraUserSign(sign)) {
                signs.add(sign);
            }
            // check the block the sign is attached to for signs
            block = block.getRelative(mat.getAttachedFace());
            if(ProtectionManager.blacklistedMaterials.contains(block.getType())) {
                signs.clear(); // clear sign -> not allowed to be placed on this material
                return signs; // ignore signs attached to blacklisted materials
            }
        }
        // block was not a wall sign, check it for signs
        for(BlockFace face : faces) {
            if(block.getRelative(face).getType() == Material.WALL_SIGN) {
                Sign sign = (Sign)block.getRelative(face).getState();
                if(ProtectionManager.isProtectionSign(sign) || ProtectionManager.isExtraUserSign(sign)) {
                    signs.add(sign);
                }
            }
        }
        return signs;
    }
}
