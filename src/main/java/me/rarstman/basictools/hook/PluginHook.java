package me.rarstman.basictools.hook;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PluginHook {

    private final Map<String, Hook> pluginHooks = new HashMap<>();

    public void initialize() {
        Arrays.asList(
                new VaultHook()
        ).forEach(hook -> this.pluginHooks.put(hook.getName(), hook));
    }

    public Optional<Hook> getHook(final String name) {
        return Optional.of(this.pluginHooks.get(name));
    }

    public interface Hook {
        boolean isInitialized();

        void initialize();

        String getName();
    }
}
