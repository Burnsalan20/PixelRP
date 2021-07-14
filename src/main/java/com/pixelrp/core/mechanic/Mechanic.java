package com.pixelrp.core.mechanic;

import com.pixelrp.core.PixelRP;

public abstract class Mechanic {
    private PixelRP plugin = PixelRP.instance;

    protected String id;
    protected boolean enabled;

    private Status status = Status.ENABLED;

    public Mechanic (String id){
        this.id = id;
        if(plugin.config.get().getNode("Mechanics", id, "enabled").isVirtual()) {
            enabled = true;
            plugin.config.get().getNode("Mechanics", id, "enabled").setValue(true);
            plugin.config.save();
        } else this.enabled = plugin.config.get().getNode("Mechanics", id, "enabled").getBoolean();
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
}

enum Status{
    ENABLED,
    DISABLED,
    ERROR
}
