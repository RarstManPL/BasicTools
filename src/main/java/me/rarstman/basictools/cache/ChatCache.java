package me.rarstman.basictools.cache;

public class ChatCache {

    private boolean enabled;

    public ChatCache() {
        this.enabled = true;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

}
