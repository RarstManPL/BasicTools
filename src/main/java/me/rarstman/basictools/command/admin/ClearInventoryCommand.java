package me.rarstman.basictools.command.admin;

import com.google.common.base.CaseFormat;
import com.google.common.collect.ImmutableList;
import me.rarstman.basictools.command.Command;
import me.rarstman.basictools.configuration.Configuration;
import me.rarstman.basictools.util.ChatUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ClearInventoryCommand extends Command {

    public ClearInventoryCommand(final Configuration.BasicCommand basicCommand) {
        super(basicCommand, false);
    }

    @Override
    public void onExecute(final CommandSender commandSender, final String[] args) {
        if(args.length < 1){
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("BadUsage"), "{usage}", this.usageMessage);
            return;
        }
        final String permission = args.length < 1 ? null : this.permission + ".other";

        if (!this.vaultHook.hasPermission(commandSender, permission)) {
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("NoPermission"), "{permission}", permission);
            return;
        }
        final String permission1 = Arrays.asList("enderchest", "inventory").stream().filter(inventoryType -> args[0].equalsIgnoreCase(inventoryType)).findAny().isPresent() ? this.permission + "." + args[0].toLowerCase() : null;

        if (!this.vaultHook.hasPermission(commandSender, permission1)) {
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("NoPermission"), "{permission}", permission1);
            return;
        }
        final Player player1 = !(commandSender instanceof Player) && args.length < 2 ? null : args.length > 1 ? this.userManager.getUser(args[1]).isPresent() ? this.userManager.getUser(args[1]).get().getOfflinePlayer().isOnline() ? this.userManager.getUser(args[1]).get().getPlayer() : null : null : (Player) commandSender;

        if (player1 == null) {
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("PlayerNotExistOrConsole"));
            return;
        }
        Inventory inventory = null;

        switch (args[0]) {
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
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("InventoryNotExist"));
            return;
        }
        inventory.clear();
        ChatUtil.sendMessage(commandSender, this.messages.getMessage("InventoryCleared"),
                "{nick}", player1.getName(),
                "{inventory}", this.messages.getMessage(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, inventory.getType().toString()))
        );
        ChatUtil.sendMessage(player1, this.messages.getMessage("InventoryClearedInfo"), "{inventory}", this.messages.getMessage(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, inventory.getType().toString())));
    }

    @Override
    public List<String> tabComplete(final CommandSender commandSender, final String alias, final String[] args) throws IllegalArgumentException {
        switch (args.length) {
            case 1: {
                return ImmutableList.of(this.vaultHook.hasPermission(commandSender, this.permission + ".other") ? this.userManager.playersThatCanInteract(commandSender).stream().map(Player::getName).collect(Collectors.joining()) : null);
            }
        }
        return ImmutableList.of();
    }
}
