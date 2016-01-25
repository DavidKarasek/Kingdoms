package com.mythbusterma.kingdoms.commands.king;

import com.mythbusterma.kingdoms.Kingdoms;
import com.mythbusterma.kingdoms.Village;
import com.mythbusterma.kingdoms.commands.CommandResult;
import com.mythbusterma.kingdoms.commands.KingdomCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * The admin command for deleting a village
 */
public class KingDisbandCommand implements KingdomCommand<CommandSender> {
    private final KingCommandHandler parent;

    public KingDisbandCommand(KingCommandHandler parent) {
        this.parent = parent;
    }

    @Override
    public CommandResult issue(CommandSender issuer, String[] args) {
        if (args[0].equalsIgnoreCase("here")) {
            if (issuer instanceof Player) {
                Player player = (Player) issuer;
                Village here = parent.getParent().getKingdomsManager()
                        .getVillage(player.getLocation());
                if (here != null) {
                    beginDelete(here, issuer);
                } else {
                    player.sendMessage(Kingdoms.ERROR_PREFIX + "There is no " +
                            "village underfoot to delete!");
                }
            }
            else {
                issuer.sendMessage(Kingdoms.ERROR_PREFIX + "Only a player can" +
                        " delete a village by standing in it. You can still " +
                        "delete a village by specifying a name.");
            }
        } else {
            String villageName = args[0];
            Village exactMatch = parent.getParent().getKingdomsManager()
                    .getVillage(villageName);
            if (exactMatch != null) {
                beginDelete(exactMatch, issuer);
            } else {
                Collection<Village> matches = parent.getParent()
                        .getKingdomsManager().matchVillage(villageName);
                if (matches.size() != 0) {
                    if (matches.size() == 1) {
                        Village match = matches.iterator().next();
                        issuer.sendMessage(Kingdoms.PREFIX + "Matched to " +
                                ChatColor.ITALIC + match.getName());
                        beginDelete(match, issuer);
                    } else {
                        issuer.sendMessage(
                                Kingdoms.PREFIX + "There are multiple " +
                                        "villages similar to \"" + villageName + ",\" " +
                                        "matches:");
                        for (Village village : matches) {
                            issuer.sendMessage("  " + village.getName());
                        }
                    }
                } else {
                    issuer.sendMessage(
                            Kingdoms.ERROR_PREFIX + "There aren't any " +
                                    "villages on the server with names similar to \"" +
                                    villageName + "\".");
                }
            }
        }
        return null;
    }

    private void beginDelete(final Village village, final CommandSender issuer) {
        issuer.sendMessage(Kingdoms.PREFIX + "Note: you are about to delete " +
                "the village \"" + village.getName() + "\", are you sure you " +
                "want to do this (/k accept to confirm)?");
        parent.getParent().getKingdomCommandManager().getAcceptCommand()
                .addAcceptAction(issuer, new Runnable() {
                    @Override
                    public void run() {
                        issuer.sendMessage(Kingdoms.PREFIX + "Deleting " +
                                village.getName() + "...");
                        village.removeVillage();
                    }
                });
    }
}
