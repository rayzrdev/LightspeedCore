package me.rayzr522.lightspeedcore;

import me.rayzr522.lightspeedcore.api.commands.CommandRegister;
import me.rayzr522.lightspeedcore.api.commands.ICommandHandler;
import me.rayzr522.lightspeedcore.api.modules.IModule;
import me.rayzr522.lightspeedcore.api.storage.IStorageProvider;
import me.rayzr522.lightspeedcore.api.storage.impl.YamlStorageProvider;
import me.rayzr522.lightspeedcore.commands.CommandAdminChat;
import me.rayzr522.lightspeedcore.commands.CommandSpawn;
import me.rayzr522.lightspeedcore.commands.admin.CommandGamemode;
import me.rayzr522.lightspeedcore.commands.admin.CommandSpeed;
import me.rayzr522.lightspeedcore.commands.fun.CommandShrug;
import me.rayzr522.lightspeedcore.commands.moderation.CommandFeed;
import me.rayzr522.lightspeedcore.commands.moderation.CommandHeal;
import me.rayzr522.lightspeedcore.commands.moderation.CommandKick;
import me.rayzr522.lightspeedcore.modules.chat.ChatModule;
import me.rayzr522.lightspeedcore.modules.chestsorter.ChestSorterModule;
import me.rayzr522.lightspeedcore.modules.deathlog.DeathLogModule;
import me.rayzr522.lightspeedcore.modules.dm.DMModule;
import me.rayzr522.lightspeedcore.modules.pvptoggle.PVPToggleModule;
import me.rayzr522.lightspeedcore.modules.whois.WhoisModule;
import me.rayzr522.lightspeedcore.utils.MessageHandler;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;

public class LightspeedCore extends JavaPlugin {
    private static LightspeedCore instance;

    private final CommandRegister commandRegister = new CommandRegister(this);
    private final MessageHandler messages = new MessageHandler();
    private final List<IModule> modules = new ArrayList<>();
    private IStorageProvider settings;
    private IStorageProvider storageProvider;
    private IStorageProvider playerData;

    private Chat vaultChat;

    public static LightspeedCore getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        vaultChat = Optional.ofNullable(getServer().getServicesManager().getRegistration(Chat.class))
                .map(RegisteredServiceProvider::getProvider)
                .orElse(null);

        settings = new YamlStorageProvider(getFile("settings.yml"));
        settings.load(this);
        settings.reload();

        storageProvider = new YamlStorageProvider(getFile("storage.yml"));
        storageProvider.load(this);
        storageProvider.reload();

        // TODO: Is this better?
        // playerData = storageProvider.fork("players");
        playerData = new YamlStorageProvider(getFile("players.yml"));
        playerData.load(this);
        playerData.reload();

        // Load config files & lang
        reload();

        try {
            commandRegister.init();
        } catch (NoSuchFieldException | NoSuchMethodException | IllegalAccessException e) {
            getLogger().log(Level.SEVERE, "Failed to set up CommandRegister, disabling plugin.", e);
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        registerModules();
        registerCommands();

        modules.forEach(module -> {
            module.onLoad(this);
            module.getCommands().forEach(this::registerCommand);
        });
    }

    private void registerModules() {
//        registerModule(new TpaModule());
        registerModule(new DMModule());
        registerModule(new PVPToggleModule());
        registerModule(new ChestSorterModule());
        registerModule(new ChatModule());
        registerModule(new DeathLogModule());
        registerModule(new WhoisModule());
    }

    private void registerCommands() {
        // Admin
        registerCommand(new CommandAdminChat());
        registerCommand(new CommandGamemode());
        registerCommand(new CommandSpeed());

        registerCommand(new CommandSpawn());

        // Fun
        registerCommand(new CommandShrug());

        // Moderation
        registerCommand(new CommandKick());
        registerCommand(new CommandHeal());
        registerCommand(new CommandFeed());
    }

    @Override
    public void onDisable() {
        instance = null;

        settings.save();
        storageProvider.save();
        playerData.save();

        settings.unload(this);
        storageProvider.unload(this);
        playerData.unload(this);

        commandRegister.unregisterAll();
    }

    /**
     * @param module The {@link IModule module} to register.
     */
    private void registerModule(IModule module) {
        modules.add(module);
    }

