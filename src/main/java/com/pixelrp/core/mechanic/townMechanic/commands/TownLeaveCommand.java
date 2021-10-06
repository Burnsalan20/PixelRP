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

public class TownLeaveCommand implements CommandExecutor {

    public static CommandSpec createSpec(TownManager tm){

        CommandSpec spec = CommandSpec.builder()
                .executor(new TownLeaveCommand(tm))
                .arguments()
                .build();

        return spec;
    }

    private TownManager tm;
    public TownLeaveCommand(TownManager tm){
        this.tm = tm;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if(src instanceof Player){
            Player player = (Player) src;
            if(tm.isPlayerInATown(player)){
                Town town = tm.getPlayerTown(player);
                if(!tm.isMayor(player, town)){
                    tm.leave(player, town);
                    player.sendMessage(Text.of(TextColors.AQUA, "You have left your town."));
                } else player.sendMessage(Text.of(TextColors.AQUA, "You cannot leave a town if you are the mayor. Either transfer leadership, or disband the town to leave."));
            } else player.sendMessage(Text.of(TextColors.AQUA, "You are not in a town."));
        }

        return CommandResult.empty();
    }
}
