package com.mythbusterma.kingdoms.commands.lord;

import com.mythbusterma.kingdoms.InviteManager;
import com.mythbusterma.kingdoms.KingdomPlayer;
import com.mythbusterma.kingdoms.Kingdoms;
import com.mythbusterma.kingdoms.commands.BaseCommandExecutor;
import com.mythbusterma.kingdoms.commands.CommandResult;
import com.mythbusterma.kingdoms.commands.PlayerCommand;
import com.mythbusterma.kingdoms.utils.PlayerUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Used by lords to extend an invitation to join their village to other players
 */
public class AddCommand implements PlayerCommand {
    private final BaseCommandExecutor parent;

    public AddCommand(BaseCommandExecutor parent) {
        this.parent = parent;
    }

    @Override
    public CommandResult issue(Player player, String[] args) {
        KingdomPlayer kingdomPlayer = parent.getParent().getKingdomsManager()
                .getPlayer(player);
        String plyName = args[0];
        List<? extends Player> matches = PlayerUtil.attemptMatch(plyName);
        if (matches.size() != 0) {
            if (matches.size() == 1) {
                final Player matchedPlayer = matches.get(0);
                final InviteManager inviteManager = parent.getParent()
                        .getInviteManager();
                final KingdomPlayer invitee = parent.getParent()
                        .getKingdomsManager().getPlayer(matchedPlayer);
                if (inviteManager.hasRequest(matchedPlayer,
                        kingdomPlayer.getVillage())) {
                    player.sendMessage(Kingdoms.PREFIX + "Adding " +
                            PlayerUtil.shownName(matchedPlayer) +
                            ") to your village...");

                    invitee.setVillage(kingdomPlayer.getVillage());
                    inviteManager.removeRequest(matchedPlayer);
                } else {
                    player.sendMessage(Kingdoms.PREFIX + "Extending an " +
                                    "invitation to join your village to " +
                                    PlayerUtil.shownName(matchedPlayer));
                    inviteManager.addInvite(kingdomPlayer.getVillage(),
                            matchedPlayer);
                    if (invitee.getVillage() != null) {
                        player.sendMessage(Kingdoms.PREFIX + "Please note" +
                                        " that " + PlayerUtil
                                        .shownName(matchedPlayer) +
                                        " is already part of " +
                                        "a village and cannot join" +
                                        " your village without leaving theirs.");
                    }
                }
            } else {
                player.sendMessage(Kingdoms.PREFIX + "There are " +
                        "multiple players that match \"" + plyName + ",\" be more specific to select one. Players" +
                        " matched:");
                for (Player matchedPlayer : matches) {
                    // for every matched player print a message
                    // similar to "  <name> (<display name>)"
                    player.sendMessage("  " + ChatColor.BLUE +
                            PlayerUtil.shownName(matchedPlayer));
                }
            }
        } else {
            player.sendMessage(Kingdoms.ERROR_PREFIX + "There are no " +
                    "online players with a name similar to \"" +
                    plyName + "\".");
        }


        return null;
    }
}
