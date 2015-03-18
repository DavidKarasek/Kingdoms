package com.mythbusterma.kingdoms.commands;

import com.mythbusterma.kingdoms.Kingdoms;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.WeakHashMap;

public class KingdomCommand implements CommandExecutor {
    
    private final Kingdoms parent;

    @Deprecated
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
                if (args.length != 1) {
                    String name = "";
                    for (int i = 1; i < args.length; i ++) {
                        name += args[i];
                    }
                    if (parent.getKingdomsManager().getPlayer((Player) commandSender) == null) {
                        if (commandSender.hasPermission("kingdoms.player.new")) {
                            if (parent.getConfiguration().isEconomy()) {
                                if (parent.getEconomy().withdrawPlayer((OfflinePlayer) commandSender,
                                        parent.getConfiguration().getVillageCost()).transactionSuccess()) {
                                    commandSender.sendMessage(Kingdoms.PREFIX + "Successfully created village "
                                            + name);
                                    if (parent.getConfiguration().getUpkeep() != 0) {
                                        commandSender.sendMessage(Kingdoms.PREFIX + "Remember that villages require " +
                                                "upkeep, and you must put money in the village coffers to keep the " +
                                                "village from disappearing.");
                                    }

                                } else {
                                    commandSender.sendMessage(Kingdoms.ERROR_PREFIX + "You don't have enough " +
                                            "money to start a village, a village costs "
                                            + parent.getConfiguration().getVillageCost());
                                }
                            } else {

                            }
                        } else {
                            commandSender.sendMessage(Kingdoms.ERROR_PREFIX + "You don't have permission to " +
                                    "create a new village.");
                        }
                    } else {
                        commandSender.sendMessage(Kingdoms.ERROR_PREFIX + "You are already part of a town, leave " +
                                "your current town before attempting to create a new one.");
                    }
                }
            }
            else {
                commandSender.sendMessage(Kingdoms.ERROR_PREFIX + "You need to give your village a name!");
            }
        }
        
        return true;
    }
}
