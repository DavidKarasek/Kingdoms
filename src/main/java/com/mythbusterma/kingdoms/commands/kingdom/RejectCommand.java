package com.mythbusterma.kingdoms.commands.kingdom;

import com.mythbusterma.kingdoms.KingdomPlayer;
import com.mythbusterma.kingdoms.commands.CommandResult;
import com.mythbusterma.kingdoms.commands.PlayerCommand;
import org.bukkit.entity.Player;

/**
 * Used for a village lord to reject requests to join their village or
 * for a normal player to reject an invitation to join a village.
 */
public class RejectCommand implements PlayerCommand {
    private final KingdomCommandManager parent;

    public RejectCommand(KingdomCommandManager parent) {
        this.parent = parent;
    }

    @Override
    public CommandResult issue(Player player, String[] args) {
        KingdomPlayer kingdomPlayer = parent.getPlayer(player);
        if (kingdomPlayer.isLord()) {

        }
        else {

        }
        return null;
    }
}
