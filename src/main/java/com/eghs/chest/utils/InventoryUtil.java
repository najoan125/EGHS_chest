package com.eghs.chest.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class InventoryUtil {
    public static ItemStack newItemStack(Material material, int num, String name, String... lore) {
        ItemStack chestItem = new ItemStack(material, 1, (short) num);
        ItemMeta chestMeta = chestItem.getItemMeta();

        chestMeta.setDisplayName(name);
        chestMeta.setLore(Arrays.asList(lore));

        chestItem.setItemMeta(chestMeta);
        return chestItem;
    }

    public static ItemStack newItemStack(Material material, int num, String name) {
        ItemStack chestItem = new ItemStack(material, 1, (short) num);
        ItemMeta chestMeta = chestItem.getItemMeta();

        chestMeta.setDisplayName(name);
        chestMeta.setLore(null);

        chestItem.setItemMeta(chestMeta);
        return chestItem;
    }
}
