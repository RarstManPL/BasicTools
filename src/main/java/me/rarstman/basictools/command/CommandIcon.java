package me.rarstman.basictools.command;

import me.rarstman.rarstapi.item.ItemBuilder;
import org.bukkit.Material;

public class CommandIcon {

    private final ItemBuilder itemBuilder;
    private final String field;
    private final boolean inGui;

    public CommandIcon(final ItemBuilder itemBuilder, final String field, final boolean inGui) {
        this.itemBuilder = itemBuilder;
        this.field = field;
        this.inGui = inGui;
    }

    public CommandIcon(final Material material, final String field, final boolean inGui) {
        this(new ItemBuilder(material), field, inGui);
    }

    public CommandIcon(final String item, final String field, final boolean inGui) {
        this(new ItemBuilder(item), field, inGui);
    }

    public ItemBuilder getItemBuilder() {
        return this.itemBuilder;
    }

    public String getField() {
        return this.field;
    }

    public boolean isInGui() {
        return this.inGui;
    }

}
