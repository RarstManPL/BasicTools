package me.rarstman.basictools.command.admin;

import com.google.common.collect.ImmutableList;
import me.rarstman.basictools.command.Command;
import me.rarstman.basictools.configuration.Configuration;
import me.rarstman.basictools.util.ChatUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class KickCommand extends Command {

    public KickCommand(final Configuration.BasicCommand basicCommand) {
        super(basicCommand, false);
    }

    @Override
    public void onExecute(final CommandSender commandSender, final String[] args) {
        if (args.length < 1) {
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("BadUsage"), "{usage}", this.usageMessage);
            return;
        }
        final String permission = args[0].equalsIgnoreCase("*") ? this.permission + ".all" : null;

        if (!this.vaultHook.hasPermission(commandSender, permission)) {
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("NoPermission"), "{permission}", permission);
            return;
        }
        final String reason = args.length > 1 && !args[args.length - 1].equalsIgnoreCase("-s") ? StringUtils.join(args, " ", 1, args.length) : this.messages.getMessage("DefaultKickReason");
        final String kickFormat = ChatUtil.fixColors(ChatUtil.replace(this.messages.getMessage("KickFormat"),
                "{reason}", reason,
                "{kicker}", commandSender.getName()
        ));
        final boolean isSilient = args[args.length - 1].equalsIgnoreCase("-s");

        if (args[0].equalsIgnoreCase("*")) {
            Bukkit.getOnlinePlayers()
                    .stream()
                    .filter(player1 -> commandSender != player1)
                    .forEach(player1 -> player1.kickPlayer(kickFormat));
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("KickedAll"), "{reason}", reason);
            if(!isSilient) {
                ChatUtil.broadCastMessage(this.messages.getMessage("KickedAllBroadCastInfo"), "{reason}", reason);
            }
            return;
        }
        final Player player1 = !(commandSender instanceof Player) && args.length < 1 ? null : args.length > 0 ? this.userManager.getUser(args[0]).isPresent() ? this.userManager.getUser(args[0]).get().getOfflinePlayer().isOnline() ? this.userManager.getUser(args[0]).get().getPlayer() : null : null : (Player) commandSender;

        if (player1 == null) {
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("PlayerNotExistOrConsole"));
            return;
        }

        if (this.vaultHook.hasPermission(player1, this.permission + ".bypass")){
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("BypassPermission"));
            return;
        }
        player1.kickPlayer(kickFormat);
        ChatUtil.sendMessage(commandSender, this.messages.getMessage("Kicked"), "{reason}", reason);
        if(!isSilient){
            ChatUtil.broadCastMessage(this.messages.getMessage("KickedBroadCastInfo"),
                    "{kicked}", player1.getName(),
                    "{kicker}", commandSender.getName(),
                    "{reason}", reason
            );
        }
    }

    @Override
    public List<String> tabComplete(final CommandSender commandSender, final String alias, final String[] args) throws IllegalArgumentException {
        switch (args.length) {
            case 1: {
                return ImmutableList.of(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.joining()), this.vaultHook.hasPermission(commandSender, this.permission + ".all") ? "*" : null);
            }
        }
        return ImmutableList.of();
    }
}
