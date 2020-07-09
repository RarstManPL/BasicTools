package me.rarstman.basictools.command.player;

import me.rarstman.basictools.BasicToolsPlugin;
import me.rarstman.basictools.cache.UserCache;
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

import java.util.List;

public class ReplyCommand extends CommandProvider {

    private final BasicToolsMessages messages;
    private final UserManager userManager;

    public ReplyCommand() {
        super(ConfigManager.getConfig(BasicToolsCommands.class).replyCommandData, "basictools.command.reply", true);

        this.messages = ConfigManager.getConfig(BasicToolsMessages.class);
        this.userManager = BasicToolsPlugin.getPlugin().getUserManager();
    }

    @Override
    public void onExecute(final CommandSender commandSender, final String[] args) {
        if (args.length < 1) {
            this.rarstAPIMessages.badUsage.send(commandSender, "{USAGE}", this.usageMessage);
            return;
        }
        final Player player = (Player) commandSender;
        final UserCache userCache = this.userManager.getUser(player).isPresent() ? this.userManager.getUser(player).get().getUserCache() : null;

        if(userCache == null | !userCache.getLastWriter().isPresent()) {
            this.messages.replyUserNotExist.send(player);
            return;
        }
        final User user1 = userCache.getLastWriter().get().isOnline() ? userCache.getLastWriter().get() : null;

        if(user1 == null || !this.userManager.canInteractPlayer(player, user1.getPlayer())) {
            this.rarstAPIMessages.playerNotExist.send(player);
            return;
        }

        if(user1.getUserCache().isBlockMessages() && !PermissionUtil.hasPermission(player, "basictools.command.messagetoggle.bypass")) {
            this.messages.playerDontReciveMessages.send(player);
            return;
        }
        user1.getUserCache().setLastWriter(userCache.getUser());
        this.userManager.sendPrivateMessage(player, user1.getPlayer(), StringUtils.join(args, " "));
    }

    @Override
    public List<String> onTabComplete(final CommandSender commandSender, final String alias, final String[] args) throws IllegalArgumentException {
        return null;
    }

}
