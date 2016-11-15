package net.senmori.simpleprotect.util;

import java.util.logging.Level;
import net.senmori.simpleprotect.ProtectionConfig;
import net.senmori.simpleprotect.SimpleProtection;

public class LogHandler {
    private static ProtectionConfig config = SimpleProtection.instance.config;
    
    public static void log(Level level, String message) {
        SimpleProtection.logger.log(level, message);
    }
    
    public static void all(String message) {
        log(Level.ALL, message);
    }
    
    public static void warning(String message) {
        log(Level.WARNING, message);
    }
    
    public static void severe(String message) {
        log(Level.SEVERE, message);
    }
    
    public static void info(String message) {
        log(Level.INFO, message);
    }
    
    public static void debug(String message) {
        if(config.debug) {
            log(Level.ALL, message);
        }
    }
    
    public static void fine(String message) {
        log(Level.FINE, message);
    }
}
