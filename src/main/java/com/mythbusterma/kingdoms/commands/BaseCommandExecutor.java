package com.mythbusterma.kingdoms.commands;

import com.mythbusterma.kingdoms.Kingdoms;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.HashMap;

public abstract class BaseCommandExecutor implements CommandExecutor {
    protected final Kingdoms parent;
    protected final HashMap<String, Subcommand> subcommands = new HashMap<>();
    protected abstract Subcommand getNoArgsCommand();

    public BaseCommandExecutor(Kingdoms parent) {
        this.parent = parent;
    }

    protected Subcommand registerSubcommand(KingdomCommand handler,
                                            String permission, int minArgs,
                                            String... names) {
        final Subcommand subcommand = new Subcommand(handler, permission,
                minArgs);
        for (String name : names) {
            subcommands.put(name, subcommand);
        }
        return subcommand;
    }

    public KingdomCommand getSubcommandHandler(String command) {
        return subcommands.get(command).handler;
    }

    public Kingdoms getParent() {
        return parent;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command,
                             String cmd, String... args) {

        if (args.length == 0) {
            getNoArgsCommand().issue(commandSender, null);
            return true;
        }

        String subcommand = args[0].toLowerCase();

        if (!subcommands.containsKey(subcommand)) {
            commandSender.sendMessage(
                    Kingdoms.ERROR_PREFIX + "Command not recognized, " +
                            "try \"/k help\" for a list of commands.");
            return true;
        }


        String permission = subcommands.get(subcommand).permission;

        if (permission != null && !commandSender.hasPermission(permission)) {
            commandSender.sendMessage(Kingdoms.ERROR_PREFIX + "You don't have" +
                    " permission to execute that command, please contact the" +
                    " server owner if you believe this is an error.");
            return true;
        }

        String[] subcommandArgs = Arrays.copyOfRange(args, 1, args.length - 1);

        CommandResult result = subcommands.get(subcommand)
                .issue(commandSender, subcommandArgs);
        return true;
    }

    protected class Subcommand {
        protected final KingdomCommand<CommandSender> handler;
        protected final String permission;
        private final int minArgs;

        public Subcommand(KingdomCommand<CommandSender> handler,
                          String permission, int minArgs) {
            this.handler = handler;
            this.permission = permission;
            this.minArgs = minArgs;
        }

        public CommandResult issue(CommandSender sender, String[] args) {
            return handler.issue(sender, args);
        }

        public KingdomCommand<CommandSender> getHandler() {
            return handler;
        }
    }
}
