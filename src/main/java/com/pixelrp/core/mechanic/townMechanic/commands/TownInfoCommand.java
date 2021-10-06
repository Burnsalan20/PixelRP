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

public class TownInfoCommand implements CommandExecutor {

    public static CommandSpec createSpec(TownManager tm){

        CommandSpec spec = CommandSpec.builder()
                .executor(new TownInfoCommand(tm))
                .arguments()
                .build();

        return spec;
    }

    private TownManager tm;
    public TownInfoCommand(TownManager tm){
        this.tm = tm;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if(src instanceof Player){
            Player player = (Player) src;
            if(tm.getPlayerTown(player) != null){
                Town town = tm.getPlayerTown(player);
                tm.info(player, town);
            } else {
                src.sendMessage(Text.of(TextColors.AQUA, "You are not in a town."));
            }
        }
        return CommandResult.empty();
    }
}