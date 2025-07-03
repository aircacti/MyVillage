package com.lambdaprofessional.myvillage.commands;

import com.lambdaprofessional.myvillage.inators.Spawninator;
import com.lambdaprofessional.myvillage.MyVillage;
import com.lambdaprofessional.myvillage.configs.Hardcode;
import com.lambdaprofessional.myvillage.configs.UserConfig;
import com.lambdaprofessional.myvillage.enums.LambdaBehaviorType;
import com.lambdaprofessional.myvillage.helpers.Helpers;
import com.lambdaprofessional.myvillage.objects.LambdaBehavior;
import com.lambdaprofessional.myvillage.objects.LambdaVillager;
import com.lambdaprofessional.myvillage.storage.BehaviorsStorage;
import com.lambdaprofessional.myvillage.storage.ConfigStorage;
import com.lambdaprofessional.myvillage.storage.VillagersStorage;
import com.lambdaprofessional.myvillage.utils.BoolUtils;
import com.lambdaprofessional.myvillage.utils.Messenger;
import com.lambdaprofessional.myvillage.utils.VillagerUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static net.kyori.adventure.text.format.NamedTextColor.*;

public class MyVillageCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0) {
            handleNoArgs(sender);
            return true;
        }

        if (!sender.hasPermission(Hardcode.PERMISSION_MANAGE)) {
            Messenger.send(sender, true, "No permission '" + Hardcode.PERMISSION_MANAGE + "'", RED);
            return true;
        }

        String subCommand = args[0].toLowerCase();

        try {
            switch (subCommand) {
                case "villager" -> handleVillagerCommand(sender, args);
                case "behavior" -> handleBehaviorCommand(sender, args);
                case "plugin" -> handlePluginCommand(sender, args);
                default -> Messenger.send(sender, true, "Unknown subcommand. Use /myvillage to view available commands", RED);
            }
        } catch (Exception e) {
            Messenger.send(sender, true, "An error occurred: " + e.getMessage(), RED);
            if (!(sender instanceof ConsoleCommandSender)) {
                Messenger.severe("Error during command '/"+label+" "+String.join(" ", Arrays.copyOfRange(args, 0, args.length))+"' user '"+sender.getName()+"': " + e.getMessage());
            }
            return false;
        }

        return false;
    }

    private void handlePluginCommand(CommandSender sender, String[] args) {
        if (args.length < 2) {
            Messenger.send(sender, true, "Usage: /myvillage plugin <action> [...]", RED);
            return;
        }

        String action = args[1].toLowerCase();

        switch (action) {
            case "refresh" -> pluginRefresh(sender, args);
            case "count" -> pluginCount(sender, args);
            case "reload" -> pluginReload(sender, args);
            case "config" -> pluginConfig(sender, args);
            default -> Messenger.send(sender, true, "Unknown plugin action: " + action, RED);
        }
    }

    private void pluginConfig(CommandSender sender, String[] args) {
        if (args.length != 2) {
            Messenger.send(sender, true, "Usage: /myvillage plugin config", RED);
            return;
        }

        Messenger.send(sender, true, "Config.yml data:", GOLD);

        Map<String, Object> all = ConfigStorage.getAllKeysAndValues();
        all.forEach((k, v) -> Messenger.send(sender, false,"- '" + k + "' = '" + v + "'", WHITE));

    }

    private void pluginReload(CommandSender sender, String[] args) {
        if (args.length != 2) {
            Messenger.send(sender, true, "Usage: /myvillage plugin reload", RED);
            return;
        }

        ConfigStorage.reload();
        Messenger.send(sender, true, "Config reloaded", GREEN);
    }

    private void pluginRefresh(CommandSender sender, String[] args) {
        if (args.length != 2) {
            Messenger.send(sender, true, "Usage: /myvillage plugin refresh", RED);
            return;
        }

        Spawninator.refresh();
        Messenger.send(sender, true, "Entities refreshed", GREEN);
    }

    private void pluginCount(CommandSender sender, String[] args) {
        if (args.length != 2) {
            Messenger.send(sender, true, "Usage: /myvillage plugin count", RED);
            return;
        }

        int expected = Spawninator.getExpectedVillagerCount();
        int actual = Spawninator.getCurrentVillagerCount();

        Messenger.send(sender, true, "Expected: " + expected + ", actual: " + actual, GREEN);
    }

    private void handleNoArgs(CommandSender sender) {
        if (!sender.hasPermission(Hardcode.PERMISSION_HELP)) {
            Messenger.send(sender, true, "No permission '" + Hardcode.PERMISSION_HELP + "'", RED);
            return;
        }

        Messenger.send(sender, true, "MyVillage v. " + MyVillage.getVersion(), GREEN);
        Messenger.send(sender, false,"Manage specific mob:", GOLD);
        sendVillagerHelp(sender);
        Messenger.send(sender, false,"Manage specific behavior:", GOLD);
        sendBehaviorHelp(sender);
        Messenger.send(sender, false,"Manage plugin:", GOLD);
        sendPluginHelp(sender);
    }

    private void sendPluginHelp(CommandSender sender) {
        Messenger.send(sender, false,"- /myvillage plugin refresh", WHITE, "Unspawn and spawn villagers");
        Messenger.send(sender, false,"- /myvillage plugin count", WHITE, "Count entities");
        Messenger.send(sender, false,"- /myvillage plugin reload", WHITE, "Reload config.yml");
        Messenger.send(sender, false,"- /myvillage plugin config", WHITE, "Show config.yml data");

    }

    private void sendVillagerHelp(CommandSender sender) {
        Messenger.send(sender, false,
                "- /myvillage villager create [VILLAGER ID] [TYPE] [PROFESSION] [LEVEL] [SILENT] [DISPLAY VISIBLE] [DISPLAY NAME]", WHITE,
                "Creates a new villager with the configuration:\n\n" +
                        "- VILLAGER ID: English letters and digits only\n" +
                        "- TYPE: Available: "+ Helpers.VillagerTypes.commaList+"\n" +
                        "- PROFESSION: Available: "+ Helpers.VillagerProfessions.commaList+"\n" +
                        "- LEVEL: Available: "+ Helpers.VillagerLevels.commaList+"\n" +
                        "- SILENT: Use true or false\n" +
                        "- DISPLAY VISIBLE: Use true or false\n" +
                        "- DISPLAY NAME: The name above the head formatted by &"
        );


        Messenger.send(sender, false,"- /myvillage villager learn [VILLAGER ID] [BEHAVIOR ID]", WHITE, "Assign behavior to villager");
        Messenger.send(sender, false,"- /myvillage villager forget [VILLAGER ID] [BEHAVIOR ID]", WHITE, "Remove behavior from villager");
        Messenger.send(sender, false,"- /myvillage villager info [VILLAGER ID]", WHITE, "Information about the villager");
        Messenger.send(sender, false,"- /myvillage villager list", WHITE, "All the villagers");
        Messenger.send(sender, false,"- /myvillage villager tp [VILLAGER ID]", WHITE, "Teleport to villager");
        Messenger.send(sender, false,"- /myvillage villager movehere [VILLAGER ID]", WHITE, "Moves the villager to your place");
        Messenger.send(sender, false,"- /myvillage villager remove [VILLAGER ID]", WHITE, "Remove villager. It's not un-summoning");

    }

    private void sendBehaviorHelp(CommandSender sender) {

        Messenger.send(sender, false, "- /myvillage behavior create [BEHAVIOR ID] [TYPE] [INSTRUCTION]", WHITE,"Available types:\n" + Helpers.BehaviorTypes.dashList);
        Messenger.send(sender, false,"- /myvillage behavior remove [BEHAVIOR ID]", WHITE, "Remove selected behavior");
        Messenger.send(sender, false,"- /myvillage behavior info [BEHAVIOR ID]", WHITE, "Information about behavior");
        Messenger.send(sender, false,"- /myvillage behavior list", WHITE, "List of behaviors");
    }

    private void handleVillagerCommand(CommandSender sender, String[] args) throws IOException {
        if (args.length < 2) {
            Messenger.send(sender, true, "Usage: /myvillage villager <action> [...]", RED);
            return;
        }

        String action = args[1].toLowerCase();

        switch (action) {
            case "create" -> villagerCreate(sender, args);
            case "forget" -> villagerForgetBehavior(sender, args);
            case "learn" -> villagerLearnBehavior(sender, args);
            case "remove" -> villagerRemove(sender, args);
            case "list" -> villagerList(sender);
            case "info" -> villagerInfo(sender, args);
            case "tp" -> villagerTp(sender, args);
            case "movehere" -> villagerMoveHere(sender, args);
            default -> Messenger.send(sender, true, "Unknown villager action: " + action, RED);
        }
    }

    private void villagerMoveHere(CommandSender sender, String[] args) throws IOException {
        if (!(sender instanceof Player)) {
            Messenger.send(sender, true, "Only players can move villagers", RED);
            return;
        }

        if (args.length != 3) {
            Messenger.send(sender, true, "Usage: /myvillage villager movehere [VILLAGER ID]", RED);
            return;
        }

        String id = args[2];

        if (!VillagersStorage.existsInFile(id)) {
            Messenger.send(sender, true, "Villager '" + id + "' does not exist", RED);
            return;
        }

        LambdaVillager newVillager = VillagersStorage.getFromFile(id);
        newVillager.setLocation(((Player) sender).getLocation());
        VillagersStorage.modifyVillagerInFile(id, newVillager);

        Spawninator.despawn(id);
        Spawninator.spawn(id);

        Messenger.send(sender, true, "Moved '" + id + "' here", GREEN);
    }

    private void villagerTp(CommandSender sender, String[] args) throws IOException {
        if (!(sender instanceof Player)) {
            Messenger.send(sender, true, "Only players can teleport to villagers", RED);
            return;
        }

        if (args.length != 3) {
            Messenger.send(sender, true, "Usage: /myvillage villager tp [VILLAGER ID]", RED);
            return;
        }

        String id = args[2];

        if (!VillagersStorage.existsInFile(id)) {
            Messenger.send(sender, true, "Villager '" + id + "' does not exist", RED);
            return;
        }

        LambdaVillager villager = VillagersStorage.getFromFile(id);

        ((Player) sender).teleport(villager.getLocation());
        Messenger.send(sender, true, "Teleported to '" + id + "'", GREEN);

    }

    private void villagerCreate(CommandSender sender, String[] args) throws IOException {
        if (!(sender instanceof Player)) {
            Messenger.send(sender, true, "Only players can create villagers", RED);
            return;
        }

        if (args.length < 9) {
            Messenger.send(sender, true, "Usage: /myvillage villager create [VILLAGER ID] [TYPE] [PROFESSION] [LEVEL] [SILENT] [DISPLAY VISIBLE] [DISPLAY NAME]", RED);
            return;
        }

        String inputId = args[2];
        String inputType = args[3];
        String inputProfession = args[4];
        String inputLevel = args[5];
        String inputSilent = args[6];
        String inputDisplayVisible = args[7];
        String inputDisplayName = String.join(" ", Arrays.copyOfRange(args, 8, args.length));

        if (!inputId.matches("^[a-zA-Z0-9]+$")) {
            Messenger.send(sender, true, "The name may only contain English letters and digits", RED);
            return;
        }

        String lowerId = inputId.toLowerCase();
        if (VillagersStorage.existsInFile(lowerId)) {
            Messenger.send(sender, true, "Villager '" + lowerId + "' already exists", RED);
            return;
        }

        if (!VillagerUtils.isValidType(inputType)) {
            Messenger.send(sender, true, "Invalid villager type '" + inputType + "'", RED, "Available:\n" + Helpers.VillagerTypes.dashList);
            return;
        }

        if (!VillagerUtils.isValidProfession(inputProfession)) {
            Messenger.send(sender, true, "Invalid villager profession '" + inputProfession + "'", RED, "Available:\n" + Helpers.VillagerProfessions.dashList);
            return;
        }

        int parsedLevel;
        try {
            parsedLevel = Integer.parseInt(inputLevel);
            if (parsedLevel < 1 || parsedLevel > 5) {
                Messenger.send(sender, true, "Level must be between 1 and 5", RED, "Available:\n" + Helpers.VillagerLevels.dashList);
                return;
            }
        } catch (NumberFormatException e) {
            Messenger.send(sender, true, "Level must be a valid integer between 1 and 5", RED, "Available:\n" + Helpers.VillagerLevels.dashList);
            return;
        }

        Boolean parsedSilent = BoolUtils.parseBoolean(inputSilent);
        if (parsedSilent == null) {
            Messenger.send(sender, true, "Invalid boolean value for silent '" + inputSilent+"'", RED, "Use 'true' or 'false'");
            return;
        }

        Boolean parsedDisplayVisible = BoolUtils.parseBoolean(inputDisplayVisible);
        if (parsedDisplayVisible == null) {
            Messenger.send(sender, true, "Invalid boolean value for displayVisible '" + inputDisplayVisible+"'", RED, "Use 'true' or 'false'");
            return;
        }

        LambdaVillager villager = new LambdaVillager(lowerId);
        villager.setType(VillagerUtils.getTypeByName(inputType));
        villager.setProfession(VillagerUtils.getProfessionByName(inputProfession));
        villager.setLevel(parsedLevel);
        villager.setSilent(parsedSilent);
        villager.setDisplayVisible(parsedDisplayVisible);
        villager.setDisplayName(inputDisplayName);
        villager.setLocation(((Player) sender).getLocation());

        VillagersStorage.addVillagerToFile(villager);

        Spawninator.spawn(villager);

        Messenger.send(sender, true, "Villager '" + lowerId + "' created successfully", GREEN);
    }

    private void villagerLearnBehavior(CommandSender sender, String[] args) throws IOException {
        if (args.length != 4) {
            Messenger.send(sender, true, "Usage: /myvillage villager learn [VILLAGER ID] [BEHAVIOR ID]", RED);
            return;
        }

        String id = args[2];
        String behavior = args[3].toLowerCase();

        if (!VillagersStorage.existsInFile(id)) {
            Messenger.send(sender, true, "Villager '" + id + "' does not exist", RED);
            return;
        }

        LambdaVillager villager = VillagersStorage.getFromFile(id);
        if (villager.getBehaviors().contains(behavior)) {
            Messenger.send(sender, true, "Villager already has behavior '" + behavior + "'", RED);
            return;
        }

        villager.getBehaviors().add(behavior);
        VillagersStorage.modifyVillagerInFile(id, villager);
        Messenger.send(sender, true, "Behavior '" + behavior + "' added to villager '" + id + "'", GREEN);

        if (!BehaviorsStorage.existsInFile(behavior) && UserConfig.getDoTips() && sender.hasPermission(Hardcode.PERMISSION_TIPS)) {
            Messenger.send(sender, false, "Note that there is no '" + behavior + "' behavior set", GOLD);
        }
    }

    private void villagerForgetBehavior(CommandSender sender, String[] args) throws IOException {
        if (args.length != 4) {
            Messenger.send(sender, true, "Usage: /myvillage villager forget [VILLAGER ID] [BEHAVIOR ID]", RED);
            return;
        }
        String id = args[2];
        String behavior = args[3].toLowerCase();

        if (!VillagersStorage.existsInFile(id)) {
            Messenger.send(sender, true, "Villager '" + id + "' does not exist", RED);
            return;
        }

        LambdaVillager villager = VillagersStorage.getFromFile(id);
        if (!villager.getBehaviors().contains(behavior)) {
            Messenger.send(sender, true, "Villager does not have behavior '" + behavior + "'", RED);
            return;
        }
        villager.getBehaviors().remove(behavior);
        VillagersStorage.modifyVillagerInFile(id, villager);
        Messenger.send(sender, true, "Behavior '" + behavior + "' removed from villager '" + id + "'", GREEN);
    }

    private void villagerRemove(CommandSender sender, String[] args) throws IOException {
        if (args.length != 3) {
            Messenger.send(sender, true, "Usage: /myvillage villager remove [VILLAGER ID]", RED);
            return;
        }

        String id = args[2];

        if (!VillagersStorage.existsInFile(id)) {
            Messenger.send(sender, true, "Villager '" + id + "' does not exist", RED);
            return;
        }

        VillagersStorage.removeVillagerFromFile(id);
        Spawninator.despawn(id);
        Messenger.send(sender, true, "Villager '" + id + "' removed successfully", GREEN);
    }

    private void villagerList(CommandSender sender) throws IOException {
        //todo: args check

        List<LambdaVillager> allVillagers = VillagersStorage.loadFromFile();

        if (allVillagers.isEmpty()) {
            Messenger.send(sender, true, "No villagers found.", GOLD);
            return;
        }

        Messenger.send(sender, false, "Villagers list:", GOLD);
        allVillagers.forEach(v -> Messenger.send(sender, false, "- " + v.getId(), WHITE));
    }

    private void villagerInfo(CommandSender sender, String[] args) throws IOException {
        if (args.length != 3) {
            Messenger.send(sender, true, "Usage: /myvillage villager info [VILLAGER ID]", RED);
            return;
        }

        String id = args[2];

        if (!VillagersStorage.existsInFile(id)) {
            Messenger.send(sender, true, "Villager '" + id + "' does not exist", RED);
            return;
        }

        LambdaVillager villager = VillagersStorage.getFromFile(id);

        Messenger.send(sender, true, "Information for villager '" + id + "':", GOLD);
        Messenger.send(sender, false, "Type: " + villager.getType(), WHITE);
        Messenger.send(sender, false, "Profession: " + villager.getProfession(), WHITE);
        Messenger.send(sender, false, "Level: " + villager.getLevel(), WHITE);
        Messenger.send(sender, false, "Silent: " + villager.getSilent(), WHITE);
        Messenger.send(sender, false, "Display Visible: " + villager.getDisplayVisible(), WHITE);
        Messenger.send(sender, false, "Display Name: '" + villager.getDisplayName()+"'", WHITE);
        Messenger.send(sender, false, "Location: " + villager.getLocation().toString(), WHITE);
        Messenger.send(sender, false, "Behaviors: " + villager.getBehaviors(), WHITE);


    }

    private void handleBehaviorCommand(CommandSender sender, String[] args) throws IOException {
        if (args.length < 2) {
            Messenger.send(sender, true, "Usage: /myvillage behavior <action> [...]", RED);
            return;
        }

        String action = args[1].toLowerCase();

        switch (action) {
            case "create" -> behaviorCreate(sender, args);
            case "remove" -> behaviorRemove(sender, args);
            case "list" -> behaviorList(sender);
            case "info" -> behaviorInfo(sender, args);
            default -> Messenger.send(sender, true, "Unknown behavior action: " + action, RED);
        }
    }

    private void behaviorCreate(CommandSender sender, String[] args) throws IOException {
        if (args.length < 5) {
            Messenger.send(sender, true, "Usage: /myvillage behavior create [BEHAVIOR ID] [TYPE] [INSTRUCTION]", RED, "Available types:\n" + Helpers.BehaviorTypes.dashList + "\n\nInstruction placeholders:\n" + Helpers.InstructionPlaceholders.dashList);
            return;
        }

        String id = args[2];
        String typeStr = args[3].toUpperCase();

        if (BehaviorsStorage.existsInFile(id)) {
            Messenger.send(sender, true, "Behavior '" + id + "' already exists", RED);
            return;
        }

        LambdaBehaviorType type;
        try {
            type = LambdaBehaviorType.valueOf(typeStr);
        } catch (IllegalArgumentException e) {
            Messenger.send(sender, true, "Invalid behavior type '" + typeStr + "'.", RED, "Available types:\n" + Helpers.BehaviorTypes.dashList);
            return;
        }

        String instruction = String.join(" ", Arrays.copyOfRange(args, 4, args.length));
        LambdaBehavior behavior = new LambdaBehavior(id, type, instruction);
        BehaviorsStorage.addBehaviorToFile(behavior);
        Messenger.send(sender, true, "Behavior '" + id + "' added successfully", GREEN);

    }

    private void behaviorRemove(CommandSender sender, String[] args) throws IOException {
        if (args.length != 3) {
            Messenger.send(sender, true, "Usage: /myvillage behavior remove [BEHAVIOR ID]", RED);
            return;
        }

        String id = args[2];
        if (!BehaviorsStorage.existsInFile(id)) {
            Messenger.send(sender, true, "Behavior '" + id + "' does not exist", RED);
            return;
        }

        BehaviorsStorage.removeBehaviorFromFile(id);
        Messenger.send(sender, true, "Behavior '" + id + "' removed successfully", GREEN);
    }

    private void behaviorList(CommandSender sender) throws IOException {
        //todo: args check

        List<LambdaBehavior> allBehaviors = BehaviorsStorage.loadFromFile();

        if (allBehaviors.isEmpty()) {
            Messenger.send(sender, true, "No behaviors found", GOLD);
            return;
        }

        Messenger.send(sender, false, "Behaviors list:", GOLD);
        allBehaviors.forEach(b -> Messenger.send(sender, false, "- " + b.getId(), WHITE));
    }

    private void behaviorInfo(CommandSender sender, String[] args) throws IOException {
        if (args.length != 3) {
            Messenger.send(sender, true, "Usage: /myvillage behavior info [BEHAVIOR ID]", RED);
            return;
        }

        String id = args[2];

        if (!BehaviorsStorage.existsInFile(id)) {
            Messenger.send(sender, true, "Behavior '" + id + "' does not exist", RED);
            return;
        }

        LambdaBehavior behavior = BehaviorsStorage.getFromFile(id);

        Messenger.send(sender, true, "Information for behavior '" + id + "':", GOLD);
        Messenger.send(sender, false, "Type: " + behavior.getType(), WHITE);
        Messenger.send(sender, false, "Instruction: '" + behavior.getInstruction()+"'", WHITE);

    }
}
