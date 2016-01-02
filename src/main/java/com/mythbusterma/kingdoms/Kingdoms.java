package com.mythbusterma.kingdoms;

import com.mythbusterma.kingdoms.commands.HelpCommand;
import com.mythbusterma.kingdoms.commands.KingCommandHandler;
import com.mythbusterma.kingdoms.commands.KingdomCommandManager;
import com.mythbusterma.kingdoms.listener.*;
import com.mythbusterma.kingdoms.sql.MySQLConnector;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class Kingdoms extends JavaPlugin {

    public static final String PREFIX = ChatColor.AQUA + "[Kingdoms] " + ChatColor.RESET;
    public static final String ERROR_PREFIX = PREFIX + ChatColor.RED + "Error: ";
    private static Kingdoms instance;

    private final KingdomsManager kingdomsManager;
    private final UuidHolder holder = new UuidHolder();
    private Configuration configuration;
    private MySQLConnector mySQLConnector;
    private EconomyManager economyManager = null;

    public Kingdoms() {
        instance = this;
        kingdomsManager = new KingdomsManager(this);
    }

    public static Kingdoms getInstance() {
        return instance;
    }

    public UuidHolder getUuidHolder() {
        return holder;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        configuration = new Configuration(this);

        mySQLConnector = new MySQLConnector(this);
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new PlayerListener(this), this);
        manager.registerEvents(new LoginListener(this), this);
        manager.registerEvents(new LogoutListener(this), this);
        manager.registerEvents(new BlockListener(this), this);
        manager.registerEvents(new EntityListener(this), this);

        getCommand("king").setExecutor(new KingCommandHandler(this));
        getCommand("kingdoms").setExecutor(new KingdomCommandManager(this));

        if (getServer().getPluginManager().getPlugin("Vault") != null) {
            this.economyManager = new EconomyManager(this);
        } else {
            Bukkit.getLogger().log(Level.WARNING, "No Vault connection found for Kingdoms. Economy support will " +
                    "be disabled, claims will be free.");
        }
    }

    @Override
    public void onDisable() {

    }

    public KingdomsManager getKingdomsManager() {
        return kingdomsManager;
    }

    public void generateError(Exception ex) {
        generateError(ex, null);
    }

    public void generateError(Exception ex, String additionalInfo) {
        String error = "==== KINGDOMS ERROR ====\n";
        if (additionalInfo != null && !additionalInfo.equals("")) {
            error += "Additional information: " + additionalInfo + "\n";
        }
        getLogger().log(Level.SEVERE, error, ex);
    }

    public MySQLConnector getMySqlConnector() {
        return mySQLConnector;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public EconomyManager getEconomy() {
        return economyManager;
    }
}
