package me.rarstman.basictools.listener;

import me.rarstman.basictools.BasicToolsPlugin;
import me.rarstman.basictools.data.User;
import me.rarstman.basictools.data.UserManager;
import me.rarstman.rarstapi.listener.ListenerProvider;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamageListener extends ListenerProvider {

    private final UserManager userManager;

    public PlayerDamageListener() {
        this.userManager = BasicToolsPlugin.getPlugin().getUserManager();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(final EntityDamageEvent event) {
        if(event.getEntityType() != EntityType.PLAYER) {
            return;
        }
        final Player player = (Player) event.getEntity();
        final User user = this.userManager.getUser(player).isPresent() ? this.userManager.getUser(player).get() : null;

        if(user == null || !user.getUserCache().isGoded()) {
            return;
        }
        event.setDamage(0.0);
        event.setCancelled(true);
    }

}
