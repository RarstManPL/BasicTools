package me.rarstman.basictools.command.admin;

import com.google.common.base.CaseFormat;
import com.google.common.collect.ImmutableList;
import me.rarstman.basictools.command.Command;
import me.rarstman.basictools.configuration.Configuration;
import me.rarstman.basictools.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class InventorySeeCommand extends Command {

    public InventorySeeCommand(final Configuration.BasicCommand basicCommand) {
        super(basicCommand, true);
    }

    @Override
    public void onExecute(final CommandSender commandSender, final String[] args) {
        final Player player = (Player) commandSender;

        if (args.length < 1) {
            ChatUtil.sendMessage(player, this.messages.getMessage("BadUsage"), "{usage}", this.usageMessage);
            return;
        }
        final String permission = args.length < 2 ? null : this.permission + ".other";

        if (!this.vaultHook.hasPermission(player, permission)) {
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("NoPermission"), "{permission}", permission);
            return;
        }
        final String permission1 = Arrays.asList("enderchest", "inventory").stream().filter(inventoryType -> args[0].equalsIgnoreCase(inventoryType)).findAny().isPresent() ? this.permission + "." + args[0].toLowerCase() : null;

        if (!this.vaultHook.hasPermission(player, permission1)) {
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("NoPermission"), "{permission}", permission1);
            return;
        }
        final Player player1 = args.length > 1 ? this.userManager.getUser(args[1]).isPresent() ? this.userManager.getUser(args[1]).get().getOfflinePlayer().isOnline() ? this.userManager.getUser(args[1]).get().getPlayer() : null : null : player;

        if (player1 == null) {
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("PlayerNotExist"));
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
            ChatUtil.sendMessage(player, this.messages.getMessage("InventoryNotExist"));
            return;
        }
        player.openInventory(inventory);
        ChatUtil.sendMessage(player, this.messages.getMessage("InventoryOpened"),
                "{inventory}", this.messages.getMessage(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, inventory.getType().toString())),
                "{nick}", player1.getName());
    }

    @Override
    public List<String> tabComplete(final CommandSender commandSender, final String alias, final String[] args) throws IllegalArgumentException {
        switch (args.length) {
            case 1: {
                return ImmutableList.of(this.vaultHook.hasPermission(commandSender, this.permission + ".enderchest") ? "enderchest" : null, this.vaultHook.hasPermission(commandSender, this.permission + ".inventory") ? "inventory" : null);
            }
            case 2: {
                return ImmutableList.of(this.vaultHook.hasPermission(commandSender, this.permission + ".other") ? Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.joining()) : null);
            }
        }
        return ImmutableList.of();
    }
}
