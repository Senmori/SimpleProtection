package net.senmori.simpleprotect.util;

import java.util.List;
import net.senmori.simpleprotect.SimpleProtection;
import org.bukkit.block.Block;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

public class BlockCache {
    private static final String CACHE_ID = "locked";
    private static SimpleProtection plugin = SimpleProtection.instance;
    
    public static void setCache(Block block) {
        if(block.hasMetadata(CACHE_ID)) {
            block.removeMetadata(CACHE_ID, plugin);
        }
        block.setMetadata(CACHE_ID, new FixedMetadataValue(plugin, true));
    }
    
    public static void resetCache(Block block) {
        block.removeMetadata(CACHE_ID, plugin);
    }
    
    public static boolean isLocked(Block block) {
        if(block == null) {
            return false;
        }
        List<MetadataValue> data = block.getMetadata(CACHE_ID);
        if(!data.isEmpty()) {
            if(data.get(0).asBoolean()) {
                return true;
            }
        }
        return false;
    }
}
