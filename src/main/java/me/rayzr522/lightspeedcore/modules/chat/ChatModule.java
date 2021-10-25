package me.rayzr522.lightspeedcore.modules.chat;

import me.rayzr522.jsonmessage.JSONMessage;
import me.rayzr522.lightspeedcore.LightspeedCore;
import me.rayzr522.lightspeedcore.api.commands.ModuleCommand;
import me.rayzr522.lightspeedcore.api.modules.AbstractModule;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Collections;
import java.util.List;

public class ChatModule extends AbstractModule implements Listener {
    @Override
    public void onLoad(LightspeedCore core) {
        super.onLoad(core);

        getPlugin().getServer().getPluginManager().registerEvents(this, getPlugin());
    }

    @EventHandler(ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent e) {
        e.getRecipients().clear();

        Player player = e.getPlayer();

        JSONMessage.create(" ")
                .then(String.format(
                        "%s%s%s",
                        getPrefixFor(player),
                        player.getDisplayName(),
                        getSuffixFor(player)
                ))
                .suggestCommand(String.format("/msg %s ", player.getName()))
                .tooltip(
                        JSONMessage.create("Click to message this player")
                                .color(ChatColor.GRAY)
                                .style(ChatColor.ITALIC)
                )
                .then("\u00bb")
                .color(ChatColor.DARK_GRAY)
                .then(" ")
                .then(e.getMessage())
                .color(ChatColor.YELLOW)
                .send(Bukkit.getOnlinePlayers().toArray(new Player[0]));
    }

    public String getPrefixFor(Player player) {
        return getPlugin().getVaultChat()
                .map(chat -> chat.getPlayerPrefix(player))
                .map(prefix -> ChatColor.translateAlternateColorCodes('&', prefix))
                .map(prefix -> String.format("%s ", prefix))
                .orElse("");
    }

    public String getSuffixFor(Player player) {
        return getPlugin().getVaultChat()
                .map(chat -> chat.getPlayerSuffix(player))
                .map(prefix -> ChatColor.translateAlternateColorCodes('&', prefix))
                .map(prefix -> String.format(" %s", prefix))
                .orElse("");
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
