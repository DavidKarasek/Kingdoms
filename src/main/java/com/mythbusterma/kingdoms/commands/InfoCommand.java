package com.mythbusterma.kingdoms.commands;

import com.mythbusterma.kingdoms.Kingdoms;
import com.mythbusterma.kingdoms.Village;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class InfoCommand implements PlayerCommand {

    private KingdomCommandManager parent;

    public InfoCommand(KingdomCommandManager parent) {
        this.parent = parent;
    }

    @Override
    public CommandResult issue(Player player, String[] args) {
        Village village = parent.getParent().getKingdomsManager().getVillage(player.getLocation());
        if (village == null) {
            player.sendMessage(Kingdoms.ERROR_PREFIX + "There isn't a village underfoot, no info to display");
        } else {
            player.sendMessage(Kingdoms.PREFIX + "You're standing in " + ChatColor.ITALIC + village.getName());
            // TODO add sql request for all residents
            player.sendMessage("Size (in chunks): " + village.getNumChunks());
            player.sendMessage("ID: " + village.getId());
        }
        return CommandResult.SUCCESS;
    }
}
