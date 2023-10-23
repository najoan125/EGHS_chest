package eghs_chest.eghs_chest;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class YamlUtil {
    public void saveToYaml(Player p, Inventory inventory, String... chests) throws IOException {
        UUID playeruuid = p.getPlayer().getUniqueId();
        for (String s : chests) {
            File file = new File(EGHS_chest.dataFolder, s + "/" + playeruuid + ".yml");

            YamlConfiguration yaml = new Utf8YamlConfiguration();
            int inventorySize = inventory.getSize();
            yaml.set("size", Integer.valueOf(inventorySize));
            ConfigurationSection items = yaml.createSection("items");
            for (int slot = 0; slot < inventorySize; slot++) {
                ItemStack stack = inventory.getItem(slot);
                if (stack != null)
                    items.set(String.valueOf(slot), stack);
            }
            yaml.save(file);
        }
    }

    public Inventory loadFromYaml(Player p, String chest, int row, String title) throws IOException, InvalidConfigurationException {
        UUID playeruuid = p.getPlayer().getUniqueId();
        File file = new File(EGHS_chest.dataFolder, chest + "/" + playeruuid + ".yml");

        YamlConfiguration yaml = new Utf8YamlConfiguration();
        yaml.load(file);
        int inventorySize = yaml.getInt("size", row*9);
        Inventory inventory = Bukkit.getServer().createInventory(null, inventorySize, title);
        ConfigurationSection items = yaml.getConfigurationSection("items");
        for (int slot = 0; slot < inventorySize; slot++) {
            String slotString = String.valueOf(slot);
            if (items.isItemStack(slotString)) {
                ItemStack itemStack = items.getItemStack(slotString);
                inventory.setItem(slot, itemStack);
            }
        }
        return inventory;
    }
}
