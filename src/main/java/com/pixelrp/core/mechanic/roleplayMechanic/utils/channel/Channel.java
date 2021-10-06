package com.pixelrp.core.mechanic.roleplayMechanic.utils.channel;

public class Channel {

    private String name;
    private String prefix_Color;
    private String chat_Color;
    private int range;
    private boolean rp;

    public Channel(String name, String prefix_Color, String chat_Color, int range, boolean rp){
        this.name = name;
        this.prefix_Color = prefix_Color;
        this.chat_Color = chat_Color;
        this.range = range;
        this.rp = rp;
    }

    public int getRange(){
        return range;
    }

}
