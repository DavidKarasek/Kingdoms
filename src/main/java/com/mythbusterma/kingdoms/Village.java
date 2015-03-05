package com.mythbusterma.kingdoms;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class Village {
    //private Set<UUID> lords;

    private final int id;
    private final VillagePermissions permissions;
    private String name;
    private Vector spawn;
    private float spawnYaw;
    private float spawnPitch;
    private String world;
    private double balance;
    private int numChunks;
    private boolean pvpAllowed;
    private boolean fireSpread;
    private boolean mobSpawn;
    private boolean lavaFlow;

    public Village(int id, VillagePermissions permissions) {
        this.id = id;
        this.permissions = permissions;
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
        this.world = loc.getWorld().getName();
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
}
