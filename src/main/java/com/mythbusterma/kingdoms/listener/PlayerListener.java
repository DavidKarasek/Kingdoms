package com.mythbusterma.kingdoms.listener;

import com.mythbusterma.kingdoms.Kingdoms;
import com.mythbusterma.kingdoms.Village;
import org.bukkit.ChatColor;
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
        final int fromX = (event.getFrom().getBlockX() / 16);
        final int toX = (event.getTo().getBlockX() / 16);
        if (fromX != toX) {
            checkMove(event);
        } else {
            final int fromZ = (event.getFrom().getBlockZ() / 16);
            final int toZ = (event.getTo().getBlockZ() / 16);
            if (fromZ != toZ) {
                checkMove(event);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {


    }

    private void checkMove(PlayerMoveEvent event) {
        Village village = parent.getKingdomsManager().getVillage(event.getTo());
        if (village != null) {
            if (village.equals(parent.getKingdomsManager()
                    .getVillage(event.getFrom()))) {
                return;
            }
            if (!village.getPermissions().isOutsiderEnter()) {
                if (parent.getKingdomsManager().getPlayer(event.getPlayer())
                        .getVillage() == null || !village
                        .equals(parent.getKingdomsManager()
                                .getPlayer(event.getPlayer()).getVillage())) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(
                            Kingdoms.PREFIX + "You cannot enter " + village
                                    .getName() +
                                    " as they do not allow visitors.");
                    return;
                }
            }
            event.getPlayer().sendMessage(
                    Kingdoms.PREFIX + "Entering: " + ChatColor.GREEN + village
                            .getName());

        }
        // TODO add plot support
    }
}
