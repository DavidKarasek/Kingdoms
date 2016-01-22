package com.mythbusterma.kingdoms.utils;

import com.mythbusterma.kingdoms.KingdomPlayer;
import com.mythbusterma.kingdoms.Kingdoms;
import com.mythbusterma.kingdoms.Village;

public class VillageUtil {
    public static void messageVillage(Village village, String message) {
        for (KingdomPlayer villageMember: Kingdoms.getInstance()
                .getKingdomsManager().getPlayers()) {
            if (villageMember.getVillage() != null && villageMember
                    .getVillage().equals(village)) {
                villageMember.getPlayer().sendMessage(message);
            }
        }
    }
}
