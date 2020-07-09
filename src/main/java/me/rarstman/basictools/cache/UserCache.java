package me.rarstman.basictools.cache;

import me.rarstman.basictools.data.User;
import me.rarstman.rarstapi.inventory.InventoryProvider;
import org.bukkit.entity.Player;

import java.util.Optional;

public class UserCache {

    private final User user;
    private User lastWriter;
    private boolean vanished;
    private boolean goded;
    private boolean socialspying;
    private boolean blockMessages;
    private InventoryProvider panelInventory;

    public UserCache(final User user) {
        this.user = user;
        this.vanished = false;
        this.goded = false;
        this.socialspying = false;
        this.blockMessages = false;
    }

    public Optional<User> getLastWriter() {
        return Optional.ofNullable(this.lastWriter);
    }

    public void setLastWriter(final User lastWriter) {
        this.lastWriter = lastWriter;
    }

    public boolean isVanished() {
        return this.vanished;
    }

    public void setVanished(final boolean vanished) {
        this.vanished = vanished;
    }

    public boolean isGoded() {
        return this.goded;
    }

    public void setGoded(final boolean goded) {
        this.goded = goded;
    }

    public boolean isSocialspying() {
        return this.socialspying;
    }

    public void setSocialspying(final boolean socialspying) {
        this.socialspying = socialspying;
    }

    public boolean isBlockMessages() {
        return this.blockMessages;
    }

    public void setBlockMessages(final boolean blockMessages) {
        this.blockMessages = blockMessages;
    }

    public void setPanelInventory(final InventoryProvider panelInventory) {
        this.panelInventory = panelInventory;
    }

    public boolean openPanelInventory() {
        final Player player = this.user.getPlayer();

        if(player == null | this.panelInventory == null) {
            return false;
        }
        this.panelInventory.openInventory(player);
        return true;
    }

    public User getUser() {
        return this.user;
    }
}
