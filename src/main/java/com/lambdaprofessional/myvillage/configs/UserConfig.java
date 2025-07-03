package com.lambdaprofessional.myvillage.configs;

import com.lambdaprofessional.myvillage.storage.ConfigStorage;

public class UserConfig {

    private static final String CHECK_EVERY_TICKS_KEY = "check-every-ticks";
    private static final String DO_TIPS_KEY = "do-tips";

    public static int getCheckEveryTicks() {
        return ConfigStorage.get().getInt(CHECK_EVERY_TICKS_KEY);
    }

    public static void setCheckEveryTicks(int ticks) {
        ConfigStorage.get().set(CHECK_EVERY_TICKS_KEY, ticks);
        ConfigStorage.save();
    }

    public static boolean getDoTips() {
        return ConfigStorage.get().getBoolean(DO_TIPS_KEY);
    }

    public static void setDoTips(boolean doTips) {
        ConfigStorage.get().set(DO_TIPS_KEY, doTips);
        ConfigStorage.save();
    }

}
