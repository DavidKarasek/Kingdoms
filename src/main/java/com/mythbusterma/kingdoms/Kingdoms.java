package com.mythbusterma.kingdoms;

import com.mythbusterma.kingdoms.commands.king.KingCommandHandler;
import com.mythbusterma.kingdoms.commands.kingdom.KingdomCommandManager;
import com.mythbusterma.kingdoms.commands.lord.LordCommand;
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
    private final InviteManager inviteManager = new InviteManager(this);
    private PlayerMovementWatcher playerMovementWatcher;
    private Configuration configuration;
    private MySQLConnector mySQLConnector;
    private EconomyManager economyManager = null;
    private KingdomCommandManager kingdomCommandManager;
    private LordCommand lordCommandHandler;
    private KingCommandHandler kingCommandHandler;

    public Kingdoms() {
        instance = this;
        kingdomsManager = new KingdomsManager(this);
    }

    public static Kingdoms getInstance() {
        return instance;
    }

    public KingdomCommandManager getKingdomCommandManager() {
        return kingdomCommandManager;
    }

    public void setKingdomCommandManager(
            KingdomCommandManager kingdomCommandManager) {
        this.kingdomCommandManager = kingdomCommandManager;
    }

    public LordCommand getLordCommandHandler() {
        return lordCommandHandler;
    }

    public void setLordCommandHandler(LordCommand lordCommandHandler) {
        this.lordCommandHandler = lordCommandHandler;
    }

    public KingCommandHandler getKingCommandHandler() {
        return kingCommandHandler;
    }

    public void setKingCommandHandler(KingCommandHandler kingCommandHandler) {
        this.kingCommandHandler = kingCommandHandler;
    }

    public UuidHolder getUuidHolder() {
        return holder;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        configuration = new Configuration(this);
        playerMovementWatcher = new PlayerMovementWatcher(this);

        mySQLConnector = new MySQLConnector(this);
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new PlayerListener(this), this);
        manager.registerEvents(new LoginListener(this), this);
        manager.registerEvents(new LogoutListener(this), this);
        manager.registerEvents(new BlockListener(this), this);
        manager.registerEvents(new EntityListener(this), this);
        manager.registerEvents(playerMovementWatcher, this);

        kingCommandHandler = new KingCommandHandler(this);
        lordCommandHandler = new LordCommand(this);
        kingdomCommandManager = new KingdomCommandManager(this);

        getCommand("king").setExecutor(kingCommandHandler);
        getCommand("kingdoms").setExecutor(kingdomCommandManager);
        getCommand("lord").setExecutor(lordCommandHandler);

        if (getServer().getPluginManager().getPlugin("Vault") != null) {
            this.economyManager = new EconomyManager(this);
        } else {
            Bukkit.getLogger().log(Level.WARNING,
                    "No Vault connection found for Kingdoms. Economy support " +
                            "will be disabled, claims will be free.");
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

    public InviteManager getInviteManager() {
        return inviteManager;
    }

    public PlayerMovementWatcher getPlayerMovementWatcher() {
        return playerMovementWatcher;
    }
}
