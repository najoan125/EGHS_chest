package com.eghs.chest.inventories.rune;

import com.eghs.chest.inventories.InventoryInterface;
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

public class InventoryRuneBackpack implements InventoryInterface {
    private static final ArrayList<Inventory> inventories = new ArrayList<>();

    public void openInventory(Player p) {
        Inventory rune = null;
        try {
            rune = YamlUtil.loadInventory(p, "rune_backpack", 1, p.getName() + "§f님의 §5룬 §6창고");
        } catch (IOException | InvalidConfigurationException e) {
            rune = Bukkit.createInventory(p, 54, p.getName() + "§f님의 §5룬 §6창고");
        } finally {
            p.openInventory(rune);
            inventories.add(rune);
        }
    }

    @Override
    public void inventoryClickEvent(InventoryClickEvent e) {
        if (e.getAction() == InventoryAction.HOTBAR_SWAP || e.getAction() == InventoryAction.HOTBAR_MOVE_AND_READD) {
            e.setCancelled(true);
        } else if (e.getCurrentItem().getType().equals(Material.INK_SACK) && e.getCurrentItem().getDurability() == (short) 4) {
            ItemMeta itemMeta = e.getCurrentItem().getItemMeta();
            e.setCancelled(!itemMeta.hasDisplayName() || !itemMeta.getDisplayName().replaceAll("§\\S", "").matches("^\\S+의 룬 \\+[0-9]+$"));
        } else {
            e.setCancelled(true);
        }
    }

    @Override
    public void inventoryCloseEvent(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        try {
            YamlUtil.save(p, e.getInventory(), "rune_backpack");
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
