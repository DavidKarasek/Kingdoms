package com.mythbusterma.kingdoms;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UuidHolder {
    private Map<UUID,String> uuidToName = new ConcurrentHashMap<>();
    private Map<String,UUID> nameToUuid = new ConcurrentHashMap<>();

    public void addUser(UUID id, String name) {
        uuidToName.put(id, name);
        nameToUuid.put(name, id);
    }

    public UUID getUuid(String name) {
        return nameToUuid.get(name);
    }

    public String getName(UUID id) {
        return uuidToName.get(id);
    }
}
