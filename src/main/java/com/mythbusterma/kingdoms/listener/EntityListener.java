package com.mythbusterma.kingdoms.listener;

import com.mythbusterma.kingdoms.Kingdoms;
import com.mythbusterma.kingdoms.Village;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityListener implements Listener {
    
    private final Kingdoms parent;

    public EntityListener(Kingdoms parent) {
        this.parent = parent;
    }

    @EventHandler
    public void onSpawn(CreatureSpawnEvent event) {
        if (event.getEntity() instanceof Monster) {
            Village village = parent.getKingdomsManager().getVillage(event.getEntity().getLocation());
            if (village == null) {
                return;
            }
            else if (village.isMobSpawn()) {
                return;
            }
            else {
                event.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        final Village village = parent.getKingdomsManager().getVillage(event.getEntity().getLocation());
        if (village == null) {
            return;
        }
        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent edbee = (EntityDamageByEntityEvent) event;
            if (edbee.getEntity() instanceof Player) {
                Player player = (Player) edbee.getEntity();
                final Village plyVillage = parent.getKingdomsManager().getPlayer(player).getVillage();
                if (plyVillage == null || !plyVillage.equals(village)) {
                    // if the player is an outsider
                    return;
                }
                else {
                    // if the player is a resident
                    if (edbee.getDamager() instanceof Player) {
                        if (village.isPvpAllowed()) {
                            return;
                        }
                        final Village damagerVillage = parent.getKingdomsManager()
                                .getPlayer((Player) edbee.getDamager()).getVillage();
                        if (damagerVillage == null || !damagerVillage.equals(plyVillage)) {
                            if (parent.getConfiguration().isDamageReflect()) {
                                // if enabled, reflect the damage back to the damager
                                event.setCancelled(true);
                                ((Player) edbee.getDamager()).damage(event.getDamage() * 3);
                            }
                            else {
                                event.setCancelled(true);
                            }
                        }
                        else {
                            event.setCancelled(true);
                        }
                    }
                    else {
                        if (edbee.getDamager() instanceof Projectile) {
                            LivingEntity shooter = ((Projectile) edbee.getDamager()).getShooter();
                            if (shooter instanceof Player) {
                                if (village.isPvpAllowed()) {
                                    return;
                                }
                                final Village damagerVillage = parent.getKingdomsManager()
                                        .getPlayer((Player) shooter).getVillage();
                                if (damagerVillage == null || !damagerVillage.equals(plyVillage)) {
                                    if (parent.getConfiguration().isDamageReflect()) {
                                        // if enabled, reflect the damage back to the damager
                                        event.setCancelled(true);
                                        shooter.damage(event.getDamage() * 3);
                                    }
                                    else {
                                        event.setCancelled(true);
                                    }
                                }
                                else {
                                    event.setCancelled(true);
                                }
                            }
                            else {
                                if (parent.getConfiguration().isDamageReflect()) {
                                    // if enabled, reflect the damage back to the damager
                                    event.setCancelled(true);
                                    shooter.damage(event.getDamage() * 3);
                                }
                                else {
                                    event.setCancelled(true);
                                }
                            }
                        }
                        if (parent.getConfiguration().isDamageReflect()) {
                            // if enabled, reflect the damage back to the damager
                            event.setCancelled(true);
                            ((Player) edbee.getDamager()).damage(event.getDamage() * 3);
                        }
                        else {
                            event.setCancelled(true);
                        }
                    }
                }
            }
            else if (event.getEntity() instanceof Animals) {
                if (edbee.getDamager() instanceof Player) {
                    final Village damagerVillage = parent.getKingdomsManager()
                            .getPlayer((Player) edbee.getDamager()).getVillage();
                    if (damagerVillage == null || !damagerVillage.equals(village)) {
                        // damager is an outsider
                        if (!village.getPermissions().isOutsiderDamageEntity()) {
                            if (parent.getConfiguration().isDamageReflect()) {
                                // if enabled, reflect the damage back to the damager
                                event.setCancelled(true);
                                ((Player) edbee.getDamager()).damage(event.getDamage() * 3);
                            } else {
                                event.setCancelled(true);
                            }
                        }
                    }
                    else {
                        if (!village.getPermissions().isResidentDamageEntity()) {
                            event.setCancelled(true);
                            ((Player) edbee.getDamager()).sendMessage(Kingdoms.PREFIX +"Your lord prevents you " +
                                    "from damaging Animals in the village.");
                        }
                    }
                }
                else {
                    event.setCancelled(true);
                }
            }
        }
        
    }
}
