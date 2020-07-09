package me.rarstman.basictools.command.admin;

import me.rarstman.basictools.BasicToolsPlugin;
import me.rarstman.basictools.configuration.BasicToolsCommands;
import me.rarstman.basictools.configuration.BasicToolsMessages;
import me.rarstman.basictools.data.UserManager;
import me.rarstman.rarstapi.command.CommandProvider;
import me.rarstman.rarstapi.configuration.ConfigManager;
import me.rarstman.rarstapi.util.PermissionUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ClearInventoryCommand extends CommandProvider {

    private final BasicToolsMessages messages;
    private final UserManager userManager;

    public ClearInventoryCommand() {
        super(ConfigManager.getConfig(BasicToolsCommands.class).clearInventoryCommandData, "basictools.command.clearinventory", false);

        this.messages = ConfigManager.getConfig(BasicToolsMessages.class);
        this.userManager = BasicToolsPlugin.getPlugin().getUserManager();
    }

    @Override
    public void onExecute(final CommandSender commandSender, final String[] args) {
        if(args.length < 1){
            this.rarstAPIMessages.badUsage.send(commandSender, "{USAGE}", this.usageMessage);
            return;
        }
        final String permission = args.length < 1 ? null : this.permission + ".other";

        if (!PermissionUtil.hasPermission(commandSender, permission)) {
            this.rarstAPIMessages.noPermission.send(commandSender, "{PERMISSION}", permission);
            return;
        }
        final String permission1 = Arrays.asList("enderchest", "inventory").stream().anyMatch(inventoryType -> args[0].equalsIgnoreCase(inventoryType)) ? this.permission + "." + args[0].toLowerCase() : null;

        if (!PermissionUtil.hasPermission(commandSender, permission1)) {
            this.rarstAPIMessages.noPermission.send(commandSender, "{PERMISSION}", permission1);
            return;
        }
        final Player player1 = !(commandSender instanceof Player) && args.length < 2 ? null : args.length > 1 ? this.userManager.getUser(args[1]).isPresent() ? this.userManager.getUser(args[1]).get().getPlayer() : null : (Player) commandSender;

        if (player1 == null || !this.userManager.canInteractPlayer(commandSender, player1)) {
            this.rarstAPIMessages.playerNotExistOrConsole.send(commandSender);
            return;
        }
        Inventory inventory = null;

        switch (args[0].toLowerCase()) {
            case "enderchest": {
                inventory = player1.getEnderChest();
                break;
            }
            case "inventory": {
                inventory = player1.getInventory();
                break;
            }
        }

        if (inventory == null) {
            this.messages.inventoryNotExist.send(commandSender);
            return;
        }
        final String variableInventory = inventory.getType() == InventoryType.ENDER_CHEST ? this.messages.enderChest : this.messages.inventory;

        inventory.clear();
        this.messages.inventoryCleared.send(commandSender,
                "{NICK}", player1.getName(),
                "{INVENTORY}", variableInventory
        );
        this.messages.inventoryClearedInfo.send(player1, "{INVENTORY}", variableInventory);
    }

    @Override
    public List<String> onTabComplete(final CommandSender commandSender, final String alias, final String[] args) throws IllegalArgumentException {
        switch (args.length) {
            case 1: {
                final List<String> list = new ArrayList<>();

                if(PermissionUtil.hasPermission(commandSender, this.permission + ".inventory")) {
                    list.add("inventory");
                }

                if(PermissionUtil.hasPermission(commandSender, this.permission + ".enderchest")) {
                    list.add("enderchest");
                }
                return list;
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
