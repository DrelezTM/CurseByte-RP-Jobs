package org.cursebyte.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.cursebyte.module.jobs.JobsService;

public class JobsListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        JobsService.initPlayer(e.getPlayer().getUniqueId());
    }
}
