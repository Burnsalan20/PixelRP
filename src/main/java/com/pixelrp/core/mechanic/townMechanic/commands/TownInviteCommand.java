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
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import java.util.Optional;

public class TownInviteCommand implements CommandExecutor {

    private static final Text KEY_USER = Text.of("user");

    public static CommandSpec createSpec(TownManager tm){

        CommandSpec spec = CommandSpec.builder()
                .executor(new TownInviteCommand(tm))
                .arguments(GenericArguments.player(KEY_USER))
                .build();

        return spec;
    }

    private TownManager tm;
    public TownInviteCommand(TownManager tm){
        this.tm = tm;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if(src instanceof Player){
            Player owner = (Player) src;
            Optional<Player> target = Optional.of(args.<Player>getOne(KEY_USER).get());

            if(tm.getPlayerTown(owner) != null){
                Town town = tm.getPlayerTown(owner);
                if(tm.isMayor(owner, town)){
                    owner.sendMessage(Text.of(TextColors.AQUA, "You have invited ", target.get().getName(), " to your town."));

                    Text accept = Text.builder("[Accept]").onClick(TextActions.runCommand("/town join " + target.get().getName()+ " " + town.getName()))
                            .onHover(TextActions.showText(Text.of("Join " + town.getName()))).build();
                    target.get().sendMessage(Text.of(TextColors.AQUA, owner.getName(), " has invited you to their town. Click ", accept, " to join."));
                } else owner.sendMessage(Text.of(TextColors.AQUA, "You do not have permission to invite to this town."));
            } else {
                owner.sendMessage(Text.of(TextColors.AQUA, "You do not have a town."));
            }
        }
        return CommandResult.empty();
    }
}