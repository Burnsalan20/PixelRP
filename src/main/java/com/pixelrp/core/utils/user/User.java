package com.pixelrp.core.utils.user;

import com.pixelrp.core.PixelRP;
import com.pixelrp.core.mechanic.roleplayMechanic.utils.card.Card;
import com.pixelrp.core.mechanic.roleplayMechanic.utils.channel.Channels;

import java.io.Serializable;
import java.util.*;

public class User implements Serializable {

    private String uuid;
    private Channels channel;

    private boolean oocActive;

    private Card card;
    private List<Card> players_Cards = new ArrayList<>();

    public User(String uuid){
        this.uuid = uuid;
        oocActive = true;
        channel = Channels.OOC;
    }

    public String getUUID(){
        return uuid;
    }

    public void setChannel(String value){
        if(PixelRP.instance.mechanicManager.getMechanic(2).isEnabled()) this.channel = Channels.valueOf(value.toUpperCase());
    }
    public void setChannel(Channels channel){
        if(PixelRP.instance.mechanicManager.getMechanic(2).isEnabled()) this.channel = channel;
    }
    public Channels getChannel(){
        if(PixelRP.instance.mechanicManager.getMechanic(2).isEnabled()) return channel;
        return Channels.NULL;
    }

    public List<Card> getCards(){
        if(PixelRP.instance.mechanicManager.getMechanic(2).isEnabled()) return players_Cards;
        return new ArrayList<Card>();
    }

    public void setCard(Card card){
        if(PixelRP.instance.mechanicManager.getMechanic(2).isEnabled()) this.card = card;
    }

    public Card getCard(){
        return card;
    }

    public void setOocActive(boolean value){
        this.oocActive = value;
    }

    public boolean isOocActive(){
        return oocActive;
    }
}
