package me.rarstman.basictools.command.admin;

import me.rarstman.basictools.BasicToolsPlugin;
import me.rarstman.basictools.configuration.BasicToolsCommands;
import me.rarstman.basictools.configuration.BasicToolsMessages;
import me.rarstman.basictools.data.UserManager;
import me.rarstman.rarstapi.command.CommandProvider;
import me.rarstman.rarstapi.configuration.ConfigManager;
import me.rarstman.rarstapi.util.BooleanUtil;
import me.rarstman.rarstapi.util.PermissionUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FlyCommand extends CommandProvider {

    private final BasicToolsMessages messages;
    private final UserManager userManager;

    public FlyCommand() {
        super(ConfigManager.getConfig(BasicToolsCommands.class).flyCommandData, "basictools.command.fly", false);

        this.messages = ConfigManager.getConfig(BasicToolsMessages.class);
        this.userManager = BasicToolsPlugin.getPlugin().getUserManager();
    }

    @Override
    public void onExecute(final CommandSender commandSender, String[] args) {
        final String permission = args.length < 2 ? null : this.permission + ".other";

        if (!PermissionUtil.hasPermission(commandSender, permission)) {
            this.rarstAPIMessages.noPermission.send(commandSender, "{PERMISSION}", permission);
            return;
        }
        final Player player1 = !(commandSender instanceof Player) && args.length < 2 ? null : args.length > 1 ? this.userManager.getUser(args[1]).isPresent() ? this.userManager.getUser(args[1]).get().getPlayer() : null : (Player) commandSender;

        if (player1 == null || !this.userManager.canInteractPlayer(commandSender, player1)) {
            this.rarstAPIMessages.playerNotExistOrConsole.send(commandSender);
            return;
        }

        if(args.length > 0 && !BooleanUtil.isStringStatus(args[0])) {
            this.rarstAPIMessages.turnOptionNotExist.send(commandSender);
            return;
        }
        final boolean isFlying = args.length > 0 ? BooleanUtil.stringStatusToBoolean(args[0]) : !player1.isFlying();
        final String variableStatus = isFlying ? this.rarstAPIMessages.on_ : this.rarstAPIMessages.off_;

        player1.setAllowFlight(isFlying);
        player1.setFlying(isFlying);
        this.messages.flyStatusChanged.send(commandSender,
                "{STATUS}", variableStatus,
                "{NICK}", commandSender instanceof Player ? (Player) commandSender == player1 ? this.rarstAPIMessages.you : player1.getName() : player1.getName()
        );
        this.messages.flyStatusChangedInfo.send(player1,"{STATUS}", variableStatus);
    }

    @Override
    public List<String> onTabComplete(final CommandSender commandSender, final String alias, final String[] args) throws IllegalArgumentException {
        switch (args.length) {
            case 1: {
                return Arrays.asList("on", "off");
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
