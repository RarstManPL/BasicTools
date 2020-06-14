package me.rarstman.basictools;

import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.logging.Logger;

public class BasicToolsLogger {

    private final Logger logger;

    public BasicToolsLogger(final Logger logger) {
        this.logger = logger;
    }

    public void info(final String message) {
        this.logger.info(message);
    }

    public void warning(final String message) {
        this.logger.warning(message);
    }

    public void error(final String message) {
        this.logger.severe(message);
    }

    public void exception(final String authorMessage, final String errorMessage, final StackTraceElement[] stackTraceElements) {
        this.error(" ");
        this.error("BasicToolsPlugin - exception");
        this.error(" ");
        this.error("Informations:");
        this.error(" > Server Version: " + Bukkit.getBukkitVersion());
        this.error(" > Java Version: " + System.getProperty("Java.Version"));
        this.error(" > Author Message: " + authorMessage);
        this.error(" > Error Message: " + errorMessage);
        this.error(" ");
        this.error("StackTraceElements: ");
        Arrays.asList(stackTraceElements)
                .forEach(stackTraceElement -> this.error(" " + stackTraceElement.toString()));
        this.error(" ");
    }
}
