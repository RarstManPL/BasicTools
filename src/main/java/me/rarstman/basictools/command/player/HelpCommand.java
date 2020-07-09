package me.rarstman.basictools.command.player;

import me.rarstman.basictools.configuration.BasicToolsCommands;
import me.rarstman.basictools.configuration.BasicToolsMessages;
import me.rarstman.rarstapi.command.CommandProvider;
import me.rarstman.rarstapi.configuration.ConfigManager;
import org.bukkit.command.CommandSender;

import java.util.List;

public class HelpCommand extends CommandProvider {

    private final BasicToolsMessages messages;

    public HelpCommand() {
        super(ConfigManager.getConfig(BasicToolsCommands.class).helpCommandData, "basictools.command.help", false);

        this.messages = ConfigManager.getConfig(BasicToolsMessages.class);
    }

    @Override
    public void onExecute(final CommandSender commandSender, final String[] args) {
        this.messages.helpMessage.send(commandSender);
    }

    @Override
    public List<String> onTabComplete(final CommandSender commandSender, final String alias, final String[] args) throws IllegalArgumentException {
        return null;
    }

}
