package me.rarstman.basictools.command.admin;

import me.rarstman.basictools.configuration.BasicToolsCommands;
import me.rarstman.basictools.configuration.BasicToolsMessages;
import me.rarstman.rarstapi.command.CommandProvider;
import me.rarstman.rarstapi.configuration.ConfigManager;
import me.rarstman.rarstapi.util.PermissionUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

import java.util.List;

public class AdminChatCommand extends CommandProvider {

    private final BasicToolsMessages messages;

    public AdminChatCommand() {
        super(ConfigManager.getConfig(BasicToolsCommands.class).adminChatCommandData, "basictools.command.adminchat", false);

        this.messages = ConfigManager.getConfig(BasicToolsMessages.class);
    }

    @Override
    public void onExecute(final CommandSender commandSender, final String[] args) {
        if (args.length < 1) {
            this.rarstAPIMessages.badUsage.send(commandSender, "{USAGE}", this.usageMessage);
            return;
        }

        if (!PermissionUtil.hasPermission(commandSender, this.permission + ".send")) {
            this.rarstAPIMessages.noPermission.send(commandSender, "{PERMISSION}", permission);
            return;
        }
        this.messages.adminChatFormat.broadCastPermission(this.permission + ".recive", "{MESSAGE}", StringUtils.join(args, " "), "{NICK}", commandSender.getName());
    }

    @Override
    public List<String> onTabComplete(final CommandSender commandSender, final String alias, final String[] args) throws IllegalArgumentException {
        return null;
    }

}
