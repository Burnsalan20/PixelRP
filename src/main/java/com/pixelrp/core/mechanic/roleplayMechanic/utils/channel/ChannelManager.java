package com.pixelrp.core.mechanic.roleplayMechanic.utils.channel;

import org.checkerframework.checker.units.qual.C;
import org.spongepowered.api.entity.living.player.Player;

public class ChannelManager {

    public static Channel OOC = new Channel("OOC", "GREEN", "WHITE", -1, false);
    public static Channel LOOC = new Channel("LOOC", "DARK_GREEN", "GRAY", 15, false);

    public static Channel RP = new Channel("", "", "YELLOW", 15, true);

    public ChannelManager(){

    }

    public static String sendOOCFormat(Player sender, String message){
        return "";
    }

    public static String sendLOOCFormat(Player sender, String message){
        return "";
    }

    public static String sendRPFormat(Player sender, String message){
        return "";
    }
}


