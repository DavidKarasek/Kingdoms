package com.mythbusterma.kingdoms.commands.kingdom;


import com.mythbusterma.kingdoms.InviteManager;
import com.mythbusterma.kingdoms.Kingdoms;
import com.mythbusterma.kingdoms.Village;
import com.mythbusterma.kingdoms.commands.CommandResult;
import com.mythbusterma.kingdoms.commands.PlayerCommand;
import com.mythbusterma.kingdoms.utils.PlayerUtil;
import com.mythbusterma.kingdoms.utils.VillageUtil;
import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * Used when a player wishes to join a village
 */
public class JoinCommand implements PlayerCommand {
    private final KingdomCommandManager parent;

    public JoinCommand(KingdomCommandManager parent) {
        this.parent = parent;
    }

    @Override
    public CommandResult issue(Player player, String[] args) {
        if (args[0].equalsIgnoreCase("here")) {
            Village here = parent.getParent().getKingdomsManager()
                    .getVillage(player.getLocation());
            if (here != null) {
                attemptJoin(player, here);
            } else {
                player.sendMessage(Kingdoms.ERROR_PREFIX + "There is no " +
                        "village underfoot to join!");
            }
        } else {
            String villageName = args[0];
            Village exactMatch = parent.getParent().getKingdomsManager()
                    .getVillage(villageName);
            if (exactMatch != null) {
                attemptJoin(player, exactMatch);
            } else {
                Collection<Village> matches = parent.getParent()
                        .getKingdomsManager().matchVillage(villageName);

                if (matches.size() != 0) {
                    if (matches.size() == 1) {
                        attemptJoin(player, matches.iterator().next());
                    } else {
                        player.sendMessage(
                                Kingdoms.PREFIX + "There are multiple " +
                                        "villages similar to \"" + villageName + ",\" " +
                                        "matches:");
                        for (Village village : matches) {
                            player.sendMessage("  " + village.getName());
                        }
                    }
                } else {
                    player.sendMessage(
                            Kingdoms.ERROR_PREFIX + "There aren't any " +
                                    "villages on the server with names similar to \"" +
                                    villageName + "\".");
                }
            }
        }
        return null;
    }

    private void attemptJoin(Player player, Village exactMatch) {
        final InviteManager inviteManager = parent.getParent()
                .getInviteManager();
        if (inviteManager.hasInvite(exactMatch, player)) {
            parent.getPlayer(player).setVillage(exactMatch);
            player.sendMessage(Kingdoms.PREFIX + "You are now a part " +
                    "of " + exactMatch.getName());
            VillageUtil.messageVillage(exactMatch,
                    Kingdoms.PREFIX + PlayerUtil.shownName(player) +
                            " is now part of your village.");
            inviteManager.removeInvite(exactMatch, player);
        } else {
            player.sendMessage(Kingdoms.PREFIX + "Sending a request to " +
                    "the lord of " + exactMatch.getName() + " to join " +
                    "their village.");
            if (inviteManager.hasRequest(player)) {
                player.sendMessage(Kingdoms.PREFIX +"Please note this will " +
                        "override your request to join "
                        + inviteManager.getRequest(player));
            }
            inviteManager.addRequest(player, exactMatch);
        }
    }
}
