package me.marcify.lavaProof.events;

import me.marcify.lavaProof.config.LavaConfig;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;

import java.util.List;

public class BurnEvent implements Listener {

    @EventHandler
    public void onBurn(EntityCombustEvent e) {
        Entity entity = e.getEntity();

        if (entity instanceof Item) {
            Item item = (Item) entity;
            Material material = item.getItemStack().getType();

            List<String> noBurnItems = LavaConfig.getInstance().getNoBurnItems();

            if (noBurnItems.contains(material.toString())) {
                e.setCancelled(true);
            }
        }

    }
}
