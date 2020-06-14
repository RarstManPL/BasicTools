package me.rarstman.basictools.cache;

public class ChatCache {

    private boolean isEnabled;

    public ChatCache() {
        this.isEnabled = true;
    }

    public boolean isEnabled() {
        return this.isEnabled;
    }

    public void setEnabled(final boolean isEnabled) {
        this.isEnabled = isEnabled;
    }
}
