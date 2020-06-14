package me.rarstman.basictools.configuration;

import me.rarstman.basictools.BasicToolsPlugin;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Configuration {

    private final BasicToolsPlugin basicToolsPlugin;
    private final FileConfiguration fileConfiguration;

    private final Map<String, BasicCommand> commandsConfiguration = new HashMap<>();
    private Messages messages;

    public Configuration(final BasicToolsPlugin basicToolsPlugin){
        this.basicToolsPlugin = basicToolsPlugin;

        this.basicToolsPlugin.saveDefaultConfig();
        this.fileConfiguration = this.basicToolsPlugin.getConfig();
    }

    public void initialize(){
        this.messages = new Messages();
        this.fileConfiguration.getConfigurationSection("Commands").getKeys(false)
                .stream()
                .map(commandConfigurationSection -> this.fileConfiguration.getConfigurationSection("Commands." + commandConfigurationSection))
                .forEach(commandConfigurationSection ->
                        this.commandsConfiguration.put(
                                commandConfigurationSection.getCurrentPath().split(".")[1],
                                new BasicCommand(
                                        commandConfigurationSection.isSet("Name") && commandConfigurationSection.isString("Name") ? commandConfigurationSection.getString("Name") : StringUtils.replace(commandConfigurationSection.getCurrentPath().split(".")[1], "Command", "").toLowerCase(),
                                        commandConfigurationSection.getString("Usage"),
                                        commandConfigurationSection.getString("Description"),
                                        commandConfigurationSection.getStringList("Aliases"),
                                        commandConfigurationSection.isSet("Enabled") && commandConfigurationSection.isBoolean("Enabled") ? commandConfigurationSection.getBoolean("Enabled") : true
                                )
                        )
                );
    }

    public Optional<BasicCommand> getCommandConfiguration(final String name){
        return Optional.of(this.commandsConfiguration.get(name));
    }

    public Messages getMessages() {
        return this.messages;
    }

    public class BasicCommand {
        private final String name;
        private final String usage;
        private final String description;
        private final List<String> aliases;
        private final boolean isEnabled;

        public BasicCommand(final String name, final String usage, final String description, final List<String> aliases, final boolean isEnabled){
            this.name = name;
            this.usage = usage;
            this.description = description;
            this.aliases = aliases;
            this.isEnabled = isEnabled;
        }

        public String getName() {
            return this.name;
        }

        public String getUsage() {
            return this.usage;
        }

        public String getDescription() {
            return this.description;
        }

        public List<String> getAliases() {
            return this.aliases;
        }

        public boolean isEnabled() {
            return this.isEnabled;
        }
    }

    public class Messages {
        private final Map<String, String> messages = new HashMap<>();

        public String getMessage(final String name){
            return this.messages.containsKey(name) ? this.messages.get(name) : "Empty message";
        }
    }
}
