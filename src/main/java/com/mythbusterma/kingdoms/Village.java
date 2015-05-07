package com.mythbusterma.kingdoms;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class Village {
    //private Set<UUID> lords;

    private volatile int id;
    private final VillagePermissions permissions;
    private volatile String name;
    private volatile Vector spawn;
    private volatile float spawnYaw;
    private volatile float spawnPitch;
    private volatile String world;
    private volatile double balance;
    private volatile int numChunks;
    private volatile boolean pvpAllowed = false;
    private volatile boolean fireSpread = false;
    private volatile boolean mobSpawn = false;
    private volatile boolean lavaFlow = false;
    private volatile boolean open = false;

    public Village(int id, VillagePermissions permissions) {
        this.id = id;
        this.permissions = permissions;
    }

    /**
     * For use in building a Village for the first time, id field not set 
     * @param name
     */
    public Village (String name, World world) {
        this.name = name;
        this.world = world.getName();
        this.permissions = new VillagePermissions();
    }

    /**
     * Should only be used by SQL classes
     * @param id
     */
    public void setId (int id) {
        this.id = id;
    }

    public boolean isPvpAllowed() {
        return pvpAllowed;
    }

    public void setPvpAllowed(boolean pvpAllowed) {
        this.pvpAllowed = pvpAllowed;
    }

    public boolean isFireSpread() {
        return fireSpread;
    }

    public void setFireSpread(boolean fireSpread) {
        this.fireSpread = fireSpread;
    }

    public boolean isMobSpawn() {
        return mobSpawn;
    }

    public void setMobSpawn(boolean mobSpawn) {
        this.mobSpawn = mobSpawn;
    }

    public Location getSpawn() {
        return new Location(Bukkit.getWorld(world), spawn.getX(), spawn.getY(), spawn.getZ(),
                spawnYaw, spawnPitch);
    }

    public void setSpawn(Location loc) {
        //this.world = loc.getWorld().getName();
        this.spawnPitch = loc.getPitch();
        this.spawnYaw = loc.getYaw();
        spawn = new Vector(loc.getX(), loc.getY(), loc.getZ());
    }

    /**
     * Schedules this village to update its SQL record at the next opportunity
     */
    public void update() {
        // TODO
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Village) {
            Village vill = (Village) obj;
            if (vill.getName().equals(this.name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public VillagePermissions getPermissions() {
        return permissions;
    }

    public int getId() {
        return id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getNumChunks() {
        return numChunks;
    }

    public void setNumChunks(int numChunks) {
        this.numChunks = numChunks;
    }

    public void incrementChunks() {
        numChunks++;
    }

    public void decrementChunks() {
        numChunks--;
    }

    public boolean isLavaFlow() {
        return lavaFlow;
    }

    public void setLavaFlow(boolean lavaFlow) {
        this.lavaFlow = lavaFlow;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
