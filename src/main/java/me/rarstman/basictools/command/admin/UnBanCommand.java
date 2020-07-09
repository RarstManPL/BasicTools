package me.rarstman.basictools.command.admin;

import me.rarstman.basictools.configuration.BasicToolsCommands;
import me.rarstman.basictools.configuration.BasicToolsMessages;
import me.rarstman.rarstapi.command.CommandProvider;
import me.rarstman.rarstapi.configuration.ConfigManager;
import me.rarstman.rarstapi.util.PermissionUtil;
import me.rarstman.rarstapi.util.RegexUtil;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UnBanCommand extends CommandProvider {

    private final BasicToolsMessages messages;

    public UnBanCommand() {
        super(ConfigManager.getConfig(BasicToolsCommands.class).unBanCommandData, "basictools.command.unban", false);

        this.messages = ConfigManager.getConfig(BasicToolsMessages.class);
    }

    @Override
    public void onExecute(final CommandSender commandSender, final String[] args) {
        if (args.length < 1) {
            this.rarstAPIMessages.badUsage.send(commandSender, "{USAGE}", this.usageMessage);
            return;
        }
        final BanList.Type type = RegexUtil.ipPatternMatch(args[0]) ? BanList.Type.IP : BanList.Type.NAME;
        final String permission = this.permission + "." + type.name().toLowerCase();

        if (!PermissionUtil.hasPermission(commandSender, permission)) {
            this.rarstAPIMessages.noPermission.send(commandSender, "{PERMISSION}", permission);
            return;
        }

        if (!Bukkit.getBanList(type).isBanned(args[0])) {
            this.messages.notBanned.send(commandSender);
            return;
        }
        Bukkit.getBanList(type).pardon(args[0]);
        this.messages.unBanned.send(commandSender);
    }

    @Override
    public List<String> onTabComplete(final CommandSender commandSender, final String alias, final String[] args) throws IllegalArgumentException {
        switch (args.length) {
            case 1: {
                final List<String> list = new ArrayList<>();

                if(PermissionUtil.hasPermission(commandSender, this.permission + ".name")) {
                    list.addAll(Bukkit.getBanList(BanList.Type.NAME).getBanEntries().stream().map(BanEntry::getTarget).collect(Collectors.toList()));
                }

                if(PermissionUtil.hasPermission(commandSender, this.permission + ".ip")) {
                    list.addAll(Bukkit.getBanList(BanList.Type.IP).getBanEntries().stream().map(BanEntry::getTarget).collect(Collectors.toList()));
                }
                return list;
            }
        }
        return new ArrayList<>();
    }

}
