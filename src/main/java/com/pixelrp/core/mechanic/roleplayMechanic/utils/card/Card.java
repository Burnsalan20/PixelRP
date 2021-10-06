package com.pixelrp.core.mechanic.roleplayMechanic.utils.card;

import com.pixelrp.core.PixelRP;
import com.pixelrp.core.utils.user.User;
import org.spongepowered.api.Sponge;

import java.io.Serializable;
import java.util.UUID;

public class Card implements Serializable {

    private String ownerUUID;
    private String dateCreated;

    private String name;
    private int age;
    private String desc;

    public Card(String ownerUUID, String dateCreated, String name, int age, String desc){
        this.ownerUUID = ownerUUID;
        this.dateCreated = dateCreated;

        this.name = name;
        this.age = age;
        this.desc = desc;
    }

    public String getOwnerUUID(){ return  ownerUUID; }

    public String getDateCreated() { return dateCreated; }

    //Set/Get Name
    public void setName(String value){
        this.name = value;
    }
    public String getName(){ return name; }

    //Set/Get Age
    public void setAge(int value){
        this.age = value;
    }
    public int getAge(){ return age; }

    //Set/Get Description
    public void setDescription(String value){
        this.desc = value;
    }
    public String getDescription(){ return desc; }
}
