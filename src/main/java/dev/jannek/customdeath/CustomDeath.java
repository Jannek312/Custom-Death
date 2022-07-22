package dev.jannek.customdeath;

import dev.jannek.customdeath.listener.DeathListener;
import dev.jannek.customdeath.manager.DeathManager;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class CustomDeath extends JavaPlugin {

    private static CustomDeath instance;

    public static CustomDeath getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        final PluginManager pluginManager = getServer().getPluginManager();

        final DeathManager deathManager = new DeathManager(getConfig());
        pluginManager.registerEvents(new DeathListener(deathManager), this);
    }

    @Override
    public void onDisable() {

    }
}
