package com.eghs.chest.inventories;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public interface InventoryInterface {
    void inventoryClickEvent(InventoryClickEvent e);
    void inventoryCloseEvent(InventoryCloseEvent e);
    boolean contains(Inventory inventory);
}
