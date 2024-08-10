package me.marcify.lavaProof.events;

import me.marcify.lavaProof.config.LavaConfig;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

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
                if (e.getCause() == EntityDamageEvent.DamageCause.LAVA) {
                    item.setInvulnerable(true);
                    itemsInLava.add(item);
                    e.setCancelled(true);
                }
            }
        }
    }

    private void startLavaCheckTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Set<Item> toRemove = new HashSet<>();
                for (Item item : itemsInLava) {
                    Block blockBelow = item.getLocation().getBlock();
                    if (blockBelow.getType() != Material.LAVA) {
                        item.setFireTicks(0);
                        item.setInvulnerable(false);
                        toRemove.add(item);
                    }
                }
                itemsInLava.removeAll(toRemove);
            }
        }.runTaskTimer(plugin, 0L, 20L); // Check every second (20 ticks)
    }
}
