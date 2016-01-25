package com.mythbusterma.kingdoms.commands.kingdom;

import com.mythbusterma.kingdoms.Kingdoms;
import com.mythbusterma.kingdoms.commands.CommandResult;
import com.mythbusterma.kingdoms.commands.KingdomCommand;
import org.bukkit.command.CommandSender;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Used when another command wants to do something that requires confirmation
 */
public class AcceptCommand implements KingdomCommand<CommandSender> {

    private final KingdomCommandManager parent;
    private Map<CommandSender, Runnable> acceptanceMap = new WeakHashMap<>();

    public AcceptCommand(KingdomCommandManager parent) {
        this.parent = parent;
    }

    public void addAcceptAction(CommandSender sender, Runnable action) {
        acceptanceMap.put(sender, action);
    }

    @Override
    public CommandResult issue(CommandSender issuer, String[] args) {
        if (acceptanceMap.get(issuer) != null) {
            issuer.sendMessage(Kingdoms.PREFIX + "Action accepted.");
            acceptanceMap.get(issuer).run();
        } else {
            issuer.sendMessage(Kingdoms.ERROR_PREFIX + "There is no action for you to accept!");
        }
        return CommandResult.SUCCESS;
    }
}
