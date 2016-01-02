package com.mythbusterma.kingdoms.commands;

import com.mythbusterma.kingdoms.Kingdoms;
import com.mythbusterma.kingdoms.Village;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;

public class KingdomCommandManager implements CommandExecutor {

    private final Kingdoms parent;

    private final HashMap<String, Subcommand> subcommands = new HashMap<>();

    private final Subcommand noArgsCommand;
    private final AcceptCommand acceptCommand;


    public KingdomCommandManager(Kingdoms parent) {
        this.parent = parent;

        noArgsCommand = registerSubcommand(new HelpCommand(this), null, 0, "help", "?", "helpme", "wtf", "h");
        registerSubcommand(new DepositCommand(this), null, 1, "deposit", "d");
        registerSubcommand(new ClaimCommand(this), "kingdoms.player.claim", 0, "claim", "c");
        registerSubcommand(new CreateCommand(this), "kingdoms.player.create", 1, "create", "new");
        registerSubcommand(new ListCommand(this), "kingdoms.player.list", 0, "list", "l");
        acceptCommand = (AcceptCommand) registerSubcommand(new AcceptCommand(this), null, 0, "accept").handler;
        registerSubcommand(new InfoCommand(this), "kingdoms.player.info", 0, "info", "i", "here");
        registerSubcommand(new DisbandCommand(this), "kingdoms.player.create", 0, "disband", "delete");
    }

    private Subcommand registerSubcommand(KingdomCommand handler, String permission, int minArgs, String... names) {
        final Subcommand subcommand = new Subcommand(handler, permission, minArgs);
        for (String name : names) {
            subcommands.put(name, subcommand);
        }
        return subcommand;
    }

    protected Village getVillage(Player player) {
        return parent.getKingdomsManager().getPlayer(player).getVillage();
    }

    public KingdomCommand getSubcommandHandler(String command) {
        return subcommands.get(command).handler;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String cmd, String... args) {

        if (args.length == 0) {
            noArgsCommand.issue(commandSender, null);
            return true;
        }

        String subcommand = args[0].toLowerCase();

        if (!subcommands.containsKey(subcommand)) {
            commandSender.sendMessage(Kingdoms.ERROR_PREFIX + "Command not recognized, try \"/k help\" for a list " +
                    "of commands.");
            return true;
        }

        String[] subcommandArgs = Arrays.copyOfRange(args, 1, args.length - 1);

        CommandResult result = subcommands.get(subcommand).issue(commandSender, subcommandArgs);
        return true;
    }

    public Kingdoms getParent() {
        return parent;
    }

    public AcceptCommand getAcceptCommand() {
        return acceptCommand;
    }


    private class Subcommand {
        private final KingdomCommand<CommandSender> handler;
        private final String permission;
        private final int minArgs;

        public Subcommand(KingdomCommand<CommandSender> handler, String permission, int minArgs) {
            this.handler = handler;
            this.permission = permission;
            this.minArgs = minArgs;
        }

        public CommandResult issue(CommandSender sender, String[] args) {
            return handler.issue(sender, args);
        }
    }
}
