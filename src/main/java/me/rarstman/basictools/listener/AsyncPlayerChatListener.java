package me.rarstman.basictools.listener;

import me.rarstman.basictools.BasicToolsPlugin;
import me.rarstman.basictools.cache.ChatCache;
import me.rarstman.basictools.configuration.BasicToolsMessages;
import me.rarstman.basictools.data.User;
import me.rarstman.basictools.data.UserManager;
import me.rarstman.rarstapi.configuration.ConfigManager;
import me.rarstman.rarstapi.listener.ListenerProvider;
import me.rarstman.rarstapi.util.DateUtil;
import me.rarstman.rarstapi.util.PermissionUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChatListener extends ListenerProvider {

    private final ChatCache chatCache;
    private final BasicToolsMessages messages;
    private final UserManager userManager;

    public AsyncPlayerChatListener() {
        this.chatCache = BasicToolsPlugin.getPlugin().getChatCache();
        this.messages = ConfigManager.getConfig(BasicToolsMessages.class);
        this.userManager = BasicToolsPlugin.getPlugin().getUserManager();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncChat(final AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();

        if (this.chatCache.isEnabled()) {
            final User user = this.userManager.getUser(player).isPresent() ? this.userManager.getUser(player).get() : null;

            if(user == null) {
                return;
            }
            user.tryUnMute();

            if(!user.isMuted()) {
                return;
            }

            if(PermissionUtil.hasPermission(player, "basictools.command.mute.bypass")) {
                return;
            }
            event.setCancelled(true);
            this.messages.youAreMuted.send(player,
                    "{REASON}", user.getMuteReason(),
                    "{DATE}", DateUtil.formatDateFromMills(user.getMuteDate())
            );
            return;
        }

        if(PermissionUtil.hasPermission(player, "basictools.command.chat.bypass")) {
            return;
        }
        event.setCancelled(true);
        this.messages.chatIsDisabled.send(player);
    }

}
