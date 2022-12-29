package com.bghddevelopment.forcefield.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.bghddevelopment.forcefield.ForceField;
import com.bghddevelopment.forcefield.utilities.Common;
import com.bghddevelopment.forcefield.utilities.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

@CommandAlias("forcefield|ff")
@Description("Toggle your forcefield on or off.")
@CommandPermission("ff.use")
@Conditions("noconsole")
public final class ForceFieldCommand extends BaseCommand {

    @Dependency
    private ForceField plugin;

    @Default
    public void onDefault(CommandSender sender, String[] args) {
            final Player player = (Player) sender;
            
                if (!plugin.FORCE_FIELDS.contains(player)) {
                    plugin.FORCE_FIELDS.add(player);

                    Common.tell(player, Messages.TOGGLE_ON);
                } else {
                    plugin.FORCE_FIELDS.remove(player);

                    Common.tell(player, Messages.TOGGLE_OFF);
                }
                return;
            }

}
