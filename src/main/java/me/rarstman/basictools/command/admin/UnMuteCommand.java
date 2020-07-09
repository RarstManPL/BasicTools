package me.rarstman.basictools.command.admin;

import me.rarstman.basictools.BasicToolsPlugin;
import me.rarstman.basictools.configuration.BasicToolsCommands;
import me.rarstman.basictools.configuration.BasicToolsMessages;
import me.rarstman.basictools.data.User;
import me.rarstman.basictools.data.UserManager;
import me.rarstman.rarstapi.command.CommandProvider;
import me.rarstman.rarstapi.configuration.ConfigManager;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UnMuteCommand extends CommandProvider {

    private final BasicToolsMessages messages;
    private final UserManager userManager;

    public UnMuteCommand() {
        super(ConfigManager.getConfig(BasicToolsCommands.class).unMuteCommandData, "basictools.command.unmute", false);

        this.messages = ConfigManager.getConfig(BasicToolsMessages.class);
        this.userManager = BasicToolsPlugin.getPlugin().getUserManager();
    }

    @Override
    public void onExecute(final CommandSender commandSender, final String[] args) {
        if (args.length < 1) {
            this.rarstAPIMessages.badUsage.send(commandSender, "{USAGE}", this.usageMessage);
            return;
        }
        final User user1 = this.userManager.getUser(args[0]).isPresent() ? this.userManager.getUser(args[0]).get() : null;

        if (user1 == null) {
            this.rarstAPIMessages.playerNotExist.send(commandSender);
            return;
        }

        if (!user1.isMuted()) {
            this.messages.notMuted.send(commandSender);
            return;
        }
        user1.unMute();

        this.messages.unMuted.send(commandSender);
        this.messages.unMutedInfo.send(user1.getOfflinePlayer());
    }

    @Override
    public List<String> onTabComplete(final CommandSender commandSender, final String alias, final String[] args) throws IllegalArgumentException {
        switch (args.length) {
            case 1: {
                return this.userManager.getMutedUsers().stream().map(User::getName).collect(Collectors.toList());
            }
        }
        return new ArrayList<>();
    }

}
