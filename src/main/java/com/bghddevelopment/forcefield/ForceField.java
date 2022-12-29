package com.bghddevelopment.forcefield;

import co.aikar.commands.BukkitCommandIssuer;
import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.ConditionFailedException;
import com.bghddevelopment.forcefield.commands.ForceFieldCommand;
import com.bghddevelopment.forcefield.commands.ReloadCommand;
import com.bghddevelopment.forcefield.utilities.Color;
import com.bghddevelopment.forcefield.utilities.Messages;
import com.bghddevelopment.forcefield.utilities.MetricsLite;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;

import static com.bghddevelopment.forcefield.utilities.Messages.*;

public final class ForceField extends JavaPlugin implements Runnable {

    private final ForceField plugin;

    public HashSet<Player> FORCE_FIELDS = new HashSet<>();

    public ForceField() {
        this.plugin = this;
    }

    public void onEnable() {
        this.loadConfig();

        getServer().getScheduler().scheduleSyncRepeatingTask(this, this, 1, 5);

        MetricsLite metrics = new MetricsLite(this);

        updateCheck(Bukkit.getConsoleSender(), true);

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
                    if (other.getGameMode() == GameMode.SPECTATOR)
                        return;
                    if (other.hasPermission("ff.ignore"))
                        return;
                    Entity bottom = other;
                    while (bottom.getVehicle() != null)
                        bottom = bottom.getVehicle();
                    velocity(bottom, getTrajectory2d(player, bottom), 1.6, true, 0.8, 0, 10);
                    if (!ENABLE_SOUND)
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

    public Vector getTrajectory2d(Entity from, Entity to) {
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

    public final ForceField getPlugin() {
        return plugin;
    }

    private void loadConfig() {
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        Messages.loadConfig(this);
    }

    private void loadCommands() {
        BukkitCommandManager manager = new BukkitCommandManager(this);
        manager.getCommandConditions().addCondition("noconsole", (context) -> {
            BukkitCommandIssuer issuer = context.getIssuer();
            if (!issuer.isPlayer()) {
                throw new ConditionFailedException("Console cannot use this command.");
            }
        });
        manager.registerCommand(new ForceFieldCommand());
        manager.registerCommand(new ReloadCommand());
    }

    public void updateCheck(CommandSender sender, boolean console) {
        try {
            String urlString = "https://updatecheck.bghddevelopment.com";
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String input;
            StringBuffer response = new StringBuffer();
            while ((input = reader.readLine()) != null) {
                response.append(input);
            }
            reader.close();
            JsonObject object = new JsonParser().parse(response.toString()).getAsJsonObject();

            if (object.has("plugins")) {
                JsonObject plugins = object.get("plugins").getAsJsonObject();
                JsonObject info = plugins.get("ForceField").getAsJsonObject();
                String version = info.get("version").getAsString();
                Boolean archived = info.get("archived").getAsBoolean();
                if (archived) {
                    sender.sendMessage(Color.translate(""));
                    sender.sendMessage(Color.translate(""));
                    sender.sendMessage(Color.translate("&cThis plugin has been marked as 'Archived' by BGHDDevelopment LLC."));
                    sender.sendMessage(Color.translate("&cThis version will continue to work but will not receive updates or support."));
                    sender.sendMessage(Color.translate(""));
                    sender.sendMessage(Color.translate(""));
                    return;
                }
                if (version.equals(getDescription().getVersion())) {
                    if (console) {
                        sender.sendMessage(Color.translate("&aForceField is on the latest version."));
                    }
                } else {
                    sender.sendMessage(Color.translate(""));
                    sender.sendMessage(Color.translate(""));
                    sender.sendMessage(Color.translate("&cYour ForceField version is out of date!"));
                    sender.sendMessage(Color.translate("&cWe recommend updating ASAP!"));
                    sender.sendMessage(Color.translate(""));
                    sender.sendMessage(Color.translate("&cYour Version: &e" + getDescription().getVersion()));
                    sender.sendMessage(Color.translate("&aNewest Version: &e" + version));
                    sender.sendMessage(Color.translate(""));
                    sender.sendMessage(Color.translate(""));
                }
            } else {
                sender.sendMessage(Color.translate("&cWrong response from update API, contact plugin developer!"));
            }
        } catch (
                Exception ex) {
            sender.sendMessage(Color.translate("&cFailed to get updater check. (" + ex.getMessage() + ")"));
        }
    }

}
