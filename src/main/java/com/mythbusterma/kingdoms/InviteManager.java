package com.mythbusterma.kingdoms;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Keeps track of all town invites and requests to join.
 *
 * One instance per runtime.
 */
public class InviteManager {
    private HashMap<Village, Set<KingdomPlayer>> invites = new HashMap<>();
    private HashMap<KingdomPlayer, Village> requests = new HashMap<>();
    private final Kingdoms parent;

    public InviteManager(Kingdoms parent) {
        this.parent = parent;
    }

    /**
     * Checks if a player has an invite to a village.
     *
     * @param village the village to check invites to
     * @param player the player to see if he has been invited
     * @return true if an invite matching this exactly exists
     */
    public boolean hasInvite(Village village, Player player) {
        return invites.get(village) != null &&
                invites.get(village)
                        .contains(parent.getKingdomsManager().getPlayer(player));
    }

    public void addInvite(Village village, Player player){
        KingdomPlayer kingdomPlayer = parent.getKingdomsManager()
                .getPlayer(player);
        if (invites.get(village) == null) {
            Set<KingdomPlayer> inviteSet = new HashSet<>();
            inviteSet.add(kingdomPlayer);
            invites.put(village, inviteSet);
        }
        else {
            invites.get(village).add(kingdomPlayer);
        }
    }

    /**
     * Removes a player's invitation to join a town.
     *
     * @param village
     * @param player
     */
    public void removeInvite(Village village, Player player) {
        if (invites.get(village) != null) {
            invites.get(village)
                    .remove(parent.getKingdomsManager().getPlayer(player));
        }
    }

    /**
     * Checks if a player has a request to join a specific village
     *
     * @param player the player which to check the request for
     * @param village the village to match the request against
     * @return true if a request matching both the player and village exists
     */
    public boolean hasRequest (Player player, Village village) {
        KingdomPlayer kingdomPlayer = parent.getKingdomsManager().getPlayer(player);
        if (requests.get(kingdomPlayer) != null) {
            return requests.get(kingdomPlayer).equals(village);
        }
        else {
            return false;
        }
    }

    public boolean hasRequest(Player player) {
        KingdomPlayer kingdomPlayer = parent.getKingdomsManager().getPlayer(player);
        return requests.get(kingdomPlayer) != null;
    }

    /**
     * Puts in a pending request for a player to join a village, overwriting
     * any previous requests.
     *
     * @param player the player requesting to join
     * @param village the village the player wishes to join
     */
    public void addRequest (Player player, Village village) {
        requests.put(parent.getKingdomsManager().getPlayer(player), village);
    }

    public void removeRequest(Player player) {
        requests.remove(parent.getKingdomsManager().getPlayer(player));
    }


    /**
     * Determines what village a player has requested to join, if any
     *
     * @param player
     * @return
     */
    public Village getRequest(Player player) {
        KingdomPlayer kingdomPlayer = parent.getKingdomsManager().getPlayer(player);
        return requests.get(kingdomPlayer);
    }
}
