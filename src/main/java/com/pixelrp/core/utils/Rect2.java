package com.pixelrp.core.utils;

import org.spongepowered.api.world.Location;

public class Rect2 {

    private Location l1, l2;

    public Rect2(){

    }

    public void setLocation1(Location loc){
        this.l1 = loc;
    }

    public Location getLocation1(){
        return l1;
    }

    public void setLocation2(Location loc){
        this.l2 = loc;
    }

    public Location getLocation2(){
        return l2;
    }
}
