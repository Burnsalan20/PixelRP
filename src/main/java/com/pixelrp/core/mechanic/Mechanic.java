package com.pixelrp.core.mechanic;

import com.pixelrp.core.PixelRP;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandManager;

public abstract class Mechanic {
    protected PixelRP plugin = PixelRP.instance;
    public static Mechanic instance;
    protected String id;

    protected boolean enabled;


    private Status status = Status.ENABLED;

    public Mechanic (String id){
        this.id = id;
        instance = this;
        enabled = getConfigBoolean("enabled");
    }

    public abstract void initialize();

    public String getId() {
        return id;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setStatus(Status status){
        this.status = status;
    }

    public String getStatusCode(){
        switch (status){
            case ENABLED:
                return "Enabled";
            case DISABLED:
                return "Disabled";
            case ERROR:
                return "Error";
        }
        return "";
    }

    public Boolean getConfigBoolean(String node){
        if(plugin.config.get().getNode("Mechanics", id, node).isVirtual()){
            plugin.config.get().getNode("Mechanics", id, node).setValue(true);
            plugin.config.save();
            return true;
        } else return plugin.config.get().getNode("Mechanics", id, node).getBoolean();
    }

    public String getConfigString(String node){
        if(plugin.config.get().getNode("Mechanics", id, node).isVirtual()){
            plugin.config.get().getNode("Mechanics", id, node).setValue("");
            plugin.config.save();
            return "";
        } else return plugin.config.get().getNode("Mechanics", id, node).getString();
    }

    public int getConfigInteger(String node){
        if(plugin.config.get().getNode("Mechanics", id, node).isVirtual()){
            plugin.config.get().getNode("Mechanics", id, node).setValue(0);
            plugin.config.save();
            return 0;
        } else return plugin.config.get().getNode("Mechanics", id, node).getInt();
    }

}

enum Status{
    ENABLED,
    DISABLED,
    ERROR
}
