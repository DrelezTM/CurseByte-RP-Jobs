package org.cursebyte.ui;

import com.cursebyte.plugin.modules.economy.TaxService;
import com.cursebyte.plugin.modules.economy.transaction.TransactionService;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.cursebyte.plugin.ui.core.Menu;
import com.cursebyte.plugin.ui.core.MenuContext;
import com.cursebyte.plugin.ui.core.MenuSession;
import com.cursebyte.plugin.modules.economy.EconomyService;
import com.cursebyte.plugin.modules.government.stock.GovernmentStockService;
import com.cursebyte.plugin.ui.core.MenuRouter;

import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.Component;

import org.bukkit.plugin.java.JavaPlugin;
import org.cursebyte.module.jobs.JobsService;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class SellItemMenu implements Menu {

    @Override
    public String id() {
        return "sell";
    }

    @Override
    public Inventory build(Player p, MenuContext ctx) {
        Inventory inv = Bukkit.createInventory(null, 45, Component.text("JUAL BARANG"));

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
        inv.setItem(4, sellInfoItem(p.getUniqueId()));
        inv.setItem(37, outOfJobs());
        inv.setItem(43, sellItem(0));

        ctx.set("inputSlots", Set.of(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34));
        return inv;
    }

    @Override
    public void onClick(Player p, int slot, MenuContext ctx) {
        UUID targetId = p.getUniqueId();
        if (slot == 37) {
            @SuppressWarnings("unchecked")
            Set<Integer> inputSlots = (Set<Integer>) ctx.get("inputSlots", Set.class);
            Inventory inv = p.getOpenInventory().getTopInventory();
            for (int getSlot : inputSlots) {
                ItemStack item = inv.getItem(getSlot);

                if (item == null || item.getType() == Material.AIR) continue;
                inv.setItem(getSlot, null);

                var leftover = p.getInventory().addItem(item);
                leftover.values().forEach(left -> p.getWorld().dropItemNaturally(p.getLocation(), left));
            }

            if (JobsService.isUnemployed(targetId)) {
                p.sendMessage("§cKamu belum memiliki pekerjaan");
                return;
            }

            double price = config().getDouble("leave.price");
            long cooldown = config().getLong("leave.cooldown-seconds");

            if (!JobsService.canLeaveJob(targetId, cooldown)) {
                long sisa = JobsService.getLeaveCooldownLeft(targetId, cooldown);
                p.sendMessage("§cKamu bisa keluar job lagi dalam §e" + sisa + " detik.");
                return;
            }

            if (!EconomyService.hasEnough(targetId, price)) {
                p.sendMessage("§cUang kamu tidak cukup. Biaya: $" + price);
                return;
            }

            EconomyService.remove(targetId, price);
            JobsService.leaveJob(targetId);
            p.sendMessage("§aKamu berhasil keluar dari pekerjaanmu!");

            MenuRouter.open(p, "jobs");
        } else if (slot == 40) {
            @SuppressWarnings("unchecked")
            Set<Integer> inputSlots = (Set<Integer>) ctx.get("inputSlots", Set.class);
            Inventory inv = p.getOpenInventory().getTopInventory();
            for (int getSlot : inputSlots) {
                ItemStack item = inv.getItem(getSlot);

                if (item == null || item.getType() == Material.AIR) continue;
                inv.setItem(getSlot, null);

                var leftover = p.getInventory().addItem(item);
                leftover.values().forEach(left -> p.getWorld().dropItemNaturally(p.getLocation(), left));
            }

            p.closeInventory();
            MenuSession.clear(p);
        } else if (slot == 43) {
            @SuppressWarnings("unchecked")
            Set<Integer> inputSlots = (Set<Integer>) ctx.get("inputSlots", Set.class);
            Inventory inv = p.getOpenInventory().getTopInventory();

            UUID uuid = p.getUniqueId();
            String job = JobsService.getJob(uuid).toLowerCase();

            ConfigurationSection prices = config().getConfigurationSection("jobs." + job + ".prices");

            if (prices == null) {
                p.sendMessage("§cPekerjaan kamu tidak bisa menjual barang apapun.");
                return;
            }

            double total = 0;
            boolean hasInvalidItem = false;

            for (int getSlot : inputSlots) {
                ItemStack item = inv.getItem(getSlot);
                if (item == null || item.getType() == Material.AIR) continue;

                String materialKey = item.getType().name();

                if (!prices.contains(materialKey)) {
                    hasInvalidItem = true;

                    inv.setItem(getSlot, null);
                    var leftover = p.getInventory().addItem(item);
                    leftover.values().forEach(left -> p.getWorld().dropItemNaturally(p.getLocation(), left));
                    continue;
                }

                int amount = item.getAmount();
                double unitPrice = prices.getDouble(materialKey);

                GovernmentStockService.submitStock(uuid, job, item.clone(), amount, unitPrice);

                total += unitPrice * amount;
                inv.setItem(getSlot, null);
            }

            if (total <= 0) {
                p.sendMessage("§cTidak ada item yang bisa dijual.");
                return;
            }

            Double tax = TaxService.calculateTax(total);
            EconomyService.add(uuid, total - tax);

            p.sendMessage("§aBerhasil menjual barang senilai §e$" + total);

            if (hasInvalidItem) {
                p.sendMessage("§eBeberapa item tidak sesuai pekerjaan dan telah dikembalikan.");
            }
        }
    }

    public void onChange(Player p, int slot, MenuContext ctx) {
        UUID uuid = p.getUniqueId();
        String job = JobsService.getJob(uuid).toLowerCase();

        ConfigurationSection prices =
                config().getConfigurationSection("jobs." + job + ".prices");

        if (prices == null) return;

        @SuppressWarnings("unchecked")
        Set<Integer> inputSlots = (Set<Integer>) ctx.get("inputSlots", Set.class);

        Inventory inv = p.getOpenInventory().getTopInventory();

        double total = 0;

        for (int s : inputSlots) {
            ItemStack item = inv.getItem(s);
            if (item == null || item.getType() == Material.AIR) continue;


            String key = item.getType().name();
            if (!prices.contains(key)) continue;

            double unitPrice = prices.getDouble(key);
            total += unitPrice * item.getAmount();
        }

        inv.setItem(43, sellItem(total));
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

    private static ItemStack sellInfoItem(UUID uuid) {
        ItemStack item = new ItemStack(Material.CLOCK);
        ItemMeta meta = item.getItemMeta();

        String playerJob = JobsService.getJob(uuid);
        ConfigurationSection section = config().getConfigurationSection("jobs." + playerJob.toLowerCase());

        meta.displayName(
                Component.text(section.getString("display-name", "Nama Jobs"))
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

        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack outOfJobs() {
        ItemStack item = new ItemStack(Material.DARK_OAK_DOOR);
        ItemMeta meta = item.getItemMeta();

        ConfigurationSection section = config().getConfigurationSection("");

        meta.displayName(
                Component.text("✖ Keluar Pekerjaan")
                        .color(TextColor.color(255, 80, 80))
                        .decorate(TextDecoration.BOLD));

        meta.lore(List.of(
                Component.text("Keluar dari pekerjaanmu sekarang")
                        .color(TextColor.color(180, 180, 180))
                        .decorate(TextDecoration.ITALIC),
                Component.text("Biaya Keluar Pekerjaan: $" + section.getDouble("leave.price", 0.0))
                        .color(TextColor.color(255, 242, 0))
                        .decorate(TextDecoration.ITALIC)));

        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack sellItem(double total) {
        ItemStack item = new ItemStack(Material.EMERALD);
        ItemMeta meta = item.getItemMeta();

        meta.displayName(
                Component.text("✔ Jual Barang")
                        .color(TextColor.color(0, 255, 42))
                        .decorate(TextDecoration.BOLD));

        meta.lore(List.of(
                Component.text("Operasi ini tidak bisa dibatalkan")
                        .color(TextColor.color(180, 180, 180))
                        .decorate(TextDecoration.ITALIC),
                Component.text("Total Harga: $" + total)
                        .color(TextColor.color(255, 204, 0))
                        .decorate(TextDecoration.ITALIC)));

        item.setItemMeta(meta);
        return item;
    }
}
