package com.mythbusterma.kingdoms.commands.king;

import com.mythbusterma.kingdoms.KingdomPlayer;
import com.mythbusterma.kingdoms.Kingdoms;
import com.mythbusterma.kingdoms.Village;
import com.mythbusterma.kingdoms.commands.CommandResult;
import com.mythbusterma.kingdoms.commands.KingdomCommand;
import com.mythbusterma.kingdoms.utils.PlayerUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;

/**
 * Used for an admin to forcibly add a player to a village
 */
public class KingAddCommand implements KingdomCommand<CommandSender> {
    private final KingCommandHandler parent;

    public KingAddCommand(KingCommandHandler parent) {
        this.parent = parent;
    }

    @Override
    public CommandResult issue(final CommandSender issuer, String[] args) {
        // args[0] is player name args[1] is village
        final List<? extends Player> players = PlayerUtil.attemptMatch(args[0]);
        if (players.size() == 0) {
            issuer.sendMessage(Kingdoms.ERROR_PREFIX + "There are no players " +
                    "on the server whose name matches \"" + args[0] + "\".");
        } else if (players.size() > 1) {
            issuer.sendMessage(Kingdoms.ERROR_PREFIX + "Found multiple " +
                    "probable matches for \"" + args[0] + "\", please be more" +
                    " specific to select one. Matched names:");
            for (Player player : players) {
                issuer.sendMessage("   " + player.getDisplayName());
            }
        } else {
            final Collection<Village> villages = parent.getParent()
                    .getKingdomsManager().matchVillage(args[1]);
            if (villages.size() == 0) {
                issuer.sendMessage(Kingdoms.ERROR_PREFIX + "There are no " +
                        "villages on the server whose name matches \"" +
                        args[1] + "\".");
            } else if (villages.size() > 1) {
                issuer.sendMessage(Kingdoms.ERROR_PREFIX + "There are " +
                        "multiple villages with names similar to \"" +
                        args[1] + "\" matched villages: ");
                for (Village village : villages) {
                    issuer.sendMessage("   " + village.getName());
                }
            } else {
                final KingdomPlayer matchedPlayer = parent.getParent()
                        .getKingdomsManager().getPlayer(players.get(0));
                final Village villageToJoin = villages.iterator().next();
                if (villageToJoin.equals(matchedPlayer.getVillage())) {
                    issuer.sendMessage(
                            Kingdoms.PREFIX + matchedPlayer.getPlayer()
                                    .getName() + " is already a part of " +
                                    villageToJoin.getName());
                } else if (matchedPlayer.getVillage() == null) {
                    issuer.sendMessage(Kingdoms.PREFIX + "Added " +
                            matchedPlayer.getPlayer().getName() + " to " +
                            villageToJoin.getName());
                    matchedPlayer.setVillage(villageToJoin);
                } else {
                    issuer.sendMessage(Kingdoms.PREFIX + "Warning, assigning " +
                            "this player to " + villageToJoin
                            .getName() + " will remove them from their " +
                            "current village, " + matchedPlayer.getVillage()
                            .getName() + ". Are you sure you want to do this " +
                            "(/k accept to confirm).");
                    parent.getParent().getKingdomCommandManager()
                            .getAcceptCommand()
                            .addAcceptAction(issuer, new Runnable() {
                                @Override
                                public void run() {
                                    issuer.sendMessage(Kingdoms.PREFIX +
                                            "Successfully switched " +
                                            matchedPlayer.getPlayer().getName()
                                            +  " from " + matchedPlayer
                                            .getVillage().getName() + " to "
                                            + villageToJoin.getName());
                                    matchedPlayer.setVillage(villageToJoin);

                                }
                            });
                }
            }
        }
        return null;
    }
}
