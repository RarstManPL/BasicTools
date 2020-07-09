package me.rarstman.basictools.listener;

import me.rarstman.basictools.BasicToolsPlugin;
import me.rarstman.basictools.data.UserManager;
import me.rarstman.rarstapi.command.CommandManager;
import me.rarstman.rarstapi.listener.ListenerProvider;
import me.rarstman.rarstapi.util.CommandUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerCommandPreprocessListener extends ListenerProvider {

    private final UserManager userManager;
    private final BasicToolsPlugin plugin;

    public PlayerCommandPreprocessListener() {
        this.plugin = BasicToolsPlugin.getPlugin();
        this.userManager = this.plugin.getUserManager();
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCommand(final PlayerCommandPreprocessEvent event) {
        final String[] args = event.getMessage().split(" ");

        if(!CommandUtil.isCommand(args[0]) || CommandManager.isCommandByJavaPlugin(this.plugin, args[0])) {
            return;
        }

        for(int i = 1; i < args.length; i++) {
            if(this.userManager.getUser(args[i]).isPresent() && this.userManager.getUser(args[i]).get().isOnline() && !this.userManager.canInteractPlayer(event.getPlayer(), this.userManager.getUser(args[i]).get().getPlayer())) {
                args[i] = "null";
            }
        }
        event.setMessage(StringUtils.join(args, " "));
    }

}
