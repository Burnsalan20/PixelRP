package com.pixelrp.core.mechanic.townMechanic.commands;

import com.pixelrp.core.mechanic.townMechanic.utils.Town;
import com.pixelrp.core.mechanic.townMechanic.utils.TownManager;
import com.pixelrp.core.utils.Rect2;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class TownEditCommand implements CommandExecutor {

    public static CommandSpec createSpec(TownManager tm){

        CommandSpec spec = CommandSpec.builder()
                .executor(new TownEditCommand(tm))
                .arguments()
                .build();

        return spec;
    }

    private TownManager tm;
    public TownEditCommand(TownManager tm){
        this.tm = tm;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if(src instanceof Player){
            Player player = (Player) src;
            if(tm.isInEditMode(player)){
                src.sendMessage(Text.of(TextColors.AQUA, "You have left edit mode for this claim."));
                tm.getEditors().remove(player.getUniqueId().toString());
                return CommandResult.empty();
            }

            if(tm.getTownAt(player.getLocation()) != null){
                Town town = tm.getTownAt(player.getLocation());
                if(tm.isMayor(player, town) || player.hasPermission("rcplugin.admin")){
                    src.sendMessage(Text.of(TextColors.AQUA, "You have entered edit mode for this Town."));
                    tm.getEditors().put(player.getUniqueId().toString(), new Rect2());
                } else {
                    src.sendMessage(Text.of(TextColors.AQUA, "You do not have permission to edit this Town."));
                }
            } else{
                src.sendMessage(Text.of(TextColors.AQUA, "You are not in a Town."));
            }


        }

        return CommandResult.empty();
    }
}