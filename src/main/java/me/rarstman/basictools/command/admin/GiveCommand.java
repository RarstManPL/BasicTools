package me.rarstman.basictools.command.admin;

import me.rarstman.basictools.BasicToolsPlugin;
import me.rarstman.basictools.configuration.BasicToolsCommands;
import me.rarstman.basictools.configuration.BasicToolsMessages;
import me.rarstman.basictools.data.UserManager;
import me.rarstman.rarstapi.command.CommandProvider;
import me.rarstman.rarstapi.configuration.ConfigManager;
import me.rarstman.rarstapi.item.ItemBuilder;
import me.rarstman.rarstapi.util.PermissionUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GiveCommand extends CommandProvider {

    private final BasicToolsMessages messages;
    private final UserManager userManager;

    public GiveCommand() {
        super(ConfigManager.getConfig(BasicToolsCommands.class).giveCommandData, "basictools.command.give", false);

        this.messages = ConfigManager.getConfig(BasicToolsMessages.class);
        this.userManager = BasicToolsPlugin.getPlugin().getUserManager();
    }

    @Override
    public void onExecute(final CommandSender commandSender, final String[] args) {
        if (args.length < 2) {
            this.rarstAPIMessages.badUsage.send(commandSender, "{USAGE}", this.usageMessage);
            return;
        }
        final Player player1 = this.userManager.getUser(args[0]).isPresent() ? this.userManager.getUser(args[0]).get().getPlayer() : null;

        if (player1 == null || !this.userManager.canInteractPlayer(commandSender, player1)) {
            this.rarstAPIMessages.playerNotExist.send(commandSender);
            return;
        }
        final String permission = !(commandSender instanceof Player) || (commandSender instanceof Player && commandSender == player1) ? this.permission + ".other" : this.permission + ".self";

        if (!PermissionUtil.hasPermission(commandSender, permission)) {
            this.rarstAPIMessages.noPermission.send(commandSender, "{PERMISSION}", permission);
            return;
        }
        final ItemStack itemStack = new ItemBuilder(StringUtils.join(Arrays.copyOfRange(args, 1, args.length), " ")).build();
        final String variableAmount = String.valueOf(itemStack.getAmount());
        final String variableMaterial = itemStack.getType().name();

        player1.getInventory().addItem(itemStack);
        this.messages.gave.send(commandSender,
                "{NICK}", player1.getName(),
                "{AMOUNT}", variableAmount,
                "{ITEM}", variableMaterial
        );
        this.messages.gaveInfo.send(player1,
                "{AMOUNT}", variableAmount,
                "{ITEM}", variableMaterial
        );
    }

    @Override
    public List<String> onTabComplete(final CommandSender commandSender, final String alias, final String[] args) throws IllegalArgumentException {
        switch (args.length) {
            case 0:
            case 3: {
                break;
            }
            case 1: {
                if(!PermissionUtil.hasPermission(commandSender, this.permission + ".other")) {
                    break;
                }
                return this.userManager.playersThatCanInteract(commandSender).stream().map(Player::getName).collect(Collectors.toList());
            }
            case 2: {
                return Arrays.stream(Material.values()).map(Material::name).collect(Collectors.toList());
            }
            default: {
                return Arrays.asList("name:", "owner:", "enchants:", "potions:", "itemflags:", "glowing:", "unbreakable:");
            }
        }
        return new ArrayList<>();
    }

}
