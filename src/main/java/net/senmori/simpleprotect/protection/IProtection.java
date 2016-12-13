package net.senmori.simpleprotect.protection;

import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

public interface IProtection {
    boolean canProtect(Block block);

    boolean isProtected(Block block);

    List<Sign> getAttachedSigns(Block block);

    Sign getOwningSign(Block block);
}
