package me.artur_gamez.forcefield.commands;

import me.artur_gamez.forcefield.ForceFieldMain;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public final class ForceFieldCommand implements TabExecutor {

    private ForceFieldMain plugin;

    public ForceFieldCommand(ForceFieldMain plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (sender.hasPermission(getPlugin().PERMISSION_USE)) {
                if (!getPlugin().e.contains(sender)) {
                    getPlugin().e.add((Player) sender);
                    sender.sendMessage(getPlugin().TOGGLE_ON.replace("&", "�"));
                } else {
                    getPlugin().e.remove(sender);
                    sender.sendMessage(getPlugin().TOGGLE_OFF.replace("&", "�"));
                }
            } else {
                sender.sendMessage(getPlugin().NO_PERMISSION.replace("&", "�"));
            }
        } else {
            sender.sendMessage("Player only");
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
