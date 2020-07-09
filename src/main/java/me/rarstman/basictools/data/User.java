package me.rarstman.basictools.data;

import me.rarstman.basictools.cache.UserCache;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.util.UUID;

public class User {

    private final UUID uuid;
    private final UserCache userCache;
    private String name;
    private InetAddress firstIP;
    private InetAddress lastIP;
    private long firstPlay;
    private long lastPlay;
    private long muteDate;
    private String muteReason;
    private WeakReference<Player> player;
    private boolean changed = false;

    public User(final UUID uuid) {
        this.uuid = uuid;
        this.player = new WeakReference<>(Bukkit.getPlayer(this.uuid));
        this.userCache = new UserCache(this);
    }

    public void setName(final String name) {
        this.name = name;
        this.markChanged();
    }

    public void setFirstIP(final InetAddress firstIP) {
        this.firstIP = firstIP;
        this.markChanged();
    }

    public void setLastIP(final InetAddress lastIP) {
        this.lastIP = lastIP;
        this.markChanged();
    }

    public void setFirstPlay(final long firstPlay) {
        this.firstPlay = firstPlay;
        this.markChanged();
    }

    public void setLastPlay(final long lastPlay) {
        this.lastPlay = lastPlay;
        this.markChanged();
    }

    public void setMuteDate(final long muteDate) {
        this.muteDate = muteDate;
        this.markChanged();
    }

    public void setMuteReason(final String muteReason) {
        this.muteReason = muteReason;
        this.markChanged();
    }

    public void setChanged(final boolean changed) {
        this.changed = changed;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }

    public InetAddress getFirstIP() {
        return this.firstIP;
    }

    public InetAddress getLastIP() {
        return this.lastIP;
    }

    public long getFirstPlay() {
        return this.firstPlay;
    }

    public long getLastPlay() {
        return this.lastPlay;
    }

    public long getMuteDate() {
        return this.muteDate;
    }

    public String getMuteReason() {
        return this.muteReason;
    }

    public UserCache getUserCache() {
        return this.userCache;
    }

    public boolean isMuted() {
        return this.muteDate != 0L;
    }

    public boolean isChanged() {
        return this.changed;
    }

    public boolean isOnline() {
        final OfflinePlayer offlinePlayer = this.getOfflinePlayer();
        return offlinePlayer != null && offlinePlayer.isOnline();
    }

    public void mute(final long time, final String reason) {
        this.setMuteDate(time);
        this.setMuteReason(reason);
    }

    public void unMute() {
        this.setMuteDate(0L);
        this.setMuteReason(null);
    }

    public void tryUnMute() {
        if(this.muteDate != 0L && this.muteDate != -1L && this.muteDate < System.currentTimeMillis()) {
            this.unMute();
        }
    }

    private void markChanged() {
        this.changed = true;
    }

    public Player getPlayer() {
        if(!this.isOnline()) {
            return null;
        }
        Player player = this.player.get();

        if(player != null) {
            return player;
        }
        player = Bukkit.getPlayer(this.uuid);

        if(player == null) {
            return null;
        }
        this.player = new WeakReference<>(player);
        return player;
    }

    public void updatePlayer(final Player player) {
        this.player = new WeakReference<>(player);
    }

    public OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(this.uuid);
    }

}
