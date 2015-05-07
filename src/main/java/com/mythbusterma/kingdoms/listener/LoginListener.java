package com.mythbusterma.kingdoms.listener;

import com.mythbusterma.kingdoms.KingdomPlayer;
import com.mythbusterma.kingdoms.Kingdoms;
import com.mythbusterma.kingdoms.utils.UUIDFetcher;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;

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

        parent.getUuidHolder().addUser(id, event.getName());
        
        loginCache.put(id, parent.getMySqlConnector().loadPlayer(id, event.getName()));
    }

    @EventHandler(priority =  EventPriority.MONITOR)
    public void onLogin(PlayerLoginEvent event) {
        if (loginCache.get(parent.getUuidHolder().getUuid(event.getPlayer().getName())) == null) {
            parent.getLogger().log(Level.SEVERE, "Failed to load player data, error LL1001");
            parent.getLogger().log(Level.SEVERE, "ID was: " + event.getPlayer().getUniqueId() + " or "
                    + parent.getUuidHolder().getUuid(event.getPlayer().getName()));
            return;
        }

        KingdomPlayer player = loginCache.get(parent.getUuidHolder().getUuid(event.getPlayer().getName()));
        player.setPlayer(event.getPlayer());

        if (player.isNew()) {
            player.getPlayer().sendMessage(Kingdoms.PREFIX + "Welcome to the server! This server uses Kingdoms for " +
                    "protection of plots. To learn how to use this system, request the contextual help with " +
                    "/kingdoms ? (/k ?)");
        }

        parent.getKingdomsManager().addPlayer(player);
        loginCache.remove(event.getPlayer().getUniqueId());
    }
}
