package com.pixelrp.core.utils;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Helper {

    public static boolean inRegion(Location curr, Location l1, Location l2){
        double x1 = l1.getX();
        double z1 = l1.getZ();

        double x2 = l2.getX();
        double z2 = l2.getZ();

        double xP = curr.getX();
        double zP = curr.getZ();

        if(((x1 < xP && xP < x2) || x1 > xP && xP > x2) && ((z1 < xP && xP < z2) || z1 > xP && xP > z2)){
            return true;
        } else {
            return false;
        }
    }

    public static String getCurrentTimeStamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
    }

    public static boolean isSneaking(Player player) {
        return player.get(Keys.IS_SNEAKING).orElse(false);
    }
}
