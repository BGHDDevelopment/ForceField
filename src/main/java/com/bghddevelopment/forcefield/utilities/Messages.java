package com.bghddevelopment.forcefield.utilities;

import com.bghddevelopment.forcefield.ForceField;

public class Messages {

    public static String CONFIG_RELOADED;
    public static int RANGE;
    public static String TOGGLE_ON;
    public static String TOGGLE_OFF;
    public static boolean ENABLE_SOUND;
    public static String SOUND;
    public static double VOLUME;
    public static double PITCH;

    public static void loadConfig(ForceField plugin) {
        CONFIG_RELOADED = plugin.getConfig().getString("ConfigReloaded");
        RANGE = plugin.getConfig().getInt("Range");
        TOGGLE_ON = plugin.getConfig().getString("Enabled");
        TOGGLE_OFF = plugin.getConfig().getString("Disabled");
        ENABLE_SOUND = plugin.getConfig().getBoolean("EnableSound");
        SOUND = plugin.getConfig().getString("Sound");
        VOLUME = plugin.getConfig().getInt("Volume");
        PITCH = plugin.getConfig().getInt("Pitch");
    }

}
