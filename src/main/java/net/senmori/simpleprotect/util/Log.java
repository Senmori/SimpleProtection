package net.senmori.simpleprotect.util;

import net.senmori.simpleprotect.SimpleProtect;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log {
    private static final Logger LOGGER = LogManager.getLogger();

    private Log() {
    }


    public static void log(Level level, String message) {
        LOGGER.log(level, message);
    }

    public static void info(String message) {
        LOGGER.info(message);
    }

    public static void debug(String message) {
        if(SimpleProtect.DEBUG) {
            LOGGER.debug(message);
        }
    }

    public static void printException(String message, Exception e) {
        LOGGER.error(message, e);
    }

    public static void printException(Exception e) {
        LOGGER.error(e.getMessage(), e);
    }
}
