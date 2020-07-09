package me.rarstman.basictools.command.admin;

import me.rarstman.basictools.BasicToolsPlugin;
import me.rarstman.basictools.configuration.BasicToolsCommands;
import me.rarstman.basictools.configuration.BasicToolsMessages;
import me.rarstman.basictools.data.UserManager;
import me.rarstman.rarstapi.command.CommandProvider;
import me.rarstman.rarstapi.configuration.ConfigManager;
import me.rarstman.rarstapi.util.LocationUtil;
import me.rarstman.rarstapi.util.NumberUtil;
import me.rarstman.rarstapi.util.PermissionUtil;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TeleportCommand extends CommandProvider {

    private final BasicToolsMessages messages;
    private final UserManager userManager;

    public TeleportCommand() {
        super(ConfigManager.getConfig(BasicToolsCommands.class).teleportCommandData, "basictools.command.teleport", false);

        this.messages = ConfigManager.getConfig(BasicToolsMessages.class);
        this.userManager = BasicToolsPlugin.getPlugin().getUserManager();
    }

    @Override
    public void onExecute(final CommandSender commandSender, final String[] args) {
        if (args.length < 1) {
            this.rarstAPIMessages.badUsage.send(commandSender, "{USAGE}", this.usageMessage);
            return;
        }
        final int playerPosition = args.length > 1 && args.length < 3 ? 1 : args.length > 3 ? 3 : -1;
        final String permission = playerPosition == -1 ? null : this.permission + ".other";

        if (!PermissionUtil.hasPermission(commandSender, permission)) {
            this.rarstAPIMessages.noPermission.send(commandSender, "{PERMISSION}", permission);
            return;
        }
        final Player player1 = playerPosition == -1 && !(commandSender instanceof Player) ? null : playerPosition != -1 ? this.userManager.getUser(args[playerPosition]).isPresent() ? this.userManager.getUser(args[playerPosition]).get().getPlayer() : null : (Player) commandSender;

        if (player1 == null || !this.userManager.canInteractPlayer(commandSender, player1)) {
            this.rarstAPIMessages.playerNotExistOrConsole.send(commandSender);
            return;
        }
        Location location;

        switch (args.length) {
            case 1:
            case 2: {
                location = this.userManager.getUser(args[0]).isPresent() ? this.userManager.getUser(args[0]).get().isOnline() ? this.userManager.canInteractPlayer(commandSender, this.userManager.getUser(args[0]).get().getPlayer()) ? this.userManager.getUser(args[0]).get().getPlayer().getLocation() : null : null : null;
                break;
            }
            default: {
                if (!NumberUtil.isNumber(args[0]) || !NumberUtil.isNumber(args[1]) || !NumberUtil.isNumber(args[2])) {
                    this.messages.coordsNotNumber.send(commandSender);
                    return;
                }
                location = new Location(player1.getWorld(), Double.parseDouble(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]));
                break;
            }
        }

        if (location == null) {
            this.messages.localizationNotExist.send(commandSender);
            return;
        }
        final String variableLocation = LocationUtil.locationToString(location);

        player1.teleport(location);
        this.messages.teleported.send(commandSender,
                "{LOCALIZATION}", variableLocation,
                "{WORLD}", location.getWorld().getName(),
                "{NICK}", commandSender instanceof Player ? (Player) commandSender == player1 ? this.rarstAPIMessages.you : player1.getName() : player1.getName()
        );
        this.messages.teleportedInfo.send(player1,
                "{LOCALIZATION}", variableLocation,
                "{WORLD}", location.getWorld().getName()
        );
    }

    @Override
    public List<String> onTabComplete(final CommandSender commandSender, final String alias, final String[] args) throws IllegalArgumentException {
        if(PermissionUtil.hasPermission(commandSender, this.permission))
        switch (args.length) {
            case 1: {
                return this.userManager.playersThatCanInteract(commandSender).stream().map(Player::getName).collect(Collectors.toList());
            }
            case 2: {
                if(!PermissionUtil.hasPermission(commandSender, this.permission + ".other")) {
                    break;
                }
                return this.userManager.playersThatCanInteract(commandSender).stream().map(Player::getName).collect(Collectors.toList());
            }
            case 4: {
                if(!PermissionUtil.hasPermission(commandSender, this.permission + ".other")) {
                    break;
                }
                return this.userManager.playersThatCanInteract(commandSender).stream().map(Player::getName).collect(Collectors.toList());
            }
        }
        return new ArrayList<>();
    }

}
