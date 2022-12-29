package com.bghddevelopment.forcefield.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.bghddevelopment.forcefield.ForceField;
import com.bghddevelopment.forcefield.utilities.Common;
import com.bghddevelopment.forcefield.utilities.Messages;
import org.bukkit.command.CommandSender;

@CommandAlias("forcefieldreload|ffrl")
@Description("Reload your forcefield.")
@CommandPermission("ff.reload")
@Conditions("noconsole")
public final class ReloadCommand extends BaseCommand {

    @Dependency
    private ForceField plugin;

    @Default
    public void onDefault(CommandSender sender, String[] args) {
        plugin.reloadConfig();
        Messages.loadConfig(plugin);

        Common.tell(sender, Messages.CONFIG_RELOADED);

        return;
    }

}