package me.rarstman.basictools.data;

import me.rarstman.basictools.cache.UserCache;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.net.InetAddress;
import java.util.UUID;

public class User {

    private final UUID uuid;
    private String name;
    private InetAddress firstIP;
    private InetAddress lastIP;
    private long firstPlay;
    private long lastPlay;
    private Long muteDate;
    private String muteReason;
    private final UserCache userCache;
    private boolean isChanged = false;

    public User(final UUID uuid) {
        this.uuid = uuid;
        this.userCache = new UserCache();
    }

    public boolean isChanged() {
        return this.isChanged;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
        this.markChanged();
    }

    public InetAddress getFirstIP() {
        return this.firstIP;
    }

    public void setFirstIP(final InetAddress firstIP) {
        this.firstIP = firstIP;
        this.markChanged();
    }

    public InetAddress getLastIP() {
        return this.lastIP;
    }

    public void setLastIP(final InetAddress lastIP) {
        this.lastIP = lastIP;
        this.markChanged();
    }

    public Long getMuteDate() {
        return this.muteDate;
    }

    public void setMuteDate(final Long muteDate) {
        this.muteDate = muteDate;
    }

    public String getMuteReason() {
        return this.muteReason;
    }

    public void setMuteReason(final String muteReason) {
        this.muteReason = muteReason;
    }

    public long getFirstPlay() {
        return this.firstPlay;
    }

    public long getLastPlay() {
        return this.lastPlay;
    }

    public boolean isMuted() {
        return this.muteDate != null;
    }

    public UserCache getUserCache() {
        return this.userCache;
    }

    private void markChanged() {
        this.isChanged = true;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }

    public OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(this.uuid);
    }
}
