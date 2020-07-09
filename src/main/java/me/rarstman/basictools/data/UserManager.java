package me.rarstman.basictools.data;

import me.rarstman.basictools.BasicToolsPlugin;
import me.rarstman.basictools.command.CommandIcon;
import me.rarstman.basictools.command.plugin.BasicToolsCommand;
import me.rarstman.basictools.configuration.BasicToolsCommands;
import me.rarstman.basictools.configuration.BasicToolsConfig;
import me.rarstman.basictools.configuration.BasicToolsMessages;
import me.rarstman.rarstapi.command.CommandManager;
import me.rarstman.rarstapi.command.CommandProvider;
import me.rarstman.rarstapi.configuration.ConfigManager;
import me.rarstman.rarstapi.configuration.impl.RarstAPIMessages;
import me.rarstman.rarstapi.database.DatabaseProvider;
import me.rarstman.rarstapi.inventory.ClickableItem;
import me.rarstman.rarstapi.inventory.InventoryProvider;
import me.rarstman.rarstapi.inventory.Rows;
import me.rarstman.rarstapi.inventory.impl.AnvilInventory;
import me.rarstman.rarstapi.inventory.impl.ChestInventory;
import me.rarstman.rarstapi.item.ItemBuilder;
import me.rarstman.rarstapi.logger.Logger;
import me.rarstman.rarstapi.task.impl.AsynchronouslyTask;
import me.rarstman.rarstapi.task.impl.LaterTask;
import me.rarstman.rarstapi.util.PermissionUtil;
import me.rarstman.rarstapi.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class UserManager {

    private final Map<UUID, User> usersByUUID = new HashMap<>();
    private final Map<String, User> usersByName = new HashMap<>();
    private final Set<UUID> vanishedUUIDs = new HashSet<>();

    private final DatabaseProvider databaseProvider;
    private final Logger logger;

    private final BasicToolsConfig config;
    private final BasicToolsCommands commands;
    private final BasicToolsMessages messages;
    private final RarstAPIMessages rarstAPIMessages;

    public UserManager() {
        this.databaseProvider = BasicToolsPlugin.getPlugin().getDatabaseProvider();
        this.logger = BasicToolsPlugin.getPlugin().getBasicToolsLogger();
        this.config = ConfigManager.getConfig(BasicToolsConfig.class);
        this.commands = ConfigManager.getConfig(BasicToolsCommands.class);
        this.messages = ConfigManager.getConfig(BasicToolsMessages.class);
        this.rarstAPIMessages = ConfigManager.getConfig(RarstAPIMessages.class);
    }

    public Optional<User> getUser(final Player player) {
        return Optional.ofNullable(this.usersByUUID.containsKey(player.getUniqueId()) ? this.usersByUUID.get(player.getUniqueId()) : this.usersByName.containsKey(player.getName()) ? this.usersByName.get(player.getName()) : null);
    }

    public Optional<User> getUser(final String name) {
        return Optional.ofNullable(this.usersByName.get(name));
    }

    public synchronized User createUser(final Player player){
        final UUID uuid = player.getUniqueId();
        final User user = new User(uuid);

        user.setName(player.getName());
        user.setFirstIP(player.getAddress().getAddress());
        user.setLastIP(player.getAddress().getAddress());
        user.setFirstPlay(System.currentTimeMillis());
        user.setLastPlay(System.currentTimeMillis());

        this.usersByUUID.put(player.getUniqueId(), user);
        this.createUserByName(player);

        this.databaseProvider.query(
                new StringBuilder()
                        .append("INSERT INTO `basictools_users` (uuid, name, firstIP, lastIP, firstPlay, lastPlay, muteDate, muteReason) VALUES (")
                        .append("'" + user.getUUID().toString() + "', ")
                        .append("'" + user.getName() + "', ")
                        .append("'" + user.getFirstIP().getHostAddress() + "', ")
                        .append("'" + user.getLastIP().getHostAddress() + "', ")
                        .append("'" + user.getFirstPlay() + "', ")
                        .append("'" + user.getLastPlay() + "', ")
                        .append("'" + user.getMuteDate() + "', ")
                        .append("'" + user.getMuteReason() + "');")
                        .toString(),
                false
        );
        return user;
    }

    public void createUserByName(final Player player){
        final User user = this.getUser(player).isPresent() ? this.getUser(player).get() : null;

        if(user == null || this.usersByName.containsKey(player.getName())) {
            return;
        }

        if(this.usersByName.containsValue(user)){
            for(Iterator<User> iterator = this.usersByName.values().stream().filter(iteratorUser -> iteratorUser == user).iterator(); iterator.hasNext();){
                iterator.remove();
            }
        }
        this.usersByName.put(player.getName(), user);
    }

    public synchronized void loadUsers(){
        this.logger.info("Loading users...");

        final ResultSet resultSet = this.databaseProvider.select("SELECT * FROM `basictools_users`;");

        int i = 0;
        try {
            while(resultSet.next()) {
                this.loadUser(resultSet);
                i++;
            }
        } catch (final SQLException exception) {
            this.logger.exception(exception, "Error while trying to load users.");
        }

        try {
            resultSet.close();
        } catch (final SQLException ignored) {}

        this.logger.info("Loaded " + i + " users.");
    }

    public synchronized void loadUser(final ResultSet resultSet){
        try {
            final UUID uuid = UUID.fromString(resultSet.getString("uuid"));
            final String name = resultSet.getString("name");

            final User user = new User(uuid);
            user.setName(name);

            try {
                user.setFirstIP(InetAddress.getByName(resultSet.getString("firstIP")));
                user.setLastIP(InetAddress.getByName(resultSet.getString("lastIP")));
            } catch (final UnknownHostException ignored) {}

            user.setFirstPlay(resultSet.getLong("firstPlay"));
            user.setLastPlay(resultSet.getLong("lastPlay"));
            user.setMuteDate(resultSet.getLong("muteDate"));
            user.setMuteReason(resultSet.getString("muteReason"));

            user.tryUnMute();

            this.usersByUUID.put(uuid, user);
            this.usersByName.put(name, user);
        } catch (final SQLException exception) {
            this.logger.exception(exception, "Error while trying to load user.");
        }
    }

    public synchronized void saveUser(final User user){
        this.databaseProvider.query(
                new StringBuilder()
                        .append("UPDATE `basictools_users` SET ")
                        .append("name = '" + user.getName() + "', ")
                        .append("firstIP = '" + user.getFirstIP().getHostAddress() + "', ")
                        .append("lastIP = '" + user.getLastIP().getHostAddress() + "', ")
                        .append("firstPlay = '" + user.getFirstPlay() + "', ")
                        .append("lastPlay = '" + user.getLastPlay() + "', ")
                        .append("muteDate = '" + user.getMuteDate() + "', ")
                        .append("muteReason = '" + user.getMuteReason() + "' ")
                        .append("WHERE uuid = '" + user.getUUID().toString() + "';")
                        .toString(),
                false
        );
        user.setChanged(false);
    }

    public synchronized void saveUsers(){
        final Set<User> users = this.usersByUUID
                .values()
                .stream()
                .filter(User::isChanged)
                .collect(Collectors.toSet());

        if(users.isEmpty()) {
            return;
        }
        this.logger.info("Saving users...");

        users.stream().forEach(this::saveUser);

        this.logger.info("Saved " + users.size() + " users.");
    }

    public Set<User> getUsers(final InetAddress address) {
        return this.usersByUUID
                .values()
                .stream()
                .filter(user -> user.getLastIP().getHostAddress().equalsIgnoreCase(address.getHostAddress()))
                .collect(Collectors.toSet());
    }

    public Set<User> getMutedUsers() {
        return this.usersByUUID
                .values()
                .stream()
                .filter(User::isMuted)
                .collect(Collectors.toSet());
    }

    public void updateDynamicInformations(final Player player) {
        final User user = this.getUser(player).isPresent() ? this.getUser(player).get() : null;

        if(user == null) {
            return;
        }
        user.updatePlayer(player);
        user.tryUnMute();
        user.setLastPlay(System.currentTimeMillis());

        if(user.getLastIP() != player.getAddress().getAddress()) {
            user.setLastIP(player.getAddress().getAddress());
        }

        if(user.getName() != player.getName()) {
            user.setName(player.getName());
            this.createUserByName(player);
        }
    }

    public boolean canInteractPlayer(final CommandSender interactor, final Player target) {
        final User user1 = this.getUser(target).isPresent() ? this.getUser(target).get().getOfflinePlayer().isOnline() ? this.getUser(target).get() : null : null;

        if (user1 == null) {
            return false;
        }

        return !user1.getUserCache().isVanished() || PermissionUtil.hasPermission(interactor, "basictools.command.vanish.bypass");
    }

    public boolean canInteractUser(final CommandSender interactor, final User target) {
        if(!target.getOfflinePlayer().isOnline()) {
            return true;
        }
        return this.canInteractPlayer(interactor, target.getPlayer());
    }

    public Set<Player> playersThatCanInteract(final CommandSender interactor) {
        return Bukkit.getOnlinePlayers()
                .stream()
                .filter(player1 -> this.canInteractPlayer(interactor, player1))
                .collect(Collectors.toSet());
    }

    public Set<Player> playersThatCantInteract(final CommandSender interactor) {
        return Bukkit.getOnlinePlayers()
                .stream()
                .filter(player1 -> !this.canInteractPlayer(interactor, player1))
                .collect(Collectors.toSet());
    }

    public Set<User> usersThatCanInteract(final CommandSender interactor) {
        return this.usersByUUID
                .values()
                .stream()
                .filter(user1 -> this.canInteractUser(interactor, user1))
                .collect(Collectors.toSet());
    }

    public void toggleVanishStatus(final Player player, final boolean status){
        if(status){
            this.hidePlayer(player);
            return;
        }
        this.showPlayer(player);
    }

    public void hidePlayer(final Player player) {
        this.vanishedUUIDs.add(player.getUniqueId());
        Bukkit.getOnlinePlayers()
                .stream()
                .filter(player1 -> player != player1 && !PermissionUtil.hasPermission(player1, "basictools.command.vanish.bypass"))
                .forEach(player1 -> player1.hidePlayer(player));
    }

    public void showPlayer(final Player player) {
        if (!this.vanishedUUIDs.contains(player.getUniqueId())) {
            return;
        }
        this.vanishedUUIDs.remove(player.getUniqueId());
        Bukkit.getOnlinePlayers()
                .stream()
                .filter(player1 -> !player1.canSee(player))
                .forEach(player1 -> player1.showPlayer(player));
    }

    public void hideVanishedPlayers(final Player player) {
        if(PermissionUtil.hasPermission(player, "basictools.command.vanish.bypass")) {
            return;
        }
        this.vanishedUUIDs
                .stream()
                .map(Bukkit::getPlayer)
                .forEach(player::hidePlayer);
    }

    public void sendPrivateMessage(final Player sender, final Player target, final String message) {
        this.messages.messageFormat.send(sender,
                "{NICK1}", sender.getName(),
                "{NICK2}", target.getName(),
                "{ARROW}", this.messages.arrowRight,
                "{MESSAGE}", message
        );

        this.messages.messageFormat.send(target,
                "{NICK1}", target.getName(),
                "{NICK2}", sender.getName(),
                "{ARROW}", this.messages.arrowLeft,
                "{MESSAGE}", message
        );

        this.sendSocialSpy(sender, target, message);
    }

    public void sendSocialSpy(final Player sender, final Player target, final String message) {
        this.usersByUUID
                .values()
                .stream()
                .filter(user -> user.isOnline() && user.getUserCache().isSocialspying())
                .map(User::getPlayer)
                .forEach(player -> this.messages.socialSpyFormat.send(player, "{SENDER}", sender.getName(), "{RECIVER}", target.getName(), "{MESSAGE}", message));
    }

    public synchronized void updatePanelInventory(final Player player) {
        final User user = getUser(player).isPresent() ? getUser(player).get() : null;

        if(user == null || !PermissionUtil.hasPermission(player, "basictools.command.panel")) {
            return;
        }

        new AsynchronouslyTask() {
            @Override
            public void onDisable() {
            }

            @Override
            public void onExecute() {
                final Set<CommandProvider> commandsSet = CommandManager.getCommandsByJavaPlugin(BasicToolsPlugin.getPlugin())
                        .stream()
                        .filter(command -> command.getClass() != BasicToolsCommand.class)
                        .collect(Collectors.toSet());
                final Rows rows = Rows.getByColumns(config.panelInventoryTemplate.size()).isPresent() ? Rows.getByColumns(config.panelInventoryTemplate.size()).get() : Rows.SIX;
                final InventoryProvider panelInventory = new ChestInventory(rows)
                        .setTitle(config.panelInventoryTitle)
                        .setInventoryTemplate(config.panelInventoryTemplate);

                commandsSet
                        .stream()
                        .forEach(command -> {
                            final CommandIcon commandIcon = commands.getFieldValue(command.getClass().getSimpleName().substring(0, 1).toLowerCase() + command.getClass().getSimpleName().substring(1) + "Icon", CommandIcon.class);

                            if(!commandIcon.isInGui()) {
                                return;
                            }

                            panelInventory.fillFirstFreeField(commandIcon.getField(),
                                    new ClickableItem(
                                            commandIcon.getItemBuilder().clone()
                                                    .setName(StringUtil.replace(config.commandTemplateName, "{NAME}", command.getName()))
                                                    .setLore(StringUtil.replace(config.commandTemplateLore, "{NAME}", command.getName(), "{USAGE}", command.getUsage(), "{PERMISSION}", command.getPermission(), "{ACCESS}", PermissionUtil.hasPermission(player, command.getPermission()) ? rarstAPIMessages.true_ : rarstAPIMessages.false_))
                                                    .addFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE)
                                                    .build()
                                    ).onClick(inventoryClickEvent -> {
                                        final InventoryProvider anvilInventory = new AnvilInventory(config.argumentsInventoryDefaultText)
                                                .setTitle(StringUtil.replace(config.argumentsInventoryTitle, "{NAME}", command.getName()))
                                                .setItem(0,
                                                        new ItemBuilder(Material.PAPER)
                                                                .setLore(StringUtil.replace(config.paperTemplateLore, "{NAME}", command.getName(), "{USAGE}", command.getUsage()))
                                                )
                                                .onComplete((player1, text) -> {
                                                    command.onExecute(player, text.split(" "));

                                                    new LaterTask(40L) {
                                                        @Override
                                                        public void onDisable() {
                                                        }

                                                        @Override
                                                        public void onExecute() {
                                                            user.getUserCache().openPanelInventory();
                                                        }
                                                    }
                                                    .once()
                                                    .register();

                                                    return null;
                                                })
                                                .build();
                                        anvilInventory.openInventory(player);
                                    }));
                        });
                config.getParsedTemplateExplanation()
                        .entrySet()
                        .stream()
                        .forEach(entrySet -> panelInventory.fill(entrySet.getKey(), entrySet.getValue()));
                user.getUserCache().setPanelInventory(panelInventory.build());
            }
        }
        .once()
        .register();
    }

}
