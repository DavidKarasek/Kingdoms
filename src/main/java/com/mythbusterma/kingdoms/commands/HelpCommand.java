package com.mythbusterma.kingdoms.commands;

import com.mythbusterma.kingdoms.KingdomPlayer;
import com.mythbusterma.kingdoms.Kingdoms;
import com.mythbusterma.kingdoms.Village;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class HelpCommand {
    private final Kingdoms parent;

    public HelpCommand(Kingdoms parent) {
        this.parent = parent;
    }

    public void provideHelp(CommandSender sender) {

        sender.sendMessage(Kingdoms.PREFIX + "==== Help ====");

        if (sender instanceof ConsoleCommandSender) {
            consoleHelp(sender);
            return;
        }

        if (sender instanceof Player) {
            Player player = (Player) sender;
            Village plyVillage = parent.getKingdomsManager().getPlayer(player).getVillage();
            Village locVillage = parent.getKingdomsManager().getVillage(player.getLocation());

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
                KingdomPlayer playerInfo = parent.getKingdomsManager().getPlayer(player);
                if (playerInfo.isLord()) {
                    // display mayor commands
                    if (player.hasPermission("kingdoms.player.new")) {
                        player.sendMessage(cf("/k", "disband/delete", "", "Permanently delete your town, cannot be undone",
                                "/kingdoms"));
                    }
                    if (player.hasPermission("kingdoms.player.invite")) {
                        player.sendMessage(cf("/k", "invite/add <player>", "<other players> ...", "Invite player(s) " +
                                "to join your village, they will have to accept your invitation", "/kingdoms"));
                    }
                }
                if (locVillage != null) {
                    if (player.hasPermission("kingdoms.player.info")) {
                        player.sendMessage(cf("k", "info", "", "Request information about the town underfoot," +
                                "currently " + ChatColor.ITALIC + locVillage.getName(), "/kingdoms"));
                    }
                }
            }
        }
    }

    private void consoleHelp(CommandSender sender) {

    }

    /**
     * Easy function to format the output of commands to make it look nice
     */
    private static String cf(@Nonnull String command,@Nonnull String args, @Nonnull String optArgs,
                             @Nonnull String desc, @Nonnull String alias) {
        return ChatColor.DARK_AQUA + command + " " + ChatColor.GREEN + args + " " + ChatColor.GOLD + optArgs +
                ChatColor.RESET + " : " + desc + " (" + ChatColor.DARK_RED + alias + ChatColor.RESET + ")";
    }
}
