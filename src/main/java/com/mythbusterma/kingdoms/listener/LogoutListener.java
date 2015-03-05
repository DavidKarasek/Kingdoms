package com.mythbusterma.kingdoms.listener;

import com.mythbusterma.kingdoms.Kingdoms;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LogoutListener implements Listener {
    
    private final Kingdoms parent;

    public LogoutListener(Kingdoms parent) {
        this.parent = parent;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onLogout (PlayerQuitEvent event) {
        parent.getKingdomsManager().logout(event.getPlayer());
    }
}
