package com.mythbusterma.kingdoms.commands.kingdom;

import com.mythbusterma.kingdoms.*;
import com.mythbusterma.kingdoms.commands.CommandResult;
import com.mythbusterma.kingdoms.commands.PlayerCommand;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

/**
 * Used by players to create a new village
 */
public class CreateCommand implements PlayerCommand {

    private final KingdomCommandManager parent;

    private static final Set<String> RESTRICTED_NAMES;

    static {
        RESTRICTED_NAMES = new HashSet<>();
        RESTRICTED_NAMES.add("here");
    }

    public CreateCommand(KingdomCommandManager parent) {
        this.parent = parent;
    }

    @Override
    public CommandResult issue(Player player, String[] args) {
        if (parent.getParent().getKingdomsManager().getPlayer((Player) player).getVillage() == null) {
            if (parent.getParent().getKingdomsManager().getVillage(player.getLocation()) == null) {
                String name = "";
                for (int i = 1; i < args.length; i++) {
                    name += args[i];
                }
                if (parent.getParent().getKingdomsManager().getVillage(name) == null) {
                    // check to make sure it is a valid name and does not contain punctuation, while permitting
                    //  east Asian, Cyrillic, etc. characters
                    if (!name.matches("[!-@\\[-`](.*)")) {
                        if (!RESTRICTED_NAMES.contains(name.toLowerCase())) {
                            if (parent.getParent().getConfiguration()
                                    .isEconomy()) {
                                if (parent.getParent().getEconomy()
                                        .withdraw((OfflinePlayer) player,
                                                parent.getParent()
                                                        .getConfiguration()
                                                        .getVillageCost()) == 0) {
                                    player.sendMessage(Kingdoms.PREFIX + "Successfully created village " + name);
                                    Village village = new Village(name,
                                            player.getLocation().getWorld());
                                    village.setBalance(parent.getParent()
                                            .getConfiguration()
                                            .getDefaultBalance());
                                    village.incrementChunks();
                                    VillageBlock newBlock = new VillageBlock(
                                            new ChunkLocation(player.getLocation()),
                                            new PlotPermissions(), village);
                                    parent.getParent().getKingdomsManager()
                                            .addVillageBlock(newBlock);
                                    parent.getParent().getMySqlConnector().addVillage(
                                            parent.getParent().getUuidHolder()
                                                    .getUuid(player.getName()),
                                            village, newBlock);
                                    parent.getParent().getKingdomsManager()
                                            .getPlayer(player)
                                            .setVillage(village);
                                    if (parent.getParent().getConfiguration()
                                            .getUpkeep() != 0 && parent
                                            .getParent().getEconomy() != null) {
                                        player.sendMessage(Kingdoms.PREFIX + "Remember that villages require " +
                                                        "upkeep, and you must put money in the village coffers to keep the " +
                                                        "village from disappearing.");
                                    }
                                } else {
                                    player.sendMessage(Kingdoms.ERROR_PREFIX + "You don't have enough " +
                                                    "money to start a village, a village costs " + parent
                                                    .getParent()
                                                    .getConfiguration()
                                                    .getVillageCost());
                                }
                            } else {
                                // TODO make this work if economy is disabled
                            }
                        }
                        else {
                            player.sendMessage(Kingdoms.ERROR_PREFIX + "That " +
                                    "is a restricted village name, you may " +
                                    "not use it.");
                        }
                    } else {
                        player.sendMessage(Kingdoms.ERROR_PREFIX + "That is not a valid name, please try another");
                    }
                } else {
                    player.sendMessage(Kingdoms.ERROR_PREFIX + "A village of that name already exists.");
                }
            } else {
                player.sendMessage(Kingdoms.ERROR_PREFIX + "A village already exists in this location! Don't try " +
                        "to take over someone's village!");
            }
        } else {
            player.sendMessage(Kingdoms.ERROR_PREFIX + "You are already part of a village, leave " +
                    "your current village before attempting to create a new one.");
        }
        return CommandResult.SUCCESS;
    }
}
