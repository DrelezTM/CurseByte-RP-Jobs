package org.cursebyte.ui;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
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
        Inventory inv = Bukkit.createInventory(null, 45, Component.text("APLIKASI PEKERJAAN"));

        ItemStack grayGlass = glass(Material.GRAY_STAINED_GLASS_PANE);
        ItemStack blackGlass = glass(Material.BLACK_STAINED_GLASS_PANE);
        for (int i = 9; i <= 36; i+=9)
            inv.setItem(i, grayGlass);
        for (int i = 17; i <= 45; i+=9)
            inv.setItem(i, grayGlass);
        for (int i = 0; i < 9; i++)
            inv.setItem(i, blackGlass);
        for (int i = 36; i < 45; i++)
            inv.setItem(i, blackGlass);

        inv.setItem(40, closeItem());
        inv.setItem(10, fishermanJobs());
        inv.setItem(11, farmerJobs());

        return inv;
    }

    @Override
    public void onClick(Player p, int slot, MenuContext ctx) {
        UUID targetId = p.getUniqueId();
        if (slot == 10) {
            String checkJob = JobsService.getJob(targetId);
            if (JobsService.isUnemployed(targetId)) {
                p.sendMessage("§cKamu sudah memiliki pekerjaan");
                return;
            }

            JobsService.changeJob(targetId, "FISHERMAN");
            p.sendMessage("§aPekerjaan kamu sekarang adalah Nelayan");

            p.closeInventory();
            MenuSession.clear(p);
        } else if (slot == 11) {
            if (JobsService.isUnemployed(targetId)) {
                p.sendMessage("§cKamu sudah memiliki pekerjaan");
                return;
            }

            JobsService.changeJob(targetId, "FARMER");
            p.sendMessage("§aPekerjaan kamu sekarang adalah Petani");

            p.closeInventory();
            MenuSession.clear(p);
        } else if (slot == 40) {
            p.closeInventory();
            MenuSession.clear(p);
        }
    }

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
        ItemStack item = new ItemStack(Material.WHEAT);
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
