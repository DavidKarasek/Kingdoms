package com.mythbusterma.kingdoms.commands.kingdom;

import com.mythbusterma.kingdoms.Kingdoms;
import com.mythbusterma.kingdoms.Village;
import com.mythbusterma.kingdoms.commands.CommandResult;
import com.mythbusterma.kingdoms.commands.KingdomCommand;
import com.mythbusterma.kingdoms.commands.kingdom.KingdomCommandManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ListCommand implements KingdomCommand<CommandSender> {
    private final KingdomCommandManager parent;

    public ListCommand(KingdomCommandManager parent) {
        this.parent = parent;
    }

    @Override
    public CommandResult issue(CommandSender issuer, String[] args) {
        if (!parent.getParent().getKingdomsManager().getVillages().isEmpty()) {

            StringBuilder builder = new StringBuilder();
            Village playerVillage = null;
            if (issuer instanceof Player) {
                 playerVillage = parent.getParent().getKingdomsManager().getPlayer((Player) issuer).getVillage();
                 issuer.sendMessage(Kingdoms.PREFIX + "Villages: (your village appears in green, " +
                        "villages open to the public in blue, others red)\n");
            }
            else {
                issuer.sendMessage(Kingdoms.PREFIX + "Villages: ");
            }
            for (Village village : parent.getParent().getKingdomsManager().getVillages()) {
                if (playerVillage != null) {
                    if (village.equals(playerVillage)) {
                        builder.append(ChatColor.GREEN);
                    }
                } else if (village.isOpen()) {
                    builder.append(ChatColor.BLUE);
                } else {
                    builder.append(ChatColor.DARK_RED);
                }
                builder.append(village.getName());
                builder.append(", ");
            }
            builder.deleteCharAt(builder.length() - 1);
            builder.deleteCharAt(builder.length() - 1);

            issuer.sendMessage(builder.toString());
        } else {
            issuer.sendMessage(Kingdoms.PREFIX + "There aren't any villages on this server.");
            if (issuer instanceof Player) {
                issuer.sendMessage(Kingdoms.PREFIX + "Perhaps you should create one? (/k new <name>)");
            }
        }

        return CommandResult.SUCCESS;
    }
}
