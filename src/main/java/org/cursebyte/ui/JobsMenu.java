package org.cursebyte.ui;

import com.cursebyte.plugin.ui.core.MenuRouter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.cursebyte.plugin.ui.core.Menu;
import com.cursebyte.plugin.ui.core.MenuContext;
import com.cursebyte.plugin.ui.core.MenuSession;

import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

import org.cursebyte.module.jobs.JobsService;

import java.util.List;
import java.util.UUID;

public class JobsMenu implements Menu {
    @Override
    public String id() {
        return "jobs";
    }

    @Override
    public Inventory build(Player p, MenuContext ctx) {
        Inventory inv = Bukkit.createInventory(null, 54, Component.text("APLIKASI PEKERJAAN"));

        ItemStack grayGlass = glass(Material.GRAY_STAINED_GLASS_PANE);
        ItemStack blackGlass = glass(Material.BLACK_STAINED_GLASS_PANE);
        for (int i = 9; i <= 44; i++)
            inv.setItem(i, grayGlass);
        for (int i = 0; i < 9; i++)
            inv.setItem(i, blackGlass);
        for (int i = 45; i < 54; i++)
            inv.setItem(i, blackGlass);

        inv.setItem(49, closeItem());
        inv.setItem(20, hunterJobs());
        inv.setItem(21, lumberjackJobs());
        inv.setItem(22, minerJobs());
        inv.setItem(23, farmerJobs());
        inv.setItem(24, fishermanJobs());
        inv.setItem(30, breederJobs());
        inv.setItem(31, smelterJobs());
        inv.setItem(32, cookJobs());

        return inv;
    }

    @Override
    public void onClick(Player p, int slot, MenuContext ctx) {
        UUID targetId = p.getUniqueId();
        if (slot == 20) {
            if (!JobsService.isUnemployed(targetId)) {
                p.sendMessage("§cKamu sudah memiliki pekerjaan");
                return;
            }

            JobsService.changeJob(targetId, "HUNTER");
            p.sendMessage("§aPekerjaan kamu sekarang adalah Pemburu");

            MenuRouter.open(p, "sell");
        } else if (slot == 21) {
            if (!JobsService.isUnemployed(targetId)) {
                p.sendMessage("§cKamu sudah memiliki pekerjaan");
                return;
            }

            JobsService.changeJob(targetId, "LUMBERJACK");
            p.sendMessage("§aPekerjaan kamu sekarang adalah Penebang");

            MenuRouter.open(p, "sell");
        } else if (slot == 22) {
            if (!JobsService.isUnemployed(targetId)) {
                p.sendMessage("§cKamu sudah memiliki pekerjaan");
                return;
            }

            JobsService.changeJob(targetId, "MINER");
            p.sendMessage("§aPekerjaan kamu sekarang adalah Penambang");

            MenuRouter.open(p, "sell");
        } else if (slot == 23) {
            if (!JobsService.isUnemployed(targetId)) {
                p.sendMessage("§cKamu sudah memiliki pekerjaan");
                return;
            }

            JobsService.changeJob(targetId, "FARMER");
            p.sendMessage("§aPekerjaan kamu sekarang adalah Petani");

            MenuRouter.open(p, "sell");
        } else if (slot == 24) {
            if (!JobsService.isUnemployed(targetId)) {
                p.sendMessage("§cKamu sudah memiliki pekerjaan");
                return;
            }

            JobsService.changeJob(targetId, "FISHERMAN");
            p.sendMessage("§aPekerjaan kamu sekarang adalah Pemancing");

            MenuRouter.open(p, "sell");
        } else if (slot == 30) {
            if (!JobsService.isUnemployed(targetId)) {
                p.sendMessage("§cKamu sudah memiliki pekerjaan");
                return;
            }

            JobsService.changeJob(targetId, "BREEDER");
            p.sendMessage("§aPekerjaan kamu sekarang adalah Peternak");

            MenuRouter.open(p, "sell");
        } else if (slot == 31) {
            if (!JobsService.isUnemployed(targetId)) {
                p.sendMessage("§cKamu sudah memiliki pekerjaan");
                return;
            }

            JobsService.changeJob(targetId, "SMELTER");
            p.sendMessage("§aPekerjaan kamu sekarang adalah Smelter");

            MenuRouter.open(p, "sell");
        } else if (slot == 32) {
            if (!JobsService.isUnemployed(targetId)) {
                p.sendMessage("§cKamu sudah memiliki pekerjaan");
                return;
            }

            JobsService.changeJob(targetId, "COOK");
            p.sendMessage("§aPekerjaan kamu sekarang adalah Pemasak");

            MenuRouter.open(p, "sell");
        } else if (slot == 49) {
            p.closeInventory();
            MenuSession.clear(p);
        }
    }

    @Override
    public void onChange(Player player, int slot, MenuContext ctx) {}

    private static FileConfiguration config() {
        return JavaPlugin.getProvidingPlugin(JobsMenu.class).getConfig();
    }

