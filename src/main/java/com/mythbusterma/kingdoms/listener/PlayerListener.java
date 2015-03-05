package com.mythbusterma.kingdoms.listener;

import com.mythbusterma.kingdoms.Kingdoms;
import com.mythbusterma.kingdoms.Village;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerListener implements Listener {
    
    private final Kingdoms parent;

    public PlayerListener(Kingdoms parent) {
        this.parent = parent;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (((int)event.getFrom().getX() << 4) != ((int)event.getTo().getX() << 4)) {
            checkMove(event);
        }
        else if (((int)event.getFrom().getZ() << 4) != ((int)event.getTo().getZ() << 4)) {
            checkMove(event);
        }
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        
        
    }

    private void checkMove(PlayerMoveEvent event) {
        Village village = parent.getKingdomsManager().getVillage(event.getTo());
        if (village != null) {
            if (!village.getPermissions().isOutsiderEnter()) {
                if (parent.getKingdomsManager().getPlayer(event.getPlayer()).getVillage() == null) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(Kingdoms.PREFIX + "You cannot enter " + village.getName() +
                        " as they do not allow visitors.");
                }
                else //noinspection ConstantConditions
                    if (!parent.getKingdomsManager().getPlayer(event.getPlayer()).getVillage().equals(village)) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(Kingdoms.PREFIX + "You cannot enter " + village.getName() +
                            " as they do not allow visitors.");
                }
            }
        }
        // TODO add plot support
    }
}
