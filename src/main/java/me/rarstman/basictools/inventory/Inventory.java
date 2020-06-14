package me.rarstman.basictools.inventory;

import me.rarstman.basictools.inventory.chest.Rows;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public interface Inventory {

    void openInventory(final Player player);
    Inventory build();
    default Inventory addClickableItem(final int slot, final ClickableItem clickableItem) {return this;}
    default Inventory addClickableItem(final ClickableItem clickableItem) {return this;};
    default Inventory addClickableItems(final Set<ClickableItem> clickableItems) {return this;};
    default Inventory fillWithClickableItem(final String place, final ClickableItem clickableItem) {return this;}
    Inventory whenClosing(final Consumer<InventoryCloseEvent> inventoryCloseEventConsumer);
    Inventory setTitle(final String title);
    Inventory setRows(final Rows rows);
    default Inventory setTemplate(final InventoryTemplate inventoryTemplate) {return this;}
}
