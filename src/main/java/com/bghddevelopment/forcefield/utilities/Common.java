package com.bghddevelopment.forcefield.utilities;

import org.bukkit.command.CommandSender;

import java.util.Arrays;

public final class Common {

    public static void tell(final CommandSender sender, String... messages) {
        Arrays.stream(messages).map(Common::translate).forEach(sender::sendMessage);
    }

    public static String translate(final String message) {
        return Color.translate(message);
    }

}