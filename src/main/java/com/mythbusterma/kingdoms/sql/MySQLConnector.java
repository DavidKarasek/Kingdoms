package com.mythbusterma.kingdoms.sql;

import com.avaje.ebean.validation.NotNull;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mythbusterma.kingdoms.*;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;


public class MySQLConnector {

    private static final String PLAYERS_TABLE_SUFFIX = "players";
    private static final String VILLAGE_TABLE_SUFFIX = "village";
    private static final String VILLAGEBLOCK_TABLE_SUFFIX = "townblocks";
    private static final String FRIENDS_TABLE_SUFFIX = "friends";
    private static final String METADATA_TABLE_SUFFIX = "metadata";
    private static final String ENGINE = "ENGINE=INNODB";

    public static final int MAX_IDLE_TIME = 1000;
    public static final String PREFERRED_TEST_QUERY = "SELECT 1";

    private final Kingdoms parent;
    private final ComboPooledDataSource dataSource;
    private final String PLAYERS_TABLE;
    private final String VILLAGE_TABLE;
    private final String VILLAGEBLOCK_TABLE;
    private final String FRIENDS_TABLE;
    private final String METADATA_TABLE;

    public MySQLConnector(Kingdoms parent) {
        this.parent = parent;

        String prefix = parent.getConfiguration().getSqlPrefix();

        if (!prefix.equals("")) {
            prefix += "_";
        }

        PLAYERS_TABLE = prefix + PLAYERS_TABLE_SUFFIX;
        VILLAGE_TABLE = prefix + VILLAGE_TABLE_SUFFIX;
        VILLAGEBLOCK_TABLE = prefix + VILLAGEBLOCK_TABLE_SUFFIX;
        FRIENDS_TABLE = prefix + FRIENDS_TABLE_SUFFIX;
        METADATA_TABLE = prefix + METADATA_TABLE_SUFFIX;

        // disable the frankly annoying console output of C3P0
        Properties p = new Properties(System.getProperties());
        p.put("com.mchange.v2.log.MLog", "com.mchange.v2.log.FallbackMLog");
        p.put("com.mchange.v2.log.FallbackMLog.DEFAULT_CUTOFF_LEVEL", "SEVERE");
        System.setProperties(p);

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            parent.getLogger().log(Level.SEVERE, "No MySQL driver found, quitting.");
            throw new NoClassDefFoundError();
        }

        dataSource = new ComboPooledDataSource();

        dataSource.setMaxIdleTime(MAX_IDLE_TIME);
        dataSource.setTestConnectionOnCheckin(true);
        dataSource.setTestConnectionOnCheckout(true);

        dataSource.setPreferredTestQuery(PREFERRED_TEST_QUERY);

        try {
            dataSource.setDriverClass("com.mysql.jdbc.Driver");
            dataSource.setJdbcUrl("jdbc:mysql://" + parent.getConfiguration().getSqlHost() + ":"
                    + parent.getConfiguration().getSqlPort() + "/" + parent.getConfiguration().getSqlDatabase());
            dataSource.setUser(parent.getConfiguration().getSqlUser());
            dataSource.setPassword(parent.getConfiguration().getSqlPass());
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }

        try {
            dataSource.getConnection().createStatement().execute("SELECT 1");
        } catch (SQLException e) {
            generateSqlError(e, "Failed to establish connection to SQL server, disabling plugin.");
            Bukkit.getPluginManager().disablePlugin(parent);
        }

