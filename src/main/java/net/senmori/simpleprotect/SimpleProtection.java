package net.senmori.simpleprotect;

import java.util.logging.Logger;
import net.senmori.simpleprotect.listeners.BlockListener;
import net.senmori.simpleprotect.listeners.DebugListener;
import net.senmori.simpleprotect.listeners.InventoryListener;
import net.senmori.simpleprotect.listeners.PlayerListener;
import net.senmori.simpleprotect.listeners.SignListener;
import net.senmori.simpleprotect.protection.ProtectionManager;
import net.senmori.simpleprotect.protection.access.AccessManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class SimpleProtection extends JavaPlugin {
    
    public static SimpleProtection instance;
    
    public FileConfiguration configuration;
    public ProtectionConfig config;
    
    public static final Logger logger = Bukkit.getLogger();
    
    public void onDisable() {
        instance = null;
    }
    
    public void onEnable() {
        instance = this;
        
        config = ProtectionConfig.init(instance);
    
        ProtectionManager.init(config);
        AccessManager.init();
        
        getServer().getPluginManager().registerEvents(new BlockListener(config), this);
        if(config.debug) {
            getServer().getPluginManager().registerEvents(new DebugListener(), this);
        }
        getServer().getPluginManager().registerEvents(new InventoryListener(config), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(config), this);
        getServer().getPluginManager().registerEvents(new SignListener(config), this);
    
        
    }
}
