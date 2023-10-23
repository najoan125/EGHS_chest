package com.eghs.chest.inventories;

import com.eghs.chest.EGHS_chest;
import com.eghs.chest.utils.InventoryUtil;
import com.eghs.chest.utils.YamlUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.io.IOException;

public class InventoryChestShop implements InventoryInterface {
    public void openInventory(Player p) {
        Inventory chest = Bukkit.createInventory(null, 27, "§f라나의 창고 상점");
        for (int n = 0; n < 27; n++) {
            chest.setItem(n, InventoryUtil.newItemStack(Material.STAINED_GLASS_PANE, 0, "§f"));
        }
        chest.setItem(11, InventoryUtil.newItemStack(Material.CHEST, 0, "§6창고 §c1 §f페이지", "§f클릭 시 창고 1페이지를 엽니다."));
        chest.setItem(15, InventoryUtil.newItemStack(Material.CHEST, 0, "§6창고 §c2 §f페이지", "§f클릭 시 창고 2페이지를 엽니다."));
        chest.setItem(22, InventoryUtil.newItemStack(Material.END_CRYSTAL, 0, "§6창고 §c업그레이드", "§f클릭 시 창고 업그레이드 창을 엽니다."));
        p.openInventory(chest);
    }

    @Override
    public void inventoryClickEvent(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        e.setCancelled(true);
        if (e.getRawSlot() == 22) {
            p.setOp(true);
            p.performCommand("상점 열기 창고-업그레이드-상점");
            p.setOp(false);
        }

        if (e.getRawSlot() == 11) {
            p.closeInventory();
            InventoryChest.openInventory(p, 1);
        } else if (e.getRawSlot() == 15) {
            try {
                if (YamlUtil.getInventoryRows(p, "chest2") >= 1) {
                    p.closeInventory();
                    InventoryChest.openInventory(p, 2);
                }
            } catch (IOException | InvalidConfigurationException ex) {
                p.sendMessage(EGHS_chest.WARNING + "창고 2-1 개방권을 사용하지 않아 2페이지 사용이 불가능합니다!");
            }
        }
    }

    @Override
    public void inventoryCloseEvent(InventoryCloseEvent e) {

    }

    @Override
    public boolean contains(Inventory inventory) {
        return false;
    }
}
