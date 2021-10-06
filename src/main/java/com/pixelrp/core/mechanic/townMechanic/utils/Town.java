package com.pixelrp.core.mechanic.townMechanic.utils;

import com.pixelrp.core.utils.user.User;
import org.spongepowered.api.world.Location;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Town implements Serializable {

    private String name;
    private String desc;

    private String foundedDate;
    private String lastActive;

    private double x1, y1, z1;
    private double x2, y2, z2;

    private double temp_x1, temp_y1, temp_z1;
    private double temp_x2, temp_y2, temp_z2;

    private Map<String, Citizen> citizens = new HashMap<>();

    public Town(String name, Location l1, Location l2){
        this.name = name;

        this.x1 = l1.getX();
        this.y1 = l1.getY();
        this.z1 = l1.getZ();

        this.x2 = l2.getX();
        this.y2 = l2.getY();
        this.z2 = l2.getZ();
    }

    public Map<String, Citizen> getCitizens(){ return citizens; }

    public void setLoc1(Location loc){
        this.x1 = loc.getX();
        this.y1 = loc.getY();
        this.z1 = loc.getZ();
    }

    public double[] getLoc1(){
        return new double[] {x1, y1, z1};
    }

    public void setLoc2(Location loc){
        this.x2 = loc.getX();
        this.y2 = loc.getY();
        this.z2 = loc.getZ();
    }

    public double[] getLoc2(){
        return new double[] {x2, y2, z2};
    }

    public void clearLocation(){
        this.temp_x1 = this.x1;
        this.temp_y1 = this.y1;
        this.temp_z1 = this.z1;

        this.temp_x2 = this.x2;
        this.temp_y2 = this.y2;
        this.temp_z2 = this.z2;

        this.x1 = 0;
        this.y1 = 0;
        this.z1 = 0;
        this.x2 = 0;
        this.y2 = 0;
        this.z2 = 0;
    }

    public void restoreLocation(){
        this.x1 = temp_x1;
        this.y1 = temp_y1;
        this.z1 = temp_z1;
        this.x2 = temp_x2;
        this.y2 = temp_y2;
        this.z2 = temp_z2;

        this.temp_x1 = 0;
        this.temp_y1 = 0;
        this.temp_z1 = 0;
        this.temp_x2 = 0;
        this.temp_y2 = 0;
        this.temp_z2 = 0;
    }

    public void setDateCreated(String value){
        this.foundedDate = value;
    }

    public String getDateCreated(){
        return foundedDate;
    }

    public void setLastActive(String value){
        this.lastActive = value;
    }

    public String getLastActive(){
        return lastActive;
    }

    public void setName(String value){
        this.name = value;
    }

    public String getName(){
        return name;
    }

    public void setDescription(String value){
        this.desc = value;
    }

    public String getDescription(){
        return desc;
    }
}
