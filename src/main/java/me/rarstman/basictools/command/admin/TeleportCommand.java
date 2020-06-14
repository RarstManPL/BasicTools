package me.rarstman.basictools.command.admin;

import com.google.common.collect.ImmutableList;
import me.rarstman.basictools.command.Command;
import me.rarstman.basictools.configuration.Configuration;
import me.rarstman.basictools.util.ChatUtil;
import me.rarstman.basictools.util.LocationUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class TeleportCommand extends Command {

    public TeleportCommand(final Configuration.BasicCommand basicCommand) {
        super(basicCommand, false);
    }

    @Override
    public void onExecute(final CommandSender commandSender, final String[] args) {
        if (args.length < 1) {
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("BadUsage"), "{usage}", this.usageMessage);
            return;
        }
        final Integer playerPosition = args.length > 1 && args.length < 3 ? 1 : args.length > 3 ? 3 : null;
        final String permission = playerPosition == null ? null : this.permission + ".other";

        if (!this.vaultHook.hasPermission(commandSender, permission)) {
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("NoPermission"), "{permission}", permission);
            return;
        }

        final Player player1 = playerPosition == null && !(commandSender instanceof Player) ? null : playerPosition != null ? this.userManager.getUser(args[playerPosition]).isPresent() ? this.userManager.getUser(args[1]).get().getOfflinePlayer().isOnline() ? this.userManager.getUser(args[playerPosition]).get().getPlayer() : null : null : (Player) commandSender;

        if (player1 == null) {
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("PlayerNotExistOrConsole"));
            return;
        }
        Location location = null;

        switch (args.length) {
            case 1:
            case 2: {
                location = this.userManager.getUser(args[0]).isPresent() ? this.userManager.getUser(args[0]).get().getPlayer().isOnline() ? this.userManager.getUser(args[0]).get().getPlayer().getLocation() : null : null;
                break;
            }
            default: {
                if (!StringUtils.isNumeric(args[0]) || !StringUtils.isNumeric(args[1]) || !StringUtils.isNumeric(args[2])) {
                    ChatUtil.sendMessage(commandSender, this.messages.getMessage("CoordsNotNumber"));
                    return;
                }
                location = new Location(player1.getWorld(), Double.parseDouble(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]));
                break;
            }
        }

        if (location == null) {
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("LocalizationNotExist"));
            return;
        }
        player1.teleport(location);
        ChatUtil.sendMessage(commandSender, this.messages.getMessage("Teleported"),
                "{localization}", LocationUtil.locationToString(location),
                "{world}", location.getWorld().getName(),
                "{nick}", commandSender instanceof Player ? (Player) commandSender == player1 ? this.messages.getMessage("You") : player1.getName() : player1.getName()
        );
        ChatUtil.sendMessage(player1, this.messages.getMessage("TeleportedInfo"),
                "{localization}", LocationUtil.locationToString(location),
                "{world}", location.getWorld().getName()
        );
    }

    @Override
    public List<String> tabComplete(final CommandSender commandSender, final String alias, final String[] args) throws IllegalArgumentException {
        switch (args.length) {
            case 1: {
                return ImmutableList.of(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.joining()), "~0");
            }
            case 2: {
                return ImmutableList.of(this.vaultHook.hasPermission(commandSender, this.permission + ".other") ? Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.joining()) : null, "~0");
            }
            case 3: {
                return ImmutableList.of("~0");
            }
            case 4: {
                return ImmutableList.of(this.vaultHook.hasPermission(commandSender, this.permission + ".other") ? Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.joining()) : null);
            }
        }
        return ImmutableList.of();
    }

}
