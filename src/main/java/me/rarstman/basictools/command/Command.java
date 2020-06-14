package me.rarstman.basictools.command;

import me.rarstman.basictools.BasicToolsPlugin;
import me.rarstman.basictools.cache.ChatCache;
import me.rarstman.basictools.configuration.Configuration;
import me.rarstman.basictools.data.UserManager;
import me.rarstman.basictools.hook.VaultHook;
import me.rarstman.basictools.util.ChatUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class Command extends org.bukkit.command.Command {

    public final BasicToolsPlugin basicToolsPlugin;
    public final UserManager userManager;
    public final Configuration configuration;
    public final Configuration.Messages messages;
    public final VaultHook vaultHook;

    public final String permission;
    public final boolean onlyPlayer;

    public Command(final Configuration.BasicCommand basicCommand, final boolean onlyPlayer) {
        super(basicCommand.getName(), basicCommand.getDescription(), basicCommand.getUsage(), basicCommand.getAliases());
        this.permission = "basictools.command." + basicCommand.getName();
        this.onlyPlayer = onlyPlayer;

        this.basicToolsPlugin = BasicToolsPlugin.getPlugin();
        this.userManager = this.basicToolsPlugin.getUserManager();
        this.configuration = this.basicToolsPlugin.getConfiguration();
        this.messages = this.configuration.getMessages();
        this.vaultHook = (VaultHook) this.basicToolsPlugin.getPluginHook().getHook("Vault").get();
    }

    public final boolean execute(final CommandSender commandSender, final String label, final String[] args) {
        if (!this.basicToolsPlugin.getPluginHook().getHook("Vault").get().isInitialized()) {
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("VaultNotHook"));
            return true;
        }

        if (!this.vaultHook.hasPermission(commandSender, this.permission)) {
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("NoPermission"),
                    "{permission}", this.permission
            );
            return true;
        }

        if (this.onlyPlayer && !(commandSender instanceof Player)) {
            ChatUtil.sendMessage(commandSender, this.messages.getMessage("OnlyPlayer"));
            return true;
        }
        this.onExecute(commandSender, args);
        return true;
    }


    public abstract void onExecute(final CommandSender commandSender, final String[] args);



}
