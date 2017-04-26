package net.senmori.simpleprotect;

import co.aikar.commands.ACF;
import co.aikar.commands.CommandManager;
import co.aikar.taskchain.BukkitTaskChainFactory;
import co.aikar.taskchain.TaskChain;
import co.aikar.taskchain.TaskChainFactory;
import net.senmori.simpleprotect.commands.DebugCommand;
import net.senmori.simpleprotect.db.SimpleDB;
import org.bstats.Metrics;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class SimpleProtect extends JavaPlugin {
    public static final boolean DEBUG = true;

    private static SimpleProtect instance;
    private static TaskChainFactory taskChainFactory;
    private static Metrics bStats;

    private CommandManager manager;

    public FileConfiguration configuration;

    public void onDisable() {
        instance = null;
    }

    public void onEnable() {
        instance = this;
        SimpleDB.init();
        taskChainFactory = BukkitTaskChainFactory.create(this);
        bStats = new Metrics(this);
        manager = ACF.createManager(this);

        manager.registerCommand(new DebugCommand());
    }

    public static SimpleProtect getInstance() {
        return instance;
    }

    public static <T> TaskChain<T> newChain() {
        return taskChainFactory.newChain();
    }

    public static <T> TaskChain<T> newSharedChain(String name) {
        return taskChainFactory.newSharedChain(name);
    }
}
