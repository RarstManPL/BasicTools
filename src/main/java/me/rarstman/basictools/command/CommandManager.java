package me.rarstman.basictools.command;

import me.rarstman.basictools.BasicToolsPlugin;
import me.rarstman.basictools.configuration.Configuration;
import me.rarstman.basictools.hook.VaultHook;
import me.rarstman.basictools.util.ChatUtil;
import me.rarstman.basictools.util.ClassUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class CommandManager {

    private final BasicToolsPlugin basicToolsPlugin;
    private final Configuration configuration;
    private final VaultHook vaultHook;

    private CommandMap commandMap = null;

    private final Map<String, Command> commands = new HashMap<>();

    public CommandManager(final BasicToolsPlugin basicToolsPlugin){
        this.basicToolsPlugin = basicToolsPlugin;
        this.configuration = this.basicToolsPlugin.getConfiguration();
        this.vaultHook = (VaultHook) this.basicToolsPlugin.getPluginHook().getHook("Vault").get();

        try {
            final Field field = SimplePluginManager.class.getDeclaredField("commandMap");
            field.setAccessible(true);
            this.commandMap = (CommandMap) field.get(Bukkit.getServer().getPluginManager());
        } catch (final NoSuchFieldException | IllegalAccessException exception){
            //ERROR
        }
    }

    public void loadCommands(){
        if(this.commandMap == null){
            return;
        }
        ClassUtil.getClassesFromPackages("me.rarstmanpl.basictools.command.admin", "me.rarstmanpl.basictools.command.player")
                .forEach(clazz -> {
                    try {
                        final String name = clazz.getCanonicalName().split(".")[5];
                        final Configuration.BasicCommand basicCommand = this.configuration.getCommandConfiguration(name).isPresent() ? this.configuration.getCommandConfiguration(name).get() : null;

                        if(basicCommand == null){
                            //ERROR
                            return;
                        }

                        if(basicCommand.isEnabled()){
                            return;
                        }
                        final Command command = (Command) clazz.getDeclaredConstructor(Configuration.BasicCommand.class).newInstance(basicCommand);

                        if(!this.registerCommand(command)){
                            //ERROR
                            return;
                        }
                        this.commands.put(name, command);
                    } catch (final NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ignored) {}
                });
    }

    public Optional<Command> getCommand(final String name){
        return Optional.of(this.commands.get(name));
    }

    public Set<Command> getCommandsWithAccess(final Player player){
        if (!this.basicToolsPlugin.getPluginHook().getHook("Vault").get().isInitialized()) {
            return new HashSet<>();
        }
        return this.commands
                .values()
                .stream()
                .filter(command -> this.vaultHook.hasPermission(player, command.permission))
                .collect(Collectors.toSet());
    }

    private boolean registerCommand(final Command command){
        return this.commandMap.register("basictools:", command);
    }
}
