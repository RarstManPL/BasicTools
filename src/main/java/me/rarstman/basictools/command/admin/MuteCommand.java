package me.rarstman.basictools.command.admin;

import com.google.common.collect.ImmutableList;
import me.rarstman.basictools.command.Command;
import me.rarstman.basictools.configuration.Configuration;
import me.rarstman.basictools.data.User;
import me.rarstman.basictools.util.ChatUtil;
import me.rarstman.basictools.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class MuteCommand extends Command {

    public MuteCommand(final Configuration.BasicCommand basicCommand) {
        super(basicCommand, false);
    }

    @Override
    public void onExecute(final CommandSender commandSender, final String[] args) {
        if (args.length < 2) {
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("BadUsage"), "{usage}", this.usageMessage);
            return;
        }
        final Long time = StringUtils.isNumeric(args[1]) && Integer.valueOf(args[1]) < 0 ? null : DateUtil.stringToMills(args[1]);

        if (time == 0L) {
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("TimeIsZero"));
            return;
        }
        final String permission = time == null ? this.permission + ".permamently" : this.permission + ".temporary";

        if (!this.vaultHook.hasPermission(commandSender, permission)) {
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("NoPermission"), "{permission}", permission);
            return;
        }
        final User user1 = this.userManager.getUser(args[0]).isPresent() ? this.userManager.getUser(args[0]).get() : null;

        if (user1 == null) {
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("PlayerNotExist"));
            return;
        }

        if (user1.isMuted()) {
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("IsMuted"));
            return;
        }
        final OfflinePlayer offlinePlayer1 = user1.getOfflinePlayer();

        if (offlinePlayer1.isOnline() ? this.vaultHook.hasPermission(offlinePlayer1.getPlayer(), this.permission + ".bypass") : this.vaultHook.hasPermission(offlinePlayer1, this.permission + ".bypass")) {
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("BypassPermission"));
            return;
        }
        final String reason = args.length > 2 && !args[args.length - 1].equalsIgnoreCase("-s") ? StringUtils.join(args, " ", 2, args.length) : this.messages.getMessage("DefaultMuteReason");
        final long date = System.currentTimeMillis() + time;
        final String variableDate = time == null ? this.messages.getMessage("Unknown") : DateUtil.formatDateFromMills(date);
        final String variableTime = time == null ? this.messages.getMessage("Permanently") : DateUtil.stringFromMills(time);

        if (!offlinePlayer1.isOnline()) {
            ChatUtil.sendMessage(offlinePlayer1.getPlayer(), this.messages.getMessage("MutedInfo"),
                    "{reason}", reason,
                    "{date}", variableDate,
                    "{time}", variableTime,
                    "{muter}", commandSender.getName()
            );
        }
        user1.setMuteDate(date);
        user1.setMuteReason(reason);
        ChatUtil.sendMessage(offlinePlayer1.getPlayer(), this.messages.getMessage("Muted"),
                "{reason}", reason,
                "{date}", variableDate,
                "{time}", variableTime,
                "{muted}", user1.getName()
        );
        if(!args[args.length - 1].equalsIgnoreCase("-s")){
            ChatUtil.broadCastMessage(this.messages.getMessage("MutedBroadCastInfo"),
                    "{reason}", reason,
                    "{muter}", commandSender.getName(),
                    "{muted}", user1.getName(),
                    "{time}", variableTime,
                    "{date}", variableDate
            );
        }
    }

    @Override
    public List<String> tabComplete(final CommandSender commandSender, final String alias, final String[] args) throws IllegalArgumentException {
        switch (args.length) {
            case 1: {
                return ImmutableList.of(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.joining()));
            }
        }
        return ImmutableList.of();
    }
}
