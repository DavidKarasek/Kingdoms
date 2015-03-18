package com.mythbusterma.kingdoms.listener;

import com.mythbusterma.kingdoms.KingdomPlayer;
import com.mythbusterma.kingdoms.Kingdoms;
import com.mythbusterma.kingdoms.utils.UUIDFetcher;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class LoginListener implements Listener {
    
    private final Kingdoms parent;
    
    private final Map<UUID,KingdomPlayer> loginCache;

    public LoginListener(Kingdoms parent) {
        this.parent = parent;
        loginCache = new ConcurrentHashMap<>();
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        if(event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            return;
        }
        
        UUID id = null;
        try {
            id = UUIDFetcher.getUUIDOf(event.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        loginCache.put(id, parent.getMySqlConnector().loadPlayer(id, event.getName()));
    }

    @EventHandler(priority =  EventPriority.MONITOR)
    public void onLogin(PlayerLoginEvent event) {
        if (loginCache.get(event.getPlayer().getUniqueId()) == null) {
            parent.getLogger().log(Level.SEVERE, "Failed to load player data, error LL1001");
            return;
        }

        KingdomPlayer player = loginCache.get(event.getPlayer().getUniqueId());
        player.setPlayer(event.getPlayer());

        if (player.isNew()) {
            player.getPlayer().sendMessage(Kingdoms.PREFIX + "Welcome to the server! This server uses Kingdoms for " +
                    "protection of plots. To learn how to use this system, request the contextual help with " +
                    "/kingdoms ? (/k ?)");
        }

        parent.getKingdomsManager().addPlayer(player);
    }
}
