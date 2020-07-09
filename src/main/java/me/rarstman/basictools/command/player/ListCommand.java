package me.rarstman.basictools.command.player;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import me.rarstman.basictools.BasicToolsPlugin;
import me.rarstman.basictools.configuration.BasicToolsCommands;
import me.rarstman.basictools.configuration.BasicToolsConfig;
import me.rarstman.basictools.configuration.BasicToolsMessages;
import me.rarstman.basictools.data.UserManager;
import me.rarstman.rarstapi.command.CommandProvider;
import me.rarstman.rarstapi.configuration.ConfigManager;
import me.rarstman.rarstapi.task.impl.AsynchronouslyTask;
import me.rarstman.rarstapi.util.PermissionUtil;
import me.rarstman.rarstapi.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ListCommand extends CommandProvider {

    private final BasicToolsMessages messages;
    private final BasicToolsConfig config;
    private final UserManager userManager;

    public ListCommand() {
        super(ConfigManager.getConfig(BasicToolsCommands.class).listCommandData, "basictools.command.list", false);

        this.messages = ConfigManager.getConfig(BasicToolsMessages.class);
        this.config = ConfigManager.getConfig(BasicToolsConfig.class);
        this.userManager = BasicToolsPlugin.getPlugin().getUserManager();
    }

    @Override
    public void onExecute(final CommandSender commandSender, final String[] args) {
        new AsynchronouslyTask() {
            @Override
            public void onDisable() {
            }

            @Override
            public void onExecute() {
                final Map<String, String> groupsInCommandList = config.getParsedGroupsInListCommand();
                final Multimap<String, String> onlineGroups = ArrayListMultimap.create();
                final StringBuilder onlineGroupsStringBuilder = new StringBuilder();

                Bukkit.getOnlinePlayers()
                        .stream()
                        .filter(player -> groupsInCommandList.containsKey(PermissionUtil.getPrimaryGroup(player)) && userManager.canInteractPlayer(commandSender, player))
                        .forEach(player -> onlineGroups.put(PermissionUtil.getPrimaryGroup(player), player.getName()));

                groupsInCommandList
                        .entrySet()
                        .stream()
                        .filter(entrySet -> onlineGroups.containsKey(entrySet.getKey()))
                        .forEach(entrySet -> {
                            onlineGroupsStringBuilder.append(StringUtil.replace(messages.groupsOnlineFormat, "{GROUP}", entrySet.getValue(), "{PLAYERS}", onlineGroups.get(entrySet.getKey()).stream().sorted(Comparator.comparing(n-> n.toString())).collect(Collectors.joining(", "))) + "\n");
                        });

                messages.listFormat.send(commandSender, "{ONLINE}", String.valueOf(Bukkit.getOfflinePlayers().length - userManager.playersThatCantInteract(commandSender).size()), "{GROUPSONLINE}", onlineGroupsStringBuilder.toString().length() < 1 ? rarstAPIMessages.lack : onlineGroupsStringBuilder.toString().substring(0, onlineGroupsStringBuilder.toString().length() - 1));
            }
        }
        .once()
        .register();
    }

    @Override
    public List<String> onTabComplete(final CommandSender commandSender, final String alias, final String[] args) throws IllegalArgumentException {
        return null;
    }

}
