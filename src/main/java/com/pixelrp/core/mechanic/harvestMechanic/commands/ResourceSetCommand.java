package com.pixelrp.core.mechanic.harvestMechanic.commands;

import com.pixelrp.core.mechanic.harvestMechanic.utils.HarvestManager;
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

public class ResourceSetCommand implements CommandExecutor {

    private static final Text KEY_RESOURCE = Text.of("resource");

    public static CommandSpec createSpec(HarvestManager hm){

        CommandSpec spec = CommandSpec.builder()
                .executor(new ResourceSetCommand(hm))
                .arguments(GenericArguments.string(KEY_RESOURCE))
                .permission("rcplugin.admin")
                .build();

        return spec;
    }

    private HarvestManager hm;
    public ResourceSetCommand(HarvestManager hm){
        this.hm = hm;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if(src instanceof Player){
            Player player = (Player) src;
            String resourceName = (String) args.getOne(KEY_RESOURCE).get();
            if(hm.isEditingResources(player)){
                if(hm.getResourceEdits().get(player.getUniqueId().toString()) != null && hm.setResourceBlock(resourceName, hm.getResourceEdits().get(player.getUniqueId().toString()))){
                    player.sendMessage(Text.of(TextColors.AQUA, "You have set a resource block!"));
                } else player.sendMessage(Text.of(TextColors.AQUA, "That resource name does not exist."));
            } else player.sendMessage(Text.of(TextColors.AQUA, "You are not in edit mode."));
        }

        return CommandResult.empty();
    }
}
