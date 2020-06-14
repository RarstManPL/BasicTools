package me.rarstman.basictools.command.admin;

import com.google.common.collect.ImmutableList;
import me.rarstman.basictools.command.Command;
import me.rarstman.basictools.configuration.Configuration;
import me.rarstman.basictools.util.BooleanUtil;
import me.rarstman.basictools.util.ChatUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class FlyCommand extends Command {

    public FlyCommand(final Configuration.BasicCommand basicCommand) {
        super(basicCommand, false);
    }

    @Override
    public void onExecute(final CommandSender commandSender, String[] args) {
        final String permission = args.length < 2 ? null : this.permission + ".other";

        if (!commandSender.hasPermission(permission)) {
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("NoPermission"), "{permission}", permission);
            return;
        }
        final Player player1 = !(commandSender instanceof Player) && args.length < 2 ? null : args.length > 1 ? this.userManager.getUser(args[1]).isPresent() ? this.userManager.getUser(args[1]).get().getOfflinePlayer().isOnline() ? this.userManager.getUser(args[1]).get().getPlayer() : null : null : (Player) commandSender;

        if (player1 == null) {
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("PlayerNotExistOrConsole"));
            return;
        }
        Boolean isFlying = args.length > 0 ? BooleanUtil.stringStatusToBoolean(args[0]) : !player1.isFlying();

        if (isFlying == null) {
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("TurnOptionNotExist"));
            return;
        }
        final String variableStatus = isFlying ? this.messages.getMessage("On") : this.messages.getMessage("Off");
        player1.setFlying(isFlying);
        ChatUtil.sendMessage(commandSender, this.messages.getMessage("FlyStatusChanged"),
                "{status}", variableStatus,
                "{nick}", commandSender instanceof Player ? (Player) commandSender == player1 ? this.messages.getMessage("You") : player1.getName() : player1.getName()
        );
        ChatUtil.sendMessage(player1, this.messages.getMessage("FlyStatusChangedInfo"), "{status}", variableStatus);
    }

    @Override
    public List<String> tabComplete(final CommandSender commandSender, final String alias, final String[] args) throws IllegalArgumentException {
        switch (args.length) {
            case 1: {
                return ImmutableList.of("on", "off", "toggle", "-t");
            }
            case 2: {
                return ImmutableList.of(this.vaultHook.hasPermission(commandSender, this.permission + ".other") ? this.userManager.playersThatCanInteract(commandSender).stream().map(Player::getName).collect(Collectors.joining()) : null);
            }
        }
        return ImmutableList.of();
    }
}
