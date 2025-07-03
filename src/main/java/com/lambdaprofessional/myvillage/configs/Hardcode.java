package com.lambdaprofessional.myvillage.configs;

import com.lambdaprofessional.myvillage.MyVillage;
import org.bukkit.NamespacedKey;

import java.io.File;

public class Hardcode {

    public static final String PERMISSION_HELP = "myvillage.help";
    public static final String PERMISSION_TIPS = "myvillage.tips";
    public static final String PERMISSION_MANAGE = "myvillage.manage";
    public static File BEHAVIORS_FILE = new File(MyVillage.getInstance().getDataFolder(), "behaviors_storage.json");
    public static File VILLAGERS_FILE = new File(MyVillage.getInstance().getDataFolder(), "villagers_storage.json");
    public static NamespacedKey KEY_MYVILLAGE_ID = new NamespacedKey(MyVillage.getInstance(), "myvillage_id");


}
