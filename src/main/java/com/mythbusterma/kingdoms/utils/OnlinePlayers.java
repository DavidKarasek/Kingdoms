package com.mythbusterma.kingdoms.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;

public class OnlinePlayers {

    static Method players = null;

    public static int size() {
        return OnlinePlayers.get().size();
    }

    public static Collection<? extends Player> get() {
        if (OnlinePlayers.players == null) {
            try {
                players = Bukkit.class.getMethod("getOnlinePlayers");
                final Class<?> returnType = players.getReturnType();
                if (Collection.class.isAssignableFrom(returnType)) {
                    return (Collection<? extends Player>) players.invoke(null);
                } else {
                    return Arrays.asList((Player[]) players.invoke(null));
                }
            } catch (NoSuchMethodException | InvocationTargetException
                    | IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            try {
                final Class<?> returnType = players.getReturnType();
                if (Collection.class.isAssignableFrom(returnType)) {
                    return (Collection<? extends Player>) players.invoke(null);
                } else {
                    return Arrays.asList((Player[]) players.invoke(null));
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        throw new java.lang.NullPointerException("Failed to find online " +
                "players.");
    }
}