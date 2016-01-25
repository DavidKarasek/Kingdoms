package com.mythbusterma.kingdoms.commands.lord;

import com.mythbusterma.kingdoms.commands.CommandResult;
import com.mythbusterma.kingdoms.commands.KingdomCommand;
import static com.mythbusterma.kingdoms.utils.ChatUtil.cf;

import org.bukkit.command.CommandSender;


public class LordHelpCommand implements KingdomCommand<CommandSender> {

    private final LordCommand parent;

    public LordHelpCommand(LordCommand parent) {
        this.parent = parent;
    }

    @Override
    public CommandResult issue(CommandSender issuer, String[] args) {
        // display lord commands
        if (issuer.hasPermission("kingdoms.player.create")) {
            issuer.sendMessage(cf("/k", "disband/delete", "", "Permanently " +
                            "delete your village, cannot be undone",
                    "/kingdoms"));
        }
        if (issuer.hasPermission("kingdoms.player.invite")) {
            issuer.sendMessage(cf("/k", "invite/add <player>", "<other " +
                    "players> ...", "Invite player(s) " +
                    "to join your village, they will have to accept your invitation", "/kingdoms"));
        }
        return null;
    }
}
