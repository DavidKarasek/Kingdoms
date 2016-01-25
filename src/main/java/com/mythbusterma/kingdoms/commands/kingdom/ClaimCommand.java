package com.mythbusterma.kingdoms.commands.kingdom;


import com.mythbusterma.kingdoms.*;
import com.mythbusterma.kingdoms.commands.CommandResult;
import com.mythbusterma.kingdoms.commands.PlayerCommand;
import org.bukkit.entity.Player;

/**
 * Allows players to claim more chunks for their village
 */
public class ClaimCommand implements PlayerCommand {
    private final KingdomCommandManager parent;

    public ClaimCommand(KingdomCommandManager parent) {
        this.parent = parent;
    }

    @Override
    public CommandResult issue(Player player, String[] args) {
        if (parent.getParent().getKingdomsManager().getVillage(player.getLocation()) == null) {
            if (parent.getParent().getKingdomsManager().getPlayer(player).isLord()) {
                if (parent.getParent().getKingdomsManager().getPlayer(player).getVillage() != null) {
                    ChunkLocation location = new ChunkLocation(player.getLocation());
                    boolean nearby = false;
                    Village village = parent.getParent().getKingdomsManager().getPlayer(player).getVillage();

                    if (parent.getParent().getKingdomsManager().getVillage(new ChunkLocation(location.x - 1, location.z, location.world)) != null &&
                            parent.getParent().getKingdomsManager().getVillage(new ChunkLocation(location.x - 1, location.z, location.world)).equals(village)) {
                        nearby = true;
                    } else if (parent.getParent().getKingdomsManager().getVillage(new ChunkLocation(location.x + 1, location.z, location.world)) != null &&
                            parent.getParent().getKingdomsManager().getVillage(new ChunkLocation(location.x + 1, location.z, location.world)).equals(village)) {
                        nearby = true;
                    } else if (parent.getParent().getKingdomsManager().getVillage(new ChunkLocation(location.x, location.z - 1, location.world)) != null &&
                            parent.getParent().getKingdomsManager().getVillage(new ChunkLocation(location.x, location.z - 1, location.world)).equals(village)) {
                        nearby = true;
                    } else if (parent.getParent().getKingdomsManager().getVillage(new ChunkLocation(location.x, location.z + 1, location.world)) != null &&
                            parent.getParent().getKingdomsManager().getVillage(new ChunkLocation(location.x, location.z + 1, location.world)).equals(village)) {
                        nearby = true;
                    }
                    if (nearby) {
                        if (parent.getParent().getConfiguration().isEconomy()) {
                            if (village.getBalance() - parent.getParent().getConfiguration().getClaimCost() >= 0) {
                                VillageBlock block = new VillageBlock(new ChunkLocation(player.getLocation()),
                                        new PlotPermissions(), village);
                                parent.getParent().getKingdomsManager().addVillageBlock(block);
                                parent.getParent().getMySqlConnector().addBlock(block);
                                village.setBalance(village.getBalance() - parent.getParent().getConfiguration().getClaimCost());
                                parent.getParent().getMySqlConnector().updateBalance(village.getId(), village.getBalance());
                                player.sendMessage(Kingdoms.PREFIX + "Land claimed.");
                                
                            } else {
                                player.sendMessage(Kingdoms.ERROR_PREFIX + "Your village doesn't have enough money " +
                                        "to claim this land, deposit more funds with /kingdoms deposit (/k d)");
                                
                            }
                        } else {
                            // TODO make this work without economy
                        }
                    } else {
                        player.sendMessage(Kingdoms.ERROR_PREFIX + "This chunk is not near your village, " +
                                "additional claimed chunks must be adjacent to the chunks you've already claimed");
                        
                    }
                } else {
                    player.sendMessage(Kingdoms.ERROR_PREFIX + "You must be a lord to claim land!");
                    

                }
            } else {
                player.sendMessage(Kingdoms.ERROR_PREFIX + "You do not belong to a village, you cannot claim land.");
                
            }
        } else {
            player.sendMessage(Kingdoms.ERROR_PREFIX + "This area is already claimed by: "
                    + parent.getParent().getKingdomsManager().getVillage(player.getLocation()).getName()
                    + ". You cannot claim here");
            
        }

        return CommandResult.SUCCESS;
    }
}
