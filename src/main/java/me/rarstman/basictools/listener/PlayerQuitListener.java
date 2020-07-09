package me.rarstman.basictools.listener;

import me.rarstman.basictools.BasicToolsPlugin;
import me.rarstman.basictools.cache.UserCache;
import me.rarstman.basictools.data.UserManager;
import me.rarstman.rarstapi.listener.ListenerProvider;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener extends ListenerProvider {

    private final UserManager userManager;

    public PlayerQuitListener() {
        this.userManager = BasicToolsPlugin.getPlugin().getUserManager();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        player.getInventory().getViewers()
                .stream()
                .map(viewer -> (Player) viewer)
                .forEach(Player::closeInventory);

        final UserCache userCache = this.userManager.getUser(player).isPresent() ? this.userManager.getUser(player).get().getUserCache() : null;

        if(userCache == null) {
            return;
        }
        userCache.setVanished(false);

        this.userManager.updateDynamicInformations(player);
        this.userManager.showPlayer(player);
    }

}
