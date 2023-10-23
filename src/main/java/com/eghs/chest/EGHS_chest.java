package com.eghs.chest;

import com.eghs.chest.inventories.InventoryChestShop;
import com.eghs.chest.inventories.InventoryFishing;
import com.eghs.chest.inventories.rune.InventoryRuneBackpack;
import com.eghs.chest.inventories.rune.InventoryRuneEquip;
import com.eghs.chest.utils.YamlUtil;
import com.eghs.chest.inventories.InventoryChest;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
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
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

public final class EGHS_chest extends JavaPlugin implements Listener, CommandExecutor {
    public static final String PREFIX = "§f:: §aEGHS §f:: ";
    public static final String WARNING = "§f[ §c경고! §f] ";
    public static final String INFO = "§f[ §6안내 §f] ";

    private final InventoryChest chest = new InventoryChest();
    private final InventoryChestShop chestShop = new InventoryChestShop();
    private final InventoryRuneEquip runeEquip = new InventoryRuneEquip();
    private final InventoryRuneBackpack runeBackpack = new InventoryRuneBackpack();
    private final InventoryFishing fishing = new InventoryFishing();
    public static File dataFolder;

    public void createFolder(String... chests) {
        try {
            File file;
            for (String s : chests) {
                file = new File(getDataFolder(), "/" + s);
                Files.createDirectories(Paths.get(file.getPath()));
                System.out.println("성공적으로 폴더 " + s + " 가 생성되었습니다.");
            }
        } catch (IOException e) {
            System.out.println("폴더 생성에 실패하였습니다 : " + e.getMessage());
        }
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("아이템저장").setExecutor(this);
        createFolder("chest1", "chest2", "rune_equip", "rune_backpack", "fish_backpack");
        dataFolder = getDataFolder();
    }

    @Override
    public void onDisable() {
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
        switch (args[0]) {
            case "창고1":
                InventoryChest.openInventory(p, 1);
                break;
            case "창고2":
                InventoryChest.openInventory(p, 2);
                break;
            case "룬장비":
                runeEquip.openInventory(p);
                break;
            case "룬가방":
                runeBackpack.openInventory(p);
                break;
            case "낚시가방":
                fishing.openInventory(p);
                break;
        }
        return false;
    }

    @EventHandler
    public void chestNPCInteract(PlayerInteractEntityEvent e) {
        Entity entity = e.getRightClicked();
        if (entity.getName().equals("창고지기 라나")) {
            Player p = e.getPlayer();
            chestShop.openInventory(p);
        }
    }

    @EventHandler
    public void inventoryClickEvent(InventoryClickEvent e) {
        if (e.getCurrentItem() == null || e.getCurrentItem().getTypeId() == 0) {
            e.setCancelled(false);
        } else {
            if (e.getView().getTitle().equals("§f라나의 창고 상점")) {
                chestShop.inventoryClickEvent(e);
            }
            Inventory inventory = e.getInventory();
            if (chest.contains(inventory)) {
                chest.inventoryClickEvent(e);
            } else if (runeEquip.contains(inventory)) {
                runeEquip.inventoryClickEvent(e);
            } else if (runeBackpack.contains(inventory)) {
                runeBackpack.inventoryClickEvent(e);
            } else if (fishing.contains(inventory)) {
                fishing.inventoryClickEvent(e);
            }
        }
    }

    @EventHandler
    public void inventoryCloseEvent(InventoryCloseEvent e) {
        Inventory inventory = e.getInventory();
        if (chest.contains(inventory)) {
            chest.inventoryCloseEvent(e);
        } else if (runeEquip.contains(inventory)) {
            runeEquip.inventoryCloseEvent(e);
        } else if (runeBackpack.contains(inventory)) {
            runeBackpack.inventoryCloseEvent(e);
        } else if (fishing.contains(inventory)) {
            fishing.inventoryCloseEvent(e);
        }
    }

    // -- 창고 개방 관련 메서드
    @EventHandler
    public void playerInteractEvent(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ItemStack itemStack = p.getInventory().getItemInMainHand();

        // 아이템 우클릭 감지
        if (e.getAction().toString().contains("RIGHT_CLICK") && itemStack != null) {
            if (itemStack.getItemMeta() == null || !itemStack.getItemMeta().hasDisplayName()) return;
            onItemRightClick(p, itemStack);
        }
    }

    private void onItemRightClick(Player p, ItemStack itemStack) {
        String itemName = itemStack.getItemMeta().getDisplayName().replaceAll("§\\S", "");

        // 창고 개방권 감지
        if (itemStack.getType() == Material.PAPER && itemName.matches("^창고 [0-9]-[0-9] 개방권$")) {
            String info = itemName.split(" ")[1];
            int page = Integer.parseInt(info.split("-")[0]);
            int row = Integer.parseInt(info.split("-")[1]);
            unlockChest(p, itemStack, page, row);
        }
    }

    private void unlockChest(Player p, ItemStack itemStack, int page, int row) {
        UUID playeruuid = p.getPlayer().getUniqueId();
        File file = new File(getDataFolder(), "chest" + page + "/" + playeruuid + ".yml");
        try {
            if (!file.exists() && page >= 2 && row == 1) {
                unlockPage(p, itemStack, page, row);
            } else if (YamlUtil.getInventoryRows(p, "chest" + page) == row - 1) {
                unlockRow(p, itemStack, page, row);
            } else {
                p.sendMessage(WARNING + "이 개방권을 이미 사용하셨거나, 사용할 권한이 없습니다!");
            }
        } catch (IOException | InvalidConfigurationException ex) {
            p.sendMessage(WARNING + "오류가 발생했습니다!");
        }
    }

    private void unlockPage(Player p, ItemStack itemStack, int page, int row) throws IOException, InvalidConfigurationException {
        if (YamlUtil.getInventoryRows(p, "chest" + (page - 1)) == 6) {
            Inventory temp = Bukkit.createInventory(p, 9, p.getName() + "의 창고 " + page + "페이지");
            YamlUtil.save(p, temp, "chest" + page);
            itemStack.setAmount(itemStack.getAmount() - 1);
            p.sendMessage(INFO + "성공적으로 창고 " + page + "페이지의 " + row + "줄이 개방되었습니다!");
        } else {
            p.sendMessage(WARNING + "아직 창고 " + (page - 1) + "-6 개방권을 사용하지 않아 " + page + "-1 개방권의 사용이 불가능합니다!");
        }
    }

    private void unlockRow(Player p, ItemStack itemStack, int page, int row) throws IOException, InvalidConfigurationException {
        itemStack.setAmount(itemStack.getAmount() - 1);
        p.getInventory().setItemInMainHand(itemStack);
        YamlUtil.setInventorySize(p, "chest" + page, row);
        p.sendMessage(INFO + "성공적으로 창고 " + page + "페이지의 " + row + "줄이 개방되었습니다!");
    }
}
