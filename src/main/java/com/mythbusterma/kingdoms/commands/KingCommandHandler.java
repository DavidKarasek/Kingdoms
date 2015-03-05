package com.mythbusterma.kingdoms.commands;

import com.mythbusterma.kingdoms.Kingdoms;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public class KingCommandHandler implements CommandExecutor {
    
    private Kingdoms kingdoms;
    
    public KingCommandHandler(Kingdoms kingdoms) {
        this.kingdoms = kingdoms;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String cmd, String[] args) {
        
        return false;
    }
}
