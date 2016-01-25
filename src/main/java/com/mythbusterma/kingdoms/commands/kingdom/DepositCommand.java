package com.mythbusterma.kingdoms.commands.kingdom;

import com.mythbusterma.kingdoms.Kingdoms;
import com.mythbusterma.kingdoms.Village;
import com.mythbusterma.kingdoms.commands.CommandResult;
import com.mythbusterma.kingdoms.commands.PlayerCommand;
import org.bukkit.entity.Player;

public class DepositCommand implements PlayerCommand {

    private final KingdomCommandManager parent;

    public DepositCommand(KingdomCommandManager manager) {
        this.parent = manager;
    }

    @Override
    public CommandResult issue(Player player, String[] args) {
        Village village = parent.getParent().getKingdomsManager()
                .getPlayer(player).getVillage();
        if (village != null) {
            double amount = 0;

            try {
                amount = Double.parseDouble(args[1]);
            } catch (NumberFormatException ex) {
                player.sendMessage(Kingdoms.ERROR_PREFIX + args[1] + " is not a number, please enter a valid number.");
            }

            if (parent.getParent().getConfiguration().isEconomy()) {
                if (parent.getParent().getEconomy().withdraw(player, amount) == 0) {
                    player.sendMessage(Kingdoms.PREFIX + "You have deposited " + args[1] + " into your " +
                            "village's coffers.");
                    village.setBalance(village.getBalance() + amount);
                    parent.getParent().getMySqlConnector().updateBalance(village.getId(), village.getBalance());

                } else {
                    player.sendMessage(Kingdoms.ERROR_PREFIX + "You don't have enough money to make that deposit, " +
                            "try spending within your means.");
                }
            } else {
                player.sendMessage(Kingdoms.ERROR_PREFIX + "These villages do not require money, and as such, you " +
                        "cannot deposit money into the village coffers.");
            }
        } else {
            player.sendMessage(Kingdoms.ERROR_PREFIX + "You aren't part of a village, you cannot deposit money " +
                    "into nothing!");
        }
        return CommandResult.SUCCESS;
    }
}
