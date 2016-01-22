package com.mythbusterma.kingdoms.commands.kingdom;

import com.mythbusterma.kingdoms.Kingdoms;
import com.mythbusterma.kingdoms.Village;
import com.mythbusterma.kingdoms.commands.CommandResult;
import com.mythbusterma.kingdoms.commands.KingdomCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DisbandCommand implements KingdomCommand<CommandSender> {

    private final KingdomCommandManager parent;

    public DisbandCommand(KingdomCommandManager parent) {
        this.parent = parent;
    }

    @Override
    public CommandResult issue(CommandSender issuer, String[] args) {
        if (args.length == 1) {
            if (issuer instanceof Player) {
                final Player player = (Player) issuer;
                final Village village = parent.getParent().getKingdomsManager().getPlayer(player).getVillage();
                if (village != null) {
                    if (parent.getParent().getKingdomsManager().getPlayer(player).isLord()) {
                        player.sendMessage(Kingdoms.PREFIX + "You are about to permanently delete " +
                                "your village, are you sure you want to do this? (\"/k accept\" to confirm)");
                        parent.getAcceptCommand().addAcceptAction(player, new Runnable() {
                            @Override
                            public void run() {
                                player.sendMessage(Kingdoms.PREFIX + "Deleting village....");
                                parent.getParent().getMySqlConnector().deleteVillage(village.getId());
                                parent.getParent().getKingdomsManager().deleteVillage(village);
                            }
                        });

                    } else {
                        player.sendMessage(Kingdoms.ERROR_PREFIX + "Only lords can disband villages, and you " +
                                "are not a lord.");
                    }
                } else {
                    player.sendMessage(Kingdoms.ERROR_PREFIX + "You are not part of a village, what are you " +
                            "trying to disband?");
                }
            } else {
                issuer.sendMessage(Kingdoms.ERROR_PREFIX + "Only a player can disband their village. Try " +
                        "/k disband (name)");
            }
        } else {
            // if a town name is provided
            if (issuer.hasPermission("kingdoms.king.delete")) {
                // TODO name matching

            } else {
                issuer.sendMessage(Kingdoms.ERROR_PREFIX + "You don't have permission to perform this " +
                        "command, perhaps you meant /k disband");
            }
        }
        return CommandResult.SUCCESS;
    }
}
