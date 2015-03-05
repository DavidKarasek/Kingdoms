package com.mythbusterma.kingdoms.commands;

import com.mythbusterma.kingdoms.Kingdoms;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.WeakHashMap;

public class KingdomCommand implements CommandExecutor {
    
    private final Kingdoms parent;
    
    private Map<Player,Runnable> acceptanceMap = new WeakHashMap<>();

    public KingdomCommand(Kingdoms parent) {
        this.parent = parent;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String cmd, String[] args) {
        if (args.length == 0 ||  args[0].equals("help") || args[0].equals("h") || args[0].equals("?")) {
            parent.getHelpCommand().provideHelp(commandSender);
        }
        
        if (commandSender instanceof Player) {
            if (args[0].equals("new") || args[0].equals("create")) {
                
            }
        }
        
        return true;
    }
}
