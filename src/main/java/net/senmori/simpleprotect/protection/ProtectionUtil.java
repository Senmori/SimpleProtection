package net.senmori.simpleprotect.protection;

import java.util.ArrayList;
import java.util.List;
import net.senmori.simpleprotect.protection.access.AccessManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.ShulkerBox;
import org.bukkit.block.Sign;

public final class ProtectionUtil {

    public static boolean isChest(Block block) {
        return block.getState() instanceof Chest;
    }

    public static boolean isShulkerBox(Block block) {
        return block.getState() instanceof ShulkerBox;
    }

    public static boolean isShulkerBox(Material material) {
        if(!material.isBlock()) {
            return false;
        }
        switch(material) {
            case WHITE_SHULKER_BOX:
            case ORANGE_SHULKER_BOX:
            case MAGENTA_SHULKER_BOX:
            case LIGHT_BLUE_SHULKER_BOX:
            case YELLOW_SHULKER_BOX:
            case LIME_SHULKER_BOX:
            case PINK_SHULKER_BOX:
            case GRAY_SHULKER_BOX:
            case SILVER_SHULKER_BOX:
            case CYAN_SHULKER_BOX:
            case PURPLE_SHULKER_BOX:
            case BLUE_SHULKER_BOX:
            case BROWN_SHULKER_BOX:
            case GREEN_SHULKER_BOX:
            case RED_SHULKER_BOX:
            case BLACK_SHULKER_BOX:
                return true;
            default:
                return false;
        }
    }

    public static boolean isDoor(Material material) {
        switch(material) {
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

    public static boolean isTrapDoor(Material material) {
        switch(material) {
            case TRAP_DOOR:
            case IRON_TRAPDOOR:
                return true;
            default:
                return false;
        }
    }

    public static boolean isFenceGate(Material material) {
        switch(material) {
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

    public static List<Sign> scanForSigns(Block block, List<BlockFace> faces) {
        List<Sign> signs = new ArrayList<>();
        if(block.getType() == Material.WALL_SIGN) {
            Sign sign = (Sign)block.getState();
            org.bukkit.material.Sign mat = (org.bukkit.material.Sign)sign.getData();
            if(AccessManager.validateProtectionSign(sign)) {
                signs.add(sign);
            }
            // check the block the sign is attached to for signs
            block = block.getRelative(mat.getAttachedFace());
            if(ProtectionManager.isBlackListed(block.getType())) {
                signs.clear(); // clear signs -> not allowed to be placed on this material
                return signs; // ignore signs attached to blacklisted materials
            }
        }
        // block was not a wall sign, check it for signs
        for(BlockFace face : faces) {
            if(block.getRelative(face).getType() == Material.WALL_SIGN) {
                Sign sign = (Sign)block.getRelative(face).getState();
                if(AccessManager.validateProtectionSign(sign)) {
                    signs.add(sign);
                }
            }
        }
        return signs;
    }
}
