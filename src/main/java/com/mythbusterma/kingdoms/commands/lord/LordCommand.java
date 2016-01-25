package com.mythbusterma.kingdoms.commands.lord;

import com.mythbusterma.kingdoms.KingdomPlayer;
import com.mythbusterma.kingdoms.Kingdoms;
import com.mythbusterma.kingdoms.commands.BaseCommandExecutor;
import com.mythbusterma.kingdoms.commands.kingdom.HelpCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class LordCommand extends BaseCommandExecutor {

    private final Subcommand noArgsCommand;

    public LordCommand(Kingdoms parent) {
        super(parent);

        noArgsCommand = registerSubcommand(new LordHelpCommand(this), null, 0,
                "help", "?", "helpme", "wtf", "h");

        registerSubcommand(new AddCommand(this), "kingdoms.player.create", 1,
                "invite", "a");

        registerSubcommand(new DisbandCommand(this),
                "kingdoms.player.create", 0, "disband", "delete");

        registerSubcommand(new SetSpawnCommand(this),
                "kingdoms.lord.setspawn", 0, "setspawn");
    }

    @Override
    protected Subcommand getNoArgsCommand() {
        return noArgsCommand;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command,
                             String label, String[] args) {
        if (commandSender instanceof ConsoleCommandSender) {
            super.onCommand(commandSender, command, label, args);
            return true;
        }

        KingdomPlayer player = parent.getKingdomsManager().getPlayer(
                (Player) commandSender);

        if (player.getVillage() == null) {
            player.getPlayer().sendMessage(Kingdoms.ERROR_PREFIX + "You must " +
                    "be a lord to execute this command but you don't have a " +
                    "village yet, try (/k new <village name>) to create one.");
        }

        if (player.isLord()) {
            super.onCommand(commandSender, command, label, args);
            return true;
        }
        else {
            player.getPlayer().sendMessage(Kingdoms.ERROR_PREFIX + "This " +
                    "command con only be executed by a lord and you are not a" +
                    " lord.");
            return true;
        }

    }
}
