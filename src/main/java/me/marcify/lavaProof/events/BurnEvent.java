package me.marcify.lavaProof.events;

import me.marcify.lavaProof.config.LavaConfig;
import me.marcify.lavaProof.util.SchedulerUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BurnEvent implements Listener {

    private final Set<Item> itemsInLava = new HashSet<>();
    private final JavaPlugin plugin;

    public BurnEvent(JavaPlugin plugin) {
        this.plugin = plugin;
        startLavaCheckTask();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBurn(EntityDamageEvent e) {
        Entity entity = e.getEntity();
        if (entity instanceof Item item) {
            Material material = item.getItemStack().getType();
            List<String> noBurnItems = LavaConfig.getNoBurnItems();

            if (noBurnItems.contains(material.toString())) {
                EntityDamageEvent.DamageCause cause = e.getCause();
                if (cause == EntityDamageEvent.DamageCause.LAVA || 
                    cause == EntityDamageEvent.DamageCause.FIRE || 
                    cause == EntityDamageEvent.DamageCause.FIRE_TICK) {
                    item.setInvulnerable(true);
                    itemsInLava.add(item);
                    e.setCancelled(true);
                }
            }
        }
    }

    private void startLavaCheckTask() {
        SchedulerUtil.runTaskTimer(plugin, () -> {
            Set<Item> toRemove = new HashSet<>();
            for (Item item : itemsInLava) {
                if (item.isDead() || !item.isValid()) {
                    toRemove.add(item);
                    continue;
                }
                Block blockAtLocation = item.getLocation().getBlock();
                Material blockType = blockAtLocation.getType();
                if (blockType != Material.LAVA && blockType != Material.FIRE) {
                    item.setFireTicks(0);
                    item.setInvulnerable(false);
                    toRemove.add(item);
                }
            }
            itemsInLava.removeAll(toRemove);
        }, 0L, 20L); // Check every second (20 ticks)
    }
}
