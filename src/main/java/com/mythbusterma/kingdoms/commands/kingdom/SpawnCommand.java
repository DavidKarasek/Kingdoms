package com.mythbusterma.kingdoms.commands.kingdom;

import com.mythbusterma.kingdoms.Kingdoms;
import com.mythbusterma.kingdoms.Village;
import com.mythbusterma.kingdoms.commands.CommandResult;
import com.mythbusterma.kingdoms.commands.PlayerCommand;
import org.bukkit.entity.Player;

public class SpawnCommand implements PlayerCommand {
    private final KingdomCommandManager parent;

    public SpawnCommand(KingdomCommandManager parent) {
        this.parent = parent;
    }

    @Override
    public CommandResult issue(final Player player, String[] args) {
        final int teleportDelay = parent.getParent().getConfiguration()
                .getTeleportDelay();
        final Village village = parent.getPlayer(player).getVillage();
        if (village != null) {
            if (teleportDelay != 0) {
                parent.getParent().getPlayerMovementWatcher()
                        .watchPlayer(player, teleportDelay * 20,
                                new Runnable() {
                                    // what happens if the player waits
                                    @Override
                                    public void run() {
                                        player.sendMessage(Kingdoms.PREFIX +
                                                "Teleportation commencing in " +
                                                teleportDelay + " seconds, don't move.");

                                        village.spawnPlayer(player);
                                    }
                                }, new Runnable() {
                                    // if the player moves
                                    @Override
                                    public void run() {
                                        player.sendMessage(
                                                Kingdoms.ERROR_PREFIX + "Teleportation cancelled.");
                                    }
                                });
            } else {
                player.sendMessage(Kingdoms.ERROR_PREFIX + "You must be part " +
                        "of a village to teleport to its spawn!");
            }
        }
        return null;
    }
}
