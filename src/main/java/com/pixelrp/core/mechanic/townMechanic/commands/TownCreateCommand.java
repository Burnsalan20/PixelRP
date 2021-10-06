package com.pixelrp.core.mechanic.townMechanic.commands;

import com.pixelrp.core.mechanic.townMechanic.utils.TownManager;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.awt.geom.GeneralPath;

public class TownCreateCommand implements CommandExecutor {

    private static final Text KEY_NAME = Text.of("name");

    public static CommandSpec createSpec(TownManager tm){

        CommandSpec spec = CommandSpec.builder()
                .executor(new TownCreateCommand(tm))
                .arguments(GenericArguments.remainingJoinedStrings(KEY_NAME))
                .build();

        return spec;
    }

    private TownManager tm;
    public TownCreateCommand(TownManager tm){
        this.tm = tm;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if(src instanceof Player){
            Player player = (Player) src;
            String townName = (String) args.getOne(KEY_NAME).get();
            if(!tm.isPlayerInATown(player)){
                World world = player.getWorld();
                Location l1 = new Location<World>(world, player.getPosition().getX() + 6, 0, player.getPosition().getZ() - 6);
                Location l2 = new Location<World>(world, player.getPosition().getX() - 6, 256, player.getPosition().getZ() + 6);
                tm.create(player, townName, l1, l2);

                player.sendMessage(Text.of(TextColors.AQUA, "You have created a town."));
            } else player.sendMessage(Text.of(TextColors.AQUA, "You are already in a town."));
        }

        return CommandResult.empty();
    }
}