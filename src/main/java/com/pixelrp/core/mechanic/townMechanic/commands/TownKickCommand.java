package com.pixelrp.core.mechanic.townMechanic.commands;

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

import java.util.Optional;

public class TownKickCommand implements CommandExecutor {

    private static final Text KEY_USER = Text.of("user");

    public static CommandSpec createSpec(TownManager tm){

        CommandSpec spec = CommandSpec.builder()
                .executor(new TownKickCommand(tm))
                .arguments(GenericArguments.player(KEY_USER))
                .build();

        return spec;
    }

    private TownManager tm;
    public TownKickCommand(TownManager tm){
        this.tm = tm;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if(src instanceof Player){
            Player player = (Player) src;
            Optional<Player> target = Optional.of(args.<Player>getOne(KEY_USER).get());

            if(tm.getPlayerTown(player) != null){
                Town town = tm.getPlayerTown(player);
                if(tm.isMayor(player, town)){
                    if(town.getCitizens().containsKey(target.get().getUniqueId().toString())){
                        tm.removeCitizen(target.get(), town);
                        target.get().sendMessage(Text.of(TextColors.AQUA, "You have been kicked from your town."));
                    } else player.sendMessage(Text.of(TextColors.AQUA, "That player does not exist in this town."));
                } player.sendMessage(Text.of(TextColors.AQUA, "You do not have permission to kick."));
            } else player.sendMessage(Text.of(TextColors.AQUA, "You are not in a town."));
        }
        return CommandResult.empty();
    }
}