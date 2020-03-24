package me.artur_gamez.forcefield;

import java.io.File;
import java.util.HashSet;

import me.artur_gamez.forcefield.commands.ForceFieldCommand;
import me.artur_gamez.forcefield.commands.ReloadCommand;
import me.artur_gamez.forcefield.listeners.JoinEvent;
import me.artur_gamez.forcefield.utilities.MetricsLite;
import me.artur_gamez.forcefield.utilities.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public final class ForceFieldMain extends JavaPlugin implements Runnable {

    private ForceFieldMain plugin;

	public String permIgnore = getConfig().getString("IgnorePermisison");
	public String permUse = getConfig().getString("UsePermission");
	public String permReload = getConfig().getString("ReloadPermission");
	public String reloaded = getConfig().getString("ConfigReloaded");
	public int range = getConfig().getInt("Range");
	public String noPerm = getConfig().getString("NoPermMessage");
	public String on = getConfig().getString("Enabled");
	public String off = getConfig().getString("Disabled");
	public boolean soundoo = getConfig().getBoolean("EnableSound");
	public String sound = getConfig().getString("Sound");
	public double volume = getConfig().getInt("Volume");
	public double pitch = getConfig().getInt("Pitch");

	public HashSet<Player> e = new HashSet<Player>();

	public void onEnable() {
        this.plugin = this;

		getServer().getScheduler().scheduleSyncRepeatingTask(this, this, 1, 5);

		File configFile = new File(getDataFolder(), "config.yml");
		if (!configFile.exists()) {
			saveDefaultConfig();
		}

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
			if (e.contains(player)) {
				for (Player other : Bukkit.getServer().getOnlinePlayers()) {
					if (player.equals(other))
						continue;
					if (offset(other, player) > range)
						continue;
					if(other.getGameMode() == GameMode.SPECTATOR)
						return;
					if(other.hasPermission(permIgnore))
						return;
					Entity bottom = other;
					while (bottom.getVehicle() != null)
						bottom = bottom.getVehicle();
					velocity(bottom, getTrajectory2d(player, bottom), 1.6, true, 0.8, 0, 10);
					if(!soundoo)
						return;
					other.getWorld().playSound(other.getLocation(), Sound.valueOf(sound), (float)volume, (float)pitch);
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
		permIgnore = getConfig().getString("IgnorePermisison");
		permUse = getConfig().getString("UsePermission");
		permReload = getConfig().getString("ReloadPermission");

		reloaded = getConfig().getString("ConfigReloaded");

		range = getConfig().getInt("Range");

		noPerm = getConfig().getString("NoPermMessage");
		on = getConfig().getString("Enabled");
		off = getConfig().getString("Disabled");

		soundoo = getConfig().getBoolean("EnableSound");
		sound = getConfig().getString("Sound");
		volume = getConfig().getInt("Volume");
		pitch = getConfig().getInt("Pitch");
	}

	public ForceFieldMain getPlugin() {
        return plugin;
    }

}
