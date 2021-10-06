package com.pixelrp.core.mechanic.arenaMechanic.utils;

import java.util.HashMap;
import java.util.Map;

public class Arena {

    public Map<String, ArenaLocation> locations = new HashMap<>();
    public String name = "", description = "";

    public Arena(String name){
        initializeLocations();
    }

    private void initializeLocations(){
        locations.put("corner1", null);
        locations.put("corner2", null);
        locations.put("entrance", null);
        locations.put("exit", null);
        locations.put("spectator", null);
        locations.put("trainer1", null);
        locations.put("trainer2", null);
        locations.put("pokemon1", null);
        locations.put("pokemon2", null);
    }

}
