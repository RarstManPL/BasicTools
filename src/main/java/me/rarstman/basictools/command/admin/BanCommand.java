package me.rarstman.basictools.command.admin;

import me.rarstman.basictools.BasicToolsPlugin;
import me.rarstman.basictools.configuration.BasicToolsCommands;
import me.rarstman.basictools.configuration.BasicToolsMessages;
import me.rarstman.basictools.data.User;
import me.rarstman.basictools.data.UserManager;
import me.rarstman.rarstapi.command.CommandProvider;
import me.rarstman.rarstapi.configuration.ConfigManager;
import me.rarstman.rarstapi.util.*;
import org.apache.commons.lang.StringUtils;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.stream.Collectors;

public class BanCommand extends CommandProvider {

    private final BasicToolsMessages messages;
    private final UserManager userManager;

    public BanCommand() {
        super(ConfigManager.getConfig(BasicToolsCommands.class).banCommandData, "basictools.command.ban", false);

        this.messages = ConfigManager.getConfig(BasicToolsMessages.class);
        this.userManager = BasicToolsPlugin.getPlugin().getUserManager();
    }

    @Override
    public void onExecute(final CommandSender commandSender, final String[] args) {
        if (args.length < 2) {
            this.rarstAPIMessages.badUsage.send(commandSender, "{USAGE}", this.usageMessage);
            return;
        }
        final long time = args[1].equalsIgnoreCase("-1") ? -1L : DateUtil.stringToMills(args[1]);

        if (time == 0L) {
            this.messages.timeIsZero.send(commandSender);
            return;
        }
        final String permission = time == -1L ? this.permission + ".permamently" : this.permission + ".temporary";

        if (!PermissionUtil.hasPermission(commandSender, permission)) {
            this.rarstAPIMessages.noPermission.send(commandSender, "{PERMISSION}", permission);
            return;
        }
        final BanList.Type type = RegexUtil.ipPatternMatch(args[0]) ? BanList.Type.IP : BanList.Type.NAME;
        final String permission1 = this.permission + "." + type.name().toLowerCase();

        if (!PermissionUtil.hasPermission(commandSender, permission1)) {
            this.rarstAPIMessages.noPermission.send(commandSender, "{PERMISSION}", permission1);
            return;
        }

        if (Bukkit.getBanList(type).isBanned(args[0])) {
            this.messages.isBanned.send(commandSender);
            return;
        }
        final String reason = args.length > 2 + (args[args.length - 1].equalsIgnoreCase("-s") ? 1 : 0) ? StringUtils.join(args, " ", 2, args.length - (args[args.length - 1].equalsIgnoreCase("-s") ? 1 : 0)) : this.messages.defaultBanReason;
        final long date = System.currentTimeMillis() + time;
        final String variableTime = time == -1L ? this.messages.permanently : DateUtil.stringFromMills(time);
        final String variableDate = time == -1L ? this.rarstAPIMessages.unknown : DateUtil.formatDateFromMills(date);
        final String banFormat = ColorUtil.color(StringUtil.replace(this.messages.banFormat,
                "{REASON}", reason,
                "{TIME}", variableTime,
                "{DATE}", variableDate,
                "{BLOCKER}", commandSender.getName()
        ));
        String blocked = this.rarstAPIMessages.unknown;

        switch (type) {
            case IP: {
                InetAddress address = null;

                try {
                    address = InetAddress.getByName(args[0]);
                } catch (final UnknownHostException ignored) {}

                if (address == null) {
                    this.messages.ipNotExist.send(commandSender);
                    return;
                }
                final Set<User> users = this.userManager.getUsers(address);

                if (users.size() < 1) {
                    this.messages.noUsers.send(commandSender);
                    return;
                }

                if (users.stream()
                        .anyMatch(user1 -> PermissionUtil.hasPermission(user1.getOfflinePlayer(), this.permission + ".bypass"))) {
                    this.messages.bypassPermission.send(commandSender);
                    return;
                }
                blocked = address.getHostAddress();

                users.stream()
                        .filter(User::isOnline)
                        .forEach(user1 -> user1.getPlayer().kickPlayer(banFormat));
                break;
            }
            case NAME: {
                final OfflinePlayer offlinePlayer1 = this.userManager.getUser(args[0]).isPresent() ? this.userManager.getUser(args[0]).get().getOfflinePlayer() : null;

                if (offlinePlayer1 == null) {
                    this.rarstAPIMessages.playerNotExist.send(commandSender);
                    return;
                }

                if (PermissionUtil.hasPermission(offlinePlayer1, this.permission + ".bypass")) {
                    this.messages.bypassPermission.send(commandSender);
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

        Bukkit.getBanList(type).addBan(args[0], reason, time != -1L ? new Date(date) : null, commandSender.getName());
        this.messages.banned.send(commandSender,
                "{BLOCKED}", blocked,
                "{DATE}", variableDate,
                "{TIME}", variableTime,
                "{REASON}", reason
        );

        if(!args[args.length - 1].equalsIgnoreCase("-s")){
            this.messages.bannedBroadCastInfo.broadCast(
                    "{BLOCKED}", blocked,
                    "{BLOCKER}", commandSender.getName(),
                    "{DATE}", variableDate,
                    "{TIME}", variableTime,
                    "{REASON}", reason
            );
        }
    }

    @Override
    public List<String> onTabComplete(final CommandSender commandSender, final String alias, final String[] args) throws IllegalArgumentException {
        switch (args.length) {
            case 0:
            case 2: {
                break;
            }
            case 1: {
                if(!PermissionUtil.hasPermission(commandSender, this.permission + ".name")) {
                    break;
                }
                return this.userManager.usersThatCanInteract(commandSender).stream().filter(user -> !PermissionUtil.hasPermission(user.getOfflinePlayer(), this.permission + ".bypass")).map(User::getName).collect(Collectors.toList());
            }
            default: {
                return Arrays.asList("-s");
            }
        }
        return new ArrayList<>();
    }

}
