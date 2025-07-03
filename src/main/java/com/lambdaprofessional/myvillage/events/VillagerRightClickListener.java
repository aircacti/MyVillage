package com.lambdaprofessional.myvillage.events;

import com.lambdaprofessional.myvillage.inators.Spawninator;
import com.lambdaprofessional.myvillage.configs.Hardcode;
import com.lambdaprofessional.myvillage.configs.UserConfig;
import com.lambdaprofessional.myvillage.objects.LambdaBehavior;
import com.lambdaprofessional.myvillage.objects.LambdaVillager;
import com.lambdaprofessional.myvillage.storage.BehaviorsStorage;
import com.lambdaprofessional.myvillage.storage.VillagersStorage;
import com.lambdaprofessional.myvillage.utils.Messenger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.persistence.PersistentDataType;

import java.io.IOException;

import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

public class VillagerRightClickListener implements Listener {

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Entity clicked = event.getRightClicked();
        if (!Spawninator.isOurVillager(clicked)) return;

        event.setCancelled(true);

        Player player = event.getPlayer();
        String villagerId = clicked.getPersistentDataContainer().get(Hardcode.KEY_MYVILLAGE_ID, PersistentDataType.STRING);

        if (player.isSneaking() && UserConfig.getDoTips() && player.hasPermission(Hardcode.PERMISSION_TIPS)) {
            Messenger.send(player, true, "Villager '" + villagerId + "'", GOLD);
        }

        if (villagerId == null) {
            Messenger.info("Clicked villager missing name in PDC: " + clicked.getUniqueId());
            return;
        }

        LambdaVillager villager;
        try {
            villager = VillagersStorage.getFromFile(villagerId);
        } catch (IOException e) {
            Messenger.info("Failed to load villager '" + villagerId + "' from file: " + e.getMessage());
            return;
        }

        if (villager == null) {
            Messenger.info("Villager '" + villagerId + "' not found in storage");
            return;
        }

        for (String behaviorStr : villager.getBehaviors()) {
            LambdaBehavior lambdaBehavior;
            try {
                if (!BehaviorsStorage.existsInFile(behaviorStr)) {
                    if (UserConfig.getDoTips() && player.hasPermission(Hardcode.PERMISSION_TIPS))  {
                        Messenger.send(player, true, "Clicked villager '"+villagerId+"' is taught unknown behavior '"+behaviorStr+"'", RED);
                    }
                    continue;
                }
                lambdaBehavior = BehaviorsStorage.getFromFile(behaviorStr);
            } catch (Exception e) {
                Messenger.info("Failed to load behavior '" + behaviorStr + "': " + e.getMessage());
                continue;
            }


            // Placeholders
            String instruction = lambdaBehavior.getInstruction().replace("<player>", player.getName());

            if (instruction.isBlank()) {
                Messenger.info("Empty instruction in behavior '" + behaviorStr + "' for villager '" + villagerId + "'");
                continue;
            }

            switch (lambdaBehavior.getType()) {
                case PLAYERCOMMAND -> Bukkit.dispatchCommand(player, instruction);
                case CONSOLECOMMAND -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), instruction);
                case MESSAGE -> {
                    Component formattedInstruction = LegacyComponentSerializer.legacyAmpersand().deserialize(instruction);
                    player.sendMessage(formattedInstruction);
                }
                default -> Messenger.info("Unknown behavior type '" + lambdaBehavior.getType() + "' for behavior '" + behaviorStr + "'");
            }
        }
    }
}
