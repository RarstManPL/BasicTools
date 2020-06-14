package me.rarstman.basictools.command.admin;

import me.rarstman.basictools.command.Command;
import me.rarstman.basictools.configuration.Configuration;
import me.rarstman.basictools.util.ChatUtil;
import me.rarstman.basictools.util.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportAllCommand extends Command {

    public TeleportAllCommand(final Configuration.BasicCommand basicCommand) {
        super(basicCommand, true);
    }

    @Override
    public void onExecute(final CommandSender commandSender, final String[] args) {
        final Player player = (Player) commandSender;
        final Location location = player.getLocation();
        Bukkit.getOnlinePlayers()
                .forEach(player1 -> {
                    player1.teleport(location);
                    ChatUtil.sendMessage(player1, this.messages.getMessage("TeleportedAllInfo"),
                            "{world}", location.getWorld().getName(),
                            "{localization}", LocationUtil.locationToString(location)
                    );
                });
        ChatUtil.sendMessage(player, this.messages.getMessage("TeleportedAll"));
    }
}
