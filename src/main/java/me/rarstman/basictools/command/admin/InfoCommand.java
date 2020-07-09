package me.rarstman.basictools.command.admin;

import me.rarstman.basictools.BasicToolsPlugin;
import me.rarstman.basictools.configuration.BasicToolsCommands;
import me.rarstman.basictools.configuration.BasicToolsMessages;
import me.rarstman.basictools.data.User;
import me.rarstman.basictools.data.UserManager;
import me.rarstman.rarstapi.command.CommandProvider;
import me.rarstman.rarstapi.configuration.ConfigManager;
import me.rarstman.rarstapi.util.DateUtil;
import me.rarstman.rarstapi.util.LocationUtil;
import me.rarstman.rarstapi.util.PermissionUtil;
import me.rarstman.rarstapi.util.RegexUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class InfoCommand extends CommandProvider {

    private final BasicToolsMessages messages;
    private final UserManager userManager;

    public InfoCommand() {
        super(ConfigManager.getConfig(BasicToolsCommands.class).infoCommandData, "basictools.command.info", false);

        this.messages = ConfigManager.getConfig(BasicToolsMessages.class);
        this.userManager = BasicToolsPlugin.getPlugin().getUserManager();
    }

    @Override
    public void onExecute(final CommandSender commandSender, final String[] args) {
        final String permission = args.length < 1 ? null : this.permission + ".other";

        if (!PermissionUtil.hasPermission(commandSender, permission)) {
            this.rarstAPIMessages.noPermission.send(commandSender, "{PERMISSION}", permission);
            return;
        }
        final boolean isIp = RegexUtil.ipPatternMatch(args[0]);
        final String permission1 = isIp ? this.permission + ".ip" : this.permission + ".name";

        if (!PermissionUtil.hasPermission(commandSender, permission1)) {
            this.rarstAPIMessages.noPermission.send(commandSender, "{PERMISSION}", permission1);
            return;
        }

        if (!isIp) {
            final User user1 = !(commandSender instanceof Player) && args.length < 1 ? null : args.length > 0 ? this.userManager.getUser(args[0]).isPresent() ? this.userManager.getUser(args[0]).get() : null : this.userManager.getUser((Player) commandSender).isPresent() ? this.userManager.getUser((Player) commandSender).get() : null;

            if (user1 == null) {
                this.rarstAPIMessages.playerNotExistOrConsole.send(commandSender);
                return;
            }
            final OfflinePlayer offlinePlayer1 = user1.getOfflinePlayer();

            this.messages.playerInfo.send(commandSender,
                    "{UUID}", user1.getUUID().toString(),
                    "{NICK}", user1.getName(),
                    "{FIRSTIP}", (PermissionUtil.hasPermission(commandSender, this.permission + ".see.ip") ? "" : "&k") + user1.getFirstIP().getHostAddress(),
                    "{LASTIP}", (PermissionUtil.hasPermission(commandSender, this.permission + ".see.ip") ? "" : "&k") + user1.getLastIP().getHostAddress(),
                    "{FIRSTPLAY}", DateUtil.formatDateFromMills(user1.getFirstPlay()),
                    "{LASTPLAY}", DateUtil.formatDateFromMills(user1.getLastPlay()),
                    "{ISFLYING}", offlinePlayer1.isOnline() ? offlinePlayer1.getPlayer().isFlying() ? this.rarstAPIMessages.true_ : this.rarstAPIMessages.false_ : this.rarstAPIMessages.lackInformation,
                    "{ISGODED}", user1.getUserCache().isGoded() ? this.rarstAPIMessages.true_ : this.rarstAPIMessages.false_,
                    "{ISVANISHED}", user1.getUserCache().isVanished() ? this.rarstAPIMessages.true_ : this.rarstAPIMessages.false_,
                    "{GAMEMODE}", offlinePlayer1.isOnline() ? this.messages.getFieldValue(offlinePlayer1.getPlayer().getGameMode().name().toLowerCase(), String.class): this.rarstAPIMessages.lackInformation,
                    "{ISSOCIALSPYING}", user1.getUserCache().isSocialspying() ? this.rarstAPIMessages.true_ : this.rarstAPIMessages.false_,
                    "{ISMUTED}", user1.isMuted() ? this.rarstAPIMessages.true_ : this.rarstAPIMessages.false_,
                    "{MUTEREASON}", user1.isMuted() ? user1.getMuteReason() : this.rarstAPIMessages.lackInformation,
                    "{MUTEREMAINING}", user1.isMuted() ? user1.getMuteDate() == -1L ? this.rarstAPIMessages.unknown : DateUtil.formatDateFromMills(user1.getMuteDate()) : this.rarstAPIMessages.lackInformation,
                    "{ISBANNED}", offlinePlayer1.isBanned() ? this.rarstAPIMessages.true_ : this.rarstAPIMessages.false_,
                    "{BANREASON}", offlinePlayer1.isBanned() ? Bukkit.getBanList(BanList.Type.NAME).getBanEntry(user1.getName()).getReason() : this.rarstAPIMessages.lackInformation,
                    "{BANREMAINING}", offlinePlayer1.isBanned() ? Bukkit.getBanList(BanList.Type.NAME).getBanEntry(user1.getName()).getExpiration() == null ? this.rarstAPIMessages.unknown : DateUtil.formatDateFromMills(Bukkit.getBanList(BanList.Type.NAME).getBanEntry(user1.getName()).getExpiration().getTime()) : this.rarstAPIMessages.lackInformation,
                    "{ISONLINE}", offlinePlayer1.isOnline() ? this.rarstAPIMessages.true_ : this.rarstAPIMessages.false_,
                    "{ISSNEAKING}", offlinePlayer1.isOnline() ? offlinePlayer1.getPlayer().isSneaking() ? this.rarstAPIMessages.true_ : this.rarstAPIMessages.false_ : this.rarstAPIMessages.lackInformation,
                    "{ISSLEEPING}", offlinePlayer1.isOnline() ? offlinePlayer1.getPlayer().isSleeping() ? this.rarstAPIMessages.true_ : this.rarstAPIMessages.false_ : this.rarstAPIMessages.lackInformation,
                    "{ISSPRINTING}", offlinePlayer1.isOnline() ? offlinePlayer1.getPlayer().isSprinting() ? this.rarstAPIMessages.true_ : this.rarstAPIMessages.false_ : this.rarstAPIMessages.lackInformation,
                    "{ISSWIMMING}", offlinePlayer1.isOnline() ? offlinePlayer1.getPlayer().isSwimming() ? this.rarstAPIMessages.true_ : this.rarstAPIMessages.false_ : this.rarstAPIMessages.lackInformation,
                    "{ISWHITELISTED}", offlinePlayer1.isWhitelisted() ? this.rarstAPIMessages.true_ : this.rarstAPIMessages.false_,
                    "{ISOP}", offlinePlayer1.isOp() ? this.rarstAPIMessages.true_ : this.rarstAPIMessages.false_,
                    "{LOCALIZATION}", offlinePlayer1.isOnline() ? LocationUtil.locationToString(offlinePlayer1.getPlayer().getLocation()) : this.rarstAPIMessages.lackInformation,
                    "{WORLD}", offlinePlayer1.isOnline() ? offlinePlayer1.getPlayer().getWorld().getName() : this.rarstAPIMessages.lackInformation,
                    "{EXPLEVEL}", offlinePlayer1.isOnline() ? String.valueOf(offlinePlayer1.getPlayer().getLevel()) : this.rarstAPIMessages.lackInformation,
                    "{HEALTHLEVEL}", offlinePlayer1.isOnline() ? String.valueOf(offlinePlayer1.getPlayer().getHealthScale()) : this.rarstAPIMessages.lackInformation,
                    "{FOODLEVEL}", offlinePlayer1.isOnline() ? String.valueOf(offlinePlayer1.getPlayer().getFoodLevel()) : this.rarstAPIMessages.lackInformation
            );
            return;
        }
        InetAddress address = null;

        try {
            address = InetAddress.getByName(args[0]);
        } catch (final UnknownHostException ignored) {}

        if (address == null) {
            this.messages.ipNotExist.send(commandSender);
            return;
        }
        final Set<User> users = this.userManager.getUsers(address);

        this.messages.ipInfo.send(commandSender,
                "{USERS}", users.size() < 1 ? this.rarstAPIMessages.lack : StringUtils.join(users.stream().map(User::getName).collect(Collectors.toSet()), ", "),
                "{ISBANNED}", Bukkit.getBanList(BanList.Type.IP).isBanned(args[0]) ? this.rarstAPIMessages.true_ : this.rarstAPIMessages.false_,
                "{BANREASON}", Bukkit.getBanList(BanList.Type.IP).isBanned(args[0]) ? Bukkit.getBanList(BanList.Type.IP).getBanEntry(args[0]).getReason() : this.rarstAPIMessages.lackInformation,
                "{BANREMAINING}", Bukkit.getBanList(BanList.Type.IP).isBanned(args[0]) ? Bukkit.getBanList(BanList.Type.IP).getBanEntry(args[0]).getExpiration() == null ? this.rarstAPIMessages.unknown : DateUtil.formatDateFromMills(Bukkit.getBanList(BanList.Type.IP).getBanEntry(args[0]).getExpiration().getTime()) : this.rarstAPIMessages.lackInformation
        );
    }

    @Override
    public List<String> onTabComplete(final CommandSender commandSender, final String alias, final String[] args) throws IllegalArgumentException {
        switch (args.length) {
            case 1: {
                if(!PermissionUtil.hasPermission(commandSender, this.permission + ".other")) {
                    break;
                }
                return this.userManager.usersThatCanInteract(commandSender).stream().map(User::getName).collect(Collectors.toList());
            }
        }
        return new ArrayList<>();
    }

}
