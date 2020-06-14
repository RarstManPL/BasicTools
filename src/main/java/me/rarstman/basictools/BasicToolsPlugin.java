package me.rarstman.basictools;

import me.rarstman.basictools.cache.ChatCache;
import me.rarstman.basictools.command.CommandManager;
import me.rarstman.basictools.configuration.Configuration;
import me.rarstman.basictools.data.UserManager;
import me.rarstman.basictools.hook.PluginHook;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class BasicToolsPlugin extends JavaPlugin {

    private BasicToolsLogger basicToolsLogger;
    private UserManager userManager;
    private CommandManager commandManager;
    private Configuration configuration;
    private ChatCache chatCache;
    private PluginHook pluginHook;

    public static BasicToolsPlugin getPlugin() {
        return JavaPlugin.getPlugin(BasicToolsPlugin.class);
    }

    @Override
    public void onLoad() {
        this.basicToolsLogger = new BasicToolsLogger(Logger.getLogger("BasicTools"));
    }

    @Override
    public void onEnable() {
        this.chatCache = new ChatCache();
        this.pluginHook = new PluginHook();
        this.pluginHook.initialize();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public BasicToolsLogger getBasicToolsLogger() {
        return this.basicToolsLogger;
    }

    public UserManager getUserManager() {
        return this.userManager;
    }

    public CommandManager getCommandManager() {
        return this.commandManager;
    }

    public Configuration getConfiguration() {
        return this.configuration;
    }

    public ChatCache getChatCache() {
        return this.chatCache;
    }

    public PluginHook getPluginHook() {
        return this.pluginHook;
    }
}
