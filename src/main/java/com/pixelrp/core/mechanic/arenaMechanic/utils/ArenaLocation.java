package com.pixelrp.core.mechanic.arenaMechanic.utils;

import java.io.Serializable;

public class ArenaLocation implements Serializable {

    public int x, y, z;
    public double pitch, yaw;
    public ArenaLocation(int x, int y, int z, double pitch, double yaw){
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
    }

}
