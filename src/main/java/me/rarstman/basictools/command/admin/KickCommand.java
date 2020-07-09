package me.rarstman.basictools.command.admin;

import me.rarstman.basictools.BasicToolsPlugin;
import me.rarstman.basictools.configuration.BasicToolsCommands;
import me.rarstman.basictools.configuration.BasicToolsMessages;
import me.rarstman.basictools.data.UserManager;
import me.rarstman.rarstapi.command.CommandProvider;
import me.rarstman.rarstapi.configuration.ConfigManager;
import me.rarstman.rarstapi.util.ColorUtil;
import me.rarstman.rarstapi.util.PermissionUtil;
import me.rarstman.rarstapi.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class KickCommand extends CommandProvider {

    private final BasicToolsMessages messages;
    private final UserManager userManager;

    public KickCommand() {
        super(ConfigManager.getConfig(BasicToolsCommands.class).kickCommandData, "basictools.command.kick", false);

        this.messages = ConfigManager.getConfig(BasicToolsMessages.class);
        this.userManager = BasicToolsPlugin.getPlugin().getUserManager();
    }

    @Override
    public void onExecute(final CommandSender commandSender, final String[] args) {
        if (args.length < 1) {
            this.rarstAPIMessages.badUsage.send(commandSender, "{USAGE}", this.usageMessage);
            return;
        }
        final String permission = args[0].equalsIgnoreCase("*") ? this.permission + ".all" : null;

        if (!PermissionUtil.hasPermission(commandSender, permission)) {
            this.rarstAPIMessages.noPermission.send(commandSender, "{PERMISSION}", permission);
            return;
        }
        final String reason = args.length > 1 + (args[args.length - 1].equalsIgnoreCase("-s") ? 1 : 0) ? StringUtils.join(args, " ", 1, args.length - (args[args.length - 1].equalsIgnoreCase("-s") ? 1 : 0)) : this.messages.defaultKickReason;
        final String kickFormat = ColorUtil.color(StringUtil.replace(this.messages.kickFormat,
                "{REASON}", reason,
                "{KICKER}", commandSender.getName()
        ));
        final boolean isSilient = args[args.length - 1].equalsIgnoreCase("-s");

        if (args[0].equalsIgnoreCase("*")) {
            Bukkit.getOnlinePlayers()
                    .stream()
                    .filter(player1 -> commandSender != player1)
                    .forEach(player1 -> player1.kickPlayer(ChatColor.RESET + kickFormat));
            this.messages.kickedAll.send(commandSender, "{REASON}", reason);

            if(!isSilient) {
                this.messages.kickedAllBroadCastInfo.broadCast(
                        "{REASON}", reason,
                        "{KICKER}", commandSender.getName()
                );
            }
            return;
        }
        final Player player1 = this.userManager.getUser(args[0]).isPresent() ? this.userManager.getUser(args[0]).get().getPlayer() : null;

        if (player1 == null || !this.userManager.canInteractPlayer(commandSender, player1)) {
            this.rarstAPIMessages.playerNotExistOrConsole.send(commandSender);
            return;
        }

        if (PermissionUtil.hasPermission(player1, this.permission + ".bypass")){
            this.messages.bypassPermission.send(commandSender);
            return;
        }

        player1.kickPlayer(ChatColor.RESET + kickFormat);
        this.messages.kicked.send(commandSender,
                "{REASON}", reason,
                "{KICKED}", player1.getName());

        if(!isSilient){
            this.messages.kickedBroadCastInfo.broadCast(
                    "{KICKED}", player1.getName(),
                    "{KICKER}", commandSender.getName(),
                    "{REASON}", reason
            );
        }
    }

    @Override
    public List<String> onTabComplete(final CommandSender commandSender, final String alias, final String[] args) throws IllegalArgumentException {
        switch (args.length) {
            case 0: {
                break;
            }
            case 1: {
                return this.userManager.playersThatCanInteract(commandSender).stream().filter(player -> !PermissionUtil.hasPermission(player, this.permission + ".bypass")).map(Player::getName).collect(Collectors.toList());
            }
            default: {
                return Arrays.asList("-s");
            }
        }
        return new ArrayList<>();
    }

}
