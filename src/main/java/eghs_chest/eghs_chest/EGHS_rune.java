package eghs_chest.eghs_chest;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EGHS_rune implements Listener {
    private YamlUtil yamlUtil = new YamlUtil();
    private ArrayList<Inventory> runeInventories = new ArrayList<>();
    private ArrayList<Inventory> runeBackpack = new ArrayList<>();

    public void openRuneEquip(Player p) {
        Inventory rune = null;
        try {
            rune = yamlUtil.loadFromYaml(p, "rune_equip", 1, p.getName()+"§f님의 §5룬 §6장비창");
        } catch (IOException | InvalidConfigurationException e) {
            rune = Bukkit.createInventory(p, 54, p.getName()+"§f님의 §5룬 §6장비창");
            for (int i=0;i<54;i++) {
                rune.setItem(i, SetItemStack(Material.STAINED_GLASS_PANE, 0, "§f", null));
            }
            int[] indexes = new int[]{13,21,23,30,32,40};

            for (int i : indexes) {
                rune.setItem(i, new ItemStack(Material.AIR, 1, (short) 0));
            }


        } finally {
            p.openInventory(rune);
            runeInventories.add(rune);
        }
    }

    public void openRuneBackPack(Player p) {
        Inventory rune = null;
        try {
            rune = yamlUtil.loadFromYaml(p, "rune_backpack", 1, p.getName()+"§f님의 §5룬 §6창고");
        } catch (IOException | InvalidConfigurationException e) {
            rune = Bukkit.createInventory(p, 54, p.getName()+"§f님의 §5룬 §6창고");
        } finally {
            p.openInventory(rune);
            runeBackpack.add(rune);
        }
    }

    @EventHandler
    public void runeCloseEvent(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        try {
            if (runeInventories.contains(e.getInventory())) {
                yamlUtil.saveToYaml(p, e.getInventory(), "rune_equip");
                runeInventories.remove(e.getInventory());
            } else if (runeBackpack.contains(e.getInventory())) {
                yamlUtil.saveToYaml(p, e.getInventory(), "rune_backpack");
                runeBackpack.remove(e.getInventory());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    @EventHandler
    public void runeClickEvent1(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getView().getTitle().equals(p.getName()+"§f님의 §5룬 §6장비창") || e.getView().getTitle().equals(p.getName()+"§f님의 §5룬 §6창고")) {
            if (e.getCurrentItem() == null || e.getCurrentItem().getTypeId() == 0) {
                e.setCancelled(false);
            } else {
                if (e.getCurrentItem().getType().equals(Material.INK_SACK) && e.getCurrentItem().getDurability() == (short)4) {
                    ItemMeta itemMeta = e.getCurrentItem().getItemMeta();
                    if (itemMeta.hasDisplayName() && itemMeta.getDisplayName().replaceAll("§\\S", "").matches("^\\S+의 룬 \\+[0-9]+$")) {
                        e.setCancelled(false);
                    }else {
                        e.setCancelled(true);
                    }
                } else {
                    e.setCancelled(true);
                }
            }
        }
    }

    ItemStack SetItemStack(Material material, int num, String nametxt, List<String> loretxt) {
        ItemStack chestItem = new ItemStack(material, 1, (short) num);
        ItemMeta chestMeta = chestItem.getItemMeta();
        chestMeta.setDisplayName(nametxt);
        chestMeta.setLore(loretxt);
        chestItem.setItemMeta(chestMeta);

        return chestItem;
    }
}
