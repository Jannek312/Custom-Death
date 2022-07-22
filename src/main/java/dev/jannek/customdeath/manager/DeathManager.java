package dev.jannek.customdeath.manager;

import dev.jannek.customdeath.CustomDeath;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DeathManager {

    private final Map<UUID, Long> deathPlayers = new HashMap<>();

    private final int reviveTimer;
    private final Material reviveMaterial;

    public DeathManager(final FileConfiguration config) {
        this.reviveTimer = config.getInt("revive.timer", 60);

        final String materialName = config.getString("revive.item", Material.GOLDEN_APPLE.name());
        for (final Material material : Material.values()) {
            if(material.name().equalsIgnoreCase(materialName)) {
                this.reviveMaterial = material;
                return;
            }
        }
        this.reviveMaterial = Material.GOLDEN_APPLE;
        System.err.printf("Could not found material %s! Using fallback!%n", materialName);
    }

    public void init() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(CustomDeath.getInstance(),
                () -> this.deathPlayers.forEach(this::checkDeath), 0, 20);
    }

    private void checkDeath(final UUID uuid, final long deathTimestamp) {
        if ((deathTimestamp + (1000L * reviveTimer)) <= System.currentTimeMillis()) return;
        final Player player = Bukkit.getPlayer(uuid);
        if (player == null) return;
        this.playerFinalDeath(player);
    }

    public void playerDeath(final Player player) {
        this.deathPlayers.put(player.getUniqueId(), System.currentTimeMillis());

        //TODO player stuck..
    }

    public void playerFinalDeath(final Player player) {
        this.deathPlayers.remove(player.getUniqueId());
        player.setHealth(0);
    }

    public void playerRevived(final Player player) {
        this.deathPlayers.remove(player.getUniqueId());
    }

    public boolean isReviveItem(final ItemStack itemStack) {
        if(itemStack == null) return false;
        return itemStack.getType().equals(getReviveMaterial());
    }

    public boolean isDeath(final Player player) {
        return this.deathPlayers.containsKey(player.getUniqueId());
    }

    public int getReviveTimer() {
        return reviveTimer;
    }

    public Material getReviveMaterial() {
        return reviveMaterial;
    }
}
