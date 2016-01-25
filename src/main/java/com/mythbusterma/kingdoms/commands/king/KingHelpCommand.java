package com.mythbusterma.kingdoms.commands.king;

import com.mythbusterma.kingdoms.commands.CommandResult;
import com.mythbusterma.kingdoms.commands.KingdomCommand;
import org.bukkit.command.CommandSender;

public class KingHelpCommand implements KingdomCommand<CommandSender> {

    private final KingCommandHandler parent;

    public KingHelpCommand(KingCommandHandler parent) {
        this.parent = parent;
    }

    @Override
    public CommandResult issue(CommandSender issuer, String[] args) {


        return null;
    }
}
