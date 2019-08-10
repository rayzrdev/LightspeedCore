package me.rayzr522.lightspeedcore.modules.whois;

import me.rayzr522.lightspeedcore.LightspeedCore;
import me.rayzr522.lightspeedcore.api.commands.ModuleCommand;
import me.rayzr522.lightspeedcore.api.modules.AbstractModule;
import me.rayzr522.lightspeedcore.api.storage.impl.PlayerData;
import me.rayzr522.lightspeedcore.commands.moderation.CommandWhois;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class WhoisModule extends AbstractModule implements Listener {
    private final CommandWhois commandWhois = new CommandWhois(this);

    @Override
    public void onLoad(LightspeedCore core) {
        super.onLoad(core);

        core.getServer().getPluginManager().registerEvents(this, core);
    }

    @Override
    public String getName() {
        return "whois";
    }

    @Override
    public List<ModuleCommand> getCommands() {
        return Collections.singletonList(commandWhois);
    }

    public Optional<Location> getLastKnownLocation(UUID id) {
        Optional<World> optionalWorld = getPlayerData(id).getString("last-known-world")
                .map(UUID::fromString)
                .map(Bukkit::getWorld);
        Optional<Integer> optionalX = getPlayerData(id).getInt("last-known-x");
        Optional<Integer> optionalY = getPlayerData(id).getInt("last-known-y");
        Optional<Integer> optionalZ = getPlayerData(id).getInt("last-known-z");

        if (!optionalWorld.isPresent() || !optionalX.isPresent() || !optionalY.isPresent() || !optionalZ.isPresent()) {
            return Optional.empty();
        }

        return Optional.of(new Location(optionalWorld.get(), optionalX.get(), optionalY.get(), optionalZ.get()));
    }

    public Optional<String> getLastKnownIp(UUID id) {
        return getPlayerData(id).getString("last-known-ip");
    }

    public void updateData(Player player) {
        PlayerData data = getPlayerData(player.getUniqueId());

        data.set("last-known-world", player.getLocation().getWorld().getUID().toString());
        data.set("last-known-x", player.getLocation().getBlockX());
        data.set("last-known-y", player.getLocation().getBlockY());
        data.set("last-known-z", player.getLocation().getBlockZ());
        data.set("last-known-ip", player.getAddress().getAddress().toString().replace("/", ""));

        data.save();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        updateData(e.getPlayer());
    }
}
