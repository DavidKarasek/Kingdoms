package com.mythbusterma.kingdoms;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
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
        return getVillage(new ChunkLocation(location.getBlockX() << 4, location.getBlockZ() << 4, 
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

    public void addPlayer(KingdomPlayer kingdomPlayer) {
    }
}
