package com.mythbusterma.kingdoms;

import com.avaje.ebean.validation.NotNull;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.UUID;

/**
 * Methods in this class rely on the WeakReference to the Player resolving properly, this may be potentially dangerous
 * but is likely not an issue because this object SHOULD become unreachable when the Player is cleaned up
 */
public class KingdomPlayer {
    @NotNull
    private WeakReference<Player> player;
    private Village village;
    
    private boolean lord;
    
    private final UUID id;

    public KingdomPlayer(UUID id) {
        this.id = id;
    }

    public UUID getUniqueId() {
        return id;
    }

    @Nullable
    public Village getVillage() {
        return village;
    }

    public void setVillage(@Nullable Village village) {
        this.village = village;
    }

    public boolean isLord() {
        return lord;
    }

    public void setLord(boolean lord) {
        this.lord = lord;
    }
}
