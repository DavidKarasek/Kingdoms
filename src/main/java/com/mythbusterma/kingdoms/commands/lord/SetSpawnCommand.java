package com.mythbusterma.kingdoms.commands.lord;

import com.mythbusterma.kingdoms.KingdomPlayer;
import com.mythbusterma.kingdoms.Kingdoms;
import com.mythbusterma.kingdoms.commands.CommandResult;
import com.mythbusterma.kingdoms.commands.PlayerCommand;
import com.mythbusterma.kingdoms.commands.kingdom.KingdomCommandManager;
import org.bukkit.entity.Player;

public class SetSpawnCommand implements PlayerCommand {

    private final LordCommand parent;

    public SetSpawnCommand(LordCommand parent) {
        this.parent = parent;
    }

    @Override
    public CommandResult issue(Player player, String[] args) {
        KingdomPlayer kingdomPlayer = parent.getParent().getKingdomsManager()
                .getPlayer(player);

        player.sendMessage(Kingdoms.PREFIX + "Successfully set your " +
                "village spawn.");
        //noinspection ConstantConditions
        kingdomPlayer.getVillage().setSpawn(player.getLocation());

        return null;
    }
}
