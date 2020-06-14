package me.rarstman.basictools.util;

import me.rarstman.basictools.BasicToolsPlugin;
import me.rarstman.basictools.hook.VaultHook;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ChatUtil {

    private static final VaultHook vaultHook = (VaultHook) BasicToolsPlugin.getPlugin().getPluginHook().getHook("Vault").get();

    public static String fixColors(final String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static List<String> fixColors(final List<String> listString) {
        return listString
                .stream()
                .map(ChatUtil::fixColors)
                .collect(Collectors.toList());
    }

    public static String replace(String string, String... replaces) {
        if (replaces.length < 2) {
            return string;
        }

        if (replaces.length % 2 != 0) {
            replaces = Arrays.copyOfRange(replaces, 0, replaces.length - 1);
        }

        for (int i = 0; i < replaces.length; i = i + 2) {
            string = StringUtils.replace(string, replaces[i], replaces[i + 1]);
        }
        return string;
    }

    public static void sendMessage(final Player player, final String message, final String... replaces) {
        player.sendMessage(fixColors(replace(message, replaces)));
    }

    public static void sendMessage(final CommandSender commandSender, final String message, final String... replaces) {
        commandSender.sendMessage(fixColors(replace(message, replaces)));
    }

    public static void broadCastMessage(final String message, final String... replaces) {
        Bukkit.getOnlinePlayers()
                .forEach(player -> sendMessage(player, message, replaces));
    }

    public static void broadCastPermissionMessage(final String permission, final String message, final String... replaces) {
        Bukkit.getOnlinePlayers()
                .stream()
                .filter(player -> vaultHook.hasPermission(player, permission))
                .forEach(player -> sendMessage(player, message, replaces));
    }

    public static void sendTitle(final Player player, final String title, final String message, final int stay, final String... replaces){
        player.sendTitle(fixColors(title), fixColors(replace(message, replaces)), 1, stay, 1);
    }

    public static void sendMessageWithDecision(final Player player, String message, final String title, final int stay, final String... replaces){
        message = fixColors(replace(message, replaces));

        if(message.length() > 50){
            sendMessage(player, message);
            return;
        }
        sendTitle(player, title, message, stay);
    }
}
