package com.mythbusterma.kingdoms.commands;

import com.mythbusterma.kingdoms.KingdomPlayer;
import com.mythbusterma.kingdoms.Kingdoms;
import com.mythbusterma.kingdoms.Village;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class HelpCommand implements KingdomCommand<CommandSender> {
    private final KingdomCommandManager parent;

    public HelpCommand(KingdomCommandManager parent) {
        this.parent = parent;
    }

    /**
     * Easy function to format the output of commands to make it look nice
     */
    private static String cf(@Nonnull String command, @Nonnull String args, @Nonnull String optArgs,
                             @Nonnull String desc, @Nonnull String alias) {
        return ChatColor.DARK_AQUA + command + " " + ChatColor.GREEN + args + " " + ChatColor.GOLD + optArgs +
                ChatColor.RESET + " : " + desc + " (" + ChatColor.DARK_RED + alias + ChatColor.RESET + ")";
    }

    public void playerHelp(Player player) {

        player.sendMessage(Kingdoms.PREFIX + "==== Help ====");
        Village plyVillage = parent.getVillage(player);
        Village locVillage = parent.getParent().getKingdomsManager().getVillage(player.getLocation());

        if (plyVillage == null) {
            if (locVillage == null) {
                if (player.hasPermission("kingdoms.player.new")) {
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
            KingdomPlayer playerInfo = parent.getParent().getKingdomsManager().getPlayer(player);
            if (playerInfo.isLord()) {
                // display mayor commands
                if (player.hasPermission("kingdoms.player.new")) {
                    player.sendMessage(cf("/k", "disband/delete", "", "Permanently delete your village, cannot be undone",
                            "/kingdoms"));
                }
                if (player.hasPermission("kingdoms.player.invite")) {
                    player.sendMessage(cf("/k", "invite/add <player>", "<other players> ...", "Invite player(s) " +
                            "to join your village, they will have to accept your invitation", "/kingdoms"));
                }
            }
            if (locVillage != null) {
                if (player.hasPermission("kingdoms.player.info")) {
                    player.sendMessage(cf("/k", "info", "", "Request information about the village underfoot, " +
                            "currently " + ChatColor.ITALIC + locVillage.getName(), "/kingdoms"));
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
