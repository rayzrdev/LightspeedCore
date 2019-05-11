package me.rayzr522.lightspeedcore.modules.deathlog;


import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import java.util.UUID;

public class DeathLogEntry {
    private final long time;
    private final Location location;
    private final UUID uuid;

    public DeathLogEntry(LivingEntity entity) {
        this.time = System.currentTimeMillis();
        this.location = entity.getLocation();
        this.uuid = entity.getUniqueId();
    }

    public long getTime() {
        return time;
    }

    public Location getLocation() {
        return location;
    }

    public UUID getUuid() {
        return uuid;
    }
}
