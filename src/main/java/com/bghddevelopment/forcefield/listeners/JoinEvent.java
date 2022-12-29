package com.bghddevelopment.forcefield.listeners;

import com.bghddevelopment.forcefield.ForceFieldMain;
import com.bghddevelopment.forcefield.utilities.UpdateChecker;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class JoinEvent implements Listener {

    private final ForceFieldMain plugin;

    public JoinEvent(ForceFieldMain plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
    	final Player player = event.getPlayer();

    	if (player.hasPermission("forcefield.update")) {
    		if (getPlugin().getConfig().getBoolean("Update.Enabled")) {
                new UpdateChecker(getPlugin(), 25228).getLatestVersion(version -> {
                    if (!getPlugin().getDescription().getVersion().equalsIgnoreCase(version)) {
                        player.sendMessage(ChatColor.GRAY + "=========================");
                        player.sendMessage(ChatColor.RED + "ForceField is outdated!");
                        player.sendMessage(ChatColor.GREEN + "Newest version: " + version);
                        player.sendMessage(ChatColor.RED + "Your version: " + getPlugin().getDescription().getVersion());
                        player.sendMessage(ChatColor.GRAY + "=========================");
                    }
                });
           }
        }
    }

    private ForceFieldMain getPlugin() {
        return plugin;
    }

}
