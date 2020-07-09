package me.rarstman.basictools.listener;

import me.rarstman.basictools.BasicToolsPlugin;
import me.rarstman.basictools.data.UserManager;
import me.rarstman.rarstapi.command.CommandManager;
import me.rarstman.rarstapi.listener.ListenerProvider;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.server.TabCompleteEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TabCompleteListener extends ListenerProvider {

    private final UserManager userManager;
    private final BasicToolsPlugin plugin;

    public TabCompleteListener() {
        this.plugin = BasicToolsPlugin.getPlugin();
        this.userManager = this.plugin.getUserManager();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTabComplete(final TabCompleteEvent event) {
        if(event.getCompletions().isEmpty() || CommandManager.isCommandByJavaPlugin(this.plugin, event.getBuffer().split(" ")[0])) {
            return;
        }
        final List<String> completions = new ArrayList<>(event.getCompletions());

        completions.removeAll(this.userManager.playersThatCantInteract(event.getSender()).stream().map(Player::getName).collect(Collectors.toList()));
        event.setCompletions(completions);
    }

}
