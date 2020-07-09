package me.rarstman.basictools.command.admin;

import me.rarstman.basictools.BasicToolsPlugin;
import me.rarstman.basictools.configuration.BasicToolsCommands;
import me.rarstman.basictools.configuration.BasicToolsMessages;
import me.rarstman.basictools.data.User;
import me.rarstman.basictools.data.UserManager;
import me.rarstman.rarstapi.command.CommandProvider;
import me.rarstman.rarstapi.configuration.ConfigManager;
import me.rarstman.rarstapi.util.DateUtil;
import me.rarstman.rarstapi.util.PermissionUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MuteCommand extends CommandProvider {

    private final BasicToolsMessages messages;
    private final UserManager userManager;

    public MuteCommand() {
        super(ConfigManager.getConfig(BasicToolsCommands.class).muteCommandData, "basictools.command.mute", false);

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
        final User user1 = this.userManager.getUser(args[0]).isPresent() ? this.userManager.getUser(args[0]).get() : null;

        if (user1 == null) {
            this.rarstAPIMessages.playerNotExist.send(commandSender);
            return;
        }

        if (user1.isMuted()) {
            this.messages.isMuted.send(commandSender);
            return;
        }
        final OfflinePlayer offlinePlayer1 = user1.getOfflinePlayer();

        if (PermissionUtil.hasPermission(offlinePlayer1, this.permission + ".bypass")) {
            this.messages.bypassPermission.send(commandSender);
            return;
        }
        final String reason = args.length > 2 + (args[args.length - 1].equalsIgnoreCase("-s") ? 1 : 0) ? StringUtils.join(args, " ", 2, args.length - (args[args.length - 1].equalsIgnoreCase("-s") ? 1 : 0)) : this.messages.defaultMuteReason;
        final long date = System.currentTimeMillis() + time;
        final String variableDate = time == -1L ? this.rarstAPIMessages.unknown : DateUtil.formatDateFromMills(date);
        final String variableTime = time == -1L ? this.messages.permanently : DateUtil.stringFromMills(time);

        user1.mute(time != -1L ? date : -1L, reason);

        this.messages.mutedInfo.send(offlinePlayer1,
                "{REASON}", reason,
                "{DATE}", variableDate,
                "{TIME}", variableTime,
                "{MUTER}", commandSender.getName()
        );

        this.messages.muted.send(commandSender,
                "{REASON}", reason,
                "{DATE}", variableDate,
                "{TIME}", variableTime,
                "{MUTED}", user1.getName()
        );

        if(!args[args.length - 1].equalsIgnoreCase("-s")){
            this.messages.mutedBroadCastInfo.broadCast(
                    "{REASON}", reason,
                    "{MUTER}", commandSender.getName(),
                    "{MUTED}", user1.getName(),
                    "{TIME}", variableDate,
                    "{DATE}", variableDate
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
                return this.userManager.usersThatCanInteract(commandSender).stream().filter(user -> !PermissionUtil.hasPermission(user.getOfflinePlayer(), this.permission + ".bypass")).map(User::getName).collect(Collectors.toList());
            }
            default: {
                return Arrays.asList("-s");
            }
        }
        return new ArrayList<>();
    }

}
