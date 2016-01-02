package com.mythbusterma.kingdoms.commands;

import org.bukkit.command.CommandSender;

public interface KingdomCommand<T extends CommandSender> {
    CommandResult issue(T issuer, String[] args);
}
