package com.bghddevelopment.forcefield.utilities;

import co.aikar.commands.annotation.Dependency;
import com.bghddevelopment.forcefield.ForceField;

public class Messages {

    @Dependency
    private ForceField plugin;

    public String PERMISSION_IGNORE = plugin.getConfig().getString("IgnorePermisison");
    public String PERMISSION_USE = plugin.getConfig().getString("UsePermission");
    public String PERMISSION_RELOAD = plugin.getConfig().getString("ReloadPermission");
    public String CONFIG_RELOADED = plugin.getConfig().getString("ConfigReloaded");
    public int RANGE = plugin.getConfig().getInt("Range");
    public String NO_PERMISSION = plugin.getConfig().getString("NoPermMessage");
    public String TOGGLE_ON = plugin.getConfig().getString("Enabled");
    public String TOGGLE_OFF = plugin.getConfig().getString("Disabled");
    public boolean ENABLE_SOUND = plugin.getConfig().getBoolean("EnableSound");
    public String SOUND = plugin.getConfig().getString("Sound");
    public double VOLUME = plugin.getConfig().getInt("Volume");
    public double PITCH = plugin.getConfig().getInt("Pitch");

}
