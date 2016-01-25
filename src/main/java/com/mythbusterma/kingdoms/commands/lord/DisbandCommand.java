package com.mythbusterma.kingdoms.commands.lord;

import com.mythbusterma.kingdoms.Kingdoms;
import com.mythbusterma.kingdoms.Village;
import com.mythbusterma.kingdoms.commands.BaseCommandExecutor;
import com.mythbusterma.kingdoms.commands.CommandResult;
import com.mythbusterma.kingdoms.commands.PlayerCommand;
import org.bukkit.entity.Player;

public class DisbandCommand implements PlayerCommand {

    private final BaseCommandExecutor parent;

    public DisbandCommand(BaseCommandExecutor parent) {
        this.parent = parent;
    }

    @Override
    public CommandResult issue(final Player player, String[] args) {

        final Village village = parent.getParent().getKingdomsManager()
                .getPlayer(player).getVillage();
        player.sendMessage(
                Kingdoms.PREFIX + "You are about to permanently delete " +
                        "your village, are you sure you want to do this? (\"/k accept\" to confirm)");
        parent.getParent().getKingdomCommandManager().getAcceptCommand()
                .addAcceptAction(player, new Runnable() {
                    @Override
                    public void run() {
                        player.sendMessage(
                                Kingdoms.PREFIX + "Deleting village....");
                        parent.getParent().getKingdomsManager()
                                .deleteVillage(village);
                    }
                });


        return CommandResult.SUCCESS;
    }
}
