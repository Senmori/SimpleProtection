package net.senmori.simpleprotect.protection;

import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

public class DoorProtection implements IProtection {

    @Override
    public boolean canProtect(Block block) {
        return false;
    }

    @Override
    public boolean isProtected(Block block) {
        return false;
    }

    @Override
    public List<Sign> getAttachedSigns(Block block) {
        return null;
    }

    @Override
    public Sign getOwningSign(Block block) {
        return null;
    }
}
