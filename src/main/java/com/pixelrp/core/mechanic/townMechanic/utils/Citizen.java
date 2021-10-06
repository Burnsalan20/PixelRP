package com.pixelrp.core.mechanic.townMechanic.utils;

import java.io.Serializable;

public class Citizen implements Serializable {

    private String uuid;
    private TownRole townRole;
    private boolean canBuild;

    public Citizen(String uuid, TownRole townRole, boolean canBuild){
        this.uuid = uuid;
        this.townRole = townRole;
        this.canBuild = canBuild;
    }

    public String getUUID(){
        return uuid;
    }

    public void setTownRole(TownRole townRole){
        this.townRole = townRole;
    }

    public TownRole getTownRole(){
        return townRole;
    }

    public void setBuilder(boolean value){
        this.canBuild = value;
    }

    public boolean canBuild(){
        return canBuild;
    }
}

