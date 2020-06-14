package me.rarstman.basictools.command.admin;

import com.google.common.collect.ImmutableList;
import me.rarstman.basictools.command.Command;
import me.rarstman.basictools.configuration.Configuration;
import me.rarstman.basictools.data.User;
import me.rarstman.basictools.util.ChatUtil;
import me.rarstman.basictools.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BanCommand extends Command {

    private final Pattern ipPattern;

    public BanCommand(final Configuration.BasicCommand basicCommand) {
        super(basicCommand, false);

        this.ipPattern = Pattern.compile("^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");
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
        final BanList.Type type = ipPattern.matcher(args[0]).matches() ? BanList.Type.IP : BanList.Type.NAME;
        final String permission1 = this.permission + "." + type.name().toLowerCase();

        if (!this.vaultHook.hasPermission(commandSender, permission1)) {
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("NoPermission"), "{permission}", permission1);
            return;
        }

        if (Bukkit.getBanList(type).isBanned(args[0])) {
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("IsBanned"));
            return;
        }
        final String reason = args.length > 2 && !args[args.length - 1].equalsIgnoreCase("-s") ? StringUtils.join(args, " ", 2, args.length) : this.messages.getMessage("DefaultBanReason");
        final long date = System.currentTimeMillis() + time;
        final String variableTime = time == null ? this.messages.getMessage("Permanently") : DateUtil.stringFromMills(time);
        final String variableDate = time == null ? this.messages.getMessage("Unknown") : DateUtil.formatDateFromMills(date);
        final String banFormat = ChatUtil.fixColors(ChatUtil.replace(this.messages.getMessage("BanFormat"),
                "{reason}", reason,
                "{time}", variableTime,
                "{date}", variableDate,
                "{blocker}", commandSender.getName()
        ));
        String blocked = this.messages.getMessage("Unknown");

        switch (type) {
            case IP: {
                InetAddress address = null;

                try {
                    address = InetAddress.getByName(args[0]);
                } catch (final UnknownHostException ignored) {}

                if (address == null) {
                    ChatUtil.sendMessage(commandSender, this.messages.getMessage("IpNotExist"));
                    return;
                }
                final Set<User> users = this.userManager.getUsers(address);

                if (users.size() < 1) {
                    ChatUtil.sendMessage(commandSender, this.messages.getMessage("NoUsers"));
                    return;
                }

                if (users.stream()
                        .map(User::getOfflinePlayer)
                        .anyMatch(offlinePlayer1 -> offlinePlayer1.isOnline() ? this.vaultHook.hasPermission(offlinePlayer1.getPlayer(), this.permission + ".bypass") : this.vaultHook.hasPermission(offlinePlayer1, this.permission + ".bypass"))) {
                    ChatUtil.sendMessage(commandSender, this.messages.getMessage("BypassPermission"));
                    return;
                }
                blocked = address.getHostAddress();
                users.stream()
                        .map(User::getOfflinePlayer)
                        .filter(OfflinePlayer::isOnline)
                        .forEach(offlinePlayer1 -> offlinePlayer1.getPlayer().kickPlayer(banFormat));
                break;
            }
            case NAME: {
                final OfflinePlayer offlinePlayer1 = this.userManager.getUser(args[0]).isPresent() ? this.userManager.getUser(args[0]).get().getOfflinePlayer() : null;

                if (offlinePlayer1 == null) {
                    ChatUtil.sendMessage(commandSender, this.messages.getMessage("PlayerNotExist"));
                }

                if (offlinePlayer1.isOnline() ? this.vaultHook.hasPermission(offlinePlayer1.getPlayer(), this.permission + ".bypass") : this.vaultHook.hasPermission(offlinePlayer1, this.permission + ".bypass")) {
                    ChatUtil.sendMessage(commandSender, this.messages.getMessage("BypassPermission"));
                    return;
                }
                blocked = offlinePlayer1.getName();

                if (!offlinePlayer1.isOnline()) {
                    break;
                }
                offlinePlayer1.getPlayer().kickPlayer(banFormat);
                break;
            }
        }
        Bukkit.getBanList(type).addBan(args[0], reason, new Date(date), commandSender.getName());
        ChatUtil.sendMessage(commandSender, this.messages.getMessage("Banned"),
                "{blocked}", blocked,
                "{date}", variableDate,
                "{time}", variableTime,
                "{reason}", reason);
        if(!args[args.length - 1].equalsIgnoreCase("-s")){
            ChatUtil.broadCastMessage(this.messages.getMessage("BannedBroadCastInfo"),
                    "{blocked}", blocked,
                    "{blocker}", commandSender.getName(),
                    "{date}", variableDate,
                    "{time}", variableTime,
                    "{reason}", reason
            );
        }
    }

    @Override
    public List<String> tabComplete(final CommandSender commandSender, final String alias, final String[] args) throws IllegalArgumentException {
        switch (args.length) {
            case 1: {
                return ImmutableList.of(
                        this.vaultHook.hasPermission(commandSender, this.permission + ".name") ? Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.joining()) : null);
            }
        }
        return ImmutableList.of();
    }

}
