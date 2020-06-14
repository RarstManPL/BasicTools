package me.rarstman.basictools.data;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.net.InetAddress;
import java.util.*;
import java.util.stream.Collectors;

public class UserManager {

    private final Map<UUID, User> usersByUUID = new HashMap<>();
    private final Map<String, User> usersByName = new HashMap<>();
    private final Set<UUID> vanishedUUIDs = new HashSet<>();

    public Optional<User> getUser(final Player player) {
        return Optional.of(this.usersByUUID.containsKey(player.getUniqueId()) ? this.usersByUUID.get(player.getUniqueId()) : this.usersByName.containsKey(player.getName()) ? this.usersByName.get(player.getName()) : null);
    }

    public Optional<User> getUser(final String name) {
        return Optional.of(this.usersByName.get(name));
    }

    public Set<User> getUsers(final InetAddress address) {
        return this.usersByUUID
                .values()
                .stream()
                .filter(user -> user.getLastIP() == address)
                .collect(Collectors.toSet());
    }

    public Set<User> getMutedUsers() {
        return this.usersByUUID
                .values()
                .stream()
                .filter(user -> user.isMuted())
                .collect(Collectors.toSet());
    }

    public boolean canInteractPlayer(final CommandSender interactor, final Player target) {
        final User user1 = this.getUser(target).isPresent() ? this.getUser(target).get().getOfflinePlayer().isOnline() ? this.getUser(target).get() : null : null;

        if (user1 == null) {
            return false;
        }

        return !user1.getUserCache().isVanished();
    }

    public Set<Player> playersThatCanInteract(final CommandSender interactor) {
        return Bukkit.getOnlinePlayers()
                .stream()
                .filter(player1 -> this.canInteractPlayer(interactor, player1))
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
                .filter(player1 -> player != player1)
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

    public Set<Player> getVanishedPlayers() {
        return this.vanishedUUIDs
                .stream()
                .map(Bukkit::getPlayer)
                .collect(Collectors.toSet());
    }
}
