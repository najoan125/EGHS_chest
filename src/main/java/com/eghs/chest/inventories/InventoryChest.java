package com.eghs.chest.inventories;

import com.eghs.chest.utils.YamlUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.io.IOException;
import java.util.HashMap;

public class InventoryChest implements InventoryInterface {
    private static final HashMap<Inventory,Integer> inventories = new HashMap<>();

    public static void openInventory(Player p, int page) {
        Inventory chestPage = null;
        try {
            chestPage = YamlUtil.loadInventory(p, "chest"+page, 1, p.getName()+"의 창고 §6"+page+"페이지");
        } catch (IOException | InvalidConfigurationException e) {
            chestPage = Bukkit.createInventory(p, 9, p.getName()+"의 창고 §6"+page+"페이지");
        } finally {
            p.openInventory(chestPage);
            inventories.put(chestPage,page);
        }
    }

    @Override
    public void inventoryClickEvent(InventoryClickEvent e) {
    }

    @Override
    public void inventoryCloseEvent(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        try {
            YamlUtil.save(p, e.getInventory(), "chest"+inventories.get(e.getInventory()));
            inventories.remove(e.getInventory());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean contains(Inventory inventory) {
        return inventories.containsKey(inventory);
    }
}
