package me.rarstman.basictools.command.admin;

import com.google.common.collect.ImmutableList;
import me.rarstman.basictools.command.Command;
import me.rarstman.basictools.configuration.Configuration;
import me.rarstman.basictools.data.User;
import me.rarstman.basictools.util.ChatUtil;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.stream.Collectors;

public class UnMuteCommand extends Command {

    public UnMuteCommand(final Configuration.BasicCommand basicCommand) {
        super(basicCommand, false);
    }

    @Override
    public void onExecute(final CommandSender commandSender, final String[] args) {
        if (args.length < 1) {
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("BadUsage"), "{usage}", this.usageMessage);
            return;
        }
        final User user1 = this.userManager.getUser(args[0]).isPresent() ? this.userManager.getUser(args[0]).get() : null;

        if (user1 == null) {
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("PlayerNotExist"));
            return;
        }

        if (!user1.isMuted()) {
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("NotMuted"));
            return;
        }
        final OfflinePlayer offlinePlayer1 = user1.getOfflinePlayer();

        if (offlinePlayer1.isOnline()) {
            ChatUtil.sendMessage(offlinePlayer1.getPlayer(), this.messages.getMessage("UnMutedInfo"));
        }
        user1.setMuteDate(null);
        user1.setMuteReason(null);
        ChatUtil.sendMessage(commandSender, this.messages.getMessage("UnMuted"));
    }

    @Override
    public List<String> tabComplete(final CommandSender commandSender, final String alias, final String[] args) throws IllegalArgumentException {
        switch (args.length) {
            case 1: {
                return ImmutableList.of(
                        this.userManager.getMutedUsers().stream().map(User::getName).collect(Collectors.joining())
                );
            }
        }
        return ImmutableList.of();
    }
}
