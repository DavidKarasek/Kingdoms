package com.mythbusterma.kingdoms;

import com.mythbusterma.kingdoms.commands.HelpCommand;
import com.mythbusterma.kingdoms.commands.KingCommandHandler;
import com.mythbusterma.kingdoms.commands.KingdomCommand;
import com.mythbusterma.kingdoms.listener.*;
import com.mythbusterma.kingdoms.sql.MySQLConnector;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class Kingdoms extends JavaPlugin {

    public static final String PREFIX = ChatColor.AQUA + "[Kingdoms] " + ChatColor.RESET;
    private static Kingdoms instance;

    private final HelpCommand helpCommand;
    private final KingdomsManager kingdomsManager;
    private final Configuration configuration;
    private MySQLConnector mySQLConnector;
    private Economy economy;

    public Kingdoms() {
        instance = this;
        helpCommand = new HelpCommand(this);
        kingdomsManager = new KingdomsManager(this);
        configuration = new Configuration(this);
    }

    public static Kingdoms getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        mySQLConnector = new MySQLConnector(this);

        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new PlayerListener(this), this);
        manager.registerEvents(new LoginListener(this), this);
        manager.registerEvents(new LogoutListener(this), this);
        manager.registerEvents(new BlockListener(this), this);
        manager.registerEvents(new EntityListener(this), this);

        getCommand("king").setExecutor(new KingCommandHandler(this));
        getCommand("kingdoms").setExecutor(new KingdomCommand(this));

        if (!setupEconomy()) {
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

    public HelpCommand getHelpCommand() {
        return helpCommand;
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }
        return (economy != null);
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
