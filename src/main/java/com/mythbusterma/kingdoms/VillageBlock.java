package com.mythbusterma.kingdoms;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class VillageBlock {
    private final ChunkLocation location;
    @Nullable
    private UUID owner;
    private float price = -1;
    private final PlotPermissions permissions;
    
    @Nonnull
    private Village village;

    public VillageBlock(ChunkLocation location, PlotPermissions permissions, Village village) {
        this.location = location;
        this.permissions = permissions;
        this.village = village;
    }

    @Nonnull
    public Village getVillage() {
        return village;
    }

    public ChunkLocation getLocation() {
        return location;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setOwner(@Nullable UUID owner) {
        this.owner = owner;
    }
}
