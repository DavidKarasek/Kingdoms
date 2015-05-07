package com.mythbusterma.kingdoms;

import org.bukkit.configuration.ConfigurationSection;

public class Configuration {
    private final boolean damageReflect;
    private final int upkeep;
    private final long interval;
    private final int villageCost;
    private final String sqlPass;
    private final String sqlHost;
    private final String sqlDatabase;
    private final String sqlPrefix;
    private final String sqlUser;
    private final int sqlPort;
    private final boolean economy;
    private final double defaultBalance;
    private final int claimCost;
    
    private final Kingdoms parent;

    public Configuration(Kingdoms parent) {
        ConfigurationSection config = parent.getConfig();
        damageReflect = config.getBoolean("towns-reflect-damage");
        upkeep = config.getInt("upkeep-cost");
        villageCost = config.getInt("village-cost");
        sqlDatabase = parent.getConfig().getString("sql.database-name");
        sqlUser = parent.getConfig().getString("sql.user");
        sqlPass = parent.getConfig().getString("sql.password");
        sqlHost = parent.getConfig().getString("sql.host");
        sqlPort = parent.getConfig().getInt("sql.port");
        sqlPrefix = parent.getConfig().getString("sql.table-prefix");
        economy = parent.getConfig().getBoolean("use-economy");
        defaultBalance = parent.getConfig().getDouble("default-balance");
        claimCost = parent.getConfig().getInt("claim-cost");
    
        this.parent = parent;
        // TODO make this work
        interval = 0;

    }

    public long getInterval() {
        return interval;
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
}
