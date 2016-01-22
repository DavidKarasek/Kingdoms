package com.mythbusterma.kingdoms.commands.kingdom;

import com.mythbusterma.kingdoms.KingdomPlayer;
import com.mythbusterma.kingdoms.Kingdoms;
import com.mythbusterma.kingdoms.commands.CommandResult;
import com.mythbusterma.kingdoms.commands.PlayerCommand;
import org.bukkit.entity.Player;

public class SetSpawnCommand implements PlayerCommand {

    private final KingdomCommandManager parent;

    public SetSpawnCommand(KingdomCommandManager parent) {
        this.parent = parent;
    }

    @Override
    public CommandResult issue(Player player, String[] args) {
        KingdomPlayer kingdomPlayer = parent.getPlayer(player);
        if (kingdomPlayer.getVillage() != null) {
            if (kingdomPlayer.isLord()) {
                player.sendMessage(Kingdoms.PREFIX + "Successfully set your " +
                        "village spawn.");
                kingdomPlayer.getVillage().setSpawn(player.getLocation());
            }
            else {
                player.sendMessage(Kingdoms.ERROR_PREFIX + "Only lords may " +
                        "set the village spawn.");
            }
        }
        else {
            player.sendMessage(Kingdoms.ERROR_PREFIX + "You must be part of a" +
                    " village to set its spawn");
        }
        return null;
    }
}