    private static String formatMaterial(String material) {
        String[] parts = material.toLowerCase().split("_");
        StringBuilder builder = new StringBuilder();

        for (String part : parts) {
            if (part.isEmpty()) continue;
            builder.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1)).append(" ");
        }

        return builder.toString().trim();
    }

    private static ItemStack glass(Material color) {
        ItemStack item = new ItemStack(color);
        ItemMeta meta = item.getItemMeta();

        meta.displayName(Component.text(" "));
        item.setItemMeta(meta);

        return item;
    }

    private static ItemStack fishermanJobs() {
        ItemStack item = new ItemStack(Material.FISHING_ROD);
        ItemMeta meta = item.getItemMeta();

        ConfigurationSection section = config().getConfigurationSection("jobs.fisherman");

        meta.displayName(
                Component.text(section.getString("display-name", "Nelayan"))
                        .color(TextColor.color(0, 130, 255))
                        .decorate(TextDecoration.BOLD));

        List<Component> lore = new java.util.ArrayList<>();

        lore.add(Component.text(""));
        lore.add(Component.text("List Harga:")
                .color(TextColor.color(180, 180, 180))
                .decorate(TextDecoration.ITALIC));

        ConfigurationSection prices = section.getConfigurationSection("prices");
        for (String key : prices.getKeys(false)) {
            double price = prices.getDouble(key);
            lore.add(
                    Component.text("• " + formatMaterial(key) + " $" + price)
                            .color(TextColor.color(120, 255, 120))
            );
        }

        lore.add(Component.text(""));
        lore.add(Component.text("Klik untuk membuka")
                .color(TextColor.color(255, 255, 255))
                .decorate(TextDecoration.BOLD));

        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack farmerJobs() {
        ItemStack item = new ItemStack(Material.NETHERITE_HOE);
        ItemMeta meta = item.getItemMeta();

        ConfigurationSection section = config().getConfigurationSection("jobs.farmer");

        meta.displayName(
                Component.text(section.getString("display-name", "Petani"))
                        .color(TextColor.color(0, 130, 255))
                        .decorate(TextDecoration.BOLD));

        List<Component> lore = new java.util.ArrayList<>();

        lore.add(Component.text(""));
        lore.add(Component.text("List Harga:")
                .color(TextColor.color(180, 180, 180))
                .decorate(TextDecoration.ITALIC));

        ConfigurationSection prices = section.getConfigurationSection("prices");
        for (String key : prices.getKeys(false)) {
            double price = prices.getDouble(key);
            lore.add(
                    Component.text("• " + formatMaterial(key) + " $" + price)
                            .color(TextColor.color(120, 255, 120))
            );
        }

        lore.add(Component.text(""));
        lore.add(Component.text("Klik untuk membuka")
                .color(TextColor.color(255, 255, 255))
                .decorate(TextDecoration.BOLD));

        meta.lore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack minerJobs() {
        ItemStack item = new ItemStack(Material.NETHERITE_PICKAXE);
        ItemMeta meta = item.getItemMeta();

        ConfigurationSection section = config().getConfigurationSection("jobs.miner");

        meta.displayName(
                Component.text(section.getString("display-name", "Penambang"))
                        .color(TextColor.color(0, 130, 255))
                        .decorate(TextDecoration.BOLD));

        List<Component> lore = new java.util.ArrayList<>();

        lore.add(Component.text(""));
        lore.add(Component.text("List Harga:")
                .color(TextColor.color(180, 180, 180))
                .decorate(TextDecoration.ITALIC));

        ConfigurationSection prices = section.getConfigurationSection("prices");
        for (String key : prices.getKeys(false)) {
            double price = prices.getDouble(key);
            lore.add(
                    Component.text("• " + formatMaterial(key) + " $" + price)
                            .color(TextColor.color(120, 255, 120))
            );
        }

        lore.add(Component.text(""));
        lore.add(Component.text("Klik untuk membuka")
                .color(TextColor.color(255, 255, 255))
                .decorate(TextDecoration.BOLD));

        meta.lore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack lumberjackJobs() {
        ItemStack item = new ItemStack(Material.NETHERITE_AXE);
        ItemMeta meta = item.getItemMeta();

        ConfigurationSection section = config().getConfigurationSection("jobs.lumberjack");

        meta.displayName(
                Component.text(section.getString("display-name", "Penebang"))
                        .color(TextColor.color(0, 130, 255))
                        .decorate(TextDecoration.BOLD));

        List<Component> lore = new java.util.ArrayList<>();

        lore.add(Component.text(""));
        lore.add(Component.text("List Harga:")
                .color(TextColor.color(180, 180, 180))
                .decorate(TextDecoration.ITALIC));

        ConfigurationSection prices = section.getConfigurationSection("prices");
        for (String key : prices.getKeys(false)) {
            double price = prices.getDouble(key);
            lore.add(
                    Component.text("• " + formatMaterial(key) + " $" + price)
                            .color(TextColor.color(120, 255, 120))
            );
        }

        lore.add(Component.text(""));
        lore.add(Component.text("Klik untuk membuka")
                .color(TextColor.color(255, 255, 255))
                .decorate(TextDecoration.BOLD));

        meta.lore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack hunterJobs() {
        ItemStack item = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = item.getItemMeta();

        ConfigurationSection section = config().getConfigurationSection("jobs.hunter");

        meta.displayName(
                Component.text(section.getString("display-name", "Pemburu"))
                        .color(TextColor.color(0, 130, 255))
                        .decorate(TextDecoration.BOLD));

        List<Component> lore = new java.util.ArrayList<>();

        lore.add(Component.text(""));
        lore.add(Component.text("List Harga:")
                .color(TextColor.color(180, 180, 180))
                .decorate(TextDecoration.ITALIC));

        ConfigurationSection prices = section.getConfigurationSection("prices");
        for (String key : prices.getKeys(false)) {
            double price = prices.getDouble(key);
            lore.add(
                    Component.text("• " + formatMaterial(key) + " $" + price)
                            .color(TextColor.color(120, 255, 120))
            );
        }

        lore.add(Component.text(""));
        lore.add(Component.text("Klik untuk membuka")
                .color(TextColor.color(255, 255, 255))
                .decorate(TextDecoration.BOLD));

        meta.lore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack breederJobs() {
        ItemStack item = new ItemStack(Material.MILK_BUCKET);
        ItemMeta meta = item.getItemMeta();

        ConfigurationSection section = config().getConfigurationSection("jobs.breeder");

        meta.displayName(
                Component.text(section.getString("display-name", "Peternak"))
                        .color(TextColor.color(0, 130, 255))
                        .decorate(TextDecoration.BOLD));

        List<Component> lore = new java.util.ArrayList<>();

        lore.add(Component.text(""));
        lore.add(Component.text("List Harga:")
                .color(TextColor.color(180, 180, 180))
                .decorate(TextDecoration.ITALIC));

        ConfigurationSection prices = section.getConfigurationSection("prices");
        for (String key : prices.getKeys(false)) {
            double price = prices.getDouble(key);
            lore.add(
                    Component.text("• " + formatMaterial(key) + " $" + price)
                            .color(TextColor.color(120, 255, 120))
            );
        }

        lore.add(Component.text(""));
        lore.add(Component.text("Klik untuk membuka")
                .color(TextColor.color(255, 255, 255))
                .decorate(TextDecoration.BOLD));

        meta.lore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack smelterJobs() {
        ItemStack item = new ItemStack(Material.BLAST_FURNACE);
        ItemMeta meta = item.getItemMeta();

        ConfigurationSection section = config().getConfigurationSection("jobs.smelter");

        meta.displayName(
                Component.text(section.getString("display-name", "Smelter"))
                        .color(TextColor.color(0, 130, 255))
                        .decorate(TextDecoration.BOLD));

        List<Component> lore = new java.util.ArrayList<>();

        lore.add(Component.text(""));
        lore.add(Component.text("List Harga:")
                .color(TextColor.color(180, 180, 180))
                .decorate(TextDecoration.ITALIC));

        ConfigurationSection prices = section.getConfigurationSection("prices");
        for (String key : prices.getKeys(false)) {
            double price = prices.getDouble(key);
            lore.add(
                    Component.text("• " + formatMaterial(key) + " $" + price)
                            .color(TextColor.color(120, 255, 120))
            );
        }

        lore.add(Component.text(""));
        lore.add(Component.text("Klik untuk membuka")
                .color(TextColor.color(255, 255, 255))
                .decorate(TextDecoration.BOLD));

        meta.lore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack cookJobs() {
        ItemStack item = new ItemStack(Material.CAKE);
        ItemMeta meta = item.getItemMeta();

        ConfigurationSection section = config().getConfigurationSection("jobs.cook");

        meta.displayName(
                Component.text(section.getString("display-name", "Pemasak"))
                        .color(TextColor.color(0, 130, 255))
                        .decorate(TextDecoration.BOLD));

        List<Component> lore = new java.util.ArrayList<>();

        lore.add(Component.text(""));
        lore.add(Component.text("List Harga:")
                .color(TextColor.color(180, 180, 180))
                .decorate(TextDecoration.ITALIC));

        ConfigurationSection prices = section.getConfigurationSection("prices");
        for (String key : prices.getKeys(false)) {
            double price = prices.getDouble(key);
            lore.add(
                    Component.text("• " + formatMaterial(key) + " $" + price)
                            .color(TextColor.color(120, 255, 120))
            );
        }

        lore.add(Component.text(""));
        lore.add(Component.text("Klik untuk membuka")
                .color(TextColor.color(255, 255, 255))
                .decorate(TextDecoration.BOLD));

        meta.lore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack closeItem() {
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();

        meta.displayName(
                Component.text("✖ Tutup")
                        .color(TextColor.color(255, 80, 80))
                        .decorate(TextDecoration.BOLD));

        meta.lore(List.of(
                Component.text("Keluar dari aplikasi")
                        .color(TextColor.color(180, 180, 180))
                        .decorate(TextDecoration.ITALIC)));

        item.setItemMeta(meta);
        return item;
    }
}
