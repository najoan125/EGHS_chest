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

public class EGHS_fishing implements Listener {
    private YamlUtil yamlUtil = new YamlUtil();
    private ArrayList<Inventory> fishBackpack = new ArrayList<>();


    public void openFishBackpack(Player p) {
        Inventory fish = null;
        try {
            fish = yamlUtil.loadFromYaml(p, "fish_backpack", 1, p.getName()+"§f님의 §9낚시 §6가방");
        } catch (IOException | InvalidConfigurationException e) {
            fish = Bukkit.createInventory(p, 54, p.getName()+"§f님의 §9낚시 §6가방");
        } finally {
            p.openInventory(fish);
            fishBackpack.add(fish);
        }
    }

    @EventHandler
    public void fishCloseEvent(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        try {
            if (fishBackpack.contains(e.getInventory())) {
                yamlUtil.saveToYaml(p, e.getInventory(), "fish_backpack");
                fishBackpack.remove(e.getInventory());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    @EventHandler
    public void fishClickEvent1(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getView().getTitle().equals(p.getName()+"§f님의 §9낚시 §6가방")) {
            if (e.getCurrentItem() == null || e.getCurrentItem().getTypeId() == 0) {
                e.setCancelled(false);
            } else {
                Material currentMaterial = e.getCurrentItem().getType();
                if (currentMaterial.equals(Material.RAW_FISH)) {
                    ItemMeta itemMeta = e.getCurrentItem().getItemMeta();
                    if (itemMeta.hasDisplayName() && itemMeta.getDisplayName().replaceAll("§\\S", "").substring(0,2).matches("^\\[\\S+$")) {
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
