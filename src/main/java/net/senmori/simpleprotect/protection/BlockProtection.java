package net.senmori.simpleprotect.protection;

import com.google.common.collect.Sets;
import java.util.Set;
import net.senmori.simpleprotect.protection.type.ProtectionType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BlockProtection extends Protection {
    private final Set<Material> validBlocks = Sets.newHashSet();

    public BlockProtection(ProtectionType type) {
        super(type);
        validBlocks.add(Material.CHEST);
        validBlocks.add(Material.TRAPPED_CHEST);
        validBlocks.add(Material.DISPENSER);
        validBlocks.add(Material.DROPPER);
        validBlocks.add(Material.HOPPER);
        validBlocks.add(Material.BEACON);
        validBlocks.add(Material.BED);
        validBlocks.add(Material.BREWING_STAND);
        validBlocks.add(Material.BURNING_FURNACE);
        validBlocks.add(Material.ENDER_CHEST);
        validBlocks.add(Material.ENCHANTMENT_TABLE);
        validBlocks.add(Material.FENCE_GATE);
        validBlocks.add(Material.SPRUCE_FENCE_GATE);
        validBlocks.add(Material.ACACIA_FENCE_GATE);
        validBlocks.add(Material.BIRCH_FENCE_GATE);
        validBlocks.add(Material.DARK_OAK_FENCE_GATE);
        validBlocks.add(Material.JUNGLE_FENCE_GATE);
    }

    @Override
    public boolean isProtected(Location loc) {
        return false;
    }

    @Override
    public boolean canProtect(Location loc) {
        return isShulker(loc.getBlock().getType()) || validBlocks.contains(loc.getBlock().getType());
    }

    @Override
    public boolean isOwner(Location loc, Player player) {
        return false;
    }

    @Override
    public boolean canInteract(Location loc, Player player) {
        return this.type.equals(ProtectionType.PUBLIC);
    }


    private static boolean isShulker(Material mat) {
        ItemStack stack = new ItemStack(mat);
        return stack.getItemMeta() != null && stack.getItemMeta() instanceof ShulkerBox;
    }
}
