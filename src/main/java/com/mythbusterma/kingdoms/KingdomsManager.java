package com.mythbusterma.kingdoms;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class KingdomsManager {
    
    private final Kingdoms parent;
    
    private Map<Player, KingdomPlayer> players = new WeakHashMap<>();
    private Map<Integer, Village> villages = new ConcurrentHashMap<>();
    
    private Map<ChunkLocation, VillageBlock> map = new HashMap<>();

    public KingdomsManager(Kingdoms parent) {
        this.parent = parent;
    }

    /**
     * Returns a village for this location, if any, may be from another world
     * @param location
     * @return
     */
    public Village getVillage(ChunkLocation location) {
        return (map.get(location) == null) ? null : map.get(location).getVillage();
    }
    
    public KingdomPlayer getPlayer(Player ply) {
        return players.get(ply);
    }
    
    public void logout(Player ply) {
        players.remove(ply);
    }
    
    public Village getVillage(Location location) {
        return getVillage(new ChunkLocation(location.getBlockX() / 16, location.getBlockZ() / 16,
                location.getWorld().getUID()));
    }
    
    public Village getVillage(int id) {
        return villages.get(id);
    }
    
    /**
     * Adds a village into memory 
     */
    public void loadVillage(Village village) {
        villages.put(village.getId(), village);
    }

    /**
     * Adds a villageblock into memory  
     */
    public void loadVillageBlock(VillageBlock block) {
        map.put(block.getLocation(), block);
    }
    
    public void addVillageBlock (VillageBlock block) {
        map.put(block.getLocation(), block);
    }

    public void addPlayer(KingdomPlayer kingdomPlayer) {
        players.put(kingdomPlayer.getPlayer(), kingdomPlayer);
    }
    
    public void addVillage (Village village) {
        villages.put(village.getId(), village);
    }
    
    public Collection<Village> getVillages() {
        return villages.values();
    }

    /**
     * Tries to match a village based on a partial name or id
     *
     * <p>Since village names cannot begin with a number, it first attempts to get the village based on
     * id and will return a singleton collection containing that village if it matches</p>
     * @param partial part name or id of village to find
     * @return null if a number is not matched, or a collection (that may be empty) of villages that match
     */
    public Collection<Village> matchVillage(String partial) {
        try {
            final int i = Integer.parseInt(partial);
            if (villages.get(i) != null) {
                return Collections.singleton(villages.get(i));
            }
            else {
                return null;
            }
        } catch (NumberFormatException ex) {
            // deliberately left blank
        }
        ArrayList<Village> retVal = new ArrayList<>();
        for (Village village : villages.values()) {
            if (village.getName().toLowerCase().startsWith(partial.toLowerCase())) {
                retVal.add(village);
            }
        }
        return retVal;
    }

    @Nullable
    public Village getVillage(String name) {
        for (Map.Entry<Integer,Village> entry: villages.entrySet()) {
            if (entry.getValue().getName().equalsIgnoreCase(name)) {
                return entry.getValue();
            }
        }
        return null;
    }

    /**
     * Deletes a village and all of it's blocks from the data set, also removes it from any players that are online 
     * @param village
     */
    public void deleteVillage (Village village) {
        removeBlocks(village);
        clearPlayers(village);
        villages.remove(village.getId());
    }

    private void clearPlayers(Village village) {
        for (Map.Entry<Player, KingdomPlayer> playerKingdomPlayerEntry : players.entrySet()) {
            final KingdomPlayer player = playerKingdomPlayerEntry.getValue();
            if (player.getVillage() == null) {
                continue;
            }
            if (player.getVillage().equals(village)) {
                player.setVillage(null);
                player.setLord(false);
            }
        }
        
    }
    
    private void removeBlocks(Village village) {
        Iterator<Map.Entry<ChunkLocation, VillageBlock>> iter = map.entrySet().iterator();
        while(iter.hasNext()) {
            if (iter.next().getValue().getVillage().equals(village)) {
                iter.remove();
            }
        }
    }
}
