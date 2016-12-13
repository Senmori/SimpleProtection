package net.senmori.simpleprotect.protection;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;

public final class ProtectionManager {
    private static final ImmutableList<Material> blackList = ImmutableList.<Material>builder()
            .add(Material.LEAVES_2).add(Material.LEAVES).add(Material.TNT)
            .add(Material.SAND).add(Material.GRAVEL).add(Material.SIGN_POST)
            .add(Material.WALL_SIGN)
            .build();

    private static final ImmutableList<BlockFace> validFaces = ImmutableList.<BlockFace>builder()
            .add(BlockFace.NORTH).add(BlockFace.EAST).add(BlockFace.SOUTH).add(BlockFace.WEST).build();

    private static List<IProtection> protections = new ArrayList<>();

    public static boolean isBlackListed(Material material) {
        return blackList.contains(material);
    }

    public static List<BlockFace> getValidFaces() {
        return validFaces;
    }

    public static boolean canProtect(Block block) {
        return getProtection(block) != null;
    }

    public static boolean isProtected(Block block) {
        IProtection prot = getProtection(block);
        return prot != null && prot.isProtected(block);
    }

    public static List<Sign> getAttachedSigns(Block block) {
        IProtection prot = getProtection(block);
        if(prot == null) {
            return new ArrayList<>();
        }
        return prot.getAttachedSigns(block);
    }

    public static Sign getOwningSign(Block block) {
        IProtection prot = getProtection(block);
        if(prot == null) {
            return null;
        }
        return prot.getOwningSign(block);
    }

    private static IProtection getProtection(Block block) {
        for(IProtection prot : protections) {
            if(prot.canProtect(block)) {
                return prot;
            }
        }
        return null;
    }

    static {
        protections.add(new ContainerProtection());
        protections.add(new DoorProtection());
    }
}
