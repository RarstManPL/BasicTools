package me.rarstman.basictools.command.player;

import me.rarstman.basictools.BasicToolsPlugin;
import me.rarstman.basictools.configuration.BasicToolsCommands;
import me.rarstman.basictools.configuration.BasicToolsMessages;
import me.rarstman.basictools.data.User;
import me.rarstman.basictools.data.UserManager;
import me.rarstman.rarstapi.command.CommandProvider;
import me.rarstman.rarstapi.configuration.ConfigManager;
import me.rarstman.rarstapi.util.PermissionUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MessageCommand extends CommandProvider {

    private final BasicToolsMessages messages;
    private final UserManager userManager;

    public MessageCommand() {
        super(ConfigManager.getConfig(BasicToolsCommands.class).messageCommandData, "basictools.command.message", true);

        this.messages = ConfigManager.getConfig(BasicToolsMessages.class);
        this.userManager = BasicToolsPlugin.getPlugin().getUserManager();
    }

    @Override
    public void onExecute(final CommandSender commandSender, final String[] args) {
        final Player player = (Player) commandSender;

        if (args.length < 2) {
            this.rarstAPIMessages.badUsage.send(player, "{USAGE}", this.usageMessage);
            return;
        }
        final User user = this.userManager.getUser(player).isPresent() ? this.userManager.getUser(player).get() : null;
        final User user1 = this.userManager.getUser(args[0]).isPresent() ? this.userManager.getUser(args[0]).get().isOnline() ? this.userManager.getUser(args[0]).get() : null : null;

        if(user == null || user1 == null || !this.userManager.canInteractPlayer(player, user1.getPlayer())) {
            this.rarstAPIMessages.playerNotExist.send(player);
            return;
        }

        if(user1.getUserCache().isBlockMessages() && !PermissionUtil.hasPermission(player, "basictools.command.messagetoggle.bypass")) {
            this.messages.playerDontReciveMessages.send(player);
            return;
        }
        user.getUserCache().setLastWriter(user1);
        user1.getUserCache().setLastWriter(user);
        this.userManager.sendPrivateMessage(player, user1.getPlayer(), StringUtils.join(args, " "));
    }

    @Override
    public List<String> onTabComplete(final CommandSender commandSender, final String alias, final String[] args) throws IllegalArgumentException {
        switch (args.length) {
            case 1: {
                return this.userManager.playersThatCanInteract(commandSender).stream().map(Player::getName).collect(Collectors.toList());
            }
        }
        return new ArrayList<>();
    }

}
