package me.rarstman.basictools.command.inventory;

import me.rarstman.basictools.BasicToolsPlugin;
import me.rarstman.basictools.configuration.BasicToolsCommands;
import me.rarstman.basictools.configuration.BasicToolsMessages;
import me.rarstman.basictools.data.User;
import me.rarstman.basictools.data.UserManager;
import me.rarstman.rarstapi.command.CommandProvider;
import me.rarstman.rarstapi.configuration.ConfigManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class PanelCommand extends CommandProvider {

    private final UserManager userManager;
    private final BasicToolsMessages messages;

    public PanelCommand() {
        super(ConfigManager.getConfig(BasicToolsCommands.class).panelCommandData, "basictools.command.panel", true);

        this.userManager = BasicToolsPlugin.getPlugin().getUserManager();
        this.messages = ConfigManager.getConfig(BasicToolsMessages.class);
    }

    @Override
    public void onExecute(final CommandSender commandSender, final String[] args) {
        final Player player = (Player) commandSender;
        final User user = this.userManager.getUser(player).isPresent() ? this.userManager.getUser(player).get() : null;

        if(user != null && user.getUserCache().openPanelInventory()) {
            return;
        }
        this.messages.cantOpenPanelInventory.send(player);
    }

    @Override
    public List<String> onTabComplete(final CommandSender commandSender, final String alias, final String[] args) throws IllegalArgumentException {
        return null;
    }

}
