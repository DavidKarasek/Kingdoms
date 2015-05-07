package com.mythbusterma.kingdoms;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

public class EconomyManager {

    private final Kingdoms parent;
    Economy economy = null;

    public EconomyManager(Kingdoms parent) {
        this.parent = parent;
        setupEconomy();
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider =
                parent.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }
        return (economy != null);
    }

    public int withdraw(OfflinePlayer player, double amount) {
        EconomyResponse res = economy.withdrawPlayer(player, amount);
        if (res.transactionSuccess()) {
            return 0;
        }
        else {
            return -1;
        }
    }
}
