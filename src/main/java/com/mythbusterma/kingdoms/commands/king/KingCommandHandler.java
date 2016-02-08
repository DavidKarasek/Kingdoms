package com.mythbusterma.kingdoms.commands.king;

import com.mythbusterma.kingdoms.Kingdoms;
import com.mythbusterma.kingdoms.commands.BaseCommandExecutor;

/**
 * This is the command handler for all the "admin" commands
 */
public class KingCommandHandler extends BaseCommandExecutor {

    private final Subcommand noArgsCommand;
    private Kingdoms kingdoms;

    @Override
    protected Subcommand getNoArgsCommand() {
        return noArgsCommand;
    }

    public KingCommandHandler(Kingdoms kingdoms) {
        super(kingdoms);
        this.kingdoms = kingdoms;

        noArgsCommand = registerSubcommand(new KingHelpCommand(this),
                null, 0, "?", "help", "wtf", "h");

        registerSubcommand(new KingDisbandCommand(this),
                "kingdoms.king.delete", 1, "disband", "delete", "remove");

        registerSubcommand(new KingAddCommand(this),
                "kingdoms.king.add", 2, "add", "forceadd", "a", "put");

        registerSubcommand(new KingSetLordCommand(this),
                "kingdoms.king.setlord", 2, "setlord");
    }
}
