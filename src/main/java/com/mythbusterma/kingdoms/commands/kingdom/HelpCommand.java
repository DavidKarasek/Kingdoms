package com.mythbusterma.kingdoms.commands.kingdom;

import com.mythbusterma.kingdoms.KingdomPlayer;
import com.mythbusterma.kingdoms.Kingdoms;
import com.mythbusterma.kingdoms.KingdomsManager;
import com.mythbusterma.kingdoms.Village;
import com.mythbusterma.kingdoms.commands.CommandResult;
import com.mythbusterma.kingdoms.commands.KingdomCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import static com.mythbusterma.kingdoms.utils.ChatUtil.cf;

import javax.annotation.Nonnull;

public class HelpCommand implements KingdomCommand<CommandSender> {
    private final KingdomCommandManager parent;

    public HelpCommand(KingdomCommandManager parent) {
        this.parent = parent;
    }

    public void playerHelp(Player player) {

        player.sendMessage(Kingdoms.PREFIX + "==== Help ====");
        final KingdomsManager kingdomsManager = parent.getParent()
                .getKingdomsManager();
        Village plyVillage = kingdomsManager.getPlayer(player).getVillage();
        Village locVillage = kingdomsManager.getVillage(player.getLocation());

        if (plyVillage == null) {
            if (locVillage == null) {
                if (player.hasPermission("kingdoms.player.create")) {
                    player.sendMessage(cf("/k", "new <name>", "", "Create a new village with the " +
                            "current chunk being the start of the claim", "/kingdoms"));
                }
                if (player.hasPermission("kingdoms.player.join")) {
                    player.sendMessage(cf("/k", "join <name>", "", "Request to join an existing village " +
                            "with a given name", "/kingdoms"));
                }
            } else {
                if (player.hasPermission("kingdoms.player.join")) {
                    player.sendMessage(cf("/k", "join here", "", "Request to join " + ChatColor.ITALIC +
                            locVillage.getName() + ChatColor.RESET, "/kingdoms"));
                }
            }
        } else {
            KingdomPlayer playerInfo = kingdomsManager
                    .getPlayer(player);
            if (locVillage != null) {
                if (player.hasPermission("kingdoms.player.info")) {
                    player.sendMessage(cf("/k", "info", "", "Request " +
                            "information about the village underfoot, " +
                            "currently " + ChatColor.ITALIC +
                            locVillage.getName(), "/kingdoms"));
                }
            }
        }

        if (player.hasPermission("kingdoms.player.list")) {
            player.sendMessage(cf("/k", "list", "", "List all villages on the server.", "/kingdoms"));
        }
    }

    private void consoleHelp(CommandSender sender) {
        // TODO: Help the poor console
    }

    @Override
    public CommandResult issue(CommandSender issuer, String[] args) {
        if (issuer instanceof Player) {
            playerHelp((Player) issuer);
        }
        else {
            consoleHelp(issuer);
        }
        return CommandResult.SUCCESS;
    }
}
