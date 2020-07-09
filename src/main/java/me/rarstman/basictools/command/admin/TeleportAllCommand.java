package me.rarstman.basictools.command.admin;

import me.rarstman.basictools.configuration.BasicToolsCommands;
import me.rarstman.basictools.configuration.BasicToolsMessages;
import me.rarstman.rarstapi.command.CommandProvider;
import me.rarstman.rarstapi.configuration.ConfigManager;
import me.rarstman.rarstapi.util.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class TeleportAllCommand extends CommandProvider {

    private final BasicToolsMessages messages;

    public TeleportAllCommand() {
        super(ConfigManager.getConfig(BasicToolsCommands.class).teleportAllCommandData, "basictools.command.teleportall", true);

        this.messages = ConfigManager.getConfig(BasicToolsMessages.class);
    }

    @Override
    public void onExecute(final CommandSender commandSender, final String[] args) {
        final Player player = (Player) commandSender;
        final Location location = player.getLocation();

        Bukkit.getOnlinePlayers()
                .forEach(player1 -> {
                    player1.teleport(location);
                    this.messages.teleportedAllInfo.send(player1,
                            "{WORLD}", location.getWorld().getName(),
                            "{LOCALIZATION}", LocationUtil.locationToString(location)
                    );
                });
        this.messages.teleportedAll.send(player);
    }

    @Override
    public List<String> onTabComplete(final CommandSender commandSender, final String alias, final String[] args) throws IllegalArgumentException {
        return null;
    }

}
