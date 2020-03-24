package me.artur_gamez.forcefield.commands;

import me.artur_gamez.forcefield.ForceFieldMain;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.Collections;
import java.util.List;

public final class ReloadCommand implements TabExecutor {

    private ForceFieldMain plugin;

    public ReloadCommand(ForceFieldMain plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission(getPlugin().PERMISSION_RELOAD)) {
            getPlugin().clear();
            getPlugin().reloadConfig();
            getPlugin().clear();
            sender.sendMessage(getPlugin().CONFIG_RELOADED.replace("&", "�"));
        } else {
            sender.sendMessage(getPlugin().NO_PERMISSION.replace("&", "�"));
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Collections.emptyList();
    }

    private ForceFieldMain getPlugin() {
        return plugin;
    }

}