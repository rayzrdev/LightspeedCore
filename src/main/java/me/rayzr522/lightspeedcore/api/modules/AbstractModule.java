package me.rayzr522.lightspeedcore.api.modules;

import me.rayzr522.lightspeedcore.LightspeedCore;
import me.rayzr522.lightspeedcore.api.storage.IStorageProvider;
import me.rayzr522.lightspeedcore.api.storage.impl.PlayerData;

import java.util.UUID;

public abstract class AbstractModule implements IModule {
    private LightspeedCore plugin;
    private IStorageProvider settings;
    private IStorageProvider storageProvider;

    private String _getCleanName() {
        return getName().trim().toLowerCase().replaceAll("[^a-zA-Z0-9_-]", "_");
    }

    @Override
    public void onLoad(LightspeedCore core) {
        this.plugin = core;
        this.settings = core.getSettings().fork(_getCleanName());
        this.storageProvider = core.getStorageProvider().fork(_getCleanName());
    }

    @Override
    public LightspeedCore getPlugin() {
        return plugin;
    }

    @Override
    public IStorageProvider getSettings() {
        return settings;
    }

    @Override
    public IStorageProvider getStorage() {
        return storageProvider;
    }

    @Override
    public PlayerData getPlayerData(UUID player) {
        IStorageProvider playerData = plugin.getPlayerData().fork(player.toString());
        return new PlayerData(playerData.getOrCreateData(_getCleanName()).getRaw(), player, playerData);
    }
}
