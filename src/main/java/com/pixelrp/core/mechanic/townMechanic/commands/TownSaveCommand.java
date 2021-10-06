package com.pixelrp.core.mechanic.townMechanic.commands;

import com.pixelrp.core.mechanic.townMechanic.utils.Town;
import com.pixelrp.core.mechanic.townMechanic.utils.TownManager;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class TownSaveCommand implements CommandExecutor {

    public static CommandSpec createSpec(TownManager tm){

        CommandSpec spec = CommandSpec.builder()
                .executor(new TownSaveCommand(tm))
                .arguments()
                .build();

        return spec;
    }

    private TownManager tm;
    public TownSaveCommand(TownManager tm){
        this.tm = tm;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if(src instanceof Player){
            Player player = (Player) src;
            if(tm.isInEditMode(player)){
                Town town = tm.getPlayerTown(player);
                if(tm.getEditors().get(player.getUniqueId().toString()).getLocation1() != null &&
                        tm.getEditors().get(player.getUniqueId().toString()).getLocation2() != null){
                    tm.edit(player, town);
                    player.sendMessage(Text.of(TextColors.AQUA, "Your town was successfully edited."));
                } else{
                    player.sendMessage(Text.of(TextColors.AQUA, "One or more of your locations are not selected. Use a gold shovel to select two spots to make a cuboid."));
                }
            } else {
                player.sendMessage(Text.of(TextColors.AQUA, "You are not in edit mode."));
            }
        }
        return CommandResult.empty();
    }
}
