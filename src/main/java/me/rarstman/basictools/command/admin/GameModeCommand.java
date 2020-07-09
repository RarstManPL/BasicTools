package me.rarstman.basictools.command.admin;

import me.rarstman.basictools.BasicToolsPlugin;
import me.rarstman.basictools.configuration.BasicToolsCommands;
import me.rarstman.basictools.configuration.BasicToolsMessages;
import me.rarstman.basictools.data.UserManager;
import me.rarstman.rarstapi.command.CommandProvider;
import me.rarstman.rarstapi.configuration.ConfigManager;
import me.rarstman.rarstapi.util.PermissionUtil;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GameModeCommand extends CommandProvider {

    private final BasicToolsMessages messages;
    private final UserManager userManager;

    public GameModeCommand() {
        super(ConfigManager.getConfig(BasicToolsCommands.class).gameModeCommandData, "basictools.command.gamemode", false);

        this.messages = ConfigManager.getConfig(BasicToolsMessages.class);
        this.userManager = BasicToolsPlugin.getPlugin().getUserManager();
    }

    @Override
    public void onExecute(final CommandSender commandSender, final String[] args) {
        if (args.length < 1) {
            this.rarstAPIMessages.badUsage.send(commandSender, "{USAGE}", this.usageMessage);
            return;
        }
        final String permission = args.length == 1 ? null : this.permission + ".other";

        if (!PermissionUtil.hasPermission(commandSender, permission)) {
            this.rarstAPIMessages.noPermission.send(commandSender, "{PERMISSION}", permission);
            return;
        }
        GameMode gameMode = null;

        switch (args[0].toLowerCase()) {
            case "0":
            case "survival": {
                gameMode = GameMode.SURVIVAL;
                break;
            }
            case "1":
            case "creative": {
                gameMode = GameMode.CREATIVE;
                break;
            }
            case "2":
            case "adventure": {
                gameMode = GameMode.ADVENTURE;
                break;
            }
            case "3":
            case "spectator": {
                gameMode = GameMode.SPECTATOR;
                break;
            }
        }

        if (gameMode == null) {
            this.messages.gameModeNotExist.send(commandSender);
            return;
        }
        final Player player1 = !(commandSender instanceof Player) && args.length < 2 ? null : args.length > 1 ? this.userManager.getUser(args[1]).isPresent() ? this.userManager.getUser(args[1]).get().getPlayer() : null : (Player) commandSender;

        if (player1 == null || !this.userManager.canInteractPlayer(commandSender, player1)) {
            this.rarstAPIMessages.playerNotExistOrConsole.send(commandSender);
            return;
        }
        final String variableGameMode = this.messages.getFieldValue(gameMode.name().toLowerCase(), String.class);

        player1.setGameMode(gameMode);
        this.messages.gameModeChanged.send(commandSender,
                "{NICK}", player1.getName(),
                "{GAMEMODE}", variableGameMode
        );
        this.messages.gameModeChangedInfo.send(player1, "{GAMEMODE}", variableGameMode);
    }

    @Override
    public List<String> onTabComplete(final CommandSender commandSender, final String alias, final String[] args) throws IllegalArgumentException {
        switch (args.length) {
            case 1: {
                return Arrays.asList("survival", "creative", "adventure", "spectator");
            }
            case 2: {
                if(!PermissionUtil.hasPermission(commandSender, this.permission + ".other")) {
                    break;
                }
                return this.userManager.playersThatCanInteract(commandSender).stream().map(Player::getName).collect(Collectors.toList());
            }
        }
        return new ArrayList<>();
    }

}
