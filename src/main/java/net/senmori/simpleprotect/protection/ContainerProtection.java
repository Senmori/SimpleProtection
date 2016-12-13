package net.senmori.simpleprotect.protection;

import com.google.common.collect.ImmutableList;
import java.util.List;
import net.senmori.simpleprotect.protection.IProtection;
import net.senmori.simpleprotect.protection.access.AccessLevel;
import net.senmori.simpleprotect.protection.access.AccessManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;

public class ContainerProtection implements IProtection {
    private ImmutableList<Material> validMaterials = ImmutableList.<Material>builder()
            .add(Material.BEACON).add(Material.BREWING_STAND).add(Material.BREWING_STAND)
            .add(Material.DISPENSER).add(Material.DROPPER).add(Material.HOPPER)
            .add(Material.ENCHANTMENT_TABLE).add(Material.FURNACE).add(Material.BURNING_FURNACE)
            .add(Material.CHEST).add(Material.TRAPPED_CHEST).add(Material.NOTE_BLOCK)
            .add(Material.JUKEBOX).add(Material.ANVIL)
            .build();
    private final ImmutableList<BlockFace> validFaces = ImmutableList.<BlockFace>builder()
            .add(BlockFace.NORTH).add(BlockFace.EAST).add(BlockFace.SOUTH).add(BlockFace.WEST).build();

    @Override
    public boolean canProtect(Block block) {
        if(block.getType() == Material.WALL_SIGN) {
            org.bukkit.material.Sign mat = (org.bukkit.material.Sign)block.getState().getData();
            Block attachedTo = block.getRelative(mat.getAttachedFace());
            if(ProtectionManager.isBlackListed(attachedTo.getType())) {
                return false;
            }
            return canProtect(block.getRelative(mat.getAttachedFace()));
        }
        return validMaterials.contains(block.getType());
    }

    @Override
    public boolean isProtected(Block block) {
        return getOwningSign(block) != null;
    }

    @Override
    public List<Sign> getAttachedSigns(Block block) {
        return null;
    }

    @Override
    public Sign getOwningSign(Block block) {
        AccessLevel level = null;
        for(Sign sign : getAttachedSigns(block)) {
            AccessLevel newLevel = AccessManager.getHighestAccessLevel(sign);
            if(newLevel.conflictsWith(level)) {
                continue;
            }
            level = newLevel;
        }
        return null;
    }
}
