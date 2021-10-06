package com.pixelrp.core.mechanic.pokeKillMechanic;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelrp.core.mechanic.Mechanic;
import com.pixelrp.core.mechanic.pokeKillMechanic.commands.PKillCommand;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandManager;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.World;

import java.util.Set;
import java.util.stream.Collectors;

public class PokeKillMechanic extends Mechanic {

    public PokeKillMechanic(String id) {
        super(id);
    }

    @Override
    public void initialize() {
        CommandSpec pKillSpec = CommandSpec.builder()
                .description(Text.of("Removes all normal non shiny/ legendary pokemon from every world."))
                .permission("rc.admin")
                .executor(new PKillCommand(this))
                .build();

        Sponge.getCommandManager().register(plugin, pKillSpec, "pkill");
    }

    public String removeAllNormalPokemon(World world){
        int i = 0;
        for(Entity entity : world.getEntities()){
            if(entity instanceof EntityPixelmon){
                EntityPixelmon pixelEntity = (EntityPixelmon) entity;
                if(!pixelEntity.getPokemonData().isLegendary() && !pixelEntity.getPokemonData().isShiny()){
                    ((EntityPixelmon) entity).unloadEntity();
                    i += 1;
                }
            }
        }
        return "[RCPlugin] " + i + " pokemon have been removed!";
    }

}
