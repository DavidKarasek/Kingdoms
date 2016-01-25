package com.mythbusterma.kingdoms.commands.kingdom;

import com.mythbusterma.kingdoms.Kingdoms;
import com.mythbusterma.kingdoms.commands.BaseCommandExecutor;
import com.mythbusterma.kingdoms.commands.lord.AddCommand;
import com.mythbusterma.kingdoms.commands.lord.DisbandCommand;

public class KingdomCommandManager extends BaseCommandExecutor {

    private final AcceptCommand acceptCommand;
    private final Subcommand noArgsCommand;


    @Override
    protected Subcommand getNoArgsCommand() {
        return noArgsCommand;
    }

    public KingdomCommandManager(Kingdoms parent) {
        super(parent);

        noArgsCommand = registerSubcommand(new HelpCommand(this), null, 0,
                "help", "?", "helpme", "wtf", "h");

        acceptCommand = (AcceptCommand) registerSubcommand(
                new AcceptCommand(this), null, 0, "accept").getHandler();


        registerSubcommand(new ClaimCommand(this),
                "kingdoms.player.claim", 0, "claim", "c");

        registerSubcommand(new CreateCommand(this),
                "kingdoms.player.create", 1, "create", "new");

        registerSubcommand(new DepositCommand(this),
                null, 1, "deposit", "d");

        registerSubcommand(new InfoCommand(this),
                "kingdoms.player.info", 0, "info", "i", "here");

        registerSubcommand(new JoinCommand(this),
                "kingdoms.player.join", 1, "join", "request", "j");

        registerSubcommand(new ListCommand(this),
                "kingdoms.player.list", 0, "list", "l");

        registerSubcommand(new RejectCommand(this),
                null, 0, "reject", "refuse", "deny");

        registerSubcommand(new SpawnCommand(this),
                "kingdoms.player.spawn", 0, "spawn", "s");
    }

    public AcceptCommand getAcceptCommand() {
        return acceptCommand;
    }


}
