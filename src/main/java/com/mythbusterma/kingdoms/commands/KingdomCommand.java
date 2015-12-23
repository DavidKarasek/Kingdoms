package com.mythbusterma.kingdoms.commands;

import com.mythbusterma.kingdoms.*;
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

    private Map<Player, Runnable> acceptanceMap = new WeakHashMap<>();

    public KingdomCommand(Kingdoms parent) {
        this.parent = parent;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String cmd, String[] args) {
        if (args.length == 0 || args[0].equals("help") || args[0].equals("h") || args[0].equals("?")) {
            parent.getHelpCommand().provideHelp(commandSender);
            return true;
        }

        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (args[0].equals("new") || args[0].equals("create")) {
                if (args.length != 1) {
                    String name = "";
                    for (int i = 1; i < args.length; i++) {
                        name += args[i];
                    }
                    if (parent.getKingdomsManager().getPlayer((Player) commandSender).getVillage() == null) {
                        if (parent.getKingdomsManager().getVillage(player.getLocation()) == null) {
                            if (commandSender.hasPermission("kingdoms.player.new")) {
                                if (parent.getKingdomsManager().getVillage(name) == null) {
                                    if (!name.matches("[!-@\\[-`](.*)")) {
                                        if (parent.getConfiguration().isEconomy()) {
                                            if (parent.getEconomy().withdraw((OfflinePlayer) commandSender,
                                                    parent.getConfiguration().getVillageCost()) == 0) {
                                                commandSender.sendMessage(Kingdoms.PREFIX + "Successfully created village "
                                                        + name);
                                                Village village = new Village(name, player.getLocation().getWorld());
                                                village.setBalance(parent.getConfiguration().getDefaultBalance());
                                                village.incrementChunks();
                                                VillageBlock newBlock = new VillageBlock(
                                                        new ChunkLocation(player.getLocation()),
                                                        new PlotPermissions(), village);
                                                parent.getKingdomsManager().addVillageBlock(newBlock);
                                                parent.getMySqlConnector().addVillage(parent.getUuidHolder()
                                                        .getUuid(player.getName()), village, newBlock);
                                                parent.getKingdomsManager().getPlayer(player).setVillage(village);
                                                if (parent.getConfiguration().getUpkeep() != 0 && parent.getEconomy() != null) {
                                                    commandSender.sendMessage(Kingdoms.PREFIX + "Remember that villages require " +
                                                            "upkeep, and you must put money in the village coffers to keep the " +
                                                            "village from disappearing.");
                                                }
                                                return true;

                                            } else {
                                                commandSender.sendMessage(Kingdoms.ERROR_PREFIX + "You don't have enough " +
                                                        "money to start a village, a village costs "
                                                        + parent.getConfiguration().getVillageCost());
                                                return true;
                                            }
                                        } else {
                                            // TODO make this work if economy is disabled
                                        }
                                    } else {
                                        commandSender.sendMessage(Kingdoms.ERROR_PREFIX + "That is not a valid name, please try another");
                                        return true;
                                    }
                                } else {
                                    commandSender.sendMessage(Kingdoms.ERROR_PREFIX + "A village of that name already exists.");
                                    return true;
                                }
                            } else {
                                commandSender.sendMessage(Kingdoms.ERROR_PREFIX + "You don't have permission to " +
                                        "create a new village.");
                                return true;
                            }
                        } else {
                            player.sendMessage(Kingdoms.ERROR_PREFIX + "A village already exists in this location! Don't try " +
                                    "to take over someone's village!");
                            return true;
                        }
                    } else {
                        commandSender.sendMessage(Kingdoms.ERROR_PREFIX + "You are already part of a village, leave " +
                                "your current village before attempting to create a new one.");
                        return true;
                    }
                } else {
                    commandSender.sendMessage(Kingdoms.ERROR_PREFIX + "You need to give your village a name!");
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("claim") || args[0].equalsIgnoreCase("c")) {
                if (parent.getKingdomsManager().getVillage(player.getLocation()) == null) {
                    if (parent.getKingdomsManager().getPlayer(player).isLord()) {
                        if (parent.getKingdomsManager().getPlayer(player).getVillage() != null) {
                            ChunkLocation location = new ChunkLocation(player.getLocation());
                            boolean nearby = false;
                            Village village = parent.getKingdomsManager().getPlayer(player).getVillage();

                            if (parent.getKingdomsManager().getVillage(new ChunkLocation(location.x - 1, location.z, location.world)) != null &&
                                    parent.getKingdomsManager().getVillage(new ChunkLocation(location.x - 1, location.z, location.world)).equals(village)) {
                                nearby = true;
                            } else if (parent.getKingdomsManager().getVillage(new ChunkLocation(location.x + 1, location.z, location.world)) != null &&
                                    parent.getKingdomsManager().getVillage(new ChunkLocation(location.x + 1, location.z, location.world)).equals(village)) {
                                nearby = true;
                            } else if (parent.getKingdomsManager().getVillage(new ChunkLocation(location.x, location.z - 1, location.world)) != null &&
                                    parent.getKingdomsManager().getVillage(new ChunkLocation(location.x, location.z - 1, location.world)).equals(village)) {
                                nearby = true;
                            } else if (parent.getKingdomsManager().getVillage(new ChunkLocation(location.x, location.z + 1, location.world)) != null &&
                                    parent.getKingdomsManager().getVillage(new ChunkLocation(location.x, location.z + 1, location.world)).equals(village)) {
                                nearby = true;
                            }
                            if (nearby) {
                                if (parent.getConfiguration().isEconomy()) {
                                    if (village.getBalance() - parent.getConfiguration().getClaimCost() >= 0) {
                                        VillageBlock block = new VillageBlock(new ChunkLocation(player.getLocation()),
                                                new PlotPermissions(), village);
                                        parent.getKingdomsManager().addVillageBlock(block);
                                        parent.getMySqlConnector().addBlock(block);
                                        village.setBalance(village.getBalance() - parent.getConfiguration().getClaimCost());
                                        parent.getMySqlConnector().updateBalance(village.getId(), village.getBalance());
                                        player.sendMessage(Kingdoms.PREFIX + "Land claimed.");
                                        return true;
                                    } else {
                                        player.sendMessage(Kingdoms.ERROR_PREFIX + "Your village doesn't have enough money " +
                                                "to claim this land, deposit more funds with /kingdoms deposit (/k d)");
                                        return true;
                                    }
                                } else {
                                    // TODO make this work without economy
                                }
                            } else {
                                player.sendMessage(Kingdoms.ERROR_PREFIX + "This chunk is not near your village, " +
                                        "additional claimed chunks must be adjacent to the chunks you've already claimed");
                                return true;
                            }
                        } else {
                            player.sendMessage(Kingdoms.ERROR_PREFIX + "You must be a lord to claim land!");
                            return true;

                        }
                    } else {
                        player.sendMessage(Kingdoms.ERROR_PREFIX + "You do not belong to a village, you cannot claim land.");
                        return true;
                    }
                } else {
                    commandSender.sendMessage(Kingdoms.ERROR_PREFIX + "This area is already claimed by: "
                            + parent.getKingdomsManager().getVillage(player.getLocation()).getName()
                            + ". You cannot claim here");
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("deposit") || args[0].equalsIgnoreCase("d")) {
                if (parent.getKingdomsManager().getPlayer(player).getVillage() != null) {
                    Village village = parent.getKingdomsManager().getPlayer(player).getVillage();
                    // just to suppress warnings
                    if (village == null) {
                        return false;
                    }
                    if (args.length >= 2) {
                        double amount = 0;

                        try {
                            amount = Double.parseDouble(args[1]);
                        } catch (NumberFormatException ex) {
                            player.sendMessage(Kingdoms.ERROR_PREFIX + args[1] + " is not a number, please enter a valid number.");
                            return true;
                        }

                        if (parent.getConfiguration().isEconomy()) {
                            if (parent.getEconomy().withdraw(player, amount) == 0) {
                                player.sendMessage(Kingdoms.PREFIX + "You have deposited " + args[1] + " into your " +
                                        "village's coffers.");
                                village.setBalance(village.getBalance() + amount);
                                parent.getMySqlConnector().updateBalance(village.getId(), village.getBalance());
                                return true;
                            } else {
                                player.sendMessage(Kingdoms.ERROR_PREFIX + "You don't have enough money to make that deposit, " +
                                        "try spending within your means.");
                                return true;
                            }
                        } else {
                            player.sendMessage(Kingdoms.ERROR_PREFIX + "These villages do not require money, and as such, you " +
                                    "cannot deposit money into the village coffers.");
                            return true;
                        }
                    } else {
                        player.sendMessage(Kingdoms.ERROR_PREFIX + "You must specify an amount when depositing " +
                                "money into your village");
                        return true;
                    }
                } else {
                    player.sendMessage(Kingdoms.ERROR_PREFIX + "You aren't part of a village, you cannot deposit money " +
                            "into nothing!");
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("list") || args[0].equalsIgnoreCase("l")) {
                if (player.hasPermission("kingdoms.player.list")) {
                    if (!parent.getKingdomsManager().getVillages().isEmpty()) {
                        player.sendMessage(Kingdoms.PREFIX + "Villages: (your village appears in green, " +
                                "villages open to the public in blue, others red)\n");
                        StringBuilder builder = new StringBuilder();
                        Village playerVillage = parent.getKingdomsManager().getPlayer(player).getVillage();
                        for (Village village : parent.getKingdomsManager().getVillages()) {
                            if (playerVillage != null) {
                                if (village.equals(playerVillage)) {
                                    builder.append(ChatColor.GREEN);
                                }
                            } else if (village.isOpen()) {
                                builder.append(ChatColor.BLUE);
                            } else {
                                builder.append(ChatColor.DARK_RED);
                            }
                            builder.append(village.getName());
                            builder.append(", ");
                        }
                        builder.deleteCharAt(builder.length() - 1);
                        builder.deleteCharAt(builder.length() - 1);

                        player.sendMessage(builder.toString());
                        return true;
                    }
                    else {
                        player.sendMessage(Kingdoms.PREFIX +"There aren't any villages on this server," +
                                "perhaps you should create one! (/k new <name>)");
                        return true;
                    }
                } else {
                    player.sendMessage(Kingdoms.ERROR_PREFIX + "You don't have permission to perform this command.");
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("accept")) {
                if (acceptanceMap.get(player) != null) {
                    player.sendMessage(Kingdoms.PREFIX + "Action accepted.");
                    acceptanceMap.get(player).run();
                    return true;
                } else {
                    player.sendMessage(Kingdoms.ERROR_PREFIX + "There is no action for you to accept!");
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("info")) {
                if (player.hasPermission("kingdoms.info")) {
                    Village village = parent.getKingdomsManager().getVillage(player.getLocation());
                    if (village == null) {
                        player.sendMessage(Kingdoms.ERROR_PREFIX + "There isn't a village underfoot, no info to display");
                        return true;
                    } else {
                        player.sendMessage(Kingdoms.PREFIX + "You're standing in " + ChatColor.ITALIC + village.getName());
                        // TODO add sql request for all residents
                        player.sendMessage("Size (in chunks): " + village.getNumChunks());
                        player.sendMessage("ID: " + village.getId());
                        return true;
                    }
                }
            }
        }
        if (args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("disband")) {
            if (args.length == 1) {
                if (commandSender instanceof Player) {
                    final Player player = (Player) commandSender;
                    if (player.hasPermission("kingdoms.player.new")) {
                        final Village village = parent.getKingdomsManager().getPlayer(player).getVillage();
                        if (village != null) {
                            if (parent.getKingdomsManager().getPlayer(player).isLord()) {
                                player.sendMessage(Kingdoms.PREFIX + "You are about to permanently delete " +
                                        "your village, are you sure you want to do this? (\"/k accept\" to confirm)");
                                acceptanceMap.put(player, new Runnable() {
                                    @Override
                                    public void run() {
                                        player.sendMessage(Kingdoms.PREFIX + "Deleting village....");
                                        parent.getMySqlConnector().deleteVillage(village.getId());
                                        parent.getKingdomsManager().deleteVillage(village);
                                    }
                                });
                                return true;
                            } else {
                                player.sendMessage(Kingdoms.ERROR_PREFIX + "Only lords can disband villages, and you " +
                                        "are not a lord");
                                return true;
                            }
                        } else {
                            player.sendMessage(Kingdoms.ERROR_PREFIX + "You are not part of a village, what are you " +
                                    "trying to disband?");
                            return true;
                        }
                    } else {
                        player.sendMessage(Kingdoms.ERROR_PREFIX + "You don't have permission to perform this command.");
                        return true;
                    }
                } else {
                    commandSender.sendMessage(Kingdoms.ERROR_PREFIX + "Only a player can disband their village. Try " +
                            "/k disband (name)");
                    return true;
                }

            } else {
                if (commandSender.hasPermission("kingdoms.king.delete")) {
                    // TODO name matching

                } else {
                    commandSender.sendMessage(Kingdoms.ERROR_PREFIX + "You don't have permission to perform this " +
                            "command, perhaps you meant /k disband");
                    return true;
                }
            }
        }
        commandSender.sendMessage(Kingdoms.ERROR_PREFIX + "Command not recognized, try \"/k help\" for a list " +
                "of commands.");
        return true;
    }
}
