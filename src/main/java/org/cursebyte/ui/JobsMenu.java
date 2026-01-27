package org.cursebyte.ui;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.cursebyte.plugin.ui.core.Menu;
import com.cursebyte.plugin.ui.core.MenuContext;
import com.cursebyte.plugin.ui.core.MenuSession;

import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.List;

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
        inv.setItem(10, fishingJobs());
        inv.setItem(11, farmerJobs());

        return inv;
    }

    @Override
    public void onClick(Player p, int slot, MenuContext ctx) {
        if (slot == 40) {
            p.closeInventory();
            MenuSession.clear(p);
        }
    }

    private static ItemStack glass(Material color) {
        ItemStack item = new ItemStack(color);
        ItemMeta meta = item.getItemMeta();

        meta.displayName(Component.text(" "));
        item.setItemMeta(meta);

        return item;
    }

    private static ItemStack fishingJobs() {
        ItemStack item = new ItemStack(Material.FISHING_ROD);
        ItemMeta meta = item.getItemMeta();

        meta.displayName(
                Component.text("🐟 Nelayan")
                        .color(TextColor.color(0, 130, 255))
                        .decorate(TextDecoration.BOLD));

        meta.lore(List.of(
                Component.text(""),

                Component.text("List Harga:")
                        .color(TextColor.color(180, 180, 180))
                        .decorate(TextDecoration.ITALIC),

                Component.text("• Raw Cod $5").color(TextColor.color(120, 255, 120)),
                Component.text("• Raw Salmon $8").color(TextColor.color(120, 255, 120)),
                Component.text("• Tropical Fish $25").color(TextColor.color(120, 255, 120)),
                Component.text("• Pufferfish $50").color(TextColor.color(120, 255, 120)),
                Component.text("• Nautilus Shell $500").color(TextColor.color(120, 255, 120)),

                Component.text(""),

                Component.text("Klik untuk membuka")
                        .color(TextColor.color(255, 255, 255))
                        .decorate(TextDecoration.BOLD)


        ));

        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack farmerJobs() {
        ItemStack item = new ItemStack(Material.WHEAT);
        ItemMeta meta = item.getItemMeta();

        meta.displayName(
                Component.text("🧑‍🌾 Petani")
                        .color(TextColor.color(0, 130, 255))
                        .decorate(TextDecoration.BOLD));

        meta.lore(List.of(
                Component.text(""),

                Component.text("List Harga:")
                        .color(TextColor.color(180, 180, 180))
                        .decorate(TextDecoration.ITALIC),

                Component.text("• Wheat / Seeds $1").color(TextColor.color(120, 255, 120)),
                Component.text("• Potato / Carrot $2").color(TextColor.color(120, 255, 120)),
                Component.text("• Sugar Cane $3").color(TextColor.color(120, 255, 120)),
                Component.text("• Melon / Pumpkin $5").color(TextColor.color(120, 255, 120)),
                Component.text("• Nether Wart $15").color(TextColor.color(120, 255, 120)),

                Component.text(""),

                Component.text("Klik untuk membuka")
                        .color(TextColor.color(255, 255, 255))
                        .decorate(TextDecoration.BOLD)


        ));

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
