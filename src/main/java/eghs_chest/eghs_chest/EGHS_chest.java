package eghs_chest.eghs_chest;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public final class EGHS_chest extends JavaPlugin implements Listener, CommandExecutor {

    public static final String PREFIX = "§f:: §aEGHS §f:: ";
    public static final String WARNING = "§f[ §c경고! §f] ";

    public static final String INFOR = "§f[ §6안내 §f] ";

    private final YamlUtil yamlUtil = new YamlUtil();
    private final EGHS_rune rune = new EGHS_rune();
    private final EGHS_fishing fishing = new EGHS_fishing();
    public static File dataFolder;

    HashMap<Inventory,Integer> inventories = new HashMap<>();

    ItemStack SetItemStack(Material material, int num, String nametxt, List<String> loretxt) {
        ItemStack chestItem = new ItemStack(material, 1, (short) num);
        ItemMeta chestMeta = chestItem.getItemMeta();
        chestMeta.setDisplayName(nametxt);
        chestMeta.setLore(loretxt);
        chestItem.setItemMeta(chestMeta);

        return chestItem;
    }

    public void createFolder(String... chests){
        try{
            File file = null;
            for (String s : chests) {
                file = new File(getDataFolder(), "/"+s);
                Files.createDirectories(Paths.get(file.getPath()));
                System.out.println("성공적으로 폴더 "+s+" 가 생성되었습니다.");
            }
        } catch (IOException e) {
            System.out.println("폴더 생성에 실패하였습니다 : " + e.getMessage());
        }
    }

    public int getInventoryRowsFromYaml(Player p, String chest) throws IOException, InvalidConfigurationException {
        UUID playeruuid = p.getPlayer().getUniqueId();
        File file = new File(getDataFolder(), chest + "/" + playeruuid + ".yml");

        YamlConfiguration yaml = new Utf8YamlConfiguration();
        yaml.load(file);
        return yaml.getInt("size", 9) / 9;
    }

    public void setInventorySizeToYaml(Player p, String chest, int row) throws IOException, InvalidConfigurationException {
        UUID playeruuid = p.getPlayer().getUniqueId();
        File file = new File(getDataFolder(), chest + "/" + playeruuid + ".yml");

        YamlConfiguration yaml = new Utf8YamlConfiguration();
        yaml.load(file);
        yaml.set("size", Integer.valueOf(row*9));
        yaml.save(file);
    }



    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(rune, this);
        getServer().getPluginManager().registerEvents(fishing, this);
        getCommand("아이템저장").setExecutor(this);
        createFolder("chest1", "chest2", "rune_equip", "rune_backpack", "fish_backpack");
        dataFolder = getDataFolder();
    }

    @Override
    public void onDisable() {


    }

    @EventHandler
    public void chestNPCInteract(PlayerInteractEntityEvent e){
        Inventory chest;
        Entity a = e.getRightClicked();
        if (a.getName().equals("창고지기 라나")){
            Player p = e.getPlayer();
            chest = Bukkit.createInventory(null, 27, "§f라나의 창고 상점");
            for (int n = 0; n < 27; n++) {
                chest.setItem(n, SetItemStack(Material.STAINED_GLASS_PANE, 0, "§f", null));
            }
            chest.setItem(11, SetItemStack(Material.CHEST, 0, "§6창고 §c1 §f페이지", Arrays.asList("§f클릭 시 창고 1페이지를 엽니다.")));
            chest.setItem(15, SetItemStack(Material.CHEST, 0, "§6창고 §c2 §f페이지", Arrays.asList("§f클릭 시 창고 2페이지를 엽니다.")));
            chest.setItem(22, SetItemStack(Material.END_CRYSTAL, 0, "§6창고 §c업그레이드", Arrays.asList("§f클릭 시 창고 업그레이드 창을 엽니다.")));
            p.openInventory(chest);

        }else {


        }


    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;
        if (args.length == 0) {
            if (p.isOp()) {
                p.sendMessage(PREFIX + "/아이템저장 창고1 - 창고1 (을)를 오픈합니다. §c[OP]");
                p.sendMessage(PREFIX + "/아이템저장 창고2 - 창고2 (을)를 오픈합니다. §c[OP]");
                p.sendMessage(PREFIX + "/아이템저장 룬장비 - 룬 장비 (을)를 오픈합니다. §c[OP]");
                p.sendMessage(PREFIX + "/아이템저장 룬가방 - 룬 가방 (을)를 오픈합니다. §c[OP]");
                p.sendMessage(PREFIX + "/아이템저장 낚시가방 - 낚시 가방 (을)를 오픈합니다. §c[OP]");
                p.sendMessage(PREFIX + "/아이템저장 확인 창고1 [닉네임] - [닉네임]의 창고1 (을)를 확인합니다. §c[OP]");
                p.sendMessage(PREFIX + "/아이템저장 확인 창고2 [닉네임] - [닉네임]의 창고2 (을)를 확인합니다. §c[OP]");
                p.sendMessage(PREFIX + "/아이템저장 확인 룬장비 [닉네임] - [닉네임]의 룬 장비 (을)를 확인합니다. §c[OP]");
                p.sendMessage(PREFIX + "/아이템저장 확인 룬가방 [닉네임] - [닉네임]의 룬 가방 (을)를 확인합니다. §c[OP]");
                p.sendMessage(PREFIX + "/아이템저장 확인 낚시가방 [닉네임] - [닉네임]의 낚시 가방 (을)를 확인합니다. §c[OP]");
            } else {
                p.sendMessage("§fUnknown Command. Type \"/help\" for help.");
            }
            return false;
        }
        if (args[0].equals("창고1")) {
            openChest(p, 1);
        } else if (args[0].equals("창고2")) {
            openChest(p, 2);
        } else if (args[0].equals("룬장비")) {
            rune.openRuneEquip(p);
        } else if (args[0].equals("룬가방")) {
            rune.openRuneBackPack(p);
        } else if (args[0].equals("낚시가방")) {
            fishing.openFishBackpack(p);
        }
        return false;
    }

    @EventHandler
    public void chestClickEvent1(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getView().getTitle().equals("§f라나의 창고 상점")) {
            if (e.getCurrentItem() == null || e.getCurrentItem().getTypeId() == 0) {
                e.setCancelled(false);
            } else {
                e.setCancelled(true);
                if (e.getRawSlot() == 22) {
                    p.setOp(true);
                    p.performCommand("상점 열기 창고-업그레이드-상점");
                    p.setOp(false);
                }
            }
            if (e.getRawSlot() == 11){
                p.closeInventory();
                openChest(p, 1);
            } else if (e.getRawSlot() == 15) {
                try {
                    if (getInventoryRowsFromYaml(p, "chest2") >= 1) {
                        p.closeInventory();
                        openChest(p, 2);
                    }
                } catch (IOException | InvalidConfigurationException ex) {
                    p.sendMessage(WARNING+"창고 2-1 개방권을 사용하지 않아 2페이지 사용이 불가능합니다!");
                }
            }
        }
    }

    @EventHandler
    public void chestCloseEvent(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        try {
            if (inventories.containsKey(e.getInventory())) {
                yamlUtil.saveToYaml(p, e.getInventory(), "chest"+inventories.get(e.getInventory()));
                inventories.remove(e.getInventory());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    @EventHandler
    public void chestPaperRClick(PlayerInteractEvent e){
        Player p = e.getPlayer();
        ItemStack itemStack = p.getInventory().getItemInMainHand();
        String itemName;

        if (e.getAction().toString().contains("RIGHT_CLICK")) {
            if (itemStack == null) return;
            if (itemStack.getItemMeta() == null || !itemStack.getItemMeta().hasDisplayName()) return;
            itemName = itemName = itemStack.getItemMeta().getDisplayName().replaceAll("§\\S" ,"");

            if (itemStack.getType() == Material.PAPER && itemName.matches("^창고 [0-9]-[0-9] 개방권$")) {
                String info = itemName.split(" ")[1];
                int page = Integer.parseInt(info.split("-")[0]);
                int row = Integer.parseInt(info.split("-")[1]);
                try {
                    UUID playeruuid = p.getPlayer().getUniqueId();
                    File file = new File(getDataFolder(), "chest2" + "/" + playeruuid + ".yml");
                    if (!file.exists() && getInventoryRowsFromYaml(p, "chest1") == 6 && page == 2 && row == 1) {
                        Inventory temp = Bukkit.createInventory(p, 9, p.getName()+"의 창고 "+page+"페이지");
                        yamlUtil.saveToYaml(p, temp, "chest2");
                        itemStack.setAmount(itemStack.getAmount()-1);
                        p.sendMessage(INFOR+"성공적으로 창고 "+page+"페이지의 "+row+"줄이 개방되었습니다!");
                    }else if (page == 2 && row == 1 && !file.exists()) {
                        p.sendMessage(WARNING+"아직 창고 1-6 개방권을 사용하지 않아 2-1 개방권의 사용이 불가능합니다!");
                    }
                    else if (getInventoryRowsFromYaml(p, "chest"+page) == row - 1) {
                        itemStack.setAmount(itemStack.getAmount()-1);
                        p.getInventory().setItemInMainHand(itemStack);
                        setInventorySizeToYaml(p, "chest"+page, row);
                        p.sendMessage(INFOR+"성공적으로 창고 "+page+"페이지의 "+row+"줄이 개방되었습니다!");
                    }
                    else {
                        p.sendMessage(WARNING+"이 개방권을 이미 사용하셨거나, 사용할 권한이 없습니다!");
                    }
                } catch (IOException | InvalidConfigurationException ex) {
                    p.sendMessage(WARNING+"오류가 발생했습니다!");
                }
            }
        }
    }

    public void openChest(Player p, int page) {
        Inventory chestPage = null;
        try {
            chestPage = yamlUtil.loadFromYaml(p, "chest"+page, 1, p.getName()+"의 창고 §6"+page+"페이지");
        } catch (IOException | InvalidConfigurationException e) {
            chestPage = Bukkit.createInventory(p, 9, p.getName()+"의 창고 §6"+page+"페이지");
        } finally {
            p.openInventory(chestPage);
            inventories.put(chestPage,page);
        }
    }
}
