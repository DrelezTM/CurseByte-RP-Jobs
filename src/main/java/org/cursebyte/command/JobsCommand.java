package org.cursebyte.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cursebyte.plugin.ui.core.MenuRouter;

public class JobsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Hanya bisa digunakan oleh player!");
            return true;
        }

        MenuRouter.open(player, "jobs");
        return true;
    }
}
