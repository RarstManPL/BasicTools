package me.rarstman.basictools.command.admin;

import me.rarstman.basictools.configuration.BasicToolsCommands;
import me.rarstman.basictools.configuration.BasicToolsMessages;
import me.rarstman.rarstapi.command.CommandProvider;
import me.rarstman.rarstapi.configuration.ConfigManager;
import me.rarstman.rarstapi.util.LocationUtil;
import me.rarstman.rarstapi.util.MinecraftUtil;
import me.rarstman.rarstapi.util.PermissionUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class TimeCommand extends CommandProvider {

    private final BasicToolsMessages messages;

    public TimeCommand() {
        super(ConfigManager.getConfig(BasicToolsCommands.class).timeCommandData, "basictools.command.time", false);

        this.messages = ConfigManager.getConfig(BasicToolsMessages.class);
    }

    @Override
    public void onExecute(final CommandSender commandSender, final String[] args) {
        if (args.length < 1) {
            this.rarstAPIMessages.badUsage.send(commandSender, "{USAGE}", this.usageMessage);
            return;
        }
        final String permission = args.length < 2 ? null : args[1].equalsIgnoreCase("*") ? this.permission + ".somewhere.all" : this.permission + ".somewhere";

        if (!PermissionUtil.hasPermission(commandSender, permission)) {
            this.rarstAPIMessages.noPermission.send(commandSender, "{PERMISSION}", permission);
            return;
        }
        final long time = MinecraftUtil.getMinecraftTime(args[0]);

        if (time == -1L) {
            this.messages.timeNotExist.send(commandSender);
            return;
        }
        final Set<World> worlds = !(commandSender instanceof Player) && args.length < 2 ? new HashSet<>() : args.length > 1 ? LocationUtil.getWorlds(args[1]) : new HashSet<>(Arrays.asList(((Player) commandSender).getWorld()));

        if (worlds.isEmpty()) {
            this.messages.worldNotExistOrConsole.send(commandSender);
            return;
        }
        worlds.stream()
                .forEach(world -> world.setTime(time));

        this.messages.timeChanged.send(commandSender,
                "{WORLDS}", worlds.stream().map(World::getName).collect(Collectors.joining(", ")),
                "{TIME}", MinecraftUtil.minecraftHourFromMills(time)
        );
    }

    @Override
    public List<String> onTabComplete(final CommandSender commandSender, final String alias, final String[] args) throws IllegalArgumentException {
        switch (args.length) {
            case 1: {
                return Arrays.asList("daystart", "day", "noon", "miday", "dawn", "sunrise", "morning", "afternoon", "sundown", "nightfall", "dusk", "sunset", "nightstart", "night", "midnight");
            }
            case 2: {
                final List<String> list = new ArrayList<>();

                if(PermissionUtil.hasPermission(commandSender, this.permission + ".somewhere")) {
                    list.addAll(Bukkit.getWorlds().stream().map(World::getName).collect(Collectors.toList()));
                }

                if(PermissionUtil.hasPermission(commandSender, this.permission + ".somewhere.all")) {
                    list.add("*");
                }
                return list;
            }
        }
        return new ArrayList<>();
    }

}
