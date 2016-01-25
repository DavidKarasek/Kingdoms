package com.mythbusterma.kingdoms.utils;

import org.bukkit.ChatColor;

import javax.annotation.Nonnull;

public class ChatUtil {

    /**
     * Easy function to format the output of commands to make it look nice
     */
    public static String cf(@Nonnull String command, @Nonnull String args,
                       @Nonnull String optArgs,
                             @Nonnull String desc, @Nonnull String alias) {
        return ChatColor.DARK_AQUA + command + " " + ChatColor.GREEN + args + " " + ChatColor.GOLD + optArgs +
                ChatColor.RESET + " : " + desc + " (" + ChatColor.DARK_RED + alias + ChatColor.RESET + ")";
    }
}
