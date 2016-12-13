package net.senmori.simpleprotect;


import java.io.File;
import net.senmori.simpleprotect.util.FileUtil;

public class ProtectionConfig {
    private SimpleProtection plugin;

    public final boolean debug = true;
    public boolean allowAdminBypass;
    public boolean adminSilentSnoop;
    public boolean explosionProtection;
    public boolean doorProtection;
    public boolean containerProtection;
    public boolean enableErrorMessage;
    public boolean enableHints;

    private ProtectionConfig(SimpleProtection plugin) {
        this.plugin = plugin;
        init();
    }

    public static ProtectionConfig init(SimpleProtection plugin) {
        SimpleProtection.instance.configuration = SimpleProtection.instance.getConfig();

        File file = new File(plugin.getDataFolder(), "config.yml");

        if(!file.exists()) {
            file.getParentFile().mkdirs();
            FileUtil.copyFile(plugin.getResource("config.yml"), file);
        }

        SimpleProtection.instance.configuration = SimpleProtection.instance.getConfig();
        return new ProtectionConfig(plugin);
    }

    private void init() {
        this.allowAdminBypass = plugin.getConfig().getBoolean("allow-admin-bypass", true);
        this.allowAdminBypass = plugin.getConfig().getBoolean("allow-admin-snoop", true);
        this.adminSilentSnoop = plugin.getConfig().getBoolean("admin-silent-snoop", true);
        this.explosionProtection = plugin.getConfig().getBoolean("explosion-protection", true);
        this.doorProtection = plugin.getConfig().getBoolean("door-protection", true);
        this.containerProtection = plugin.getConfig().getBoolean("container-protection", true);
        this.enableErrorMessage = plugin.getConfig().getBoolean("enable-error-messages", true);
        this.enableHints = plugin.getConfig().getBoolean("enable-hints", true);
    }
}