        setupTables();
    }

    private void generateSqlError(SQLException ex) {
        generateSqlError(ex, null);
    }

    private void generateSqlError(SQLException ex, @Nullable String additionalInfo) {
        String error = "==== ERROR IN SQL ====\n";
        error += "SQL MESSAGE: " + ex.getMessage() + "\n";
        error += "SQL STATE: " + ex.getSQLState() + "\n";
        error += "SQL VENDOR CODE: " + ex.getErrorCode() + "\n";
        if (additionalInfo != null && !additionalInfo.equals("")) {
            error += "ADDITIONAL INFORMATION: " + additionalInfo + "\n";
        }
        error += "STACK TRACE: \n";
        parent.getLogger().log(Level.SEVERE, error, ex);
        parent.getLogger().log(Level.SEVERE, "==== END SQL ERROR ====");
    }

    public void setupTables() {

        PreparedStatement ps;
        Connection conn;

        try {
            conn = dataSource.getConnection();

            ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS " + VILLAGE_TABLE + " (" +
                    "village_id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "village_name varchar(30) UNIQUE NOT NULL, " +
                    "balance FLOAT(14,2) NOT NULL DEFAULT 0, " +
                    "mob_spawn BOOL NOT NULL DEFAULT 0, " +
                    "fire_spread BOOL NOT NULL DEFAULT 0, " +
                    "pvp BOOL NOT NULL DEFAULT 0," +
                    "num_chunks BOOL NOT NULL DEFAULT 0," +
                    "lava_flow BOOL NOT NULL DEFAULT 0," +
                    "village_open BOOL NOT NULL DEFAULT 0," +
                    "permissions INT NOT NULL" +
                    ") " + ENGINE);

            examineSql(ps.toString());
            ps.executeUpdate();

            ps = conn.prepareStatement("INSERT IGNORE INTO " + VILLAGE_TABLE + " (village_id, village_name, balance) " +
                    "VALUES (-1, '~~defaultville~~847392', -100)");
            examineSql(ps.toString());
            ps.executeUpdate();

            ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS " + PLAYERS_TABLE + " (" +
                    "uuid char(36) PRIMARY KEY," +
                    "name varchar(16) NOT NULL," +
                    "village INT DEFAULT -1," +
                    "lord BOOL DEFAULT FALSE NOT NULL," +
                    "FOREIGN KEY (village) REFERENCES " + VILLAGE_TABLE + "(village_id) ON DELETE SET NULL" +
                    ") " + ENGINE);
            examineSql(ps.toString());
            ps.executeUpdate();

            ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS " + VILLAGEBLOCK_TABLE + " (" +
                    "village_id INT NOT NULL, " +
                    "x INT NOT NULL, " +
                    "z INT NOT NULL, " +
                    "world_id char(36)," +
                    "price FLOAT(10,2) DEFAULT -1, " +
                    "block_owner char(36) DEFAULT NULL, " +
                    "FOREIGN KEY (block_owner) REFERENCES " + PLAYERS_TABLE + "(uuid) ON DELETE SET NULL, " +
                    "FOREIGN KEY (village_id)" +
                    "REFERENCES " + VILLAGE_TABLE + "(village_id) ON DELETE CASCADE, " +
                    "PRIMARY KEY (x, z)) "
                    + ENGINE);
            examineSql(ps.toString());
            ps.executeUpdate();

            // num chunks triggers
            try {
                ps = conn.prepareStatement(
                        "CREATE TRIGGER chunk_updater AFTER INSERT ON " + VILLAGEBLOCK_TABLE + " FOR EACH ROW " +
                                "BEGIN " +
                                "UPDATE " + VILLAGE_TABLE + " SET num_chunks = num_chunks + 1 WHERE village_id = NEW.village_id;" +
                                "END;");
                examineSql(ps.toString());
                ps.executeUpdate();
            } catch (SQLException ex) {
                if (ex.getErrorCode() != 1235) {
                    throw ex;
                }
                // otherwise, swallow exception, it was expected
            }

            try {
                ps = conn.prepareStatement(
                        "CREATE TRIGGER chunk_updater_delete AFTER DELETE ON " + VILLAGEBLOCK_TABLE + " FOR EACH ROW " +
                                "BEGIN " +
                                "UPDATE " + VILLAGE_TABLE + " SET num_chunks = num_chunks -1 WHERE village_id = OLD.village_id;" +
                                "END;");
                examineSql(ps.toString());
                ps.executeUpdate();
            } catch (SQLException ex) {
                if (ex.getErrorCode() != 1235) {
                    throw ex;
                }
                // otherwise, swallow exception, it was expected
            }

            ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS " + FRIENDS_TABLE + " (" +
                    "player_id char(36) NOT NULL," +
                    "friend_id char(36) NOT NULL," +
                    "FOREIGN KEY (player_id) REFERENCES " + PLAYERS_TABLE + "(uuid) ON DELETE CASCADE, " +
                    "FOREIGN KEY (friend_id) REFERENCES " + PLAYERS_TABLE + "(uuid) ON DELETE CASCADE," +
                    "CONSTRAINT check_dupe CHECK (player_id <> friend_id)" +
                    ") " + ENGINE);
            examineSql(ps.toString());
            ps.executeUpdate();

            ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS " + METADATA_TABLE + " (" +
                    "mKey varchar(20) UNIQUE," +
                    "mValue varchar(20) NOT NULL)");

            ps.executeUpdate();

            metadataDefaults(conn);
            loadTowns(conn);

        } catch (SQLException e) {
            generateSqlError(e);
        }
    }

    private void loadTowns(Connection conn) throws SQLException {
        ResultSet rs = conn.prepareStatement("SELECT * FROM " + VILLAGE_TABLE).executeQuery();
        while (rs.next()) {
            //VillagePermissions permissions = new VillagePermissions();
            // TODO load village permissions

            if (rs.getInt(1) == -1) {
                continue;
            }

            Village temp = new Village(rs.getInt(1), VillagePermissions.fromValue(rs.getInt("permissions")));
            temp.setName(rs.getString(2));
            temp.setBalance(rs.getDouble(3));
            temp.setMobSpawn(rs.getBoolean(4));
            temp.setFireSpread(rs.getBoolean(5));
            temp.setPvpAllowed(rs.getBoolean(6));
            temp.setNumChunks(rs.getInt(7));
            temp.setLavaFlow(rs.getBoolean(8));
            temp.setOpen(rs.getBoolean(9));
            
            parent.getKingdomsManager().loadVillage(temp);
        }

        rs = conn.prepareStatement("SELECT * FROM " + VILLAGEBLOCK_TABLE).executeQuery();
        while (rs.next()) {
            PlotPermissions permissions = new PlotPermissions();

            // TODO load plotpermissions
            VillageBlock temp = new VillageBlock(new ChunkLocation(rs.getInt(2), rs.getInt(3),
                    UUID.fromString(rs.getString(4))), permissions,
                    parent.getKingdomsManager().getVillage(rs.getInt(1)));

            temp.setPrice(rs.getFloat(5));
            if (rs.getString(6) != null) {
                temp.setOwner(UUID.fromString(rs.getString(6)));
            }

            parent.getKingdomsManager().loadVillageBlock(temp);
        }
    }

    private void metadataDefaults(Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("INSERT IGNORE INTO " + METADATA_TABLE
                + " VALUES ('collection', ?)");
        ps.setLong(1, System.currentTimeMillis());
        ps.executeUpdate();
    }

    private void examineSql(String sql) {
        // parent.getLogger().log(Level.INFO, "Executed: " + sql);

    }

    public void disconnect() {
        dataSource.close();
    }

    /*

     */
    @NotNull
    public KingdomPlayer loadPlayer(UUID id, String name) {
        KingdomPlayer retVal = null;
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM " + PLAYERS_TABLE + " WHERE uuid = ?");
            ps.setString(1, id.toString());
            ResultSet rs = ps.executeQuery();
            boolean runOnce = false;

            while (rs.next()) {
                if (runOnce) {
                    sqlError("1001");
                    throw new SQLException("Found more than one record for " + id);
                }
                runOnce = true;
                retVal = new KingdomPlayer(id);
                int village = rs.getInt("village");
                if (village != -1) {
                    Village villageFound = parent.getKingdomsManager().getVillage(village);
                    if (villageFound != null) {
                        retVal.setVillage(villageFound);
                        if (rs.getBoolean("lord")) {
                            retVal.setLord(true);
                        }
                    } else {
                        sqlError("1002");
                        throw new SQLException("Didn't find a town for player " + id + " default to no town");
                    }
                }
            }
            if (!runOnce) {
                ps.close();
                ps = conn.prepareStatement("INSERT INTO " + PLAYERS_TABLE + " (uuid, name) VALUES (?,?)");
                ps.setString(1, id.toString());
                ps.setString(2, name);
                ps.execute();
                retVal = new KingdomPlayer(id);
                retVal.setNewPlayer();
            }

        } catch (SQLException ex) {
            generateSqlError(ex, "Failed to load player information for " + id + " ");
        }
        return retVal;
    }

    public void sqlError(String errorNo) {
        parent.getLogger().log(Level.SEVERE, "Detected severe SQL error, contact developer. Code: SQL" + errorNo);
    }

    /**
     * Called when a village is created via command, runs asynchrnously
     *
     * @param uniqueId
     * @param village
     * @param newBlock
     */
    public void addVillage(final UUID uniqueId, final Village village, final VillageBlock newBlock) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    Connection conn = dataSource.getConnection();
                    conn.setAutoCommit(false);

                    PreparedStatement ps = conn.prepareStatement("INSERT INTO " + VILLAGE_TABLE +
                            " (village_name, balance, permissions) VALUES " +
                            "(?,?, ?)");
                    ps.setString(1, village.getName());
                    ps.setDouble(2, village.getBalance());
                    ps.setInt(3, village.getPermissions().getValue());
                    ps.execute();
                    ResultSet rs = conn.createStatement().executeQuery("SELECT LAST_INSERT_ID()");
                    rs.next();
                    int id = rs.getInt(1);
                    village.setId(id);
                    ps.close();

                    ps = conn.prepareStatement("INSERT INTO " + VILLAGEBLOCK_TABLE + " " +
                            " (village_id, x, z,world_id) VALUES (?,?,?, ?)");
                    ps.setInt(1, id);
                    ps.setInt(2, newBlock.getLocation().x);
                    ps.setInt(3, newBlock.getLocation().z);
                    ps.setString(4, newBlock.getLocation().world.toString());
                    ps.execute();
                    ps.close();

                    ps = conn.prepareStatement("UPDATE " + PLAYERS_TABLE +
                            " SET lord = TRUE, village = ? WHERE uuid = ?");
                    ps.setInt(1, id);
                    ps.setString(2, uniqueId.toString());
                    ps.execute();

                    conn.commit();

                    parent.getKingdomsManager().addVillage(village);
                } catch (SQLException e) {
                    generateSqlError(e);
                }
            }
        }.runTaskAsynchronously(parent);
    }

    public void addBlock(final VillageBlock block) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    Connection conn = dataSource.getConnection();
                    PreparedStatement ps = conn.prepareStatement("INSERT INTO " + VILLAGE_TABLE + " (village_id, x, z) " +
                            "VALUES (?,?,?)");
                    ps.setInt(1, block.getVillage().getId());
                    ps.setInt(2, block.getLocation().x);
                    ps.setInt(3, block.getLocation().z);
                    ps.execute();

                } catch (SQLException e) {
                    generateSqlError(e);
                }

            }
        }.runTaskAsynchronously(parent);
    }

    public void updateBalance(final int id, final double balance) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    Connection conn = dataSource.getConnection();

                    PreparedStatement ps = conn.prepareStatement("UPDATE " + VILLAGE_TABLE + " SET balance = ? WHERE " +
                            "village_id = ?");
                    ps.setDouble(1, balance);
                    ps.setInt(2, id);
                    ps.execute();
                    ps.close();
                } catch (SQLException e) {
                    generateSqlError(e);
                }
            }
        }.runTaskAsynchronously(parent);
    }

    public void deleteVillage(final int id) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    Connection conn = dataSource.getConnection();
                    conn.setAutoCommit(false);
                    PreparedStatement ps = conn.prepareStatement("UPDATE " + PLAYERS_TABLE + " SET village = -1 WHERE village = ?");
                    ps.setInt(1, id);
                    ps.execute();
                    ps.close();
                    ps = conn.prepareStatement("DELETE FROM " + VILLAGE_TABLE + " WHERE village_id = ?");
                    ps.setInt(1, id);
                    ps.execute();
                    ps.close();
                    conn.commit();
                    parent.getLogger().log(Level.INFO, "Town deleted: " + id);
                } catch (SQLException e) {
                    generateSqlError(e);
                }
            }
        }.runTaskAsynchronously(parent);
    }
}
