package com.pixelrp.core.mechanic.harvestMechanic.commands;

import com.pixelrp.core.mechanic.harvestMechanic.utils.HarvestManager;
import com.pixelrp.core.mechanic.townMechanic.commands.TownInfoCommand;
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

public class ResourceEditCommand implements CommandExecutor {

    public static CommandSpec createSpec(HarvestManager hm){

        CommandSpec spec = CommandSpec.builder()
                .executor(new ResourceEditCommand(hm))
                .arguments()
                .permission("rcplugin.admin")
                .build();

        return spec;
    }

    private HarvestManager hm;
    public ResourceEditCommand(HarvestManager hm){
        this.hm = hm;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if(src instanceof Player){
            Player player = (Player) src;
            if(!hm.isEditingResources(player)){
                hm.getResourceEdits().put(player.getUniqueId().toString(), null);
                player.sendMessage(Text.of(TextColors.AQUA, "You have entered resource edit mode."));
            } else {
                hm.getActiveResources().remove(player.getUniqueId().toString());
                player.sendMessage(Text.of(TextColors.AQUA, "You have left resource edit mode."));
            }

        }
        return CommandResult.empty();
    }
}
