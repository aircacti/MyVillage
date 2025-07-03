package com.lambdaprofessional.myvillage.events;

import com.lambdaprofessional.myvillage.inators.Spawninator;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class VillagerDamageBlockListener implements Listener {
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity damaged = event.getEntity();
        if (Spawninator.isOurVillager(damaged)) {
            event.setCancelled(true);
        }

    }
}