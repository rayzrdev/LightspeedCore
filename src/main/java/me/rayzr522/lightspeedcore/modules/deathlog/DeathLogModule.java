package me.rayzr522.lightspeedcore.modules.deathlog;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import me.rayzr522.lightspeedcore.LightspeedCore;
import me.rayzr522.lightspeedcore.api.commands.ModuleCommand;
import me.rayzr522.lightspeedcore.api.modules.AbstractModule;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class DeathLogModule extends AbstractModule implements Listener {
    private Multimap<UUID, DeathLogEntry> deathLog = new HashMultimap<>();
    private CommandDeathLog commandDeathLog;

    @Override
    public void onLoad(LightspeedCore core) {
        super.onLoad(core);

        commandDeathLog = new CommandDeathLog(this);

        core.getServer().getPluginManager().registerEvents(this, core);

        long delay = TimeUnit.MINUTES.toSeconds(10) * 20L;

        // auto-clear any logs from greater than 6 hours ago
        new BukkitRunnable() {
            @Override
            public void run() {
                long now = System.currentTimeMillis();

                List<DeathLogEntry> entriesToRemove = deathLog.values().stream().filter(entry -> (now - entry.getTime()) > TimeUnit.HOURS.toMillis(6)).collect(Collectors.toList());

                entriesToRemove.forEach(entry -> deathLog.remove(entry.getUuid(), entry));
            }
        }.runTaskTimer(core, delay, delay);
    }

    @Override
    public String getName() {
        return "deathlog";
    }

    @Override
    public List<ModuleCommand> getCommands() {
        return Collections.singletonList(commandDeathLog);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDeath(PlayerDeathEvent e) {
        deathLog.put(e.getEntity().getUniqueId(), new DeathLogEntry(e.getEntity()));
    }

    public Collection<DeathLogEntry> getDeathLogsFor(Player player) {
        return deathLog.get(player.getUniqueId());
    }
}
