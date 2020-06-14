package me.rarstman.basictools.command.admin;

import com.google.common.collect.ImmutableList;
import me.rarstman.basictools.command.Command;
import me.rarstman.basictools.configuration.Configuration;
import me.rarstman.basictools.util.ChatUtil;
import me.rarstman.basictools.util.LocationUtil;
import me.rarstman.basictools.util.MinecraftCycleUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TimeCommand extends Command {

    public TimeCommand(final Configuration.BasicCommand basicCommand) {
        super(basicCommand, false);
    }

    @Override
    public void onExecute(final CommandSender commandSender, final String[] args) {
        if (args.length < 1) {
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("BadUsage"), "{usage}", this.usageMessage);
            return;
        }
        final String permission = args.length > 1 ? this.permission + ".info" : args[1].equalsIgnoreCase("*") ? this.permission + ".somewhere.all" : this.permission + ".somewhere";

        if (!this.vaultHook.hasPermission(commandSender, permission)) {
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("NoPermission"), "{permission}", permission);
            return;
        }
        final Long time = MinecraftCycleUtil.getMinecraftTime(args[0]);

        if (time == null) {
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("TimeNotExist"));
            return;
        }
        final Set<World> worlds = !(commandSender instanceof Player) && args.length < 2 ? new HashSet<>() : args.length > 1 ? LocationUtil.getWorlds(args[1]) : new HashSet<>(Arrays.asList(((Player) commandSender).getWorld()));

        if (worlds.isEmpty()) {
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("WorldNotExistOrConsole"));
            return;
        }
        worlds.stream()
                .forEach(world -> world.setTime(time));
        ChatUtil.sendMessage(commandSender, this.messages.getMessage("TimeChanged"),
                "{worlds}", StringUtils.join(worlds, ", "),
                "{time}", MinecraftCycleUtil.minecraftHourFromMills(time)
        );
    }

    @Override
    public List<String> tabComplete(final CommandSender commandSender, final String alias, final String[] args) throws IllegalArgumentException {
        switch (args.length) {
            case 1: {
                return ImmutableList.of("daystart", "day", "noon", "miday", "dawn", "sunrise", "morning", "afternoon", "sundown", "nightfall", "dusk", "sunset", "nightstart", "night", "midnight");
            }
            case 2: {
                return ImmutableList.of(this.vaultHook.hasPermission(commandSender, this.permission + ".somewhere") ? Bukkit.getWorlds().stream().map(World::getName).collect(Collectors.joining()) : null, this.vaultHook.hasPermission(commandSender, this.permission + ".somewhere.all") ? "*" : null);
            }
        }
        return ImmutableList.of();
    }
}
