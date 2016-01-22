package com.mythbusterma.kingdoms.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerUtil {
    /**
     * This methods tries varies ways of finding a player based on their
     * name, using a couple different methods provided by Bukkit.
     *
     * @param name a part of a name to search for
     * @return a Player if one was found, or null if no Player was found
     */
    public static Player getPlayer (String name) {
        Player playerExact = Bukkit.getPlayerExact(name);
        if (playerExact != null) {
            return playerExact;
        }
        Player playerGet = Bukkit.getPlayer(name);
        if (playerGet != null) {
            return playerGet;
        }

        int delta = 500;
        String lowerName = name.toLowerCase();
        Player bestFit = null;
        for (Player player : OnlinePlayers.get()) {
            String cleanName = ChatColor.stripColor(player.getDisplayName())
                    .toLowerCase();
            if (cleanName.startsWith(lowerName)) {
                int curDelta = lowerName.length() - cleanName.length();
                if (curDelta < delta) {
                    delta = curDelta;
                    bestFit = player;
                }
            }
        }

        // if bestFit is also null, this returns null
        return bestFit;
    }

    /**
     * Tries using Bukkit's match player, and if that yields no results,
     * tries matching players based on display names.
     *
     * @param name the name of a player to look for
     * @return a List containing zero or more players that are matched to name
     */
    public static List<? extends Player> attemptMatch(String name) {
        List<? extends Player> players = Bukkit.matchPlayer(name);
        if (players.size() > 0) {
            return players;
        }

        List<Player> matches = new ArrayList<>();
        String lowerName = name.toLowerCase();
        for (Player player : OnlinePlayers.get()) {
            String cleanName = ChatColor.stripColor(player.getDisplayName())
                    .toLowerCase();
            if (cleanName.startsWith(lowerName)) {
                matches.add(player);
            }
        }
        return matches;
    }

    public static String shownName (Player player) {
        return player.getName() + " (" + player
                .getDisplayName() + ChatColor.RESET + ")";
    }

}
