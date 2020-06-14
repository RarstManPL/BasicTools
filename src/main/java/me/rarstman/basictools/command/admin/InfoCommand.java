package me.rarstman.basictools.command.admin;

import com.google.common.collect.ImmutableList;
import me.rarstman.basictools.command.Command;
import me.rarstman.basictools.configuration.Configuration;
import me.rarstman.basictools.data.User;
import me.rarstman.basictools.util.ChatUtil;
import me.rarstman.basictools.util.DateUtil;
import me.rarstman.basictools.util.LocationUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class InfoCommand extends Command {

    private final Pattern ipPattern;

    public InfoCommand(final Configuration.BasicCommand basicCommand) {
        super(basicCommand, false);

        this.ipPattern = Pattern.compile("^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");
    }

    @Override
    public void onExecute(final CommandSender commandSender, final String[] args) {
        final String permission = args.length < 1 ? null : this.permission + ".other";

        if (!this.vaultHook.hasPermission(commandSender, permission)) {
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("NoPermission"), "{permission}", permission);
            return;
        }
        final boolean isIp = ipPattern.matcher(args[0]).matches();
        final String permission1 = isIp ? this.permission + ".ip" : this.permission + ".name";

        if (!this.vaultHook.hasPermission(commandSender, permission1)) {
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("NoPermission"), "{permission}", permission);
            return;
        }

        if (!isIp) {
            final User user1 = !(commandSender instanceof Player) && args.length < 1 ? null : args.length > 0 ? this.userManager.getUser(args[0]).isPresent() ? this.userManager.getUser(args[0]).get() : null : this.userManager.getUser((Player) commandSender).isPresent() ? this.userManager.getUser((Player) commandSender).get() : null;

            if (user1 == null) {
                ChatUtil.sendMessage(commandSender, this.messages.getMessage("PlayerNotExistOrConsole"));
                return;
            }
            final OfflinePlayer offlinePlayer1 = user1.getOfflinePlayer();
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("PlayerInfo"),
                    "{uuid}", user1.getUUID().toString(),
                    "{nick}", user1.getName(),
                    "{firstip}", this.vaultHook.hasPermission(commandSender, this.permission + ".see.ip") ? "" : "&k" + user1.getFirstIP().getHostAddress(),
                    "{lastip}", this.vaultHook.hasPermission(commandSender, this.permission + ".see.ip") ? "" : "&k" + user1.getLastIP().getHostAddress(),
                    "{firstplay}", DateUtil.formatDateFromMills(user1.getFirstPlay()),
                    "{lastplay}", DateUtil.formatDateFromMills(user1.getLastPlay()),
                    "{isflying}", offlinePlayer1.isOnline() ? this.messages.getMessage(StringUtils.capitalize(String.valueOf(offlinePlayer1.getPlayer().isFlying()))) : this.messages.getMessage("LackInformation"),
                    "{isgoded}", this.messages.getMessage(StringUtils.capitalize(String.valueOf(user1.getUserCache().isGoded()))),
                    "{isvanished}", this.messages.getMessage(StringUtils.capitalize(String.valueOf(user1.getUserCache().isVanished()))),
                    "{gamemode}", offlinePlayer1.isOnline() ? this.messages.getMessage(StringUtils.capitalize(offlinePlayer1.getPlayer().getGameMode().toString())) : this.messages.getMessage("LackInformation"),
                    "{issocialspying}", this.messages.getMessage(StringUtils.capitalize(String.valueOf(user1.getUserCache().isSocialspying()))),
                    "{ismuted}", this.messages.getMessage(StringUtils.capitalize(String.valueOf(user1.isMuted()))),
                    "{mutereason}", user1.isMuted() ? user1.getMuteReason() : this.messages.getMessage("LackInformation"),
                    "{muteremaining}", user1.isMuted() ? DateUtil.formatDateFromMills(user1.getMuteDate()) : this.messages.getMessage("LackInformation"),
                    "{isbaned}", this.messages.getMessage(StringUtils.capitalize(String.valueOf(offlinePlayer1.isBanned()))),
                    "{banreason}", offlinePlayer1.isBanned() ? Bukkit.getBanList(BanList.Type.NAME).getBanEntry(user1.getName()).getReason() : this.messages.getMessage("LackInformation"),
                    "{banremaining}", offlinePlayer1.isBanned() ? DateUtil.formatDateFromMills(Bukkit.getBanList(BanList.Type.NAME).getBanEntry(user1.getName()).getExpiration().getTime()) : this.messages.getMessage("LackInformation"),
                    "{isonline}", this.messages.getMessage(StringUtils.capitalize(String.valueOf(offlinePlayer1.isOnline()))),
                    "{issneaking}", offlinePlayer1.isOnline() ? this.messages.getMessage(StringUtils.capitalize(String.valueOf(offlinePlayer1.getPlayer().isSneaking()))) : this.messages.getMessage("LackInformation"),
                    "{issleeping}", offlinePlayer1.isOnline() ? this.messages.getMessage(StringUtils.capitalize(String.valueOf(offlinePlayer1.getPlayer().isSleeping()))) : this.messages.getMessage("LackInformation"),
                    "{issprinting}", offlinePlayer1.isOnline() ? this.messages.getMessage(StringUtils.capitalize(String.valueOf(offlinePlayer1.getPlayer().isSprinting()))) : this.messages.getMessage("LackInformation"),
                    "{isswimming}", offlinePlayer1.isOnline() ? this.messages.getMessage(StringUtils.capitalize(String.valueOf(offlinePlayer1.getPlayer().isSwimming()))) : this.messages.getMessage("LackInformation"),
                    "{iswhitelisted}", offlinePlayer1.isOnline() ? this.messages.getMessage(StringUtils.capitalize(String.valueOf(offlinePlayer1.getPlayer().isWhitelisted()))) : this.messages.getMessage("LackInformation"),
                    "{isop}", offlinePlayer1.isOnline() ? this.messages.getMessage(StringUtils.capitalize(String.valueOf(offlinePlayer1.getPlayer().isOp()))) : this.messages.getMessage("LackInformation"),
                    "{localization}", offlinePlayer1.isOnline() ? LocationUtil.locationToString(offlinePlayer1.getPlayer().getLocation()) : this.messages.getMessage("LackInformation"),
                    "{world}", offlinePlayer1.isOnline() ? offlinePlayer1.getPlayer().getWorld().getName() : this.messages.getMessage("LackInformation"),
                    "{explevel}", offlinePlayer1.isOnline() ? String.valueOf(offlinePlayer1.getPlayer().getLevel()) : this.messages.getMessage("LackInformation"),
                    "{healthlevel}", offlinePlayer1.isOnline() ? String.valueOf(offlinePlayer1.getPlayer().getHealthScale()) : this.messages.getMessage("LackInformation"),
                    "{foodlevel}", offlinePlayer1.isOnline() ? String.valueOf(offlinePlayer1.getPlayer().getFoodLevel()) : this.messages.getMessage("LackInformation")
            );
            return;
        }
        InetAddress address = null;

        try {
            address = InetAddress.getByName(args[0]);
        } catch (final UnknownHostException ignored) {}

        if (address == null) {
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("IpNotExist"));
            return;
        }
        final Set<User> users = this.userManager.getUsers(address);

        ChatUtil.sendMessage(commandSender, this.messages.getMessage("IpInfo"),
                "{players}", users.size() < 0 ? this.messages.getMessage("Lack") : StringUtils.join(users.stream().map(User::getName).collect(Collectors.toSet()), ", "),
                "{isbaned}", this.messages.getMessage(StringUtils.capitalize(String.valueOf(Bukkit.getBanList(BanList.Type.IP).isBanned(args[0])))),
                "{banreason}", Bukkit.getBanList(BanList.Type.IP).isBanned(args[0]) ? Bukkit.getBanList(BanList.Type.IP).getBanEntry(args[0]).getReason() : this.messages.getMessage("LackInformation"),
                "{banremaining}", Bukkit.getBanList(BanList.Type.IP).isBanned(args[0]) ? DateUtil.formatDateFromMills(Bukkit.getBanList(BanList.Type.IP).getBanEntry(args[0]).getExpiration().getTime()) : this.messages.getMessage("LackInformation")
        );
    }

    @Override
    public List<String> tabComplete(final CommandSender commandSender, final String alias, final String[] args) throws IllegalArgumentException {
        switch (args.length) {
            case 1: {
                return ImmutableList.of(this.vaultHook.hasPermission(commandSender, this.permission + ".other") ? Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.joining()) : null);
            }
        }
        return ImmutableList.of();
    }
}
