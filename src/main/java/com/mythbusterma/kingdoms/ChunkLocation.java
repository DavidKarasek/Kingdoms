package com.mythbusterma.kingdoms;

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

    @Override
    public int hashCode() {
        return (x & POSITIVE_MASK) << 16 + (z & POSITIVE_MASK) + world.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ChunkLocation) {
            ChunkLocation loc = (ChunkLocation) obj;
            if(loc.x == x && loc.z == z) {
                if(loc.world == world) {
                    return true;
                }
            }
        }
        return false;
    }
}
