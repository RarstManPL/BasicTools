package me.rarstman.basictools.listener;

import me.rarstman.basictools.BasicToolsPlugin;
import me.rarstman.basictools.data.User;
import me.rarstman.basictools.data.UserManager;
import me.rarstman.rarstapi.listener.ListenerProvider;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener extends ListenerProvider {

    private final UserManager userManager;

    public PlayerJoinListener() {
        this.userManager = BasicToolsPlugin.getPlugin().getUserManager();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        if(!this.userManager.getUser(player).isPresent()) {
            this.userManager.createUser(player);
        }

        this.userManager.updateDynamicInformations(player);
        this.userManager.hideVanishedPlayers(player);
        this.userManager.updatePanelInventory(player);
    }

}
