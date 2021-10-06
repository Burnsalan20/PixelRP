package com.pixelrp.core.mechanic.pokeKillMechanic.commands;

import com.pixelrp.core.mechanic.pokeKillMechanic.PokeKillMechanic;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

public class PKillCommand implements CommandExecutor {

    PokeKillMechanic mechanic;

    public PKillCommand(PokeKillMechanic mechanic){
        this.mechanic = mechanic;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (src instanceof Player) {
            Player player = (Player) src;
            player.sendMessage(Text.of(mechanic.removeAllNormalPokemon(player.getWorld())));
        }
        else if(src instanceof ConsoleSource) {
            src.sendMessage(Text.of(mechanic.removeAllNormalPokemon(Sponge.getServer().getWorld("world").get())));
        }

        return CommandResult.success();
    }
}
