package com.mythbusterma.kingdoms;

import org.bukkit.Location;

import java.util.UUID;

public class ChunkLocation {
    
    public static final int POSITIVE_MASK = 0x7FFFFFFF;
    
    public final int x;
    public final int z;
    public final UUID world;
    
    public ChunkLocation(int x, int z, UUID world) {
        this.x = x;
        this.z = z;
        this.world = world;
    }

    public ChunkLocation(Location location) {
        this.x = location.getBlockX() / 16;
        this.z = location.getBlockZ() / 16;
        this.world = location.getWorld().getUID();
    }

    @Override
    public int hashCode() {
        return (x & POSITIVE_MASK) << 16 + (z & POSITIVE_MASK) + world.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ChunkLocation) {
            ChunkLocation loc = (ChunkLocation) obj;
            if(loc.x == x && loc.z == z) {
                if(loc.world.equals(world)) {
                    return true;
                }
            }
        }
        return false;
    }
}
