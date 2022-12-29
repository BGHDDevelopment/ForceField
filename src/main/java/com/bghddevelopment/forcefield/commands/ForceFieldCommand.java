package com.bghddevelopment.forcefield.commands;

import com.bghddevelopment.forcefield.ForceFieldMain;
import com.bghddevelopment.forcefield.utilities.Common;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public final class ForceFieldCommand implements TabExecutor {

    private final ForceFieldMain plugin;

    public ForceFieldCommand(ForceFieldMain plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;

            if (player.hasPermission(getPlugin().PERMISSION_USE)) {
                if (!getPlugin().FORCE_FIELDS.contains(player)) {
                    getPlugin().FORCE_FIELDS.add(player);

                    Common.tell(player, getPlugin().TOGGLE_ON);
                } else {
                    getPlugin().FORCE_FIELDS.remove(player);

                    Common.tell(player, getPlugin().TOGGLE_OFF);
                }
            } else {
                Common.tell(player, getPlugin().NO_PERMISSION);
            }
        } else {
            Common.tell(sender, "Only players can execute this command ...");
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
