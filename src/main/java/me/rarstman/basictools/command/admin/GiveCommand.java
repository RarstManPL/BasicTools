package me.rarstman.basictools.command.admin;

import com.google.common.collect.ImmutableList;
import me.rarstman.basictools.command.Command;
import me.rarstman.basictools.configuration.Configuration;
import me.rarstman.basictools.util.ChatUtil;
import me.rarstman.basictools.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GiveCommand extends Command {

    public GiveCommand(final Configuration.BasicCommand basicCommand) {
        super(basicCommand, false);
    }

    @Override
    public void onExecute(final CommandSender commandSender, final String[] args) {
        if (args.length < 2) {
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("BadUsage"), "{usage}", this.usageMessage);
            return;
        }
        final Player player1 = this.userManager.getUser(args[0]).isPresent() ? this.userManager.getUser(args[0]).get().getPlayer().isOnline() ? this.userManager.getUser(args[0]).get().getPlayer() : null : null;

        if (player1 == null) {
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("PlayerNotExist"));
            return;
        }
        final String permission = !(commandSender instanceof Player) || (commandSender instanceof Player && commandSender == player1) ? this.permission + ".other" : this.permission + ".self";

        if (!this.vaultHook.hasPermission(commandSender, permission)) {
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("NoPermission"), "{permission}", permission);
            return;
        }
        final ItemBuilder itemBuilder = new ItemBuilder(Arrays.copyOfRange(args, 1, args.length));
        final String variableAmount = String.valueOf(itemBuilder.getAmount());
        final String variableMaterial = itemBuilder.getMaterial().toString();
        player1.getInventory().addItem(itemBuilder.build());
        ChatUtil.sendMessage(commandSender, this.messages.getMessage("Gived"),
                "{nick}", player1.getName(),
                "{amount}", variableAmount,
                "{item}", variableMaterial
        );
        ChatUtil.sendMessage(player1, this.messages.getMessage("Gived"),
                "{amount}", variableAmount,
                "{item}", variableMaterial
        );
    }

    @Override
    public List<String> tabComplete(final CommandSender sender, final String alias, final String[] args) throws IllegalArgumentException {
        switch (args.length) {
            case 0: {
                break;
            }
            case 1: {
                return ImmutableList.of(Arrays.stream(Material.values()).map(Material::name).collect(Collectors.joining()));
            }
            case 2: {
                return ImmutableList.of("~0");
            }
            default: {
                return ImmutableList.of("name:", "enchant:", "lore:", "glowing:", "unbreakable:", "owner:");
            }
        }
        return ImmutableList.of();
    }
}
