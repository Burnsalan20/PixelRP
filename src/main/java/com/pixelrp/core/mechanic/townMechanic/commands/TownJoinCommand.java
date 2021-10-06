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

import java.util.Optional;

public class TownJoinCommand implements CommandExecutor {

    private static final Text KEY_USER = Text.of("user");
    private static final Text KEY_TOWN = Text.of("town");

    public static CommandSpec createSpec(TownManager tm){

        CommandSpec spec = CommandSpec.builder()
                .executor(new TownJoinCommand(tm))
                .arguments(GenericArguments.player(KEY_USER),
                        GenericArguments.remainingJoinedStrings(KEY_TOWN))
                .permission("fake.perm")
                .build();

        return spec;
    }

    private TownManager tm;
    public TownJoinCommand(TownManager tm){
        this.tm = tm;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Optional<Player> target = Optional.of(args.<Player>getOne(KEY_USER).get());
        String townName = (String) args.getOne(KEY_TOWN).get();

        if(!tm.isPlayerInATown(target.get())){
            tm.broadCastToTown(tm.getTownByName(townName), target.get().getName() + " has joined the town!");
            tm.addCitizen(target.get(), tm.getTownByName(townName));
            target.get().sendMessage(Text.of(TextColors.AQUA, "You have joined the town!"));
        } else target.get().sendMessage(Text.of(TextColors.AQUA, "You are already in a town."));

        return CommandResult.empty();
    }
}
