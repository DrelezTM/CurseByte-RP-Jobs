package org.cursebyte.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cursebyte.plugin.ui.core.MenuRouter;
import org.cursebyte.module.jobs.JobsService;

public class JobsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Hanya bisa digunakan oleh player!");
            return true;
        }

        if (JobsService.isUnemployed(player.getUniqueId())) {
            MenuRouter.open(player, "jobs");
        } else {
            MenuRouter.open(player, "sell");
        }
        return true;
    }
}
