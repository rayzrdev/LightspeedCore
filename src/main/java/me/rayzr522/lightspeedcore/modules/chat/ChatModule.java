package me.rayzr522.lightspeedcore.modules.chat;

import me.rayzr522.jsonmessage.JSONMessage;
import me.rayzr522.lightspeedcore.LightspeedCore;
import me.rayzr522.lightspeedcore.api.commands.ModuleCommand;
import me.rayzr522.lightspeedcore.api.modules.AbstractModule;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

import java.util.Collections;
import java.util.List;

public class ChatModule extends AbstractModule implements Listener {
    @Override
    public void onLoad(LightspeedCore core) {
        super.onLoad(core);

        getPlugin().getServer().getPluginManager().registerEvents(this, getPlugin());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(PlayerChatEvent e) {
        e.setCancelled(true);

        Player player = e.getPlayer();

        JSONMessage.create(" ")
                .then(player.getDisplayName())
                .suggestCommand(String.format("/msg %s ", player.getName()))
                .then(" \u00bb ")
                .color(ChatColor.DARK_GRAY)
                .then(" ")
                .then(e.getMessage())
                .send(Bukkit.getOnlinePlayers().toArray(new Player[0]));
    }

    @Override
    public String getName() {
        return "chat";
    }

    @Override
    public List<ModuleCommand> getCommands() {
        return Collections.emptyList();
    }
}
