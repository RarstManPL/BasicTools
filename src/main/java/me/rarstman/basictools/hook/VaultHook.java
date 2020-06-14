package me.rarstman.basictools.hook;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHook implements PluginHook.Hook {

    private final boolean isInitialized = false;
    private Permission permissionHook;

    @Override
    public boolean isInitialized() {
        return this.isInitialized;
    }

    @Override
    public void initialize() {
        final RegisteredServiceProvider<Permission> permissionProvider = Bukkit.getServer().getServicesManager().getRegistration(Permission.class);

        if (permissionProvider == null) {
            //ERROR
            return;
        }
        this.permissionHook = permissionProvider.getProvider();
        return;
    }

    @Override
    public String getName() {
        return "Vault";
    }

    public boolean hasPermission(final OfflinePlayer offlinePlayer, final String permission) {
        return this.permissionHook.playerHas(null, offlinePlayer, permission);
    }

    public boolean hasPermission(final CommandSender commandSender, final String permission) {
        return this.permissionHook.has(commandSender, permission);
    }

    public boolean hasPermission(final Player player, final String permission) {
        return this.permissionHook.has(player, permission);
    }
}
