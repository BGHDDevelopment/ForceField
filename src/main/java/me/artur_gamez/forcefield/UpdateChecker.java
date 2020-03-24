package me.artur_gamez.forcefield;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.function.Consumer;

public final class UpdateChecker {
    private String URL = "https://api.spigotmc.org/legacy/update.php?resource=";

    private Plugin plugin;
    private int resourceID;

    public UpdateChecker(final Plugin plugin, final int resourceID) {
        this.plugin = plugin;
        this.resourceID = resourceID;
    }

    public void getLatestVersion(final Consumer<String> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), () -> {
           try (InputStream inputStream = new URL(getURL(getResourceID())).openStream(); Scanner scanner = new Scanner(inputStream)) {
               if (scanner.hasNext()) {
                   consumer.accept(scanner.next());
               }
           } catch (IOException exception) {
               getPlugin().getLogger().warning("Cannot look for updates: " + exception.getMessage());
           }
        });
    }

    private String getURL(final int resourceID) {
        return URL + resourceID;
    }

    private int getResourceID() {
        return resourceID;
    }

    private Plugin getPlugin() {
        return plugin;
    }

}
