package com.eghs.chest.inventories;

import com.eghs.chest.utils.YamlUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.ArrayList;

public class InventoryFishing implements InventoryInterface {
    private static final ArrayList<Inventory> inventories = new ArrayList<>();

    public void openInventory(Player p) {
        Inventory fish = null;
        try {
            fish = YamlUtil.loadInventory(p, "fish_backpack", 1, p.getName() + "§f님의 §9낚시 §6가방");
        } catch (IOException | InvalidConfigurationException e) {
            fish = Bukkit.createInventory(p, 54, p.getName() + "§f님의 §9낚시 §6가방");
        } finally {
            p.openInventory(fish);
            inventories.add(fish);
        }
    }

    @Override
    public void inventoryClickEvent(InventoryClickEvent e) {
        Material currentMaterial = e.getCurrentItem().getType();
        if (e.getAction() == InventoryAction.HOTBAR_SWAP || e.getAction() == InventoryAction.HOTBAR_MOVE_AND_READD) {
            e.setCancelled(true);
        } else if (currentMaterial.equals(Material.RAW_FISH)) {
            ItemMeta itemMeta = e.getCurrentItem().getItemMeta();
            e.setCancelled(!itemMeta.hasDisplayName() || !itemMeta.getDisplayName().replaceAll("§\\S", "").substring(0, 2).matches("^\\[\\S+$"));
        } else {
            e.setCancelled(true);
        }
    }

    @Override
    public void inventoryCloseEvent(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        try {
            YamlUtil.save(p, e.getInventory(), "fish_backpack");
            inventories.remove(e.getInventory());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean contains(Inventory inventory) {
        return inventories.contains(inventory);
    }
}
