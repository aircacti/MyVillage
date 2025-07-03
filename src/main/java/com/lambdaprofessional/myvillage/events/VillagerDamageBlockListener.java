package com.lambdaprofessional.myvillage.events;

import com.lambdaprofessional.myvillage.Inators.Spawninator;
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

//        PersistentDataContainer pdc = damaged.getPersistentDataContainer();
//        event.getDamager().sendMessage(Component.text("-  " + pdc.has(Hardcode.KEY_MYVILLAGE_ID, PersistentDataType.STRING) + " / " +  pdc.get(Hardcode.KEY_MYVILLAGE_ID, PersistentDataType.STRING)));
    }
}