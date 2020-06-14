package me.rarstman.basictools.command.admin;

import com.google.common.collect.ImmutableList;
import me.rarstman.basictools.command.Command;
import me.rarstman.basictools.configuration.Configuration;
import me.rarstman.basictools.util.ChatUtil;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class UnBanCommand extends Command {

    private final Pattern ipPattern;

    public UnBanCommand(final Configuration.BasicCommand basicCommand) {
        super(basicCommand, false);

        this.ipPattern = Pattern.compile("^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");
    }

    @Override
    public void onExecute(final CommandSender commandSender, final String[] args) {
        if (args.length < 1) {
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("BadUsage"), "{usage}", this.usageMessage);
            return;
        }
        final BanList.Type type = ipPattern.matcher(args[0]).matches() ? BanList.Type.IP : BanList.Type.NAME;
        final String permission = this.permission + "." + type.name().toLowerCase();

        if (!this.vaultHook.hasPermission(commandSender, permission)) {
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("NoPermission"), "{permission}", permission);
            return;
        }

        if (!Bukkit.getBanList(type).isBanned(args[0])) {
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("NotBanned"));
            return;
        }
        Bukkit.getBanList(type).pardon(args[0]);
        ChatUtil.sendMessage(commandSender, this.messages.getMessage("UnBanned"));
    }

    @Override
    public List<String> tabComplete(final CommandSender commandSender, final String alias, final String[] args) throws IllegalArgumentException {
        switch (args.length) {
            case 1: {
                return ImmutableList.of(this.vaultHook.hasPermission(commandSender, this.permission + ".name") ? Bukkit.getBanList(BanList.Type.NAME).getBanEntries().stream().map(BanEntry::getTarget).collect(Collectors.joining()) : null, this.vaultHook.hasPermission(commandSender, this.permission + ".ip") ? Bukkit.getBanList(BanList.Type.IP).getBanEntries().stream().map(BanEntry::getTarget).collect(Collectors.joining()) : null);
            }
        }
        return ImmutableList.of();
    }
}
