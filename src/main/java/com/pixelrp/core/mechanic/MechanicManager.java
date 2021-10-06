 package com.pixelrp.core.mechanic;

import com.pixelrp.core.PixelRP;
import com.pixelrp.core.mechanic.LoggerMechanic.LoggerMechanic;
import com.pixelrp.core.mechanic.harvestMechanic.HarvestMechanic;
import com.pixelrp.core.mechanic.pokeKillMechanic.PokeKillMechanic;
import com.pixelrp.core.mechanic.roleplayMechanic.RoleplayMechanic;
import com.pixelrp.core.mechanic.townMechanic.TownMechanic;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandManager;

import java.util.ArrayList;
import java.util.List;

public class MechanicManager {
    private PixelRP plugin = PixelRP.instance;

    private List<Mechanic> mechanics = new ArrayList<Mechanic>();

    private boolean debug = false;

    public MechanicManager(){
        loadMechanics();
        initializeMechanics();
    }

    public void loadMechanics(){
        mechanics.add(new PokeKillMechanic("PokeKillMechanic"));
        mechanics.add(new LoggerMechanic("LoggerMechanic"));
        mechanics.add(new RoleplayMechanic("RoleplayMechanic"));
        mechanics.add(new TownMechanic("TownMechanic"));
        mechanics.add(new HarvestMechanic("HarvestMechanic"));
    }

    public void initializeMechanics(){
        if(mechanics.size() == 0) return;
        for(Mechanic mechanic : mechanics){
            if(mechanic.isEnabled()){
                System.out.println("[PixelRP] " + mechanic.id + " initializing....");
                if(debug){
                    mechanic.initialize();
                } else {
                    try{
                        mechanic.initialize();
                        mechanic.setStatus(Status.ENABLED);
                    } catch (Exception e){
                        System.out.println("[PixelRP] " + mechanic.id + " could not be initialized. Skipping....");
                        mechanic.setStatus(Status.ERROR);
                    }
                }
            } else mechanic.setStatus(Status.DISABLED);
        }
    }

    public Mechanic getMechanic(int index){
        return mechanics.get(index);
    }
}
