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

public class TownSetStewartCommand implements CommandExecutor {

    private static final Text KEY_USER = Text.of("user");

    public static CommandSpec createSpec(TownManager tm){

        CommandSpec spec = CommandSpec.builder()
                .executor(new TownSetStewartCommand(tm))
                .arguments(GenericArguments.player(KEY_USER))
                .build();

        return spec;
    }

    private TownManager tm;
    public TownSetStewartCommand(TownManager tm){
        this.tm = tm;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if(src instanceof Player){
            Player player = (Player) src;
            Optional<Player> target = Optional.of(args.<Player>getOne(KEY_USER).get());
            if(tm.getPlayerTown(player) != null) {
                if(tm.isMayor(player, tm.getPlayerTown(player)) || src.hasPermission("rcplugin.admin")){
                    if(!player.getUniqueId().toString().equals(target.get().toString())){
                        tm.toggleStewart(player, target.get(), tm.getPlayerTown(player));
                    } else src.sendMessage(Text.of(TextColors.AQUA, "You cannot change your own town role."));
                } else {
                    src.sendMessage(Text.of(TextColors.AQUA, "You do not have permission to add a Stewart to this town."));
                }
            } else{
                src.sendMessage(Text.of(TextColors.AQUA, "You are not in a Town."));
            }

        }
        return CommandResult.empty();
    }
}