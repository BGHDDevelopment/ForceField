package com.bghddevelopment.forcefield.utilities;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.Arrays;

public final class Common {

    public static double offset(Entity a, Entity b) {
        return a.getLocation().toVector().subtract(b.getLocation().toVector()).length();
    }

    public static Vector getTrajectory2d(Entity from, Entity to) {
        return to.getLocation().toVector().subtract(from.getLocation().toVector()).setY(0).normalize();
    }

    public static void velocity(Entity ent, Vector vec, double str, boolean ySet, double yBase, double yAdd, double yMax) {
        if (Double.isNaN(vec.getX()) || Double.isNaN(vec.getY()) || Double.isNaN(vec.getZ()) || vec.length() == 0)
            return;
        if (ySet)
            vec.setY(yBase);
        vec.normalize();
        vec.multiply(str);
        vec.setY(vec.getY() + yAdd);
        if (vec.getY() > yMax)
            vec.setY(yMax);
        ent.setFallDistance(0);
        ent.setVelocity(vec);
    }

    public static void tell(final CommandSender sender, String... messages) {
        Arrays.stream(messages).map(Common::translate).forEach(sender::sendMessage);
    }

    public static String translate(final String message) {
        return Color.translate(message);
    }

}