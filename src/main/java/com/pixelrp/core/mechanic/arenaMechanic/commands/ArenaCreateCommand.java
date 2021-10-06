package com.pixelrp.core.mechanic.arenaMechanic.commands;

import com.pixelrp.core.mechanic.arenaMechanic.utils.ArenaManager;
import com.pixelrp.core.mechanic.townMechanic.TownMechanic;
import com.pixelrp.core.mechanic.townMechanic.commands.TownCreateCommand;
import com.pixelrp.core.mechanic.townMechanic.utils.Town;
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

public class ArenaCreateCommand implements CommandExecutor {

    private static final Text KEY_NAME = Text.of("name");

    public static CommandSpec createSpec(ArenaManager am){

        CommandSpec spec = CommandSpec.builder()
                .executor(new ArenaCreateCommand(am))
                .arguments(GenericArguments.remainingJoinedStrings(KEY_NAME))
                .build();

        return spec;
    }

    private ArenaManager am;
    public ArenaCreateCommand(ArenaManager am){
        this.am = am;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if(src instanceof Player){
            Player player = (Player) src;
            String arenaName = (String) args.getOne(KEY_NAME).get();

            TownMechanic townMechanic = (TownMechanic) TownMechanic.instance;
            if(townMechanic.isEnabled()){
                TownManager tm = townMechanic.getTownManager();
                if(tm.getPlayerTown(player) != null){
                    Town town = tm.getPlayerTown(player);
                    if(tm.isGymLeader(player, town)){
                        World world = player.getWorld();
                        Location l1 = new Location<World>(world, player.getPosition().getX() + 6, 0, player.getPosition().getZ() - 6);
                        Location l2 = new Location<World>(world, player.getPosition().getX() - 6, 256, player.getPosition().getZ() + 6);
                        if(tm.getTownCuboid(town).inRegion(l1) && tm.getTownCuboid(town).inRegion(l2)){
                            if(am.createArena(arenaName, l1, l2)) player.sendMessage(Text.of(TextColors.AQUA, "You have created an arena."));
                                else player.sendMessage(Text.of(TextColors.AQUA, "That name is already taken."));
                        } player.sendMessage(Text.of(TextColors.AQUA, "All corners of your town must be inside a town."));
                    } else player.sendMessage(Text.of(TextColors.AQUA, "You do not have permission to create an arena."));
                } else player.sendMessage(Text.of(TextColors.AQUA, "You do not have permission to create an arena."));
            }
        }
        return CommandResult.empty();
    }
}
