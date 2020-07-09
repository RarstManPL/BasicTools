package me.rarstman.basictools.command.plugin;

import me.rarstman.basictools.BasicToolsPlugin;
import me.rarstman.basictools.configuration.BasicToolsCommands;
import me.rarstman.basictools.configuration.BasicToolsConfig;
import me.rarstman.basictools.configuration.BasicToolsMessages;
import me.rarstman.rarstapi.command.CommandProvider;
import me.rarstman.rarstapi.configuration.ConfigManager;
import org.bukkit.command.CommandSender;

import java.util.List;

public class BasicToolsCommand extends CommandProvider {

    private final BasicToolsPlugin plugin;
    private final BasicToolsConfig config;
    private final BasicToolsCommands commands;
    private final BasicToolsMessages messages;

    public BasicToolsCommand() {
        super(ConfigManager.getConfig(BasicToolsCommands.class).basicToolsCommandData, "basictools.command.basictools", false);

        this.plugin = BasicToolsPlugin.getPlugin();
        this.config = ConfigManager.getConfig(BasicToolsConfig.class);
        this.commands = ConfigManager.getConfig(BasicToolsCommands.class);
        this.messages = ConfigManager.getConfig(BasicToolsMessages.class);
    }

    @Override
    public void onExecute(final CommandSender commandSender, final String[] args) {
        if(!this.config.reload() || !this.commands.reload() || !this.messages.reload()){
            this.rarstAPIMessages.configurationNotCorrectlyReloaded.send(commandSender);
            return;
        }
        this.plugin.registerDatabase();
        this.rarstAPIMessages.configurationReloaded.send(commandSender);
    }

    @Override
    public List<String> onTabComplete(final CommandSender commandSender, final String alias, final String[] args) throws IllegalArgumentException {
        return null;
    }

}
