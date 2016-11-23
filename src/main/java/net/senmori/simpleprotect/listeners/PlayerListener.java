package net.senmori.simpleprotect.listeners;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Predicate;
import net.senmori.simpleprotect.ProtectionConfig;
import net.senmori.simpleprotect.protection.Protection;
import net.senmori.simpleprotect.protection.ProtectionManager;
import net.senmori.simpleprotect.util.ActionBar;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerListener implements Listener {
    private ProtectionConfig config;

    public PlayerListener(ProtectionConfig config) {
        this.config = config;
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent e) {
        Iterator<Block> iter = e.blockList().iterator();

        e.blockList().removeIf(new Predicate<Block>() {
            @Override
            public boolean test(Block block) {
                return ProtectionManager.isProtected(block);
            }
        });
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Block b = e.getClickedBlock();

        if(ProtectionManager.isProtected(b)) {
            if(!ProtectionManager.canInteract(e.getPlayer(), b)) {
                e.setCancelled(true);
                e.setUseInteractedBlock(Event.Result.DENY);
                e.setUseItemInHand(Event.Result.DENY);
            }
        }
    }
}
