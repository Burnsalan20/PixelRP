package com.pixelrp.core.mechanic.harvestMechanic.utils;

import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.text.Text;

import java.io.Serializable;

public class Resource implements Serializable {

    private String itemType;
    private int respawn;

    private String name, desc;

    public Resource(String itemType, int respawn, String name, String desc){
        this.itemType = itemType;
        this.respawn = respawn;
        this.name = name;
        this.desc = desc;
    }

    public String getItemType() {
        return itemType;
    }

    public int getRespawn() {
        return respawn;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }
}
