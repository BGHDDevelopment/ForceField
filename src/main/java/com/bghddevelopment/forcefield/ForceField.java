package com.bghddevelopment.forcefield;

import java.util.HashSet;

import com.bghddevelopment.forcefield.commands.ForceFieldCommand;
import com.bghddevelopment.forcefield.commands.ReloadCommand;
import com.bghddevelopment.forcefield.listeners.JoinEvent;
import com.bghddevelopment.forcefield.utilities.MetricsLite;
import com.bghddevelopment.forcefield.utilities.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public final class ForceField extends JavaPlugin implements Runnable {

    private final ForceField plugin;

	public String PERMISSION_IGNORE = getConfig().getString("IgnorePermisison");
	public String PERMISSION_USE = getConfig().getString("UsePermission");
	public String PERMISSION_RELOAD = getConfig().getString("ReloadPermission");
	public String CONFIG_RELOADED = getConfig().getString("ConfigReloaded");
	public int RANGE = getConfig().getInt("Range");
	public String NO_PERMISSION = getConfig().getString("NoPermMessage");
	public String TOGGLE_ON = getConfig().getString("Enabled");
	public String TOGGLE_OFF = getConfig().getString("Disabled");
	public boolean ENABLE_SOUND = getConfig().getBoolean("EnableSound");
	public String SOUND = getConfig().getString("Sound");
	public double VOLUME = getConfig().getInt("Volume");
	public double PITCH = getConfig().getInt("Pitch");

	public HashSet<Player> FORCE_FIELDS = new HashSet<>();

	public ForceField() {
		this.plugin = this;
	}

	public void onEnable() {
		this.loadConfig();

		getServer().getScheduler().scheduleSyncRepeatingTask(this, this, 1, 5);

		MetricsLite metrics = new MetricsLite(this);

		if (getConfig().getBoolean("CheckForUpdates.Enabled", true)) {
			new UpdateChecker(this, 25228).getLatestVersion(version -> {
				getPlugin().getLogger().info("Checking for Updates ... ");

				if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
					getLogger().info("No new version available");
				} else {
					getLogger().warning(String.format("Newest version: %s is out! You are running version: %s", version, getPlugin().getDescription().getVersion()));
					getLogger().warning("Please Update Here: https://www.spigotmc.org/resources/25228");
				}
			});
		}

		Bukkit.getPluginManager().registerEvents(new JoinEvent(this), this);

		getCommand("forcefield").setExecutor(new ForceFieldCommand(this));
		getCommand("forcefieldreload").setExecutor(new ReloadCommand(this));

	}

	@Override
	public void run() {
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			if (FORCE_FIELDS.contains(player)) {
				for (Player other : Bukkit.getServer().getOnlinePlayers()) {
					if (player.equals(other))
						continue;
					if (offset(other, player) > RANGE)
						continue;
					if(other.getGameMode() == GameMode.SPECTATOR)
						return;
					if(other.hasPermission(PERMISSION_IGNORE))
						return;
					Entity bottom = other;
					while (bottom.getVehicle() != null)
						bottom = bottom.getVehicle();
					velocity(bottom, getTrajectory2d(player, bottom), 1.6, true, 0.8, 0, 10);
					if(!ENABLE_SOUND)
						return;
					other.getWorld().playSound(other.getLocation(), Sound.valueOf(SOUND), (float) VOLUME, (float) PITCH);
					//other.getWorld().playSound(other.getLocation(), Sound.w, 2f, 0.5f);
				}
			}
		}
	}

	public double offset(Entity a, Entity b) {
		return a.getLocation().toVector().subtract(b.getLocation().toVector()).length();
	}

	public Vector getTrajectory2d(Entity from, Entity to){
		return to.getLocation().toVector().subtract(from.getLocation().toVector()).setY(0).normalize();
	}

	public void velocity(Entity ent, Vector vec, double str, boolean ySet, double yBase, double yAdd, double yMax) {
		if (Double.isNaN(vec.getX()) || Double.isNaN(vec.getY()) || Double.isNaN(vec.getZ()) || vec.length() == 0)
			return;
		if (ySet)
			vec.setY(yBase);
		vec.normalize();
		vec.multiply(str);
		vec.setY(vec.getY() + yAdd);
		if (vec.getY() > yMax)
			vec.setY(yMax);
		ent.setFallDistance(0);
		ent.setVelocity(vec);	
	}

	public void clear() {
		PERMISSION_IGNORE = getConfig().getString("IgnorePermisison");
		PERMISSION_USE = getConfig().getString("UsePermission");
		PERMISSION_RELOAD = getConfig().getString("ReloadPermission");
		CONFIG_RELOADED = getConfig().getString("ConfigReloaded");
		RANGE = getConfig().getInt("Range");
		NO_PERMISSION = getConfig().getString("NoPermMessage");
		TOGGLE_ON = getConfig().getString("Enabled");
		TOGGLE_OFF = getConfig().getString("Disabled");
		ENABLE_SOUND = getConfig().getBoolean("EnableSound");
		SOUND = getConfig().getString("Sound");
		VOLUME = getConfig().getInt("Volume");
		PITCH = getConfig().getInt("Pitch");
	}

	public final ForceField getPlugin() {
        return plugin;
    }

    private void loadConfig() {
		saveDefaultConfig();
		getConfig().options().copyDefaults(true);
	}



}
