package me.artur_gamez.forcefield;

import java.io.File;
import java.util.HashSet;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class ForceFieldMain extends JavaPlugin implements Listener, Runnable{

    public static ForceFieldMain plugin;
    private UpdateChecker checker;

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
        ForceFieldMain.plugin = this;
        PluginDescriptionFile VarUtilType = this.getDescription();
        this.getLogger().info("ForceField V " + VarUtilType.getVersion() + " starting...");
		Bukkit.getPluginManager().registerEvents(this, this);
		getServer().getScheduler().scheduleSyncRepeatingTask(this, this, 1, 5);
		File configFile = new File(getDataFolder(), "config.yml");
		if (!configFile.exists()) {
			saveDefaultConfig();
	        this.setEnabled(true);
			MetricsLite metrics = new MetricsLite(this);
			this.getLogger().info("ForceField V " + VarUtilType.getVersion() + " checking for updates...");
	        this.checker = new UpdateChecker(this);
	        if (this.checker.isConnected()) {
	            if (this.checker.hasUpdate()) {
	            	getServer().getConsoleSender().sendMessage("------------------------");
	            	getServer().getConsoleSender().sendMessage("ForceField is outdated!");
	            	getServer().getConsoleSender().sendMessage("Newest version: " + this.checker.getLatestVersion());
	            	getServer().getConsoleSender().sendMessage("Your version: " + ForceFieldMain.plugin.getDescription().getVersion());
	            	getServer().getConsoleSender().sendMessage("Please Update Here: https://www.spigotmc.org/resources/25228");
	                getServer().getConsoleSender().sendMessage("------------------------");
	            }
	            else {
	            	getServer().getConsoleSender().sendMessage("------------------------");
	            	getServer().getConsoleSender().sendMessage("ForceField is up to date!");
	            	getServer().getConsoleSender().sendMessage("------------------------");
	           }
	        }
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("forcefield")) {
			if (sender instanceof Player) {
				if (sender.hasPermission(permUse)) {
					if (!e.contains(sender)) {
						e.add((Player) sender);
						sender.sendMessage(on.replace("&", "�"));
					} else {
						e.remove(sender);
						sender.sendMessage(off.replace("&", "�"));
					}
				} else {
					sender.sendMessage(noPerm.replace("&", "�"));
				}
			} else {
				sender.sendMessage("Player only");
			}
		}

		if (cmd.getName().equalsIgnoreCase("forcefieldreload")) {
			if (sender.hasPermission(permReload)) {
				clear();
				reloadConfig();
				clear();
				sender.sendMessage(reloaded.replace("&", "�"));
			} else {
				sender.sendMessage(noPerm.replace("&", "�"));
			}
		}

		return false;
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

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static ForceFieldMain getPlugin() {
        return (ForceFieldMain)getPlugin((Class)ForceFieldMain.class);
    }

    public static Plugin getPlugin2() {
        return ForceFieldMain.plugin;
    }

    public static void registerEvents(final Plugin plugin, final Listener... listeners) {
        for (final Listener listener : listeners) {
            Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }

}
