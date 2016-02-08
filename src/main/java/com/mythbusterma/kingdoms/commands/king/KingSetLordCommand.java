package com.mythbusterma.kingdoms.commands.king;

import com.mythbusterma.kingdoms.commands.CommandResult;
import com.mythbusterma.kingdoms.commands.KingdomCommand;
import org.bukkit.command.CommandSender;

/**
 * Used for admins to change who the lord of a village is
 */
public class KingSetLordCommand implements KingdomCommand<CommandSender> {

    private final KingCommandHandler parent;

    public KingSetLordCommand(KingCommandHandler parent) {
        this.parent = parent;
    }

    @Override
    public CommandResult issue(CommandSender issuer, String[] args) {

        return null;
    }
}
