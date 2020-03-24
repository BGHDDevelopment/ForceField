package me.artur_gamez.forcefield;

import org.bukkit.event.player.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.*;
import org.bukkit.event.*;

public class JoinEvent implements Listener {

    private ForceFieldMain plugin;

    public JoinEvent(ForceFieldMain plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent e) {
    	Player p = e.getPlayer();
    	if (p.hasPermission("forcefield.update")) {
    		if (getPlugin().getConfig().getBoolean("Update.Enabled") == true) {
                new UpdateChecker(getPlugin(), 25228).getLatestVersion(version -> {
                    if (!getPlugin().getDescription().getVersion().equalsIgnoreCase(version)) {
                        p.sendMessage(ChatColor.GRAY + "=========================");
                        p.sendMessage(ChatColor.RED + "ForceField is outdated!");
                        p.sendMessage(ChatColor.GREEN + "Newest version: " + version);
                        p.sendMessage(ChatColor.RED + "Your version: " + getPlugin().getDescription().getVersion());
                        p.sendMessage(ChatColor.GRAY + "=========================");
                    }
                });
           }
        }
    }

    private ForceFieldMain getPlugin() {
        return plugin;
    }

}
