package com.mythbusterma.kingdoms.listener;

import com.mythbusterma.kingdoms.KingdomPlayer;
import com.mythbusterma.kingdoms.Kingdoms;
import com.mythbusterma.kingdoms.utils.UUIDFetcher;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.HashMap;
import java.util.UUID;

public class LoginListener implements Listener {
    
    private final Kingdoms parent;
    
    private final HashMap<UUID,KingdomPlayer> loginCache;

    public LoginListener(Kingdoms parent) {
        this.parent = parent;
        loginCache = new HashMap<>();
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
        
        loginCache.put(id, parent.getMySqlConnector().loadPlayer(id));

    }
}
