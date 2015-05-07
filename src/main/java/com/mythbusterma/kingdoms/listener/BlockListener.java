package com.mythbusterma.kingdoms.listener;

import com.mythbusterma.kingdoms.Kingdoms;
import com.mythbusterma.kingdoms.Village;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;

public class BlockListener implements Listener {
    private Kingdoms parent;

    public BlockListener(Kingdoms parent) {
        this.parent = parent;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getPlayer().hasPermission("kingdoms.admin.build") 
                || event.getPlayer().hasPermission("kingdoms.king.build")) {
            return;
        }
        Village village = parent.getKingdomsManager().getVillage(event.getBlock().getLocation());
        if (village == null) {
            return;
        }
        if (village.getPermissions().isOutsiderBuild()) {
            return;
        }
        Village playerVillage = parent.getKingdomsManager().getPlayer(event.getPlayer()).getVillage();
        if (playerVillage == null || !village.equals(playerVillage)) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(Kingdoms.PREFIX + "You cannot build here!");
        }
        else if (!village.getPermissions().isResidentBuild()) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(Kingdoms.PREFIX + "Your lord does not allow you to build in the town!");
        }
        
        // TODO add plot support
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getPlayer().hasPermission("kingdoms.admin.build")
                || event.getPlayer().hasPermission("kingdoms.king.build")) {
            return;
        }
        Village village = parent.getKingdomsManager().getVillage(event.getBlock().getLocation());
        if (village == null) {
            return;
        }
        if (village.getPermissions().isOutsiderBuild()) {
            return;
        }
        Village playerVillage = parent.getKingdomsManager().getPlayer(event.getPlayer()).getVillage();
        if (playerVillage == null || !village.equals(playerVillage)) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(Kingdoms.PREFIX + "You cannot build here!");
        }
        else if (!village.getPermissions().isResidentBuild()) {
            if (parent.getKingdomsManager().getPlayer(event.getPlayer()).isLord()) {
                return;
            }
            event.setCancelled(true);
            event.getPlayer().sendMessage(Kingdoms.PREFIX + "Your lord does not allow you to build in the town!");
        }
        // TODO add plot support
    }
    
    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event) {
        Village village = parent.getKingdomsManager().getVillage(event.getBlock().getLocation());
        if (village == null) {
            return;
        }
        if (event.getCause() == BlockIgniteEvent.IgniteCause.FLINT_AND_STEEL) {
            Village playerVillage = parent.getKingdomsManager().getPlayer(event.getPlayer()).getVillage();
            if(event.getPlayer().hasPermission("kingdoms.king.ignite") ||
                    event.getPlayer().hasPermission("kingdoms.admin.ignite")) {
                return;
            }
            
            if (playerVillage == null || !village.equals(playerVillage)) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(Kingdoms.PREFIX + "You cannot start fires here!");
            }
            else {
                if (!village.getPermissions().isResidentIgnite()) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(Kingdoms.PREFIX 
                            + "Your lord does not allow you to set fire in the town!");
                }
            }
        }
        else if (event.getCause() == BlockIgniteEvent.IgniteCause.SPREAD) {
            if (!village.isFireSpread()) {
                event.setCancelled(true);
            }
        }
        else {
            event.setCancelled(true);
        }
        // TODO add plot support
    }
    
    @EventHandler
    public void onBlockFlow(BlockFromToEvent event) {
        Village village = parent.getKingdomsManager().getVillage(event.getBlock().getLocation());
        if (village == null) {
            return;
        }
        if (event.getBlock().getType() == Material.LAVA || event.getBlock().getType() == Material.STATIONARY_LAVA) {
            event.setCancelled(true);
        }
    }
}
