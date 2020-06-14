package me.rarstman.basictools.cache;

import me.rarstman.basictools.data.User;
import me.rarstman.basictools.inventory.Inventory;

import java.util.Optional;

public class UserCache {

    private User lastWriter;
    private boolean isVanished;
    private boolean isGoded;
    private boolean isSocialspying;
    private Inventory panelInventory;

    public UserCache() {
        this.isVanished = false;
        this.isGoded = false;
        this.isSocialspying = false;
    }

    public Optional<User> getLastWriter() {
        return Optional.of(this.lastWriter);
    }

    public void setLastWriter(final User lastWriter) {
        this.lastWriter = lastWriter;
    }

    public boolean isVanished() {
        return this.isVanished;
    }

    public void setVanished(final boolean isVanished) {
        this.isVanished = isVanished;
    }

    public boolean isGoded() {
        return this.isGoded;
    }

    public void setGoded(final boolean isGoded) {
        this.isGoded = isGoded;
    }

    public boolean isSocialspying() {
        return this.isSocialspying;
    }

    public void setSocialspying(final boolean isSocialspying) {
        this.isSocialspying = isSocialspying;
    }
}
