package me.marcify.lavaProof.events;

import me.marcify.lavaProof.config.LavaConfig;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.List;

public class ExplosionEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onExplosion(EntityDamageEvent e) {
        Entity entity = e.getEntity();
        if (entity instanceof Item item) {
            String material = item.getItemStack().getType().toString();
            List<String> noExplosionItems = LavaConfig.getNoExplosionItems();

            if (noExplosionItems.contains(material)) {
                if (e.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION ||
                    e.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {
                    e.setCancelled(true);
                }
            }
        }
    }
}
