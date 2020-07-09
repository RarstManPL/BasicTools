package me.rarstman.basictools.configuration;

import me.rarstman.basictools.BasicToolsPlugin;
import me.rarstman.rarstapi.configuration.ConfigProvider;
import me.rarstman.rarstapi.configuration.annotation.ConfigName;
import me.rarstman.rarstapi.configuration.annotation.ParseValue;
import me.rarstman.rarstapi.configuration.customparser.impl.DatabaseDataParser;
import me.rarstman.rarstapi.database.DatabaseData;
import me.rarstman.rarstapi.item.ItemBuilder;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BasicToolsConfig extends ConfigProvider {

    @ConfigName("Database")
    @ParseValue(DatabaseDataParser.class)
    public DatabaseData mySQLDatabaseData = new DatabaseData("localhost", 3306, "root", "123", "basictools");

    @ConfigName("Panel.PanelInventory.Title")
    public String panelInventoryTitle = "Panel Inventory";

    @ConfigName("Panel.PanelInventory.Template")
    public List<String> panelInventoryTemplate = Arrays.asList("xxxxxxxxx", "xabcdefgx", "xhijklmnx", "xoprstuw", "x1234567x", "xxxxxxxxx");

    @ConfigName("Panel.PanelInventory.TemplateExplanation")
    public List<String> panelInventoryTemplateExplanation = Arrays.asList("x:BLACK_STAINED_GLASS_PANE");

    @ConfigName("Panel.PanelInventory.CommandTemplate.Name")
    public String commandTemplateName = "Polecenie {NAME}";

    @ConfigName("Panel.PanelInventory.CommandTemplate.Lore")
    public List<String> commandTemplateLore = Arrays.asList("Kliknij, by wykonać.", "Nazwa: {NAME}", "Użycie: {USAGE}", "Uprawnienie: {PERMISSION}", "Masz dostęp: {ACCESS}");

    @ConfigName("Panel.ArgumentsInventory.Title")
    public String argumentsInventoryTitle = "Argumenty: {NAME}";

    @ConfigName("Panel.ArgumentsInventory.DefaultText")
    public String argumentsInventoryDefaultText = "Wpisz argumenty";

    @ConfigName("Panel.ArgumentsInventory.PaperTemplate.Lore")
    public List<String> paperTemplateLore = Arrays.asList("Wpisz argumenty!", "Nazwa komendy: {NAME}", "Użycie: {USAGE}");

    @ConfigName("GroupsInListCommand")
    public List<String> groupsInListCommand = Arrays.asList("root:&4ROOT", "admin:&cAdmin", "mod:&aModerator", "default:&7Gracz");

    public Map<String, ItemBuilder> getParsedTemplateExplanation() {
        return this.panelInventoryTemplateExplanation
                .stream()
                .map(string -> string.split(":", 2))
                .filter(strings -> strings.length > 1)
                .collect(Collectors.toMap(strings -> strings[0], strings -> new ItemBuilder(strings[1])));
    }

    public Map<String, String> getParsedGroupsInListCommand() {
        return this.groupsInListCommand
                .stream()
                .map(string -> string.split(":", 2))
                .filter(strings -> strings.length > 1)
                .collect(Collectors.toMap(strings -> strings[0], strings -> strings[1]));
    }

    public BasicToolsConfig() {
        super(new File(BasicToolsPlugin.getPlugin().getDataFolder(), "config.yml"), BasicToolsPlugin.getPlugin().getResource("config.yml"));
    }

}
