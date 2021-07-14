package com.pixelrp.core.mechanic;

import com.pixelrp.core.PixelRP;
import com.pixelrp.core.mechanic.chatMechanic.ChatMechanic;
import com.pixelrp.core.mechanic.protectionMechanic.ProtectionMechanic;
import com.pixelrp.core.mechanic.safariMechanic.SafariMechanic;

import java.util.ArrayList;
import java.util.List;

public class MechanicManager {
    private PixelRP plugin = PixelRP.instance;

    private List<Mechanic> mechanics = new ArrayList<Mechanic>();

    public MechanicManager(){
        loadMechanics();
        initializeMechanics();
    }

    public void loadMechanics(){
        mechanics.add(new ChatMechanic("ChatMechanic"));
        mechanics.add(new ProtectionMechanic("ProtectionMechanic"));
        mechanics.add(new SafariMechanic("SafariMechanic"));
    }

    public void initializeMechanics(){
        if(mechanics.size() == 0) return;
        for(Mechanic mechanic : mechanics){
            if(mechanic.isEnabled()){
                System.out.println("[PixelRP-Core] " + mechanic.id + " initializing....");
                try{
                    mechanic.initialize();
                } catch (Exception e){
                    System.out.println("[PixelRP-Core] " + mechanic.id + " could not be initialized. Skipping....");
                }
            }
        }
    }
}
