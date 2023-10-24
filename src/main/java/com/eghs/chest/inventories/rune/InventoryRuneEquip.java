package com.eghs.chest.inventories.rune;

import com.eghs.chest.inventories.InventoryInterface;
import com.eghs.chest.utils.InventoryUtil;
import com.eghs.chest.utils.YamlUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.ArrayList;

public class InventoryRuneEquip implements InventoryInterface {
    private static final ArrayList<Inventory> inventories = new ArrayList<>();

    public void openInventory(Player p) {
        Inventory rune = null;
        try {
            rune = YamlUtil.loadInventory(p, "rune_equip", 1, p.getName() + "§f님의 §5룬 §6장비창");
        } catch (IOException | InvalidConfigurationException e) {
            rune = Bukkit.createInventory(p, 54, p.getName() + "§f님의 §5룬 §6장비창");
            for (int i = 0; i < 54; i++) {
                rune.setItem(i, InventoryUtil.newItemStack(Material.STAINED_GLASS_PANE, 0, "§f"));
            }
            for (int i : new int[]{13, 21, 23, 30, 32, 40}) {
                rune.setItem(i, new ItemStack(Material.AIR, 1, (short) 0));
            }
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
            YamlUtil.save(p, e.getInventory(), "rune_equip");
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