    /**
     * @param handler The {@link ICommandHandler command handler} to register.
     */
    private void registerCommand(ICommandHandler handler) {
        Objects.requireNonNull(handler, "handler cannot be null!");

        // TODO: More than this?
        commandRegister.register(handler);
    }

    public Optional<IModule> getModule(Class<IModule> moduleClass) {
        return modules.stream().filter(module -> moduleClass == module.getClass()).findFirst();
    }

    public Optional<IModule> getModule(String name) {
        return modules.stream().filter(module -> module.getName().equalsIgnoreCase(name)).findFirst();
    }

    /**
     * (Re)loads all configs from the disk.
     */
    private void reload() {
        saveDefaultConfig();
        reloadConfig();


        // TODO: Remove when I'm done testing
        System.out.println("Checking messages.yml...");
        if (getFile("messages.yml").exists()) {
            System.out.println("Deleting messages.yml");
            getFile("messages.yml").delete();
        }

        messages.load(getConfig("messages.yml"));
    }

    /**
     * @return The settings storage provider.
     */
    public IStorageProvider getSettings() {
        return settings;
    }

    /**
     * @return The general purpose storage provider.
     */
    public IStorageProvider getStorageProvider() {
        return storageProvider;
    }

    /**
     * @return The player data storage provider.
     */
    public IStorageProvider getPlayerData() {
        return playerData;
    }

    /**
     * If the file is not found and there is a default file in the JAR, it saves the default file to the plugin data folder first.
     *
     * @param path The path to the settings file (relative to the plugin data folder).
     * @return The {@link YamlConfiguration}.
     */
    private YamlConfiguration getConfig(String path) {
        if (!getFile(path).exists() && getResource(path) != null) {
            saveResource(path, true);
        }
        return YamlConfiguration.loadConfiguration(getFile(path));
    }

    /**
     * Attempts to save a {@link YamlConfiguration} to the disk, and any {@link IOException}s are printed to the console.
     *
     * @param config The settings to save.
     * @param path   The path to save the settings file to (relative to the plugin data folder).
     */
    public void saveConfig(YamlConfiguration config, String path) {
        try {
            config.save(getFile(path));
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Failed to save settings", e);
        }
    }

    /**
     * @param path The path of the file (relative to the plugin data folder).
     * @return The {@link File}.
     */
    private File getFile(String path) {
        return new File(getDataFolder(), path.replace('/', File.separatorChar));
    }

    /**
     * Returns a message from the language file.
     *
     * @param key     The key of the message to translate.
     * @param objects The formatting objects to use.
     * @return The formatted message.
     */
    public String tr(String key, Object... objects) {
        return messages.tr(key, objects);
    }

    /**
     * Returns a message from the language file without adding the prefix.
     *
     * @param key     The key of the message to translate.
     * @param objects The formatting objects to use.
     * @return The formatted message.
     */
    public String trRaw(String key, Object... objects) {
        return messages.trRaw(key, objects);
    }

    /**
     * Checks a target {@link CommandSender} for a given permission (excluding the permission base). Example:
     * <p>
     * <pre>
     *     checkPermission(sender, "command.use", true);
     * </pre>
     * <p>
     * This would check if the player had the permission <code>"{plugin name}.command.use"</code>, and if they didn't, it would send them the no-permission message from the messages settings file.
     *
     * @param target      The target {@link CommandSender} to check.
     * @param permission  The permission to check, excluding the permission base (which is the plugin name).
     * @param sendMessage Whether or not to send a no-permission message to the target.
     * @return Whether or not the target has the given permission.
     */
    public boolean checkPermission(CommandSender target, String permission, boolean sendMessage) {
        String fullPermission = String.format("%s.%s", getName(), permission);

        if (!target.hasPermission(fullPermission)) {
            if (sendMessage) {
                target.sendMessage(tr("no-permission", fullPermission));
            }

            return false;
        }

        return true;
    }

    /**
     * @return The {@link MessageHandler} instance for this plugin.
     */
    public MessageHandler getMessages() {
        return messages;
    }

    public Optional<Chat> getVaultChat() {
        return Optional.of(vaultChat);
    }
}
