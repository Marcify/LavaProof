package me.marcify.lavaProof.events;

import me.marcify.lavaProof.config.LavaConfig;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.List;

public class BurnEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBurn(EntityDamageEvent e) {
        Entity entity = e.getEntity();
        if (entity instanceof Item item) {
            Material material = item.getItemStack().getType();
            List<String> noBurnItems = LavaConfig.getInstance().getNoBurnItems();

            if (noBurnItems.contains(material.toString())) {
                if (e.getCause() == EntityDamageEvent.DamageCause.LAVA) {
                    entity.setInvulnerable(true);
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCombust(EntityCombustByBlockEvent e) {
        Entity entity = e.getEntity();
        Block combuster = e.getCombuster();

        if(entity instanceof Item item) {
            Material material = item.getItemStack().getType();
            List<String> noBurnItems = LavaConfig.getInstance().getNoBurnItems();

            if (noBurnItems.contains(material.toString())) {
                if (combuster.getType() == Material.LAVA) {
                    entity.setInvulnerable(true);
                    e.setCancelled(true);
                }
            }
        }
    }
}
