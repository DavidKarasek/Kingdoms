package com.mythbusterma.kingdoms.commands;

import org.bukkit.entity.Player;

public interface PlayerCommand extends KingdomCommand<Player> {
    CommandResult issue(Player player, String[] args);
}
