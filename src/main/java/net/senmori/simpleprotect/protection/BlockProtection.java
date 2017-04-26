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
        validBlocks.add(null);
    }

    @Override
    public boolean isProtected(Location loc) {
        return false;
    }

    @Override
    public boolean canProtect(Location loc) {
        return false;
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
