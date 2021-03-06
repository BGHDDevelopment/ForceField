package me.artur_gamez.forcefield.commands;

import me.artur_gamez.forcefield.ForceFieldMain;
import me.artur_gamez.forcefield.utilities.Common;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.Collections;
import java.util.List;

public final class ReloadCommand implements TabExecutor {

    private final ForceFieldMain plugin;

    public ReloadCommand(ForceFieldMain plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission(getPlugin().PERMISSION_RELOAD)) {
            getPlugin().clear();
            getPlugin().reloadConfig();
            getPlugin().clear();

            Common.tell(sender, getPlugin().CONFIG_RELOADED);
        } else {
            Common.tell(sender, getPlugin().NO_PERMISSION);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Collections.emptyList();
    }

    private ForceFieldMain getPlugin() {
        return plugin;
    }

}