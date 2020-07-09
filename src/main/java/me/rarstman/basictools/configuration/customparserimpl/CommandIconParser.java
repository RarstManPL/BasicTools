package me.rarstman.basictools.configuration.customparserimpl;

import me.rarstman.basictools.command.CommandIcon;
import me.rarstman.rarstapi.configuration.ConfigProvider;
import me.rarstman.rarstapi.configuration.customparser.CustomParser;
import me.rarstman.rarstapi.configuration.customparser.exception.CustomParserException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.lang.reflect.Field;

public class CommandIconParser extends CustomParser<CommandIcon> {

    public CommandIconParser(final Class<? extends ConfigProvider> configClass, final Field field, final File configFile, final YamlConfiguration yamlConfiguration, final String configPath) {
        super(configClass, field, configFile, yamlConfiguration, configPath);
    }

    @Override
    public CommandIcon parse() throws CustomParserException {
        if(!this.yamlConfiguration.isString(this.configPath + ".Item")
                || !this.yamlConfiguration.isString(this.configPath + ".Field")
                || !this.yamlConfiguration.isBoolean(this.configPath + ".InPanel")) {
            throw new CustomParserException("Incomplete command icon configuration data in file '" + this.configFile.getPath() + "', path '" + this.configPath + "'. Using default or last correctly parsed value...");
        }
        return new CommandIcon(
                this.yamlConfiguration.getString(this.configPath + ".Item"),
                this.yamlConfiguration.getString(this.configPath + ".Field"),
                this.yamlConfiguration.getBoolean(this.configPath + ".InPanel")
        );
    }

}
