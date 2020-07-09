package me.rarstman.basictools;

import me.rarstman.basictools.cache.ChatCache;
import me.rarstman.basictools.command.admin.*;
import me.rarstman.basictools.command.inventory.PanelCommand;
import me.rarstman.basictools.command.player.*;
import me.rarstman.basictools.configuration.BasicToolsCommands;
import me.rarstman.basictools.configuration.BasicToolsConfig;
import me.rarstman.basictools.configuration.BasicToolsMessages;
import me.rarstman.basictools.data.UserManager;
import me.rarstman.basictools.listener.*;
import me.rarstman.basictools.task.UserUpdateTask;
import me.rarstman.rarstapi.command.CommandManager;
import me.rarstman.rarstapi.configuration.ConfigManager;
import me.rarstman.rarstapi.database.DatabaseProvider;
import me.rarstman.rarstapi.database.exception.DatabaseInitializeException;
import me.rarstman.rarstapi.database.impl.MySQL;
import me.rarstman.rarstapi.listener.ListenerManager;
import me.rarstman.rarstapi.logger.Logger;
import me.rarstman.rarstapi.task.TaskManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BasicToolsPlugin extends JavaPlugin {

    private Logger basicToolsLogger;
    private DatabaseProvider databaseProvider;
    private UserManager userManager;
    private ChatCache chatCache;

    @Override
    public void onLoad() {
        this.basicToolsLogger = new Logger(java.util.logging.Logger.getLogger("BasicTools"));
    }

    @Override
    public void onEnable() {
        this.basicToolsLogger.clearly(" ");

        this.basicToolsLogger.info("Registering configs...");
        ConfigManager.registerConfigs(
                new BasicToolsConfig(),
                new BasicToolsCommands(),
                new BasicToolsMessages()
        );

        this.basicToolsLogger.info("Registering database...");
        this.registerDatabase();

        this.userManager = new UserManager();
        this.userManager.loadUsers();

        this.chatCache = new ChatCache();

        this.basicToolsLogger.info("Registering listeners...");
        ListenerManager.registerListeners(
                new PlayerJoinListener(),
                new PlayerLoginListener(),
                new AsyncPlayerChatListener(),
                new PlayerDamageListener(),
                new PlayerQuitListener(),
                new TabCompleteListener(),
                new PlayerCommandPreprocessListener()
        );

        this.basicToolsLogger.info("Registering commands...");
        CommandManager.registerCommands(
                this,
                new AdminChatCommand(),
                new BanCommand(),
                new BroadCastCommand(),
                new ChatCommand(),
                new ClearInventoryCommand(),
                new FlyCommand(),
                new GameModeCommand(),
                new GiveCommand(),
                new GodCommand(),
                new InfoCommand(),
                new InventorySeeCommand(),
                new KickCommand(),
                new MuteCommand(),
                new SocialSpyCommand(),
                new TeleportAllCommand(),
                new TeleportCommand(),
                new UnBanCommand(),
                new UnMuteCommand(),
                new VanishCommand(),
                new WeatherCommand(),
                new ReportCommand(),
                new PanelCommand(),
                new TimeCommand(),
                new MessageToggleCommand(),
                new MessageCommand(),
                new ListCommand(),
                new HelpCommand(),
                new ReplyCommand()
        );

        this.basicToolsLogger.info("Registering task...");
        TaskManager.registerTask(new UserUpdateTask());

        this.basicToolsLogger.clearly(" ");
    }

    @Override
    public void onDisable() {
        this.userManager.saveUsers();
        this.databaseProvider.disableDatabase();
    }

    public void registerDatabase() {
        try {
            this.databaseProvider = new MySQL(ConfigManager.getConfig(BasicToolsConfig.class).mySQLDatabaseData)
                    .createTable(
                            new StringBuilder()
                                    .append("CREATE TABLE IF NOT EXISTS `basictools_users` (")
                                    .append("`id` INTEGER PRIMARY KEY AUTO_INCREMENT, ")
                                    .append("`uuid` VARCHAR(255) NOT NULL, ")
                                    .append("`name` TEXT NOT NULL, ")
                                    .append("`firstIP` TEXT NOT NULL, ")
                                    .append("`lastIP` TEXT NOT NULL, ")
                                    .append("`firstPlay` LONG NOT NULL, ")
                                    .append("`lastPlay` LONG NOT NULL, ")
                                    .append("`muteDate` LONG NOT NULL, ")
                                    .append("`muteReason` TEXT NOT NULL);")
                                    .toString()
                    )
                    .initialize();
        } catch (final DatabaseInitializeException exception) {
            this.basicToolsLogger.error(exception.getMessage());
            this.getPluginLoader().disablePlugin(this);
            return;
        }
    }

    public UserManager getUserManager() {
        return this.userManager;
    }

    public ChatCache getChatCache() {
        return this.chatCache;
    }

    public DatabaseProvider getDatabaseProvider() {
        return this.databaseProvider;
    }

    public Logger getBasicToolsLogger() {
        return this.basicToolsLogger;
    }

    public static BasicToolsPlugin getPlugin() {
        return JavaPlugin.getPlugin(BasicToolsPlugin.class);
    }

}
