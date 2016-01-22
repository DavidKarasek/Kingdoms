package com.mythbusterma.kingdoms.listener;

import com.mythbusterma.kingdoms.Kingdoms;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.WeakHashMap;

public class PlayerMovementWatcher implements Listener {
    private final Kingdoms parent;

    WeakHashMap<Player, WatcherMeta> watched = new WeakHashMap<>();

    private static final class WatcherMeta {
        private Runnable callbackSuccess;
        private Runnable callbackFailure;

        public WatcherMeta(Runnable callbackSuccess, Runnable callbackFailure) {
            this.callbackFailure = callbackFailure;
            this.callbackSuccess = callbackSuccess;
        }
    }

    public PlayerMovementWatcher(Kingdoms parent) {
        this.parent = parent;
    }


    public void watchPlayer(final Player player, int duration, Runnable
            callbackSuccess,Runnable callbackFailure) {
        watched.put(player, new WatcherMeta(callbackSuccess, callbackFailure));
        new BukkitRunnable() {
            @Override
            public void run() {
                final WatcherMeta watcherMeta = watched.get(player);
                if (watcherMeta != null) {
                    watched.remove(player);
                    watcherMeta.callbackSuccess.run();
                }
            }
        }.runTaskLater(parent, duration);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        final WatcherMeta watcherMeta = watched.get(event.getPlayer());
        if (watcherMeta != null) {
            watcherMeta.callbackFailure.run();
            watched.remove(event.getPlayer());
        }
    }

}
