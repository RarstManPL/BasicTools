package me.rarstman.basictools.command.admin;

import com.google.common.collect.ImmutableList;
import me.rarstman.basictools.command.Command;
import me.rarstman.basictools.configuration.Configuration;
import me.rarstman.basictools.util.ChatUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class GameModeCommand extends Command {

    public GameModeCommand(final Configuration.BasicCommand basicCommand) {
        super(basicCommand, false);
    }

    @Override
    public void onExecute(final CommandSender commandSender, final String[] args) {
        if (args.length < 1) {
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("BadUsage"), "{usage}", this.usageMessage);
            return;
        }
        final String permission = args.length == 1 ? null : this.permission + ".other";

        if (!this.vaultHook.hasPermission(commandSender, permission)) {
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("NoPermission"), "{permission}", permission);
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
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("GameModeNotExist"));
            return;
        }
        final Player player1 = !(commandSender instanceof Player) && args.length < 1 ? null : args.length > 0 ? this.userManager.getUser(args[1]).isPresent() ? this.userManager.getUser(args[1]).get().getOfflinePlayer().isOnline() ? this.userManager.getUser(args[1]).get().getPlayer() : null : null : (Player) commandSender;

        if (player1 == null) {
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("PlayerNotExistOrConsole"));
            return;
        }
        final String variableGameMode = this.messages.getMessage(StringUtils.capitalize(gameMode.toString()));
        player1.setGameMode(gameMode);
        ChatUtil.sendMessage(commandSender, this.messages.getMessage("GameModeChanged"),
                "{nick}", player1.getName(),
                "{gamemode}", variableGameMode
        );
        ChatUtil.sendMessage(player1, this.messages.getMessage("GameModeChangedInfo"), "{gamemode}", variableGameMode);
    }

    @Override
    public List<String> tabComplete(final CommandSender commandSender, final String alias, final String[] args) throws IllegalArgumentException {
        switch (args.length) {
            case 1: {
                return ImmutableList.of("survival", "creative", "adventure", "spectator");
            }
            case 2: {
                return ImmutableList.of(this.vaultHook.hasPermission(commandSender, this.permission + ".other") ? this.userManager.playersThatCanInteract(commandSender).stream().map(Player::getName).collect(Collectors.joining()) : null);
            }
        }
        return ImmutableList.of();
    }
}
