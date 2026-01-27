package org.cursebyte;

import org.bukkit.plugin.java.JavaPlugin;

import org.cursebyte.command.JobsCommand;
import org.cursebyte.database.DatabaseManager;
import org.cursebyte.listener.JobsListener;
import org.cursebyte.module.jobs.JobsRepository;
import org.cursebyte.ui.JobsMenu;

import com.cursebyte.plugin.ui.core.MenuRegistry;

public final class CursebyteJobs extends JavaPlugin {
    @Override
    public void onEnable() {
        // Plugin startup logic
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        DatabaseManager.init(getDataFolder().getAbsolutePath());

        JobsRepository.init();

        registerCommands();
        registerMenus();
        registerListeners();

        getLogger().info("Cursebyte Jobs launched!");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        DatabaseManager.close();

        getLogger().info("Cursebyte Jobs stopped!");
    }

    private void registerCommands() {
        getCommand("jobs").setExecutor(new JobsCommand());
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new JobsListener(), this);
    }

    private void registerMenus() {
        MenuRegistry.register(new JobsMenu());
    }
}
