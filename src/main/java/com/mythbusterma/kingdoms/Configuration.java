package com.mythbusterma.kingdoms;

import org.bukkit.configuration.ConfigurationSection;

public class Configuration {
    
    private static final int DEFAULT_TELEPORT_DELAY = 5;
    private static final boolean DEFAULT_REFLECT_DAMAGE = false;
    private static final int DEFAULT_UPKEEP = 100;
    private static final int DEFAULT_VILLAGE_COST = 1000;
    private static final String DEFAULT_INTERVAL = "12h";
    private static final String DEFAULT_SOL_PREFIX = "";
    private static final String DEFAULT_DATABASE_NAME = "kingdoms";
    private static final String DEFAULT_SQL_USER = "kingdoms";
    private static final String DEFAULT_SQL_PASSWORD = "";
    private static final String DEFAULT_SQL_HOST = "localhost";
    private static final int DEFAULT_SQL_PORT = 3306;
    private static final boolean DEFAULT_USE_ECONOMY = true;
    private static final int DEFAULT_CLAIM_COST = 100;
    private static final int DEFAULT_VILLAGE_BALANCE = 100;

    private final int teleportDelay;
    private final boolean damageReflect;
    private final int upkeep;
    private final int villageCost;
    private final String interval;
    private final String sqlPass;
    private final String sqlHost;
    private final String sqlDatabase;
    private final String sqlPrefix;
    private final String sqlUser;
    private final int sqlPort;
    private final boolean economy;
    private final int defaultBalance;
    private final int claimCost;
    
    private final Kingdoms parent;

    public Configuration(Kingdoms parent) {
        ConfigurationSection config = parent.getConfig();
        teleportDelay = config.getInt("teleport-delay", DEFAULT_TELEPORT_DELAY);
        damageReflect = config.getBoolean("towns-reflect-damage", DEFAULT_REFLECT_DAMAGE);
        upkeep = config.getInt("upkeep-cost", DEFAULT_UPKEEP);
        villageCost = config.getInt("village-cost", DEFAULT_VILLAGE_COST);
        sqlDatabase = config.getString("sql.database-name", DEFAULT_DATABASE_NAME);
        sqlUser = config.getString("sql.user", DEFAULT_SQL_USER);
        sqlPass = config.getString("sql.password", DEFAULT_SQL_PASSWORD);
        sqlHost = config.getString("sql.host", DEFAULT_SQL_HOST);
        sqlPort = config.getInt("sql.port", DEFAULT_SQL_PORT);
        sqlPrefix = config.getString("sql.table-prefix", DEFAULT_SOL_PREFIX);
        economy = config.getBoolean("use-economy", DEFAULT_USE_ECONOMY);
        defaultBalance = config.getInt("default-village-balance",
                DEFAULT_VILLAGE_BALANCE);
        claimCost = config.getInt("claim-cost", DEFAULT_CLAIM_COST);
    
        this.parent = parent;
        // TODO make this work
        interval = config.getString("upkeep-interval", DEFAULT_INTERVAL);

    }

    public long getInterval() {
        // TODO implement this
        return -1;
    }

    public int getVillageCost() {
        return villageCost;
    }

    public String getSqlPass() {
        return sqlPass;
    }

    public String getSqlHost() {
        return sqlHost;
    }

    public String getSqlDatabase() {
        return sqlDatabase;
    }

    public String getSqlPrefix() {
        return sqlPrefix;
    }

    public String getSqlUser() {
        return sqlUser;
    }

    public int getSqlPort() {
        return sqlPort;
    }

    public boolean isDamageReflect() {
        return damageReflect;
    }

    public int getUpkeep() {
        return upkeep;
    }
    public boolean isEconomy () {
        return economy && parent.getEconomy() != null;
    }
    
    public double getDefaultBalance() {
        return defaultBalance;
    }
    
    public int getClaimCost () {
        return claimCost;
        
    }

    /**
     * The delay, in seconds, to delay teleportation by
     * @return
     */
    public int getTeleportDelay() {
        return teleportDelay;
    }
}
