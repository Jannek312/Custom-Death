package dev.jannek.customdeath.listener;

import dev.jannek.customdeath.manager.DeathManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class DeathListener implements Listener {

    private final DeathManager deathManager;

    public DeathListener(DeathManager deathManager) {
        this.deathManager = deathManager;
    }

    @EventHandler
    public void onDeath(final EntityDamageEvent event) {
        final Entity entity = event.getEntity();
        if(!(entity instanceof  Player)) return;
        final Player player = (Player) entity;

        this.onFinalDeath(player);

        if(player.getHealth() - event.getDamage() >= 0) return;
        event.setCancelled(true);

        player.setHealth(0.5);
        deathManager.playerDeath(player);
    }

    public void onFinalDeath(final Player player) {
        //TODO Should player finely be killed by any damage or only by hit from other player?
        if(!deathManager.isDeath(player)) return;
        deathManager.playerFinalDeath(player);
    }

    @EventHandler
    public void onFinalDeath(final PlayerDeathEvent event) {
        final Player player = event.getEntity();
        if(!deathManager.isDeath(player)) return;
        deathManager.playerFinalDeath(player);
    }

    @EventHandler
    public void onInteract(final PlayerInteractEntityEvent event) {
        final Entity rightClicked = event.getRightClicked();
        if(!(rightClicked instanceof Player)) return;

        final Player player = event.getPlayer();
        final Player rightClickedPlayer = (Player) rightClicked;

        if(!deathManager.isReviveItem(player.getItemInUse())) return;

        deathManager.playerRevived(rightClickedPlayer);
    }




}
